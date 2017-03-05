package com.akartkam.inShop.thymeleaf.processors;

import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.number.AbstractNumberFormatter;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AdditionalSkuAttributeValuesProcessor extends AbstractModelVarModifierProcessor {
	
	private static final Log LOG = LogFactory.getLog(AdditionalSkuAttributeValuesProcessor.class);
    protected static final Map<Object, String> JSON_CACHE = Collections.synchronizedMap(new LRUMap<Object, String>(500));
	
    private MessageSource messageSource;

	public AdditionalSkuAttributeValuesProcessor() {
		super("addtitional_sku_attr_values");
	}
	
	public MessageSource getMessageSource() {
		return messageSource;
	}
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}


	@Override
    public int getPrecedence() {
        return 200002;
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue("product"));
        Product product = (Product) expression.execute(arguments.getConfiguration(), arguments);
        if (product != null) {
            addAdditSkuAttrValuesToModel(arguments, product);
        }
    }
    
    private void addAdditSkuAttrValuesToModel(Arguments arguments, Product product) {
        List<Sku> skus = product.getSkus();
        List<AdditSkuAttrValuesForJSON> additSkuAttrValues = new ArrayList<AdditSkuAttrValuesForJSON>();
        for (Sku sku : skus) {
            List<UUID> poValueIds = new ArrayList<UUID>();
            Set<ProductOptionValue> poValues = sku.getProductOptionValues();
            for (ProductOptionValue poValue : poValues) {
            	poValueIds.add(poValue.getId());
            }
            UUID[] values = new UUID[poValueIds.size()];
            poValueIds.toArray(values);
            
            AdditSkuAttrValuesForJSON oJson = new AdditSkuAttrValuesForJSON();
            for (AbstractAttributeValue av : sku.getAttributeValues()) {
            	AbstractAttribute at = av.getAttribute();
            	String[] atv = new String[2];
            	String avl = av.getStringValue();
            	atv[0] = avl;
            	atv[1] = messageSource.getMessage("unit."+at.getUnit().name(), null, Locale.getDefault());;
            	oJson.getAttr().put(at.getId().toString(), atv);
            }
            oJson.setSelectedOptions(values);
            additSkuAttrValues.add(oJson);
        }
        writeJSONToModel(arguments, "additSkuAttrValues", additSkuAttrValues);
    }    
    
    private void writeJSONToModel(Arguments arguments, String modelKey, Object o) {
        try {
            String jsonValue = JSON_CACHE.get(o);
            if (jsonValue == null) {
                ObjectMapper mapper = new ObjectMapper();
                Writer strWriter = new StringWriter();
                mapper.writeValue(strWriter, o);
                jsonValue = strWriter.toString();
                JSON_CACHE.put(o, jsonValue);
            }
            addToModel(arguments, modelKey, jsonValue);
        } catch (Exception ex) {
            LOG.error("Error writing the sku attr map to JSON", ex);
        }
    }
    
    @SuppressWarnings("unused")
	private class AdditSkuAttrValuesForJSON {
        private UUID[] selectedOptions;
        private Map<String, String[]> attr = new HashMap<String, String[]>();

        public UUID[] getSelectedOptions() {
            return selectedOptions;
        }
        public void setSelectedOptions(UUID[] selectedOptions) {
            this.selectedOptions = selectedOptions;
        }
        
		public Map<String, String[]> getAttr() {
			return attr;
		}
		public void setAttr(Map<String, String[]> attr) {
			this.attr = attr;
		}
		@Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (!getClass().isAssignableFrom(o.getClass())) return false;
            AdditSkuAttrValuesForJSON that = (AdditSkuAttrValuesForJSON) o;
            if (attr != null ? !attr.equals(that.attr) : that.attr != null) return false;
            if (!Arrays.equals(selectedOptions, that.selectedOptions)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = selectedOptions != null ? Arrays.hashCode(selectedOptions) : 0;
            result = 31 * result + (attr.keySet() != null ? attr.keySet().hashCode() : 0);
            result = 31 * result + (attr.values() != null ? attr.values().hashCode() : 0);
            return result;
        }
    }    

}
