package com.akartkam.inShop.common;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;
    
@SuppressWarnings({ "serial", "rawtypes" })
public class UUIDStringCustomType extends AbstractSingleColumnStandardBasicType {

    @SuppressWarnings("unchecked")
	public UUIDStringCustomType() {
        super(VarcharTypeDescriptor.INSTANCE, UUIDTypeDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "pg-uuid";
    }

}