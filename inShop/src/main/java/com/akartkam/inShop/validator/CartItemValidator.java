package com.akartkam.inShop.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.exception.AddToCartException;
import com.akartkam.inShop.exception.InventoryUnavailableException;
import com.akartkam.inShop.exception.ProductNotFoundException;
import com.akartkam.inShop.exception.RequiredAttributeNotProvidedException;
import com.akartkam.inShop.exception.SkuNotFoundException;
import com.akartkam.inShop.formbean.CartItemForm;
import com.akartkam.inShop.service.extension.EntityUrlModificator;
import com.akartkam.inShop.service.extension.ProductDisplayNameModificator;
import com.akartkam.inShop.service.order.InventoryService;
import com.akartkam.inShop.service.product.ProductService;

@Component
public class CartItemValidator implements Validator {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private ProductDisplayNameModificator productDisplayNameModificator;
	
	@Autowired
	private EntityUrlModificator entityUrlModificator;
	
	@Override
	public boolean supports(Class<?> arg0) {
		return CartItemForm.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object arg0, Errors error) {
		CartItemForm cartItem = (CartItemForm) arg0;
		if (cartItem.getQuantity()<= 0) {
			error.rejectValue("quantity", "error.bad.cartItemForm.quantity", new String[] {String.valueOf(cartItem.getQuantity())}, null);
		}
		String producrId = cartItem.getProductId();
		Product product = null;
		if (producrId != null && !"".equals(producrId.trim())) {
			try {
				product = productService.getProductById(UUID.fromString(producrId));
			} catch (IllegalArgumentException e) {
				error.rejectValue("productId", "error.bad.cartItemForm.productId", new String[] {producrId}, null);
			}
		} else {
			error.rejectValue("productId", "error.bad.cartItemForm.productId");
		}
		try{	
			if (product != null && !error.hasErrors()) {
				Sku sku = determineSku(product, cartItem.getItemAttributes());
				if (sku == null) {
		            StringBuilder sb = new StringBuilder();
		            for (Entry<String, String> entry : cartItem.getItemAttributes().entrySet()) {
		                sb.append(entry.toString());
		            }
		            throw new SkuNotFoundException("Could not find SKU for :" +
		                    " productId: " + (product == null ? "null" : product.getId()) + 
		                    " skuId: " + cartItem.getSkuId() + 
		                    " attributes: " + sb.toString());
		        };
		        boolean isAvailable = inventoryService.isQuantityAvailable(sku, cartItem.getQuantity() + cartItem.getFullQuantityOnCart());
		        if (!isAvailable) 
		        	throw new InventoryUnavailableException("The referenced Sku " + sku.getId() + " is marked as unavailable, or an insufficient amount",
		        			                                 sku.getId(), cartItem.getQuantity(), inventoryService.retrieveQuantityAvailable(sku));
	        	cartItem.setSkuId(sku.getId().toString());
	        	entityUrlModificator.setEntity(product);
	        	cartItem.setProductUrl(entityUrlModificator.getPrefixedUrl(product.getUrl()));
	        	productDisplayNameModificator.setSku(sku);
	        	cartItem.setProductName(productDisplayNameModificator.getModifyedDisplayName(sku.lookupName()));
	        	cartItem.setPriceForUnit(sku.getPrice());
	        	cartItem.setPrice(sku.getPriceForPackage());
	        	cartItem.setImageUrl(!sku.getImages().isEmpty()? sku.getImages().get(0): !product.getAllImages().isEmpty()? product.getAllImages().get(0): null);
	        	cartItem.setSku(sku);
	        	cartItem.setIsNotShowPriceForUnit(product.getIsNotShowPriceForUnit());
	        	
			} else if (product == null) {
				throw new ProductNotFoundException("The referenced Product "+producrId+" could not found.");
			}
		} catch (ProductNotFoundException | RequiredAttributeNotProvidedException | SkuNotFoundException | InventoryUnavailableException e) {
			throw new AddToCartException("Could not add to cart", e);
		}


    }
	
	private Sku determineSku(Product product, Map<String, String> attributeValues) {
    Sku sku = findMatchingSku(product, attributeValues);

    if (sku == null && product != null) {
        // Set to the default sku
        if (product.hasAdditionalSkus() /*&& !product.isCanSellWithoutOptions()*/) {
            throw new RequiredAttributeNotProvidedException("Unable to find non-default sku matching given options and cannot sell default sku", null);
        }
        sku = product.getDefaultSku();
    }
    return sku;
    }
 	
    private Sku findMatchingSku(Product product, Map<String, String> attributeValues) {
        Map<String, String> attributeValuesForSku = new HashMap<String,String>();
        // Verify that required product-option values were set.
        if (product != null && product.getProductOptions().size() > 0) {
            for (ProductOption productOption : product.getProductOptions()) {
                if (productOption.getRequired()) {
                    if (StringUtils.isEmpty(attributeValues.get(productOption.getName()))) {
                        throw new RequiredAttributeNotProvidedException("Unable to add to product ("+ product.getId() +") cart. Required attribute was not provided: " + productOption.getName());
                    } else if (productOption.getUseInSkuGeneration()) {
                        attributeValuesForSku.put(productOption.getName(), attributeValues.get(productOption.getName()));
                    }
                }
            }
            if (product !=null && product.getSkus() != null) {
                for (Sku sku : product.getSkus()) {
                   if (checkSkuForMatch(sku, attributeValuesForSku)) {
                       return sku;
                   }
                }
            }
        }

        return null;
    }
    
    private boolean checkSkuForMatch(Sku sku, Map<String,String> attributeValues) {
        if (attributeValues == null || attributeValues.size() == 0) {
            return false;
        }

        for (String attributeName : attributeValues.keySet()) {
            boolean optionValueMatchFound = false;
            for (ProductOptionValue productOptionValue : sku.getProductOptionValues()) {
                if (productOptionValue.getProductOption().getName().equals(attributeName)) {
                    if (productOptionValue.getOptionValue().equals(attributeValues.get(attributeName))) {
                        optionValueMatchFound = true;
                        break;
                    } else {
                        return false;
                    }
                }
            }

            if (optionValueMatchFound) {
                continue;
            } else {
                return false;
            }
        }

        return true;
    }    
	

}
