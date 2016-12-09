package com.akartkam.inShop.thymeleaf.dialects;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import com.akartkam.inShop.service.extension.ProductDisplayNameModificator;
import com.akartkam.inShop.thymeleaf.processors.ProductDisplayNameModifyerProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.processor.IProcessor;

public class AisDialect extends AbstractDialect {
	
	@Autowired
	private ProductDisplayNameModificator productDisplayNameModificator;
	
	@Autowired
	@Qualifier("entityUrlPrefixes")
	private Properties urlPrefixes;
	
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
		processors.add(productDisplayNameModifyerProcessor);
		return processors;
		
	}

}
