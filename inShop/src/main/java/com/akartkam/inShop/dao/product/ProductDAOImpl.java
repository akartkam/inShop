package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.formbean.DataTableForm;

@Repository
public class ProductDAOImpl extends AbstractGenericDAO<Product> implements 
                             ProductDAO {
	
	@Autowired
	private SkuDAO skuDAO;
	
	@Resource
	@Qualifier("productDataTableColumnsMap")
	private Map<String, String> productDataTableColumnsMap;
	
	@Override
	public List<Product> findProductsByName(String name) {
		List<Sku> skus = skuDAO.findSkusByName(name);
		List<Product> products = new ArrayList<Product>();
		for (Sku sku : skus) {
			if (sku.getDefaultProduct() != null) {
			   if (!products.contains(sku.getDefaultProduct())) products.add(sku.getDefaultProduct());
			} else {
			   if (!products.contains(sku.getProduct())) products.add(sku.getProduct());
			}
		}
	
		return products;
		
	}
	

	@Override
	public List<Product> findProductsByProductStatus(ProductStatus  productStatus) {
		Query q = currentSession().getNamedQuery("findProductByProductStatus");
		q.setParameter("productStatus", productStatus.toString());
		//q.setParameterList("productStatus", Arrays.asList(new ProductStatus[]{ProductStatus.HIT, ProductStatus.NEW}));
		return q.list();
	}



	@Override
	public List<Product> findAllProducts() {
		Query q = currentSession().getNamedQuery("findAllProducts");
		return q.list();
	}





	@Override
	public long countTotalProducts() {
		Criteria criteria = currentSession().createCriteria(Product.class)
				.setProjection(Projections.rowCount());
		return (long) criteria.uniqueResult();
	}
	
	@Override
	public Object[] findProductsForDataTable(DataTableForm dt) {
		Object[] res = new Object[2];
		Criteria criteria = currentSession().createCriteria(Product.class, "pr");
		StringBuilder s = null;
		DetachedCriteria dc = null;
		if (dt.getSearch() != null && dt.getSearch().getValue() != null && !"".equals(dt.getSearch().getValue())) {
			s = new StringBuilder(dt.getSearch().getValue());
			s = s.insert(0, "%").append("%");
			dc = DetachedCriteria.forClass(Sku.class, "as");
			dc.add(Restrictions.ilike("code", s.toString()));
			dc.add(Restrictions.eqProperty("as.product.id", "pr.id"));
			dc.setProjection(Projections.property("as.id"));			
		}
		if (s != null) {
			criteria
			      .createAlias("defaultSku", "ds")
			      .createAlias("category", "ct")
			      .createAlias("brand", "br")
			      .add(Restrictions.or(
					 Restrictions.ilike("ds.name", s.toString()),
					 Restrictions.ilike("ds.code", s.toString()),
					 Restrictions.ilike("ct.name", s.toString()),
					 Restrictions.ilike("br.name", s.toString()),
					 Restrictions.ilike("model", s.toString()),
					 StringUtils.isNumeric(dt.getSearch().getValue())? 
					 Restrictions.eq("ordering", Integer.parseInt(dt.getSearch().getValue())):
				     Restrictions.sqlRestriction("1<>1"),
					 Subqueries.exists(dc)));
		}
		criteria.setProjection(Projections.rowCount());
		res[0] = (Long) criteria.uniqueResult();
		
		StringBuilder query = new StringBuilder();
		query.append("select p.* "+
					 "  from Product p "+ 
					 "       left join Sku s on p.default_sku_id=s.id "+ 
					 "       left join Brand b on p.brand_id=b.id "+
					 "		 left join Category c on c.id=p.category_id ");
		
		if (s != null) {			
			query.append(String.format(" where s.code ilike '%1$s' or s.name ilike '%1$s' or b.name ilike '%1$s' or c.name ilike '%1$s' or p.model ilike '%1$s' or"+
		                               " exists(select 1 from Sku s1 where s1.product_id=p.id and s1.code ilike '%1$s' )", s.toString()));
			if (StringUtils.isNumeric(dt.getSearch().getValue())) {
				query.append(" or p.ordering = "+dt.getSearch().getValue());
			}
		}
		if (dt.getOrder().length > 0) {
		    query.append(" order by ");		
			int i=0;
			for (DataTableForm.Order dtOrder : dt.getOrder()) {
				if (dtOrder.getColumn() != null) {
					if (i > 0) query.append(", "); 
					query.append(productDataTableColumnsMap.get(dtOrder.getColumn())).append(" ").append(dtOrder.getDir());				
				}
				i++;
			}		
		}
		SQLQuery qquery = currentSession().createSQLQuery(query.toString());
		if(dt.getStart() != null) qquery.setFirstResult(dt.getStart());
		if(dt.getLength() != null) qquery.setMaxResults(dt.getLength());
		qquery.addEntity(Product.class);
		res[1] =qquery.list();
		return res;
	}


	@Override
	public List<String> findAllProductUrls() {
		Query query = currentSession().getNamedQuery("findAllProductUrls");
		List<String> ret = (List<String>)query.list();
		return ret;
	}
}
