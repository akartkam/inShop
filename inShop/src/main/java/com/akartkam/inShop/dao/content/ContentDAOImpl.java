package com.akartkam.inShop.dao.content;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.config.BeanDefinition;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.content.AbstractContent;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ContentDAOImpl<T extends AbstractContent> extends AbstractGenericDAO<T>
		implements ContentDAO<T> {

}
