package com.akartkam.inShop.service.product;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.BrandDAO;
import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.attribute.AttributeCategory;

@Service("BrandService")
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandDAO brandDAO; 
	
	@Override
	public List<Brand> getAllBrand() {
		return brandDAO.list();
	}
	
	@Override
	public List<Brand> getAllBrand(Boolean useDisabled) {
		return brandDAO.readAllBrand(useDisabled);
	}


	@Override
	public Brand getBrandById(UUID id) {
		return brandDAO.get(id);
	}

	@Override
	public Brand cloneBrandById(UUID id) throws CloneNotSupportedException{
		Brand clonedBrand = getBrandById(id);
		if (clonedBrand == null) return null;
		return clonedBrand.clone();
	}
	
	@Override
	public Brand loadBrandById(UUID id, Boolean lock) {
		return brandDAO.findById(id, lock);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void softDeleteBrandById(UUID id) {
		Brand brand = getBrandById(id);
		if (brand != null) {
			brand.setEnabled(false);
			updateBrand(brand);
		}
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteBrand(Brand brand) {
		brandDAO.delete(brand);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteBrandById(UUID id) {
		brandDAO.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void updateBrand(Brand brand) {
		brandDAO.update(brand);
	}

	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(Brand brandFromPost) {
		if (brandFromPost == null) return;
		final Brand existingBrand = getBrandById(brandFromPost.getId());
		brandFromPost.buildFullLink(brandFromPost.getUrlForForm());
		if (existingBrand != null) {
			existingBrand.setName(brandFromPost.getName());
			existingBrand.setUrl(brandFromPost.getUrl());
			existingBrand.setDescription(brandFromPost.getDescription());
			existingBrand.setEnabled(brandFromPost.isEnabled());
		} else {
			createBrand(brandFromPost);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public Brand createBrand(Brand brand) {
		return brandDAO.create(brand);
	}

}
