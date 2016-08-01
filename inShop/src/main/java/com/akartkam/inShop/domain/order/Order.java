
package com.akartkam.inShop.domain.order;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.customer.Customer;
import com.akartkam.inShop.domain.product.Sku;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

@Entity
@Table(name = "Customer_order")
public class Order extends AbstractDomainObjectOrdering {

	private static final long serialVersionUID = 4901621243351863111L;

	private String name;
	private Customer customer;
	private OrderStatus status;
	private BigDecimal subTotal;
    private BigDecimal total;
    private Date submitDate;
    private String orderNumber;
    private String emailAddress;
    protected List<OrderItem> orderItems = new ArrayList<OrderItem>();


    @Column(name = "order_subtotal", precision=19, scale=5)
	@NumberFormat(style=Style.CURRENCY)
	@DecimalMin("0.01")	
	@Digits(fraction = 5, integer = 14)
    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    @Transient
    public BigDecimal calculateSubTotal() {
    	BigDecimal calculatedSubTotal = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            calculatedSubTotal = calculatedSubTotal.add(orderItem.getTotalPrice());
        }
        return calculatedSubTotal;
    }


    @Column(name = "order_total", precision=19, scale=5)
	@NumberFormat(style=Style.CURRENCY)
	@DecimalMin("0.01")	
	@Digits(fraction = 5, integer = 14)
    public BigDecimal getTotal() {
        return total;
    }


    public void setTotal(BigDecimal orderTotal) {
        this.total = orderTotal;
    }

	@DateTimeFormat(pattern="${date.format}")
    @Column(name = "submit_date")
    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }


    @ManyToOne(optional=false)
    @JoinColumn(name = "customer_id", nullable = false)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL})
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

	@Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "order_number")
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

	@Email
    @Column(name = "email_address")
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    @Transient
    public boolean isContainsSku(Sku sku) {
    	if (sku == null) return false;
    	for (OrderItem oi :  getOrderItems()) {
    		if (oi.getSku().equals(sku)) return true;
    	}
    	return false;
    }

}
