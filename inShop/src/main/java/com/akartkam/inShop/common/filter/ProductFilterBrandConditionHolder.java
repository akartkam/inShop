package com.akartkam.inShop.common.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.formbean.ProductFilterDTO;
import com.akartkam.inShop.formbean.ProductFilterFacetDTO;

public class ProductFilterBrandConditionHolder implements ProductFilterConditionHolder {

	private Brand brand;
	
	public ProductFilterBrandConditionHolder(Brand brand) {
		this.brand = brand;
	}
	
	@Override
	public Query getInitializedQueryForFilterDTO(Session session) {
		Query query = session.getNamedQuery("findFilteredProductByBrand").setString("b", brand.getId().toString());
		return query;
	}

	@Override
	public Query getInitializedQueryForFilteredProduct(ProductFilterDTO productFilterDTO, Session session) {
		StringBuilder query = new StringBuilder();
		query.append(" select p.* ").
		  append("  from Product p ").
		  append("  where p.enabled=true and cast(brand_id as varchar)='").append(brand.getId().toString()).append("'");
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

	@Override
	public void adjustFilterDTO(ProductFilterDTO filterDTO) {
		filterDTO.setBrandId(brand.getId());
	}

}
