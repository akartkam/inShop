package com.akartkam.inShop.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.product.Category;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.validator.HtmlSafe;

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
	private List<Category> categories = new ArrayList<Category>();
	private List<Product> products = new ArrayList<Product>();
	
	@NotEmpty
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@HtmlSafe
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "content", length = Integer.MAX_VALUE - 1)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	@OneToMany(mappedBy="instruction")
	@BatchSize(size = 50)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	@OneToMany(mappedBy="instruction")
	@BatchSize(size = 50)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
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
