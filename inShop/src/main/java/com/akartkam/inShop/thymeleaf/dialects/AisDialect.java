package com.akartkam.inShop.thymeleaf.dialects;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import com.akartkam.inShop.service.extension.EntityUrlModificator;
import com.akartkam.inShop.service.extension.ProductDisplayNameModificator;
import com.akartkam.inShop.thymeleaf.processors.AbstractModelVarModifierProcessor;
import com.akartkam.inShop.thymeleaf.processors.AdditionalSkuAttributeValuesProcessor;
import com.akartkam.inShop.thymeleaf.processors.EntityUrlPrefixHrefProcessor;
import com.akartkam.inShop.thymeleaf.processors.ProductDisplayNameModifyerProcessor;
import com.akartkam.inShop.thymeleaf.processors.ProductOptionsProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.format.number.AbstractNumberFormatter;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.processor.IProcessor;

public class AisDialect extends AbstractDialect {
	
	@Autowired
	private ProductDisplayNameModificator productDisplayNameModificator;
	
	@Autowired
    private AbstractNumberFormatter currencyNumberFormatter;
	
	@Autowired
	private EntityUrlModificator entityUrlModificator;
	
	@Autowired
	private MessageSource messageSource;
	
	public AisDialect() {
		super();
	}

	@Override
	public String getPrefix() {
		return "aid";
	}
	
	@Override
    public Set<IProcessor> getProcessors(){
		final Set<IProcessor> processors = new HashSet<IProcessor>();
		ProductDisplayNameModifyerProcessor productDisplayNameModifyerProcessor = new ProductDisplayNameModifyerProcessor();
		productDisplayNameModifyerProcessor.setProductDisplayNameModificator(productDisplayNameModificator);
		EntityUrlPrefixHrefProcessor entityUrlPrefixHrefProcessor = new EntityUrlPrefixHrefProcessor();
		entityUrlPrefixHrefProcessor.setEntityUrlModificator(entityUrlModificator);
		ProductOptionsProcessor modVarModifProc = new ProductOptionsProcessor();
		modVarModifProc.setCurrencyNumberFormatter(currencyNumberFormatter);
		AdditionalSkuAttributeValuesProcessor additSkuAttribValProc = new AdditionalSkuAttributeValuesProcessor();
		additSkuAttribValProc.setMessageSource(messageSource);
		processors.add(productDisplayNameModifyerProcessor);
		processors.add(entityUrlPrefixHrefProcessor);
		processors.add(modVarModifProc);
		processors.add(additSkuAttribValProc);
		return processors;
		
	}

}
