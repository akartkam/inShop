
package com.akartkam.inShop.domain.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.akartkam.inShop.domain.AbstractDomainObjectOrdering;
import com.akartkam.inShop.domain.customer.Customer;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.formatter.CurrencyFormat;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

@SqlResultSetMappings({
    @SqlResultSetMapping(name = "ordersByStatusRSM", columns = {
        @ColumnResult(name = "status"), @ColumnResult(name = "count_orders"),
        @ColumnResult(name = "sum_order_total"), @ColumnResult(name = "sum_delivery"), 
        @ColumnResult(name = "order_status")})        
})

@NamedNativeQueries({
	@NamedNativeQuery(
			name = "ordersByStatus",
			query = "select 'Всего' status, count(o.id) count_orders, sum(o.order_total) sum_order_total, "+
					" sum(f.delivery_price) sum_delivery, '' as order_status"+
					" from customer_order o "+
					"      left join fulfillment f on f.order_id=o.id " +
					" union all "+
					"select s.short_name_r status, count(o.id) count_orders, sum(o.order_total) sum_order_total, "+
					"sum(f.delivery_price) sum_delivery, s.name as order_status"+
					" from customer_order o "+
					"      left join order_status s on s.name=o.order_status "+
					"      left join fulfillment f on f.order_id=o.id "+
					" group by 1,5",
			resultSetMapping = "ordersByStatusRSM")	
})

@Entity
@Table(name = "Customer_order")
public class Order extends AbstractDomainObjectOrdering {

	private static final long serialVersionUID = 4901621243351863111L;

	private String name;
	private Customer customer;
	private OrderStatus status = OrderStatus.NEW;
	private BigDecimal subTotal;
    private BigDecimal total;
    private BigDecimal deliveryTotal;    
    private DateTime submitDate;
    private String orderNumber;
    private String emailAddress;
    protected List<OrderItem> orderItems = new ArrayList<OrderItem>();
    private List<Fulfillment> fulfillment = new ArrayList<Fulfillment>(); 

    @Column(name = "order_subtotal", precision=19, scale=5)
	@CurrencyFormat
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
            calculatedSubTotal = calculatedSubTotal.add(orderItem.getRowTotal());
        }
        return calculatedSubTotal;
    }

    @Transient
    public BigDecimal calculateTotal() {
    	BigDecimal res = calculateSubTotal();
    	res = res.add(calculateDelivaryTotal());
        return res;
    }
    
    @Transient
    public BigDecimal calculateDelivaryTotal() {
    	BigDecimal res = BigDecimal.ZERO;
    	Fulfillment fl = getActualFulfillment();
    	if (fl != null && fl.getDeliveryPrice() != null) res = res.add(fl.getDeliveryPrice());
    	return res;
    }
   
    
    @Column(name = "order_total", precision=19, scale=5)
    @CurrencyFormat	
	@Digits(fraction = 5, integer = 14)
    public BigDecimal getTotal() {
        return total;
    }


    public void setTotal(BigDecimal orderTotal) {
        this.total = orderTotal;
    }
  
    @NotNull
    @DateTimeFormat(pattern="${date.formatshort}")
    @Column(name = "submit_date")
    @Past
	public DateTime getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(DateTime submitDate) {
        this.submitDate = submitDate;
    }

    @ManyToOne
    @JoinColumn(name = "customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    @NotNull
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @AdminPresentation(tab=EditTab.CONTENT)
    @Valid
    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL}, orphanRemoval=true) 
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
 
    @Valid
    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL}, orphanRemoval=true) 
    @Cascade(org.hibernate.annotations.CascadeType.ALL) 
    @OrderColumn
    public List<Fulfillment> getFulfillment() {
		return fulfillment;
	}

	public void setFulfillment(List<Fulfillment> fulfillment) {
		this.fulfillment = fulfillment;
	}
	
	public void addFulfillment(Fulfillment fulfil){
		if(fulfil != null) {
			fulfillment.add(fulfil);
			fulfil.setOrder(this);
		}
	}
	
	public void removeFulfillment(Fulfillment fulfil){
		if(fulfil != null) {
			fulfillment.remove(fulfil);
		}		
	}

	public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
    	if (orderItem != null) {
            orderItems.add(orderItem);
            orderItem.setOrder(this);
    	}
    }
    
    public void removeOrderItem(OrderItem orderItem) {
    	if (orderItem != null) {
    		List<OrderItem> oil = getOrderItems();
    		oil.remove(orderItem);
    	}
    }
    
    public void removeOrderItem(UUID id) {
    	OrderItem oi = getOrderItem(id);    		
    	removeOrderItem(oi);
    }
    
    @Transient
    public OrderItem getOrderItem (UUID id) {
    	if (id == null) return null;
    	for (OrderItem oi : getOrderItems()) {
    		if (id.equals(oi.getId())) return oi;
    	}
    	return null;
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
    
    @Transient
    public OrderItem findOrderItem(Sku sku) {
    	for (OrderItem oi :  getOrderItems()) {
    		if (oi.getSku().equals(sku)) return oi;
    	}
    	return null;
    }
    
    @Transient
    public List<Sku> getSkusFromOrderItems() {
    	List<Sku> skus = new ArrayList<Sku>();
    	for (OrderItem oi : getOrderItems()) {
    		skus.add(oi.getSku());
    	}
    	return skus;
    }

	public BigDecimal getDeliveryTotal() {
		return deliveryTotal;
	}

	public void setDeliveryTotal(BigDecimal deliveryTotal) {
		this.deliveryTotal = deliveryTotal;
	}
	
	@Transient
	public Fulfillment getActualFulfillment() {
		Fulfillment ret = null;
		if (fulfillment.size()>0) ret = fulfillment.get(fulfillment.size()-1);
		return ret;
	}
	

}
