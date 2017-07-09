package com.akartkam.inShop.common.filter;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.formbean.ProductFilterDTO;
import com.akartkam.inShop.formbean.ProductFilterFacetDTO;

public class ProductFilterCategoryConditionHolder implements ProductFilterConditionHolder {
	
	private Category category;

	public Category getCategory() {
		return category;
	}
	
	public ProductFilterCategoryConditionHolder (Category category) {
		this.category = category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public Query getInitializedQueryForFilterDTO(Session session) {
		Query query = session.getNamedQuery("findFilteredProductByCategory").setString("c", category.getId().toString());
		return query;
	}

	@Override
	public void adjustFilterDTO(ProductFilterDTO filterDTO) {
		filterDTO.setCategoryId(category.getId());
		
	}

	@Override
	public Query getInitializedQueryForFilteredProduct(ProductFilterDTO productFilterDTO, Session session) {
		StringBuilder query = new StringBuilder();		
		query.append("WITH RECURSIVE r AS ( ").
			  append("	select id ").
			  append("	  from category ").  
			  append("	  where cast(id as varchar) = '").append(category.getId().toString()).append("'").
			  append("	union all ").
			  append("	select c.id ").
			  append("	  from category c ").
			  append("	       join r on parent_id=r.id  ").
			  append(" ) ").				
			  append(" select p.* ").
			  append("  from Product p, r ").
			  append("  where p.enabled=true and p.category_id=r.id");
		StringBuilder brandClaus = new StringBuilder(); 
		for (ProductFilterFacetDTO brandFacet : productFilterDTO.getBrandFacets()) {
			if (brandFacet.isActive()) {
				if (brandClaus.length() == 0) brandClaus.append("('").append(brandFacet.getId()).append("'"); 
				else brandClaus.append(", '").append(brandFacet.getId()).append("'");
			}
		}
		if (brandClaus.length() > 0) {
			brandClaus.append(")");
			brandClaus.insert(0, " and exists(select 1 from brand where trim(name) in ").append(" and p.brand_id=id)");
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
			modelClaus.insert(0, " and trim(p.model) in ");
			query.append(modelClaus);
		}
		/*attributes and options*/
		Map<String, List<String>> mapAttrVals = new HashMap<String, List<String>>();
		for(ProductFilterFacetDTO attributeFacet : productFilterDTO.getAttributesFacets()){
			if (attributeFacet.isActive()) {
				if (!mapAttrVals.containsKey(attributeFacet.getFacet())){
					List<String> listVals = new ArrayList<String>();
					listVals.add(attributeFacet.getId());
					mapAttrVals.put(attributeFacet.getFacet(), listVals);
				} else {
					mapAttrVals.get(attributeFacet.getFacet()).add(attributeFacet.getId());
				}
			}
		}

		if (mapAttrVals.size() > 0) {
			StringBuilder attributeClaus = new StringBuilder();
			for (Entry<String, List<String>> entry : mapAttrVals.entrySet()) {
				attributeClaus.append(" and check_attribute_product_according('")
				              .append(entry.getKey())
				              .append("', '")
				              .append(StringUtils.join(entry.getValue(),","))
				              .append("', p.id, p.default_sku_id)");
			}
			query.append(attributeClaus);				
		}
		SQLQuery qquery = session.createSQLQuery(query.toString());
		qquery.addEntity(Product.class);
		return qquery;
	}

}
