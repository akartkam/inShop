package com.akartkam.inShop.thymeleaf.processors;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;
import org.thymeleaf.standard.expression.Expression;
import org.thymeleaf.standard.expression.StandardExpressions;

import com.akartkam.inShop.domain.AbstractWebDomainObject;
import com.akartkam.inShop.service.extension.EntityUrlModificator;


public class EntityUrlPrefixHrefProcessor extends AbstractAttributeModifierAttrProcessor {

    private static final String PHREF = "phref";
    private static final String HREF = "href";
    private EntityUrlModificator entityUrlModificator;
    
	public EntityUrlModificator getEntityUrlModificator() {
		return entityUrlModificator;
	}

	public void setEntityUrlModificator(EntityUrlModificator entityUrlModificator) {
		this.entityUrlModificator = entityUrlModificator;
	}

	public EntityUrlPrefixHrefProcessor() {
		super(PHREF);
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
	    
	     Object obj = expression.execute(arguments.getConfiguration(), arguments);
	     String attrValue = "";
	     if (obj instanceof AbstractWebDomainObject) {
	    	 entityUrlModificator.setEntity((AbstractWebDomainObject)obj);
	    	 String prefixedPath = entityUrlModificator.getPrefixedUrl(((AbstractWebDomainObject) obj).getUrl());
	    	 String fullPath = "@{"+prefixedPath+"}";
			 expression = (Expression) StandardExpressions.getExpressionParser(arguments.getConfiguration())
		                  .parseExpression(arguments.getConfiguration(), arguments, fullPath);	    
			 attrValue = (String) expression.execute(arguments.getConfiguration(), arguments);
	     } else {
	         attrValue = obj.toString();
	     }

	     attrs.put(HREF, attrValue);
	        
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
