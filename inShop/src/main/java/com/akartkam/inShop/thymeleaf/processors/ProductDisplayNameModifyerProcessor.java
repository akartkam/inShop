package com.akartkam.inShop.thymeleaf.processors;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractUnescapedTextChildModifierAttrProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.service.extension.ProductDisplayNameModificator;

public class ProductDisplayNameModifyerProcessor extends
		AbstractUnescapedTextChildModifierAttrProcessor {
	

	private ProductDisplayNameModificator productDisplayNameModificator;
	
	public ProductDisplayNameModificator getProductDisplayNameModificator() {
		return productDisplayNameModificator;
	}

	public void setProductDisplayNameModificator(
			ProductDisplayNameModificator productDisplayNameModificator) {
		this.productDisplayNameModificator = productDisplayNameModificator;
	}

	public ProductDisplayNameModifyerProcessor() {
		super("product_modifyed_display_name_utext");
	}
	
	@Override
	protected String getText(final Arguments arguments, final Element element, final String attributeName) {
	      final Configuration configuration = arguments.getConfiguration();
	      final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
	      final String attributeValue = element.getAttributeValue(attributeName);
	      final IStandardExpression expression = parser.parseExpression(configuration, arguments, attributeValue);
	      final Sku sku = (Sku) expression.execute(configuration, arguments);
	      productDisplayNameModificator.setSku(sku);
		return productDisplayNameModificator.getModifyedDisplayName(sku.lookupName());
	}

	@Override
	public int getPrecedence() {
		return 12000;
	}

}
