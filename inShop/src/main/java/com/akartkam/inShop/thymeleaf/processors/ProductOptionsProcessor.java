package com.akartkam.inShop.thymeleaf.processors;

import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.number.AbstractNumberFormatter;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductOptionsProcessor extends AbstractModelVarModifierProcessor {
	
	private static final Log LOG = LogFactory.getLog(ProductOptionsProcessor.class);
    protected static final Map<Object, String> JSON_CACHE = Collections.synchronizedMap(new LRUMap<Object, String>(500));
	

    private AbstractNumberFormatter currencyNumberFormatter;
	
	public ProductOptionsProcessor() {
		super("product_options");
	}
	
	public AbstractNumberFormatter getCurrencyNumberFormatter() {
		return currencyNumberFormatter;
	}

	public void setCurrencyNumberFormatter(
			AbstractNumberFormatter currencyNumberFormatter) {
		this.currencyNumberFormatter = currencyNumberFormatter;
	}
	
	@Override
    public int getPrecedence() {
        return 19999;
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
                .parseExpression(arguments.getConfiguration(), arguments, element.getAttributeValue("product"));
        Product product = (Product) expression.execute(arguments.getConfiguration(), arguments);
        if (product != null) {
            addPOPricingToModel(arguments, product);
        }
    }
    
    private void addPOPricingToModel(Arguments arguments, Product product) {
        List<Sku> skus = product.getSkus();
        List<POPricingForJSON> poPricing = new ArrayList<POPricingForJSON>();
        for (Sku sku : skus) {
            List<UUID> poValueIds = new ArrayList<UUID>();
            Set<ProductOptionValue> poValues = sku.getProductOptionValues();
            for (ProductOptionValue poValue : poValues) {
            	poValueIds.add(poValue.getId());
            }
            UUID[] values = new UUID[poValueIds.size()];
            poValueIds.toArray(values);
            
            POPricingForJSON oJson = new POPricingForJSON();
            BigDecimal currPriceForUnit, currPriceForPkg, currPriceForUnitOld;
            currPriceForUnit = sku.getPrice();
            currPriceForPkg = sku.getPriceForPackage();
            currPriceForUnitOld = sku.isOnSale()? sku.getRetailPrice(): null;
            oJson.setPriceForUnit(currPriceForUnit == null? null: currencyNumberFormatter.print(currPriceForUnit, Locale.getDefault()));
            oJson.setPriceForUnitOld(currPriceForUnitOld == null? null: currencyNumberFormatter.print(currPriceForUnitOld, Locale.getDefault()));
            oJson.setPriceForPkg(currPriceForPkg == null? null: currencyNumberFormatter.print(currPriceForPkg, Locale.getDefault()));
            oJson.setSelectedOptions(values);
            poPricing.add(oJson);
        }
        writeJSONToModel(arguments, "poPricing", poPricing);
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
            LOG.error("Error writing the product option map to JSON", ex);
        }
    }
    
    @SuppressWarnings("unused")
	private class POPricingForJSON {
        private UUID[] selectedOptions;
        private String priceForUnit;
        private String priceForUnitOld;
        private String priceForPkg;

        public UUID[] getSelectedOptions() {
            return selectedOptions;
        }
        public void setSelectedOptions(UUID[] selectedOptions) {
            this.selectedOptions = selectedOptions;
        }

        public String getPriceForUnit() {
            return priceForUnit;
        }
        public void setPriceForUnit(String priceForUnit) {
            this.priceForUnit = priceForUnit;
        }

        public String getPriceForUnitOld() {
			return priceForUnitOld;
		}
		public void setPriceForUnitOld(String priceForUnitOld) {
			this.priceForUnitOld = priceForUnitOld;
		}
		public String getPriceForPkg() {
            return priceForPkg;
        }
        public void setPriceForPkg(String priceForPkg) {
            this.priceForPkg = priceForPkg;
        }
        
        
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null) return false;
            if (!getClass().isAssignableFrom(o.getClass())) return false;
            POPricingForJSON that = (POPricingForJSON) o;
            if (priceForUnit != null ? !priceForUnit.equals(that.priceForUnit) : that.priceForUnit != null) return false;
            if (priceForUnitOld != null ? !priceForUnitOld.equals(that.priceForUnitOld) : that.priceForUnitOld != null) return false;
            if (priceForPkg != null ? !priceForPkg.equals(that.priceForPkg) : that.priceForPkg != null) return false;
            if (!Arrays.equals(selectedOptions, that.selectedOptions)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = selectedOptions != null ? Arrays.hashCode(selectedOptions) : 0;
            result = 31 * result + (priceForUnit != null ? priceForUnit.hashCode() : 0);
            result = 31 * result + (priceForUnitOld != null ? priceForUnitOld.hashCode() : 0);
            result = 31 * result + (priceForPkg != null ? priceForPkg.hashCode() : 0);
            return result;
        }
    }    

}
