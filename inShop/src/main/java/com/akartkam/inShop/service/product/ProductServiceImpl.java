package com.akartkam.inShop.service.product;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.akartkam.inShop.controller.admin.product.AdminSkuController;
import com.akartkam.inShop.dao.product.ProductDAO;
import com.akartkam.inShop.dao.product.SkuDAO;
import com.akartkam.inShop.dao.product.option.ProductOptionDAO;
import com.akartkam.inShop.dao.product.option.ProductOptionValueDAO;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.formbean.ProductForm;
import com.akartkam.inShop.util.NullAwareBeanUtilsBean;

@Service("ProductService")
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
	
	private static final Log LOG = LogFactory.getLog(ProductServiceImpl.class);
	
	@Autowired
	ProductOptionDAO productOptionDAO;

	@Autowired
	ProductOptionValueDAO productOptionValueDAO;	
	
	@Autowired
	ProductDAO productDAO;
	
	@Autowired
	SkuDAO skuDAO;
	
	
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
	@Transactional(readOnly = false)
	public Sku createSku(Sku sku) {
		return skuDAO.create(sku);
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
	public ProductOptionValue getPOVById(UUID id) {
		return productOptionValueDAO.get(id);
	}

	
	@Override
	public ProductOption clonePOById(UUID id) throws CloneNotSupportedException {
		ProductOption clonedPO = getPOById(id);
		if (clonedPO == null) return null;
		return clonedPO.clone();
	}

	@Override
	public ProductOption loadPOById(UUID id, Boolean lock) {
		return productOptionDAO.findById(id, lock);
	}
	
	@Override
	public ProductOptionValue loadPOVById(UUID id, Boolean lock) {
		return productOptionValueDAO.findById(id, lock);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(final ProductForm productFromPost) {
		if (productFromPost == null) return;
		final Product existingProduct = getProductById(productFromPost.getId());
		if (existingProduct != null) {
			existingProduct.getDefaultSku().setName(productFromPost.getDefaultSku().getName());
			existingProduct.getDefaultSku().setCode(productFromPost.getDefaultSku().getCode());
			String url = productFromPost.getUrl();
			if (url != null && !"".equals(url) && !url.startsWith("/")) url = "/"+url; 
			existingProduct.setUrl(url);
			existingProduct.setOrdering(productFromPost.getOrdering());
			existingProduct.setBrand(productFromPost.getBrand());
			existingProduct.setModel(productFromPost.getModel());
			existingProduct.getDefaultSku().setCostPrice(productFromPost.getDefaultSku().getCostPrice());
			existingProduct.getDefaultSku().setRetailPrice(productFromPost.getDefaultSku().getRetailPrice());
			existingProduct.getDefaultSku().setSalePrice(productFromPost.getDefaultSku().getSalePrice());
			existingProduct.getDefaultSku().setDescription(productFromPost.getDefaultSku().getDescription());
			existingProduct.getDefaultSku().setLongDescription(productFromPost.getDefaultSku().getLongDescription());
			existingProduct.getDefaultSku().setActiveStartDate(productFromPost.getDefaultSku().getActiveStartDate());
			existingProduct.getDefaultSku().setActiveEndDate(productFromPost.getDefaultSku().getActiveEndDate());
			existingProduct.setEnabled(productFromPost.isEnabled());
			existingProduct.setCanSellWithoutOptions(productFromPost.isCanSellWithoutOptions());
			if (!existingProduct.getCategory().equals(productFromPost.getCategory())) {
				//Check attributes for choosed category
				Category ctgFromPost = categoryService.loadCategoryById(productFromPost.getCategory().getId(), false);
				List<AbstractAttributeValue> avCtgFromPost = ctgFromPost.getAllAttributeValues(true);
				Iterator<AbstractAttributeValue> avifp = productFromPost.getAttributeValues().iterator();
				while (avifp.hasNext()) {
					AbstractAttributeValue av = avifp.next();
					if(!avCtgFromPost.contains(av)) avifp.remove();
				}
				//Set category
				existingProduct.setCategory(productFromPost.getCategory());				
			}	
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
			//ProductStatus
			existingProduct.getDefaultSku().setProductStatus(new HashSet<ProductStatus>(productFromPost.getProductStatus()));
			//ProductOption
			Iterator<ProductOption> poi = existingProduct.getProductOptions().iterator();
	        while(poi.hasNext()){
	        	ProductOption p = poi.next();
	        	if (productFromPost.getProductOptionsForForm().contains(p)) {
	        		productFromPost.removeProductOption(p);
	        	} else {
	        		poi.remove();
	        	}
	        	
	        }
	        for (ProductOption poEx : productFromPost.getProductOptionsForForm()) {
	        	existingProduct.addProductOption(poEx);
	        }
	        //Images
	        existingProduct.getDefaultSku().getImages().clear();
			for (String i : productFromPost.getDefaultSku().getImages()) existingProduct.getDefaultSku().getImages().add(i);
		} else {
			productFromPost.setProductStatusFromList();
			productFromPost.setProductOptionFromList();
			Product product = new Product();
			Sku sku = new Sku();
			BeanUtilsBean bu = new NullAwareBeanUtilsBean();
			try {
				bu.copyProperties(product, productFromPost);
				bu.copyProperties(sku, productFromPost.getDefaultSku());
			} catch (IllegalAccessException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			product.setDefaultSku(sku);
	        createProduct(product);
		}
	}
	
	//Sku
	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingSkuAndUpdateOrCreate(final Sku skuFromPost, Errors errors) {
		if (skuFromPost == null) return;
		if (!checkSku(skuFromPost, errors)) return;
		Sku sku = getSkuById(skuFromPost.getId());
		if (sku != null) {
			sku.setName(skuFromPost.getName());
			sku.setActiveEndDate(skuFromPost.getActiveEndDate());
			sku.setActiveStartDate(skuFromPost.getActiveStartDate());
			sku.setCode(skuFromPost.getCode());
			sku.setCostPrice(skuFromPost.getCostPrice());
			sku.setRetailPrice(skuFromPost.getRetailPrice());
			sku.setDescription(skuFromPost.getDescription());
			sku.setLongDescription(skuFromPost.getLongDescription());
			sku.setEnabled(skuFromPost.isEnabled());
			sku.setActiveStartDate(skuFromPost.getActiveStartDate());
			sku.setActiveEndDate(skuFromPost.getActiveEndDate());
	        //Images
			sku.getImages().clear();
			for (String i : skuFromPost.getImages()) sku.getImages().add(i);
			sku.setInventoryType(skuFromPost.getInventoryType());
			sku.setOrdering(skuFromPost.getOrdering());
			
		} else {
			createSku(skuFromPost);
		}
	}
	
	private boolean checkSku(final Sku skuFromPost, Errors errors) {
		Product product = getProductById(skuFromPost.getProduct().getId());
		if (product == null) {
			LOG.error("Product "+skuFromPost.getProduct().getId()+" not found!");
			throw new ProductNotFoundException("Product "+skuFromPost.getProduct().getId()+" not found!");
		}
		for (Sku lsku : product.getAdditionalSku()) {
			if (lsku.equals(skuFromPost)) continue;
			if (isSamePermutation(lsku.getProductOptionValuesList() , skuFromPost.getProductOptionValuesList())) {
				errors.rejectValue("productOptionValuesList", "error.duplicate.optionValue");
			}
		}
		return !errors.hasErrors();
	}
	
	@Override
	public Sku getSkuById(UUID id) {
		return skuDAO.get(id);
	}
	
	@Override
	public Sku cloneSkuById(UUID id) throws CloneNotSupportedException {
		Sku sku = getSkuById(id);
		if (sku == null) return null;
		return sku.clone();
	}
	
	@Override
	public Sku loadSkuById(UUID id, Boolean lock) {
		return skuDAO.findById(id, lock);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void softDeleteSkuById(UUID id) {
		Sku sku = getSkuById(id);
		if (sku != null) {
			sku.setEnabled(false);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteSku(Sku sku) {
		skuDAO.delete(sku);
	}	
		
	@Override
	@Transactional(readOnly = false)
	public Product generateSkusFromProduct(UUID productId) {
        Product product = getProductById(productId);
        
        if (product == null) {
        	LOG.info("Product with Id="+productId.toString()+" not found!");
        	return null;
        }
        
        if (CollectionUtils.isEmpty(product.getProductOptions())) {
            return null;
        }
        
        List<List<ProductOptionValue>> allPermutations = generatePermutations(0, new ArrayList<ProductOptionValue>(), new ArrayList<ProductOption>(product.getProductOptions()));
        LOG.info("Total number of permutations: " + allPermutations.size());
        LOG.info(allPermutations);
        
        //determine the permutations that I already have Skus for
        List<List<ProductOptionValue>> previouslyGeneratedPermutations = new ArrayList<List<ProductOptionValue>>();
        if (CollectionUtils.isNotEmpty(product.getAdditionalSku())) {
            for (Sku additionalSku : product.getAdditionalSku()) {
                if (CollectionUtils.isNotEmpty(additionalSku.getProductOptionValues())) {
                    previouslyGeneratedPermutations.add(new ArrayList<ProductOptionValue>(additionalSku.getProductOptionValues()));
                }
            }
        }
        
        List<List<ProductOptionValue>> permutationsToGenerate = new ArrayList<List<ProductOptionValue>>();
        for (List<ProductOptionValue> permutation : allPermutations) {
            boolean previouslyGenerated = false;
            for (List<ProductOptionValue> generatedPermutation : previouslyGeneratedPermutations) {
                if (isSamePermutation(permutation, generatedPermutation)) {
                    previouslyGenerated = true;
                    break;
                }
            }
            
            if (!previouslyGenerated) {
                permutationsToGenerate.add(permutation);
            }
        }

        
        for (List<ProductOptionValue> permutation : permutationsToGenerate) {
            if (permutation.isEmpty()) continue;
            Sku permutatedSku = new Sku();
            permutatedSku.setName(product.getDefaultSku().getName());
            permutatedSku.setProductOptionValues(new HashSet<ProductOptionValue>(permutation));
            product.addAdditionalSku(permutatedSku);
        }
        return product;
    }

    protected boolean isSamePermutation(List<ProductOptionValue> perm1, List<ProductOptionValue> perm2) {
        if (perm1.size() == perm2.size()) {
            
            Collection<UUID> perm1Ids = CollectionUtils.collect(perm1, new Transformer<ProductOptionValue, UUID>() {
                @Override
                public UUID transform(ProductOptionValue input) {
                    return input != null?((ProductOptionValue) input).getId(): null;
                }
            });
            
            Collection<UUID> perm2Ids = CollectionUtils.collect(perm2, new Transformer<ProductOptionValue, UUID>() {
                @Override
                public UUID transform(ProductOptionValue input) {
                    return  input != null? ((ProductOptionValue) input).getId(): null;
                }
            });
            
            return perm1Ids.containsAll(perm2Ids);
        }
        return false;
    }
    

    public List<List<ProductOptionValue>> generatePermutations(int currentTypeIndex, List<ProductOptionValue> currentPermutation, List<ProductOption> options) {
        List<List<ProductOptionValue>> result = new ArrayList<List<ProductOptionValue>>();
        if (currentTypeIndex == options.size()) {
            result.add(currentPermutation);
            return result;
        }
        
        ProductOption currentOption = options.get(currentTypeIndex);
        if (!currentOption.getUseInSkuGeneration()) {
            //This flag means do not generate skus and so do not create permutations for this productoption, 
            //end it here and return the current list of permutations.
            result.addAll(generatePermutations(currentTypeIndex + 1, currentPermutation, options));
            return result;
        }
        for (ProductOptionValue option : currentOption.getProductOptionValues()) {
            List<ProductOptionValue> permutation = new ArrayList<ProductOptionValue>();
            permutation.addAll(currentPermutation);
            permutation.add(option);
            result.addAll(generatePermutations(currentTypeIndex + 1, permutation, options));
        }
        if (currentOption.getProductOptionValues().size() == 0) {
            //There are still product options left in our array to compute permutations, even though this productOption does not have any values associated.
            result.addAll(generatePermutations(currentTypeIndex + 1, currentPermutation, options));
        }
        
        return result;
    }
	

}

//}
