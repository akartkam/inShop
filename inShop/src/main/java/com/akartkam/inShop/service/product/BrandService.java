package com.akartkam.inShop.service.product;

import java.util.List;
import java.util.UUID;

import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Category;

public interface BrandService {
	Brand createBrand(Brand brand);
	List<Brand> getAllBrand();
	List<Brand> getAllBrand(Boolean useDisabled);
	Brand getBrandById(UUID id);
	Brand cloneBrandById(UUID id) throws CloneNotSupportedException;
	Brand loadBrandById(UUID id, Boolean lock);
	void softDeleteBrandById(UUID id);
	void deleteBrand(Brand brand);
	void deleteBrandById(UUID id);
	void updateBrand(Brand brand);
	void mergeWithExistingAndUpdateOrCreate(final Brand brandFromPost);
}
