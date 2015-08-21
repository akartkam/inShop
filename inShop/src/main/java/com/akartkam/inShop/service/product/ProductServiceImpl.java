package com.akartkam.inShop.service.product;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import com.akartkam.inShop.dao.product.ProductDAO;
import com.akartkam.inShop.dao.product.option.ProductOptionDAO;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;

@Service("ProductService")
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductOptionDAO productOptionDAO;
	
	@Autowired
	ProductDAO productDAO;
	

	@Override
	@Transactional(readOnly = false)
	public ProductOption createPO(ProductOption po) {
		return productOptionDAO.create(po);
	}
	
	@Override
	public Product createProduct(Product product) {
		return productDAO.create(product);
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
	public void mergeWithExistingPOAndUpdateOrCreate(final ProductOption poFromPost, Errors errors) {
		if (poFromPost == null) return;
		if (checkPo(poFromPost, errors)) return;
		final ProductOption existingPo = getPOById(poFromPost.getId());
		if (existingPo != null) {
			existingPo.setName(poFromPost.getName());
			existingPo.setLabel(poFromPost.getLabel());
			existingPo.setRequired(poFromPost.getRequired());
			existingPo.setOrdering(poFromPost.getOrdering());
			existingPo.setEnabled(poFromPost.isEnabled());
			existingPo.setUseInSkuGeneration(poFromPost.getUseInSkuGeneration());
			Iterator<ProductOptionValue> povi = existingPo.getProductOptionValues().iterator();
			while(povi.hasNext()){
				ProductOptionValue pov = povi.next();
				int povIndex = poFromPost.getProductOptionValues().indexOf(pov);
				if(povIndex >= 0) {
					ProductOptionValue formPov = poFromPost.getProductOptionValues().get(povIndex);
					pov.setEnabled(formPov.isEnabled());
					pov.setOptionValue(formPov.getOptionValue());
					pov.setOrdering(formPov.getOrdering());
					pov.setPriceAdjustment(formPov.getPriceAdjustment());
					poFromPost.getProductOptionValues().remove(formPov);
				} else {
					povi.remove();
				}
			}
			for(ProductOptionValue pov: poFromPost.getProductOptionValues()) {
				pov.setProductOption(existingPo);
				existingPo.getProductOptionValues().add(pov);
			}
			poFromPost.getProductOptionValues().clear();
        //updatePO(existingPo);
		} else {
			for (ProductOptionValue pov: poFromPost.getProductOptionValues()) pov.setProductOption(poFromPost);
			createPO(poFromPost);
		}
	}
	
	private boolean checkPo(final ProductOption poFromPost, Errors errors) {
		if (errors.hasFieldErrors("priceAdjustment"))
			errors.rejectValue("priceAdjustment", "error.convert.bigdecimal");
		return errors.hasErrors();
		
	}

	@Override
	public List<Product> getAllProduct() {
		return productDAO.list();
	}

	@Override
	public Product getProductById(UUID id) {
		return productDAO.get(id);
	}
	
	@Override
	public Product cloneProductById(UUID id) throws CloneNotSupportedException{
		Product clonedProduct = getProductById(id);
		if (clonedProduct == null) return null;
		return clonedProduct.clone();
	}	
	
	@Override
	public Product loadProductById(UUID id, Boolean lock) {
		return productDAO.findById(id, lock);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void softDeleteProductById(UUID id) {
		Product product = getProductById(id);
		if (product != null) {
			product.setEnabled(false);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteProduct(Product product) {
		productDAO.delete(product);
	}

	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(final Product productFromPost, final Set<String> po, final Set<String> ps) {
		if (productFromPost == null) return;
		final Product existingProduct = getProductById(productFromPost.getId());
		if (existingProduct != null) {
			existingProduct.setName(productFromPost.getName());
			existingProduct.setCode(productFromPost.getCode());
			existingProduct.setCategory(productFromPost.getCategory());
			existingProduct.setUrl(productFromPost.getUrl());
			existingProduct.setOrdering(productFromPost.getOrdering());
			existingProduct.setBrand(productFromPost.getBrand());
			existingProduct.setModel(productFromPost.getModel());
			existingProduct.setCostPrice(productFromPost.getCostPrice());
			existingProduct.setRetailPrice(productFromPost.getRetailPrice());
			existingProduct.setSalePrice(productFromPost.getSalePrice());
			existingProduct.setDescription(productFromPost.getDescription());
			existingProduct.setLongDescription(productFromPost.getLongDescription());
			existingProduct.setEnabled(productFromPost.isEnabled());
			existingProduct.setCanSellWithoutOptions(productFromPost.isCanSellWithoutOptions());
	        Iterator<ProductStatus> psi = existingProduct.getProductStatus().iterator();
	        while(psi.hasNext()){
	        	ProductStatus p = psi.next();
	        	if (ps.contains(p.name())) {
	        		ps.remove(p.name());
	        	} else {
	        		psi.remove();
	        	}
	        	
	        }
	        for (String psii : ps) {
	        	existingProduct.getProductStatus().add(ProductStatus.forName(psii));
	        }
	        
	        for (ProductOption poCurr : existingProduct.getProductOptions()) {
	        	if (po.contains(poCurr.getId().toString())) {
	        		po.remove(poCurr.getId().toString());
	        	} else {
	        		existingProduct.removeProductOption(poCurr);
	        	}
	        }
	        for (String poId : po) {
	        	ProductOption poEx = productOptionDAO.findById(UUID.fromString(poId), false);
	        	existingProduct.addProductOption(poEx);
	        }
			existingProduct.getImages().clear();
			for (String i : productFromPost.getImages()) existingProduct.getImages().add(i);
		} else {
	        for (String poId : po) {
	        	ProductOption poEx = productOptionDAO.findById(UUID.fromString(poId), false);
	        	productFromPost.addProductOption(poEx);
	        }
	        for (String psi : ps) {
	        	productFromPost.getProductStatus().add(ProductStatus.forName(psi));
	        }			        
	        createProduct(productFromPost);
		}
	}


}
