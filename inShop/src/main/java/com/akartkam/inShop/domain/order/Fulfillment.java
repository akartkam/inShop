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
	private String lastName, firstName, middleName, phone, address;
	private BigDecimal deliveryPrice;
    private String customerMessage;

    
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
	
	@NotEmpty
	@Size(min = 1, max = 50)
	@Column(name = "last_name")	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@NotEmpty
	@Size(min = 1, max = 50)
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
	
	

}
