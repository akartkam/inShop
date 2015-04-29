package com.akartkam.inShop.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.BrandDAO;
import com.akartkam.inShop.domain.product.Brand;

@Service("BrandService")
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandDAO brandDAO; 
	
	@Override
	public List<Brand> getAllBrand() {
		return brandDAO.list();
	}

}
