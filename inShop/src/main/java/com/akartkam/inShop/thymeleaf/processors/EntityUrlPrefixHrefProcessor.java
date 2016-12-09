package com.akartkam.inShop.thymeleaf.processors;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

public class EntityUrlPrefixHrefProcessor extends AbstractAttributeModifierAttrProcessor {

    private static final String PHREF = "phref";
    private static final String HREF = "href";
    
    private Properties urlPrefixes;
	
	protected EntityUrlPrefixHrefProcessor() {
		super(PHREF);
	}
	
	public Properties getUrlPrefixes() {
		return urlPrefixes;
	}

	public void setUrlPrefixes(Properties urlPrefixes) {
		this.urlPrefixes = urlPrefixes;
	}


	@Override
	protected ModificationType getModificationType(Arguments arg0,
			Element arg1, String arg2, String arg3) {
		return ModificationType.SUBSTITUTION;
	}

	@Override
	protected Map<String, String> getModifiedAttributeValues(Arguments arguments, Element element, String attributeName) {
		 String elementValue = element.getAttributeValue(attributeName);
		 Map<String, String> attrs = new HashMap<String, String>();
	     Expression expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
	                .parseExpression(arguments.getConfiguration(), arguments, elementValue);
	     
		 Object entity = expression.execute(arguments.getConfiguration(), arguments);
		 String currentPrefix = urlPrefixes.getProperty(entity.getClass().getCanonicalName(), "");
		 StringBuilder prefixedPath = new StringBuilder();
		 //prefixedPath.append(currentPrefix.startsWith("/")? currentPrefix: "/"+currentPrefix).
		 //             append()
		 
	     
	     String prefixedPath1 = (String) expression.execute(arguments.getConfiguration(), arguments);

	     
	     
	     //attrs.put(HREF, prefixedPath);
	        
	     return attrs;
	}

	@Override
	protected boolean recomputeProcessorsAfterExecution(Arguments arg0,
			Element arg1, String arg2) {
		return false;
	}

	@Override
	protected boolean removeAttributeIfEmpty(Arguments arg0, Element arg1,
			String arg2, String arg3) {
		return false;
	}

	@Override
	public int getPrecedence() {
		return 12001;
	}

}
