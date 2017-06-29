package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

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
import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.formbean.DataTableForm;
import com.akartkam.inShop.formbean.ProductFilterDTO;
import com.akartkam.inShop.formbean.ProductFilterFacetDTO;

@Repository
public class ProductDAOImpl extends AbstractGenericDAO<Product> implements ProductDAO {
	
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


	@Override
	public List<Object[]> findProductFilterDTOByCategory(UUID categoryId) {
		Query query = currentSession().getNamedQuery("findFilteredProductByCategory").setString("c", categoryId.toString());
		List<Object[]> ret = (List<Object[]>)query.list();
		return ret;
	}


	@Override
	public List<Product> findProductsFilteredByCategory(ProductFilterDTO productFilterDTO, UUID categoryId) {
		StringBuilder query = new StringBuilder();		
		query.append(
				"WITH RECURSIVE r AS ( "+
						"	select id "+
						"	  from category "+  
						"	  where cast(id as varchar) = :c "+
						"	union all "+
						"	select c.id "+
						"	  from category c "+
						"	       join r on parent_id=r.id  "+
						" ) "+				
						"select p.* "+
							 "  from Product p, r "+
							 "	where p.enabled=true and p.category_id=r.id");
		StringBuilder brandClaus = new StringBuilder(); 
		for (ProductFilterFacetDTO brandFacet : productFilterDTO.getBrandFacets()) {
			if (brandFacet.isActive()) {
				if (brandClaus.length() == 0) brandClaus.append("('").append(brandFacet.getId()).append("'"); 
				else brandClaus.append(", '").append(brandFacet.getId()).append("'");
			}
		}
		if (brandClaus.length() > 0) {
			brandClaus.append(")");
			brandClaus.insert(0, " and exists(select 1 from brand where name in ").append(" and p.brand_id=id)");
			query.append(brandClaus);
		}
		StringBuilder modelClaus = new StringBuilder();
		for (ProductFilterFacetDTO modelFacet : productFilterDTO.getModelFacets()) {
			if (modelFacet.isActive()) {
				if (modelClaus.length() == 0) modelClaus.append("('").append(modelFacet.getId()).append("'");
				else modelClaus.append(", '").append(modelFacet.getId()).append("'");
			}
		}
		if (modelClaus.length() > 0) {
			modelClaus.append(")");
			modelClaus.insert(0, " and p.model in ");
			query.append(modelClaus);
		}
		/*attributes and options*/
		StringBuilder attributeClaus = new StringBuilder();
		for(ProductFilterFacetDTO attributeFacet : productFilterDTO.getAttributesFacets()){
			if (attributeFacet.isActive()) {
				if (attributeClaus.length() == 0) attributeClaus.append("('").append(attributeFacet.getId()).append("'"); 
				else attributeClaus.append(", '").append(attributeFacet.getId()).append("'");
			}
		}
		if (attributeClaus.length() > 0) {
			attributeClaus.append(")");
			StringBuilder attributeClausFull = new StringBuilder();
			attributeClausFull.append(" and exists(select 1 from sku s ").
				append(" left join attribute_value av on (av.product_id=p.id or av.sku_id=s.id) ").
				append(" left join attribute_decimal_value adv on adv.id=av.id ").
			    append(" left join attribute_int_value aiv on aiv.id=av.id ").
			    append(" left join attribute_slist_value alv on alv.id=av.id ").
			    append(" left join attribute_string_value asv on asv.id=av.id ").
			    append(" left join attribute a on a.id=av.attribute_id ").
			    append(" left join lnk_sku_option_value lsov on lsov.sku_id=s.id ").
			    append(" left join product_option_value pov on pov.id=lsov.product_option_value_id ").
			    append(" left join product_option po on po.id=pov.productoption_id ").			
			    append("    where (s.product_id=p.id or p.default_sku_id=s.id) and s.enabled=true and (").
			    append(" cast(round(cast(adv.attributevalue as numeric), 2) as varchar) in ").
			    append(attributeClaus).
			    append(" or cast(aiv.attributevalue as varchar) in ").
			    append(attributeClaus).
			    append(" or trim(alv.attributevalue) in ").
			    append(attributeClaus).
			    append(" or trim(asv.attributevalue) in ").
			    append(attributeClaus).
			    append(" or (").
			    append(" trim(pov.option_value) in ").
			    append(attributeClaus).
			    append(" )))");
			query.append(attributeClausFull);	
		}
		SQLQuery qquery = currentSession().createSQLQuery(query.toString());
		qquery.addEntity(Product.class);
		return qquery.list();
	}
}
