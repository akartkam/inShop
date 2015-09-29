package com.akartkam.inShop.service.product;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import com.akartkam.inShop.dao.product.ProductDAO;
import com.akartkam.inShop.dao.product.option.ProductOptionDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.formbean.ProductForm;

@Service("ProductService")
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductOptionDAO productOptionDAO;
	
	@Autowired
	ProductDAO productDAO;
	
	@Autowired
	CategoryService categoryService; 
	

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
	public void mergeWithExistingAndUpdateOrCreate(final ProductForm productFromPost, final Set<String> po, final Set<String> ps) {
		if (productFromPost == null) return;
		final Product existingProduct = getProductById(productFromPost.getId());
		if (existingProduct != null) {
			existingProduct.getDefaultSku().setName(productFromPost.getDefaultSku().getName());
			existingProduct.getDefaultSku().setCode(productFromPost.getDefaultSku().getCode());
			existingProduct.setUrl(productFromPost.getUrl());
			existingProduct.setOrdering(productFromPost.getOrdering());
			existingProduct.setBrand(productFromPost.getBrand());
			existingProduct.setModel(productFromPost.getModel());
			existingProduct.getDefaultSku().setCostPrice(productFromPost.getDefaultSku().getCostPrice());
			existingProduct.getDefaultSku().setRetailPrice(productFromPost.getDefaultSku().getRetailPrice());
			existingProduct.getDefaultSku().setSalePrice(productFromPost.getDefaultSku().getSalePrice());
			existingProduct.getDefaultSku().setDescription(productFromPost.getDefaultSku().getDescription());
			existingProduct.getDefaultSku().setLongDescription(productFromPost.getDefaultSku().getLongDescription());
			existingProduct.setEnabled(productFromPost.isEnabled());
			existingProduct.setCanSellWithoutOptions(productFromPost.isCanSellWithoutOptions());
			if (!existingProduct.getCategory().equals(productFromPost.getCategory())) {
				productFromPost.setAttributeValuesFromMap();
				List<AbstractAttributeValue> lavfp = productFromPost.getAttributeValues();
				Iterator<AbstractAttributeValue> avi = existingProduct.getAttributeValues().iterator();
				while(avi.hasNext()) {
					AbstractAttributeValue av = avi.next();
					int idx = lavfp.indexOf(av);
					if (lavfp.contains(av)) {
						av.setValue(lavfp.get(idx).getValue());
						lavfp.remove(idx);
					} else {
						avi.remove();
					}
				}
				for (AbstractAttributeValue av1:lavfp) {
					existingProduct.addAttributeValue(av1);
				}				
				//Set category
				Category ctgFromPost = categoryService.loadCategoryById(productFromPost.getCategory().getId(), false);
				List<AbstractAttributeValue> avCtgFromPost = ctgFromPost.getAllAttributeValues(true);
				for (AbstractAttributeValue av : existingProduct.getCategory().getAllAttributeValues(true)) {
					//if (!avCtgFromPost.contains(av)) 
				}
				existingProduct.setCategory(productFromPost.getCategory());
			} else {
				existingProduct.getAttributeValues().clear();
			}

			
	        Iterator<ProductStatus> psi = existingProduct.getDefaultSku().getProductStatus().iterator();
	        while(psi.hasNext()){
	        	ProductStatus p = psi.next();
	        	if (ps.contains(p.name())) {
	        		ps.remove(p.name());
	        	} else {
	        		psi.remove();
	        	}
	        	
	        }
	        for (String psii : ps) {
	        	existingProduct.getDefaultSku().getProductStatus().add(ProductStatus.forName(psii));
	        }
	        
	        Iterator<ProductOption> poi = existingProduct.getProductOptions().iterator();
	        while(poi.hasNext()){
	        	ProductOption p = poi.next();
	        	if (po.contains(p.getId().toString())) {
	        		po.remove(p.getId().toString());
	        	} else {
	        		poi.remove();
	        	}
	        	
	        }
	        for (String poId : po) {
	        	ProductOption poEx = loadPOById(UUID.fromString(poId), false);
	        	existingProduct.addProductOption(poEx);
	        }
			existingProduct.getDefaultSku().getImages().clear();
			for (String i : productFromPost.getDefaultSku().getImages()) existingProduct.getDefaultSku().getImages().add(i);
		} else {
			setProductOptions(productFromPost, po);
			setProductStatuses(productFromPost, ps);
	        createProduct(productFromPost);
		}
	}

	@Override
	public void setProductStatuses(Product product, Set<String> ps) {
        if (product == null || ps == null) return;
		for (String psii : ps) {
			product.getDefaultSku().getProductStatus().add(ProductStatus.forName(psii));
        }		
	}

	@Override
	public void setProductOptions(Product product, Set<String> po) {
		if (product == null || po == null) return;
        for (String poId : po) {
        	ProductOption poEx = loadPOById(UUID.fromString(poId), false);
        	product.addProductOption(poEx);
        }
	}
}

//}
