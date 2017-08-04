package com.akartkam.inShop.dao.content;

import java.util.UUID;

import com.akartkam.inShop.dao.GenericDAO;
import com.akartkam.inShop.domain.content.AbstractContent;

public interface ContentDAO<T extends AbstractContent> extends GenericDAO<T, UUID> {

}
