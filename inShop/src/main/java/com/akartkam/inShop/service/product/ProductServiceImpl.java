package com.akartkam.inShop.service.product;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import com.akartkam.inShop.dao.InstructionDAO;
import com.akartkam.inShop.dao.product.BrandDAO;
import com.akartkam.inShop.dao.product.CategoryDAO;
import com.akartkam.inShop.dao.product.ProductDAO;
import com.akartkam.inShop.dao.product.SkuDAO;
import com.akartkam.inShop.dao.product.option.ProductOptionDAO;
import com.akartkam.inShop.dao.product.option.ProductOptionValueDAO;
import com.akartkam.inShop.domain.Instruction;
import com.akartkam.inShop.domain.product.Brand;
import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.ProductStatus;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.formbean.DataTableForm;
import com.akartkam.inShop.formbean.ProductFilterDTO;
import com.akartkam.inShop.formbean.ProductFilterFacetDTO;
import com.akartkam.inShop.formbean.ProductForm;
import com.akartkam.inShop.formbean.SkuForJSON;
import com.akartkam.inShop.formbean.SkuForm;
import com.akartkam.inShop.service.extension.ProductDisplayNameModificator;
import com.akartkam.inShop.service.order.InventoryService;
import com.akartkam.inShop.util.NullAwareBeanUtilsBean;

