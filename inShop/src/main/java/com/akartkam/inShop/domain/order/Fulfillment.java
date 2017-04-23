package com.akartkam.inShop.domain.order;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.akartkam.inShop.domain.AbstractDomainObject;
import com.akartkam.inShop.formbean.CartItemForm;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;

@Entity
@Table(name = "Fulfillment")
public class Fulfillment extends AbstractDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4466127935412937535L;
	private Order order;
	private Delivery delivery;
	private Store store;
	private String buy1clickName, lastName, firstName, middleName, phone, city, address;
	private BigDecimal deliveryPrice;
    private String customerMessage;

    @Column(name="buy1click_name")
    public String getBuy1clickName() {
		return buy1clickName;
	}
	public void setBuy1clickName(String buy1clickName) {
		this.buy1clickName = buy1clickName;
	}
	@Column(name="phone")
    public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column(name="cust_message")
    public String getCustomerMessage() {
		return customerMessage;
	}
	public void setCustomerMessage(String customerMessage) {
		this.customerMessage = customerMessage;
	}
	
	@Column(name="delivery_price")
	public BigDecimal getDeliveryPrice() {
		return deliveryPrice;
	}
	public void setDeliveryPrice(BigDecimal deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}
	
	@ManyToOne
	@JoinColumn
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
    @OneToOne
    @JoinColumn(name="delivery_id")	
	public Delivery getDelivery() {
		return delivery;
	}
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

    @OneToOne
    @JoinColumn(name="store_id")
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	
	@Column(name = "last_name")	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(name = "first_name")	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "middle_name")
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	@Column(name = "address")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "city")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((delivery == null) ? 0 : delivery.hashCode());
		result = prime * result + ((store == null) ? 0 : store.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		return result;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Fulfillment))
		      return false;		
		if (getClass() != obj.getClass())
			return false;
		Fulfillment other = (Fulfillment) obj;
		if (delivery == null) {
			if (other.delivery != null)
				return false;
		} else if (!delivery.equals(other.delivery))
			return false;		
		if (store == null) {
			if (other.store != null)
				return false;
		} else if (!store.equals(other.store))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;		
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;		
		if (middleName == null) {
			if (other.middleName != null)
				return false;
		} else if (!middleName.equals(other.middleName))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;		
		return true;		
	}

}
