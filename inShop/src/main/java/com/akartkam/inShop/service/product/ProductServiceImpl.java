package com.akartkam.inShop.service.product;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.option.ProductOptionDAO;
import com.akartkam.inShop.domain.product.option.ProductOption;

@Service("ProductService")
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductOptionDAO productOptionDAO;

	@Override
	@Transactional(readOnly = false)
	public ProductOption createPO(ProductOption po) {
		return productOptionDAO.create(po);
	}

	@Override
	public List<ProductOption> getAllPO() {
		return productOptionDAO.list();
	}

	@Override
	public ProductOption getPOById(UUID id) {
		return productOptionDAO.get(id);
	}

	@Override
	public ProductOption clonePOById(UUID id) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductOption loadPOById(UUID id, Boolean lock) {
		return productOptionDAO.findById(id, lock);
	}

	@Override
	@Transactional(readOnly = false)
	public void softDeletePOById(UUID id) {
		ProductOption po = getPOById(id);
		if (po != null) {
			po.setEnabled(false);
			updatePO(po);
		}	}

	@Override
	@Transactional(readOnly = false)
	public void deletePO(ProductOption po) {
		productOptionDAO.delete(po);
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePOById(UUID id) {
		productOptionDAO.deleteById(id);

	}

	@Override
	@Transactional(readOnly = false)
	public void updatePO(ProductOption po) {
		productOptionDAO.update(po);

	}

	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingPOAndUpdateOrCreate(ProductOption po) {
		// TODO Auto-generated method stub

	}

}
