package com.akartkam.inShop.dao.product;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.akartkam.inShop.common.filter.ProductFilterConditionHolder;
import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.formbean.DataTableForm;
import com.akartkam.inShop.formbean.ProductFilterDTO;

public interface ProductDAO extends GenericDAO<Product, UUID> {
	List<Product> findAllProducts();
	List<Product> findProductsByName(String name);
	List<Product> findProductsByProductStatus(ProductStatus productStatus);
	long countTotalProducts();
	Object[] findProductsForDataTable(DataTableForm dt);
	List<String> findAllProductUrls();
	List<Object[]> findProductFilterDTO(ProductFilterConditionHolder filterCondHolder);
	List<Product> findProductsFiltered(ProductFilterDTO productFilterDTO, ProductFilterConditionHolder filterCondHolder);
}
