package com.akartkam.inShop.dao.product.option;


import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;

@Repository
public class ProductOptionDAOImpl extends AbstractGenericDAO<ProductOptionValue>
		implements ProductOptionValueDAO {

}
