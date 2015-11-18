package com.akartkam.inShop.service.product;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.validation.Errors;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.formbean.ProductForm;


public interface ProductService {
	ProductOption createPO(ProductOption po);
	Product createProduct(Product product);
	List<ProductOption> getAllPO();
	List<Product> getAllProduct();
	Product getProductById(UUID id);
	ProductOption getPOById(UUID id);
	ProductOption clonePOById(UUID id) throws CloneNotSupportedException;
	Product cloneProductById(UUID id) throws CloneNotSupportedException;
	ProductOption loadPOById(UUID id, Boolean lock);
	ProductOptionValue loadPOVById(UUID id, Boolean lock);
	Product loadProductById(UUID id, Boolean lock);
	void softDeletePOById(UUID id);
	void deletePO(ProductOption po);
	void deletePOById(UUID id);
	void softDeleteProductById(UUID id);
	void deleteProduct(Product product);
	void updatePO(ProductOption po);
	void mergeWithExistingPOAndUpdateOrCreate(final ProductOption poFromForm, Errors errors);
	void mergeWithExistingAndUpdateOrCreate(final ProductForm productFromPost);
	Product generateSkusFromProduct(UUID productId);
	Sku getSkuById(UUID id);
	Sku cloneSkuById(UUID id) throws CloneNotSupportedException;
}
