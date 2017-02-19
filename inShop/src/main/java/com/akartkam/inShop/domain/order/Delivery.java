package com.akartkam.inShop.domain.order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.validator.HtmlSafe;

@Entity
@Table(name = "Delivery")
public class Delivery  extends AbstractDomainObjectOrdering {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1811489750810777317L;
	
	private String name;
	private String longDescription;
	private DeliveryType deliveryType;
	private List<Store> stores = new ArrayList<Store>();
	private Boolean isPublic=false; 

	@NotEmpty
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@HtmlSafe
    @Lob
    @Type(type = "org.hibernate.type.TextType")	
    @Column(name = "long_description", length = Integer.MAX_VALUE - 1)	
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_type")	
	public DeliveryType getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}
	
	@OneToMany	
	@JoinTable(
			name = "Delivery_stores",
			joinColumns = {@JoinColumn(name = "delivery_id")},
			inverseJoinColumns = {@JoinColumn(name = "store_id")}
			)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	public List<Store> getStores() {
		return stores;
	}
	public void setStores(List<Store> stores) {
		this.stores = stores;
	}
	
	@Column(name="is_public")
	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	@Override
	@Transient
	public Delivery clone() throws CloneNotSupportedException {
		Delivery delivery = (Delivery) super.clone();
		delivery.setId(UUID.randomUUID());
		delivery.setName(new String(getName()));
		delivery.setCreatedBy(null);
		delivery.setCreatedDate(null);
		delivery.setUpdatedBy(null);
		delivery.setUpdatedDate(null);
		delivery.setLongDescription(getLongDescription() != null? new String(getLongDescription()): null);
		delivery.setDeliveryType(getDeliveryType());
		return delivery;
	}
	
	public void addStore (Store store) {
		if (store != null) {
			getStores().add(store);
		}
	}
	
	public void removeStore (Store store) {
		if (store != null) {
			getStores().remove(store);
		}
	}
	
}
