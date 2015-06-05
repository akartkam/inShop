package com.akartkam.inShop.service.product;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akartkam.inShop.dao.product.option.ProductOptionDAO;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;

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
	public void mergeWithExistingPOAndUpdateOrCreate(final ProductOption poFromPost) {
		if (poFromPost == null) return;
		final ProductOption existingPo = getPOById(poFromPost.getId());
		if (existingPo != null) {
			existingPo.setName(poFromPost.getName());
			existingPo.setLabel(poFromPost.getLabel());
			existingPo.setRequired(poFromPost.getRequired());
			existingPo.setOrdering(poFromPost.getOrdering());
			existingPo.setEnabled(poFromPost.isEnabled());
			existingPo.setUseInSkuGeneration(poFromPost.getUseInSkuGeneration());
			for (ProductOptionValue pov: existingPo.getProductOptionValues()){
				int povIndex = poFromPost.getProductOptionValues().indexOf(pov);
				ProductOptionValue formPov = poFromPost.getProductOptionValues().get(povIndex);
				if (!poFromPost.getProductOptionValues().contains(pov)) {
					existingPo.getProductOptionValues().remove(pov);
				} else {
					pov.setEnabled(formPov.isEnabled());
					pov.setOptionValue(formPov.getOptionValue());
					pov.setOrdering(formPov.getOrdering());
					pov.setPriceAdjustment(formPov.getPriceAdjustment());
					poFromPost.getProductOptionValues().remove(formPov);
				}
			}
			for(ProductOptionValue pov: poFromPost.getProductOptionValues()) {
				existingPo.getProductOptionValues().add(pov);
			}
	        updatePO(existingPo);
		} else {
			createPO(poFromPost);
		}
	}
	
	

}
