package com.akartkam.inShop.dao.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.order.OrderItem;
import com.akartkam.inShop.util.Constants;

@Repository
public class OrderItemDAOImpl extends AbstractGenericDAO<OrderItem> implements OrderItemDAO {

	@Override
	public Map<UUID, Integer> findMapOrderItemIdQuantity(Collection<OrderItem> orderItems) {
		List<String> ids = new ArrayList<String>();
		Object[] obj = null;
		Map<UUID, Integer> resMap = new HashMap<UUID, Integer>();
		if (orderItems == null) return resMap;
		for (OrderItem orderItem : orderItems){ 
			ids.add(orderItem.getId().toString());
			resMap.put(orderItem.getId(), new Integer(0));
		}
		List<?> res = currentSession()
				        .createSQLQuery(Constants.SELECT_ORDERITEM_MAP_ID_QUANTITY)
						.addScalar("id", StringType.INSTANCE)
						.addScalar("quantity", IntegerType.INSTANCE)
				        .setParameterList("ids", ids)
				        .list();
		for (Iterator<?> iter = res.iterator(); iter.hasNext();) {
			obj = (Object[]) iter.next();	
			resMap.put(UUID.fromString((String)obj[0]), (Integer)obj[1]);
		}
		return resMap;
	}

}
