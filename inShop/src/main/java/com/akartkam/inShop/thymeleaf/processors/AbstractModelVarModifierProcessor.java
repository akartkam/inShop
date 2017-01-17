package com.akartkam.inShop.thymeleaf.processors;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractModelVarModifierProcessor extends AbstractElementProcessor {

    public AbstractModelVarModifierProcessor(String elemName) {
        super(elemName);
    }

    @Override
    public int getPrecedence() {
        return 1111;
    }
	
	@Override
	protected ProcessorResult processElement(Arguments arg0, Element arg1) {
        modifyModelAttributes(arg0, arg1);
        final NestableNode parent = arg1.getParent();
        parent.removeChild(arg1);       
        return ProcessorResult.OK;
	}
	
    @SuppressWarnings("unchecked")
    protected void addToModel(Arguments arguments, String key, Object value) {
        ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).put(key, value);
    }
    
    @SuppressWarnings("unchecked")
    protected <T> void addCollectionToExistingSet(Arguments arguments, String key, Collection<T> value) {
        Set<T> items = (Set<T>) ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).get(key);
        if (items == null) {
            items = new HashSet<T>();
            ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).put(key, items);
        }
        items.addAll(value);
    }

    @SuppressWarnings("unchecked")
    protected <T> void addItemToExistingSet(Arguments arguments, String key, Object value) {
        Set<T> items = (Set<T>) ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).get(key);
        if (items == null) {
            items = new HashSet<T>();                         
            ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).put(key, items);
        }
        items.add((T) value);
    }
    
    protected abstract void modifyModelAttributes(Arguments arguments, Element element);	


}
