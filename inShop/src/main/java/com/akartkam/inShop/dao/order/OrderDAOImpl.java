package com.akartkam.inShop.dao.order;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.dao.AbstractGenericDAO;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.formbean.DataTableForm;

@Repository
public class OrderDAOImpl extends AbstractGenericDAO<Order> implements OrderDAO {

	@Resource
	@Qualifier("orderDataTableColumnsMap")
	private Map<String, String> orderDataTableColumnsMap;
	
	@Override
	public long countTotalOrders() {
		Criteria criteria = currentSession().createCriteria(Order.class)
				.setProjection(Projections.rowCount());
		return (long) criteria.uniqueResult();
	}

	@Override
	public Object[] findOrdersForDataTable(DataTableForm dt) {
		Object[] res = new Object[2];
		StringBuilder s = null;
		String cOrd = "select count(o.id) ", ord = "select o.*";
		StringBuilder query = new StringBuilder();
		query.append(cOrd);
		query.append(" from customer_order o "+ 
					 "       left join customer c on c.id=o.customer_id "+ 
					 "       left join order_status s on s.name=o.order_status ");		
		if (dt.getSearch() != null && dt.getSearch().getValue() != null && !"".equals(dt.getSearch().getValue())) {
			s = new StringBuilder(dt.getSearch().getValue());
			s = s.insert(0, "%").append("%");			
		}
		if (s != null) {
			query.append(String.format(" where o.order_number ilike '%1$s' or to_char(o.submit_date, 'DD.MM.YYYY HH:MI') ilike '%1$s' or "+
                    " (coalesce(c.first_name,'')||' '||coalesce(c.last_name,'')||' '||coalesce(c.middle_name,'')) ilike '%1$s' or " +
					" o.email_address ilike '%1$s' or trim(to_char(order_total, '999999999.99')) ilike '%1$s' or "+
                    " trim(to_char(order_total, '999999999D99')) ilike '%1$s' or s.short_name_r ilike '%1$s' ", s.toString()));
		}
		SQLQuery qcount = currentSession().createSQLQuery(query.toString()); 
		res[0] = ((BigInteger) qcount.uniqueResult()).longValue();
		

		query.delete(0, 18);
		query.insert(0, ord);

		if (dt.getOrder().length > 0) {
		    query.append(" order by ");		
			int i=0;
			for (DataTableForm.Order dtOrder : dt.getOrder()) {
				if (dtOrder.getColumn() != null) {
					if (i > 0) query.append(", "); 
					query.append(orderDataTableColumnsMap.get(dtOrder.getColumn())).append(" ").append(dtOrder.getDir());				
				}
				i++;
			}		
		}
		SQLQuery qquery = currentSession().createSQLQuery(query.toString()); 
		if(dt.getStart() != null) qquery.setFirstResult(dt.getStart());
		if(dt.getLength() != null) qquery.setMaxResults(dt.getLength());
		qquery.addEntity(Order.class);
		res[1] =qquery.list();
		return res;

	}

	@Override
	public List<Object[]> findOrdersByStatus() {
		Query query = currentSession().getNamedQuery("ordersByStatus");
		List<Object[]> ret = (List<Object[]>)query.list();
		return ret;
	}

}