@Service("ProductService")
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
	
	private static final Log LOG = LogFactory.getLog(ProductServiceImpl.class);
	
	@Autowired
	private ProductOptionDAO productOptionDAO;

	@Autowired
	private ProductOptionValueDAO productOptionValueDAO;	
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private SkuDAO skuDAO;
		
	@Autowired
	private CategoryService categoryService; 
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ProductDisplayNameModificator productDisplayNameModificator;
	
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private BrandDAO brandDAO;
	@Autowired
	private InstructionDAO instructionDAO;
	
	@Override
	@Transactional(readOnly = false)
	public ProductOption createPO(ProductOption po) {
		return productOptionDAO.create(po);
	}
	
	@Override
	@Transactional(readOnly = false)
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
	public ProductOption getPOByIdForForm(UUID id) {
		ProductOption po = getPOById(id);
		if (po != null) {
			Hibernate.initialize(po.getProductOptionValues());
		}
		return po;
	}

	@Override
	public ProductOptionValue getPOVById(UUID id) {
		return productOptionValueDAO.get(id);
	}

	
	@Override
	public List<Product> getProductsByName(String name) {
		return productDAO.findProductsByName(name);
	}
	
	@Override
	public Product getProductByUrl(String url) {
		return productDAO.findByUrl(url);
	}	
	
	@Override
	public List<Sku> getSkusByName(String name) {
		return skuDAO.findSkusByName(name);
	}
	
	@Override
	public Object[] findSkusByCodeOrNameForPaging(String s, int rowPerPage, int pageNumber){
		return skuDAO.findSkusByCodeOrNameForPaging(s, rowPerPage, pageNumber);
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
			existingPo.setUnit(poFromPost.getUnit());
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
		return productDAO.findAllProducts();
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
		Category c = product.getCategory();
		Brand b = product.getBrand();
		Instruction i = product.getInstruction();
		if (c != null) {
			categoryDAO.reattach(c);
			c.getProducts().remove(product);			
		}
		if(b != null) {
			brandDAO.reattach(b);
			b.getProducts().remove(product);			
		}

		if (i != null){
			instructionDAO.reattach(i);
			i.getProducts().remove(product);
		}
		
		productDAO.delete(product);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingAndUpdateOrCreate(final ProductForm productFromPost, Errors errors) {
		if (productFromPost == null) return;
		if (!errors.hasFieldErrors("category")) productFromPost.buildFullLink(productFromPost.getUrlForForm());
		if (!checkProduct(productFromPost, errors)) return;
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
			existingProduct.getDefaultSku().setActiveStartDate(productFromPost.getDefaultSku().getActiveStartDate());
			existingProduct.getDefaultSku().setActiveEndDate(productFromPost.getDefaultSku().getActiveEndDate());
			existingProduct.getDefaultSku().setQuantityPerPackage(productFromPost.getDefaultSku().getQuantityPerPackage());
			Integer qa = productFromPost.getDefaultSku().getQuantityAvailable();
			if (qa != null) existingProduct.getDefaultSku().setQuantityAvailable(new Integer(qa));
			existingProduct.getDefaultSku().setInventoryType(productFromPost.getDefaultSku().getInventoryType());
			existingProduct.setEnabled(productFromPost.isEnabled());
			existingProduct.getDefaultSku().setEnabled(productFromPost.isEnabled());
			existingProduct.setCanSellWithoutOptions(productFromPost.isCanSellWithoutOptions());
			existingProduct.setInstruction(productFromPost.getInstruction());
			existingProduct.setH1(productFromPost.getH1());
			existingProduct.setMetaTitle(productFromPost.getMetaTitle());
			existingProduct.setMetaDescription(productFromPost.getMetaDescription());
			existingProduct.setMetaKeywords(productFromPost.getMetaKeywords());
			existingProduct.setIsNotShowPriceForUnit(productFromPost.getIsNotShowPriceForUnit() );

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
					if (lavfp.get(idx).getValue() == null || "".equals(lavfp.get(idx).getValue())) {
						avi.remove();
					} else {
						av.setValue(lavfp.get(idx).getValue());
						if (av.getCategory() == null) av.setCategory(existingProduct.getCategory());
					}
					lavfp.remove(idx);
				} else {
					avi.remove();
				}
			}
			for (AbstractAttributeValue av1:lavfp) {
				if (av1.getValue() != null && !"".equals(av1.getValue())) existingProduct.addAttributeValue(av1);
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
			productFromPost.buildFullLink(productFromPost.getUrlForForm());
			Iterator<AbstractAttributeValue> avifp = productFromPost.getAttributeValues().iterator();
			while (avifp.hasNext()) {
				AbstractAttributeValue av = avifp.next();
				if(av.getValue() == null || "".equals(av.getValue())) avifp.remove();
				else av.setCategory(productFromPost.getCategory());
			}
			Product product = new Product();
			Sku sku = new Sku();
			BeanUtilsBean bu = new NullAwareBeanUtilsBean();
			try {
				bu.copyProperties(product, productFromPost);
				bu.copyProperties(sku, productFromPost.getDefaultSku());
			} catch (IllegalAccessException | InvocationTargetException e) {
				LOG.error("",e);
			}
			product.setDefaultSku(sku);
	        createProduct(product);
		}
	}
	
	private boolean checkProduct(final ProductForm productFromPost, Errors errors) {
		if (productFromPost != null) {
			if (productFromPost.getDefaultSku() != null) {
				if (productFromPost.getDefaultSku().getInventoryType() == null) {
					errors.rejectValue("defaultSku.inventoryType", "error.null", 
						   new String[]{messageSource.getMessage("admin.catalog.product.inventoryType",null, Locale.getDefault())}, null);
				}
			}
			if (productFromPost.getUrl() != null && "".equals(productFromPost.getUrl())) {
				Product exProduct = getProductByUrl(productFromPost.getUrl());
				if (!errors.hasFieldErrors("urlForForm") && exProduct != null && !exProduct.getId().equals(productFromPost.getId())) {
					errors.rejectValue("urlForForm", "error.duplicate");				
				}				
			}
		}
		return !errors.hasErrors();
	}	
	
	//Sku
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = false)
	public void mergeWithExistingSkuAndUpdateOrCreate(final SkuForm skuFromPost, Errors errors) {
		if (skuFromPost == null) return;
		Sku sku = getSkuById(skuFromPost.getId());
		if (!checkSku(skuFromPost, errors, (sku == null))) return;
		if (sku != null) {
			sku.setName(skuFromPost.getName());
			sku.setActiveEndDate(skuFromPost.getActiveEndDate());
			sku.setActiveStartDate(skuFromPost.getActiveStartDate());
			sku.setCode(skuFromPost.getCode());
			sku.setCostPrice(skuFromPost.getCostPrice());
			sku.setSalePrice(skuFromPost.getSalePrice());
			sku.setRetailPrice(skuFromPost.getRetailPrice());
			sku.setDescription(skuFromPost.getDescription());
			sku.setLongDescription(skuFromPost.getLongDescription());
			sku.setEnabled(skuFromPost.isEnabled());
			sku.setActiveStartDate(skuFromPost.getActiveStartDate());
			sku.setActiveEndDate(skuFromPost.getActiveEndDate());
			sku.setInventoryType(skuFromPost.getInventoryType());
			sku.setQuantityPerPackage(skuFromPost.getQuantityPerPackage());
			Integer qa = skuFromPost.getQuantityAvailable();
			if (qa != null) sku.setQuantityAvailable(new Integer(qa));	
	        //Images
			sku.getImages().clear();
			for (String i : skuFromPost.getImages()) sku.getImages().add(i);
			sku.setInventoryType(skuFromPost.getInventoryType());
			sku.setOrdering(skuFromPost.getOrdering());
			//Attributes
			List<AbstractAttributeValue> lavfp = skuFromPost.getAttributeValues();			
			Iterator<AbstractAttributeValue> avi = sku.getAttributeValues().iterator();
			while(avi.hasNext()) {
				AbstractAttributeValue av = avi.next();
				int idx = lavfp.indexOf(av);
				if (lavfp.contains(av)) {
					if (lavfp.get(idx).getValue() == null || "".equals(lavfp.get(idx).getValue())) {
						avi.remove();
					} else {
						av.setValue(lavfp.get(idx).getValue());
					}
					lavfp.remove(idx);
				} else {
					avi.remove();
				}
			}
			for (AbstractAttributeValue av1:lavfp) {
				if (av1.getValue() != null && !"".equals(av1.getValue())) sku.addAttributeValue(av1);
			}
		} else {
			Product product = loadProductById(skuFromPost.getProduct().getId(), false);
			skuFromPost.setProductOptionValuesFromList();
			Sku skuC = new Sku();
			BeanUtilsBean bu = new NullAwareBeanUtilsBean();
			try {
				bu.copyProperties(skuC, skuFromPost);
			} catch (IllegalAccessException | InvocationTargetException e) {
				LOG.error("",e);
			}
			product.addAdditionalSku(skuC);
		}
	}
	
	private boolean checkSku(final SkuForm skuFromPost, Errors errors, boolean isNew) {
		Product product = getProductById(skuFromPost.getProduct().getId());
		if (product == null) {
			LOG.error("Product "+skuFromPost.getProduct().getId()+" not found!");
			throw new ProductNotFoundException("Product "+skuFromPost.getProduct().getId()+" not found!");
		}
		if (!errors.hasFieldErrors("productOptionValuesList") && skuFromPost.getProductOptionValuesList().size() != skuFromPost.getProductOptionsList().size()) {
			errors.rejectValue("productOptionValuesList", "error.incomplete.optionValue");
		}
		if (isNew) {
			for (Sku lsku : product.getAdditionalSku()) {
				if (lsku.equals(skuFromPost)) continue;
				if (isSamePermutation(new ArrayList<ProductOptionValue>(lsku.getProductOptionValues()) , skuFromPost.getProductOptionValuesList())) {
					errors.rejectValue("productOptionValuesList", "error.duplicate.optionValue");
				}
			} 
		}
		return !errors.hasErrors();
	}
	
	@Override
	public Sku getSkuById(UUID id) {
		return skuDAO.get(id);
	}
	
	@Override
	public Sku getSkuByIdForForm(UUID id) {
		Sku sku = getSkuById(id);
		if (sku != null) {
			Hibernate.initialize(sku.getProductOptionValues());
		}
		return sku;
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
            permutatedSku.setQuantityPerPackage(product.getDefaultSku().getQuantityPerPackage());
            permutatedSku.setInventoryType(product.getDefaultSku().getInventoryType());
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

	@Override
	public Object[] getSkusForJSONByName(String name, int rowPerPage, int pageNumber) {	
		final String appName = appContext.getApplicationName();
		List<SkuForJSON> ret = new ArrayList<SkuForJSON>();
		Object[] skusArray = findSkusByCodeOrNameForPaging(name, rowPerPage, pageNumber);
		Long totalRows = (Long) skusArray[0];
		List<Sku> skus = (List<Sku>) skusArray[1];
		String pname, images[], code, brand, model, description, productOptions[] = new String[0];
		SkuForJSON.ProductStatusForJSON productStatus[];
		Integer quantityAvailable;
		BigDecimal retailPrice, salePrice, priceForPackage;
		for (Sku sku: skus) {
			if (!inventoryService.isAvailable(sku)) continue;
			productDisplayNameModificator.setSku(sku);
			pname = productDisplayNameModificator.getModifyedDisplayName(sku.lookupName());
			images = new String[0];
			if (sku.getImages() != null && sku.getImages().size() != 0) {
				images= CollectionUtils.collect (sku.getImages(), new Transformer<String, String>() {
 											@Override
							                public String transform(final String input) {
							                    return  input != null && input != ""? appName+input : "";
							                };
				}).toArray(new String[0]);	
			}
			if (images.length == 0) {
				if (sku.getDefaultProduct() == null) {
					Sku dsku = sku.getProduct().getDefaultSku();
					if (dsku != null) {
						images= CollectionUtils.collect (dsku.getImages(), new Transformer<String, String>() {
											@Override
							                public String transform(final String input) {
							                    return  input != null && input != ""? appName+input : "";
							                };
						}).toArray(new String[0]);						
					}
				}
			}
			
			code = sku.getCode();
			brand = "";
			model = "";
			if (sku.getDefaultProduct() != null) { 
				brand = sku.getDefaultProduct().getBrand() != null ? sku.getDefaultProduct().getBrand().getName() : "";
				model = sku.getDefaultProduct().getModel();
			}
			else
			  if (sku.getProduct() != null) { 
				 brand = sku.getProduct().getBrand() != null ? sku.getProduct().getBrand().getName() : "";
				 model = sku.getProduct().getModel();
			  }
			description = sku.getDescription();
			if (description == null || description == "") {
				if (sku.getDefaultProduct() == null) 
					description = sku.getProduct().getDefaultSku().getDescription();
			}
			//NumberFormat money = NumberFormat.getCurrencyInstance();
			retailPrice = sku.getRetailPrice() != null ? new BigDecimal(sku.getRetailPrice().toPlainString()) : null;
			if (retailPrice == null) {
				if (sku.getDefaultProduct() == null) {
					retailPrice = sku.getProduct().getDefaultSku().getRetailPrice() != null ? new BigDecimal(sku.getProduct().getDefaultSku().getRetailPrice().toPlainString()) : null;
				}
			}
			salePrice = sku.getSalePrice() != null ? new BigDecimal(sku.getSalePrice().toPlainString()) : null;
			if (salePrice == null) {
				if (sku.getDefaultProduct() == null) {
					salePrice = sku.getProduct().getDefaultSku().getSalePrice() != null ? new BigDecimal(sku.getProduct().getDefaultSku().getSalePrice().toPlainString()) : null;
				}
			}
			priceForPackage = sku.getPriceForPackage();
			if (sku.getProductStatus().size() != 0) {
				productStatus = new SkuForJSON.ProductStatusForJSON[sku.getProductStatus().size()];
				Iterator<ProductStatus> ips = sku.getProductStatus().iterator();
				int i = 0;
				while (ips.hasNext()) {
					SkuForJSON.ProductStatusForJSON psfjson = new SkuForJSON.ProductStatusForJSON();
					String nm = ips.next().name();
					psfjson.setProductStatus(nm);
					psfjson.setProductStatusDisplayName(messageSource.getMessage("product.status."+nm, null, Locale.getDefault()));
					psfjson.setProductStatusIcon(messageSource.getMessage("product.status.icon."+nm, null, Locale.getDefault()));
					productStatus[i] = psfjson;
					i++;
				}
			} else {
				productStatus = new SkuForJSON.ProductStatusForJSON[0];	
			}
			productOptions = sku.getProductOptionValues() != null && sku.getProductOptionValues().size() != 0 ? sku.getCommaDelemitedPOVL().split(",") : new String[0];
			quantityAvailable = sku.getQuantityAvailable() != null ? new Integer(sku.getQuantityAvailable()) : null;
			if (quantityAvailable == null && sku.getDefaultProduct() == null) 
				 quantityAvailable = sku.getProduct().getDefaultSku().getQuantityAvailable() != null ? new Integer(sku.getProduct().getDefaultSku().getQuantityAvailable()) : null;
 			SkuForJSON sj = new SkuForJSON (sku.getId(), pname,images, code, brand, model, description, retailPrice, salePrice, priceForPackage, quantityAvailable, productStatus, productOptions);
			ret.add(sj);
		}
		
		return new Object[] {totalRows, ret};
	}

	@Override
	public List<Product> getProductsByProductStatus(ProductStatus productStatus) {
		return productDAO.findProductsByProductStatus(productStatus);
	}

	@Override
	public boolean canDeleteSku(UUID id) {
		return skuDAO.canDeleteSku(id);
	}

	@Override
	@Transactional(readOnly = false)
	public void removeAdditionalSku(UUID productId, UUID skuId) {
		Product product = loadProductById(productId, false);
		Sku sku = loadSkuById(skuId, false);
		product.removeAdditionalSku(sku);
		deleteSku(sku);
	}

	@Override
	@Transactional(readOnly = false)
	public void removeAdditionalSkus(UUID productId, List<UUID> skuIds) {
		Product product = loadProductById(productId, false);
		for (UUID skuId : skuIds) {
			Sku sku = loadSkuById(skuId, false);
			product.removeAdditionalSku(sku);
		}
	}

	@Override
	public Object[] getProductsForDataTable(DataTableForm dt) {
		return productDAO.findProductsForDataTable(dt);
	}

	@Override
	public long countTotalProducts() {
		return productDAO.countTotalProducts();
	}

	@Override
	public List<Sku> getActiveSkuList() {
		return skuDAO.findActiveSkuList();
	}

	@Override
	public ProductFilterDTO getFilteredProductByCategory(UUID categoryId) {
		List<Object[]> fProd = productDAO.findFilteredProductByCategory(categoryId);
		ProductFilterDTO res = new ProductFilterDTO();
		List<ProductFilterFacetDTO> lpfBrands = new ArrayList<ProductFilterFacetDTO>();
		List<ProductFilterFacetDTO> lpfModels = new ArrayList<ProductFilterFacetDTO>();
		List<ProductFilterFacetDTO> mpfAttributes = new ArrayList<ProductFilterFacetDTO>();
		for (Object[] fProdRow : fProd) {
			if ((Integer)fProdRow[0] == 0) {
				ProductFilterFacetDTO pfPrand = new ProductFilterFacetDTO();
				pfPrand.setId((String)fProdRow[2]);
				lpfBrands.add(pfPrand);
			} else 
			if ((Integer)fProdRow[0] == 1) {
				ProductFilterFacetDTO pfModel = new ProductFilterFacetDTO();
				pfModel.setId((String)fProdRow[2]);
				lpfModels.add(pfModel);		
			} else
			if ((Integer)fProdRow[0] == 2) {
				ProductFilterFacetDTO pfAttr = new ProductFilterFacetDTO();
				pfAttr.setFacet((String)fProdRow[1]);
				pfAttr.setId((String)fProdRow[2]);
				mpfAttributes.add(pfAttr);
			} else 
			if ((Integer)fProdRow[0] == 3) {
				String sMinP = (String)fProdRow[1];
				String sMaxP = (String)fProdRow[2];
				BigDecimal minPrice = null;
				if (sMinP != null && !"".equals(sMinP)) minPrice = new BigDecimal(sMinP);
				BigDecimal maxPrice = null;
				if (sMaxP != null && !"".equals(sMaxP)) maxPrice = new BigDecimal(sMaxP);
				if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) != 0) {
					res.setMinPrice(minPrice);
					res.setMaxPrice(maxPrice);
				}
			}
		}
		res.setBrandFacets(lpfBrands);
		res.setModelFacets(lpfModels);
		res.setAttributesFacets(mpfAttributes);
		res.setCategoryId(categoryId);
		return res;
	}

}