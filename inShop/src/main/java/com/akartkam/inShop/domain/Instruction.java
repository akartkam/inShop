package com.akartkam.inShop.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

@NamedNativeQueries({
@NamedNativeQuery(
		name = "findInstructions",
		query = "select i.*, (select string_agg(s.name||'_'||cast(p.id as varchar(36)), ',') from product p, sku s where p.instruction_id=i.id and s.id=p.default_sku_id) products," +
	            "(select string_agg(c.name||'_'||cast(c.id as varchar(36)), ',') from category c where c.instruction_id=i.id) categories " +
	            "from instruction i " +
	            "where (:id is null or cast(i.id as varchar(36))=cast(:id as varchar(36))) and i.enabled=true", resultClass=Instruction.class),  
})

@Entity
@Table(name = "instruction")
public class Instruction extends AbstractDomainObject  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5011359561478384004L;
	private String name;
	private String content;
	
	@NotEmpty
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "long_description", length = Integer.MAX_VALUE - 1)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Transient
	@Override
	public Instruction clone() throws CloneNotSupportedException {
		Instruction instr = (Instruction) super.clone();
		instr.setId(UUID.randomUUID());
		instr.setName(new String(getName()));
		instr.setContent(new String(getContent()));
		return instr;
	}
	

}
