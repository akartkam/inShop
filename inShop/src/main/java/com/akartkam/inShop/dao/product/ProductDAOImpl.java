package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
		criteria.setCacheable(true);
		criteria.setCacheRegion("query.Catalog");
		return (long) criteria.uniqueResult();
	}
	
	@Override
	public Object[] findProductsForDataTable(DataTableForm dt) {
		Object[] res = new Object[2];
		Criteria criteria = currentSession().createCriteria(Product.class);
		if (dt.getSearch() != null && dt.getSearch().getValue() != null && !"".equals(dt.getSearch().getValue())) {
			criteria
			      .createAlias("defaultSku", "ds")
			      .createAlias("category", "ct")
			      .createAlias("brand", "br")
			      .add(Restrictions.or(
					 Restrictions.ilike("ds.name", dt.getSearch().getValue()),
					 Restrictions.ilike("ct.name", dt.getSearch().getValue()),
					 Restrictions.ilike("br.name", dt.getSearch().getValue()),
					 Restrictions.ilike("model", dt.getSearch().getValue()),
					 StringUtils.isNumeric(dt.getSearch().getValue())? 
					 Restrictions.eq("ordering", Integer.parseInt(dt.getSearch().getValue())):
				     Restrictions.sqlRestriction("1=1")));
		}
		criteria.setProjection(Projections.rowCount());
		res[0] = (Long) criteria.uniqueResult();
		
		Criteria criteria1 = currentSession().createCriteria(Product.class);
		if (dt.getSearch() != null && dt.getSearch().getValue() != null && !"".equals(dt.getSearch().getValue())) {
			criteria1
		      .createAlias("defaultSku", "ds")
		      .createAlias("category", "ct")
		      .createAlias("brand", "br")
			  .add(Restrictions.or(
					 Restrictions.ilike("ds.name", dt.getSearch().getValue()),
					 Restrictions.ilike("ct.name", dt.getSearch().getValue()),
					 Restrictions.ilike("br.name", dt.getSearch().getValue()),
					 Restrictions.ilike("model", dt.getSearch().getValue()),
					 StringUtils.isNumeric(dt.getSearch().getValue())? 
					 Restrictions.eq("ordering", Integer.parseInt(dt.getSearch().getValue())):
					 Restrictions.sqlRestriction("1=1")));
		}
		for (DataTableForm.Order dtOrder : dt.getOrder()) {
			if ("asc".equals(dtOrder.getDir()) && dtOrder.getColumn() != null) {
				criteria1
			      .createAlias("defaultSku", "ds")
			      .createAlias("category", "ct")
			      .createAlias("brand", "br")
				  .addOrder(Order.asc(productDataTableColumnsMap.get(dtOrder.getColumn())));				
			} else if ("desc".equals(dtOrder.getDir()) && dtOrder.getColumn() != null) {
				criteria1
			      .createAlias("defaultSku", "ds")
			      .createAlias("category", "ct")
			      .createAlias("brand", "br")				
				  .addOrder(Order.desc(productDataTableColumnsMap.get(dtOrder.getColumn())));
			}
		}		
		
		if(dt.getStart() != null) criteria1.setFirstResult(dt.getStart());
		if(dt.getLength() != null) criteria1.setMaxResults(dt.getLength());	;
		res[1] = criteria1.list();
		return res;
	}

}
