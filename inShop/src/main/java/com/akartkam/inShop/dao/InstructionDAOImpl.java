package com.akartkam.inShop.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import com.akartkam.inShop.domain.Instruction;

@Repository
public class InstructionDAOImpl extends AbstractGenericDAO<Instruction> implements InstructionDAO {

	@Override
	public List<Object[]> findInstruction(UUID id) {
		List<Object[]> res = null;
		Query q = currentSession().createSQLQuery("select {i.*}, (select string_agg(s.name||'_'||cast(p.id as varchar(36)), ',') from product p, sku s where p.instruction_id=i.id and s.id=p.default_sku_id) products," +
	            "(select string_agg(c.name||'_'||cast(c.id as varchar(36)), ',') from category c where c.instruction_id=i.id) categories " +
	            "from instruction {i} " +
	            "where (:id is null or cast(i.id as varchar(36))=cast(:id as varchar(36))) and i.enabled=true")
	              .addEntity("i", Instruction.class)
	              .addScalar("products", StandardBasicTypes.STRING)
	              .addScalar("categories", StandardBasicTypes.STRING)
				  .setParameter("id", id != null? id.toString(): null);
		res = q.list();
		return res;
	}
		
}
