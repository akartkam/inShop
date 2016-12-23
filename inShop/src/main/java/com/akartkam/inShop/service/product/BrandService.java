package com.akartkam.inShop.service.product;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;

public interface BrandService {
	Brand createBrand(Brand brand);
	List<Brand> getAllBrand();
	List<Brand> getAllBrand(Boolean useDisabled);
	List<Product> getProductsByBrand(Brand brand);
	Brand getBrandById(UUID id);
	Brand getBrandByUrl(String url);
	Brand cloneBrandById(UUID id) throws CloneNotSupportedException;
	Brand loadBrandById(UUID id, Boolean lock);
	void softDeleteBrandById(UUID id);
	void deleteBrand(Brand brand);
	void deleteBrandById(UUID id);
	void updateBrand(Brand brand);
	void mergeWithExistingAndUpdateOrCreate(final Brand brandFromPost);
}
