
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
        @ColumnResult(name = "order_status")}),

    @SqlResultSetMapping(name = "orderCheckQueryRSM", columns = {
        @ColumnResult(name = "order_number"), @ColumnResult(name = "order_subtotal"), @ColumnResult(name = "submit_date"),
        @ColumnResult(name = "order_total"), @ColumnResult(name = "deliverytotal"), 
        @ColumnResult(name = "c_name"), @ColumnResult(name = "c_phone"), @ColumnResult(name = "c_address"), 
        @ColumnResult(name = "c_email"), @ColumnResult(name = "delivery_name"), @ColumnResult(name = "store_name"), 
        @ColumnResult(name = "store_addres"), @ColumnResult(name = "city"), @ColumnResult(name = "order_status")}),
                
    @SqlResultSetMapping(name = "orderItemCheckQueryRSM", columns = {
        @ColumnResult(name = "product_name"), @ColumnResult(name = "quantity"),
        @ColumnResult(name = "price"), @ColumnResult(name = "oi_total"), @ColumnResult(name = "price_for_unit")})       
        
})

@NamedNativeQueries({
	@NamedNativeQuery(
			name = "ordersByStatus",
			query = "select 'Всего' status, count(case when order_status <> 'CANCELLED' then o.id else null end) count_orders, "+
					"   sum(case when order_status <> 'CANCELLED' then o.order_total else 0 end) sum_order_total, "+
					"   sum(case when order_status <> 'CANCELLED' then f.delivery_price else 0 end) sum_delivery, '' as order_status"+
					"   from customer_order o "+
					"        left join fulfillment f on f.order_id=o.id " +
				    "        left outer join fulfillment f1 on f1.order_id=f.order_id and f.fulfillment_order<f1.fulfillment_order "+ 
					"   where f1.id is null "+ 					
					" union all "+
					"select s.short_name_r status, count(o.id) count_orders, sum(o.order_total) sum_order_total, "+
					"sum(f.delivery_price) sum_delivery, s.name as order_status"+
					" from customer_order o "+
					"      left join order_status s on s.name=o.order_status "+
					"      left join fulfillment f on f.order_id=o.id "+
				    "        left outer join fulfillment f1 on f1.order_id=f.order_id and f.fulfillment_order<f1.fulfillment_order "+ 
					"   where f1.id is null "+ 					
					" group by 1,5",
			resultSetMapping = "ordersByStatusRSM"),	
	
	@NamedNativeQuery(
			name = "orderCheckQuery",
			query = "select o.order_number, o.order_subtotal, to_char(o.submit_date, 'dd.MM.yyyy hh:mm') submit_date, o.order_total, o.deliverytotal, "+  
					"       case when coalesce(f.first_name,'') <> '' then f.first_name||' '||coalesce(f.last_name,'')||' '||coalesce(f.middle_name,'') "+
					"		else coalesce(c.first_name, '')||' '||coalesce(c.last_name,'')||' '||coalesce(c.middle_name,'') end as c_name, "+
					"		case when coalesce(f.phone,'') <> '' then f.phone else c.phone end as c_phone, "+ 
					"		case when coalesce(f.address,'') <> '' then f.address else c.address end as c_address, "+
					"		case when coalesce(o.email_address,'') <> '' then o.email_address else c.email end as c_email, "+ 
					"		coalesce(d.name,'') as delivery_name, coalesce(d.delivery_type, '') , coalesce(s.name, '') as store_name, "+ 
					"		coalesce(s.address, '') as store_addres, coalesce(f.city, '') as city, o.order_status "+ 
					"   from Customer_order o "+ 
					"		 left join Customer c on c.id=o.customer_id "+
					"		 left join Fulfillment f on f.order_id=o.id "+
					"		 left outer join Fulfillment f1 on f1.order_id=f.order_id and f.fulfillment_order<f1.fulfillment_order "+
					"		 left join Delivery d on d.id=f.delivery_id "+
					" 		 left join Store s on s.id=f.store_id "+
					"	where cast(o.id as varchar(36))=? and f1.id is null ",
			resultSetMapping = "orderCheckQueryRSM"),
			
	@NamedNativeQuery(
					name = "orderItemCheckQuery",
					query = "select oi.product_name, oi.price, (oi.quantity * oi.price) as oi_total, oi.quantity, "+
							"       case when sale_price is not null and sale_price <> 0 then sale_price else retail_price end as price_for_unit "+
							"  from customer_order_item oi "+
							"  where cast(oi.order_id as varchar(36))=? ",
			resultSetMapping = "orderItemCheckQueryRSM")
	
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

    public void removeFulfillment(UUID id) {
    	Fulfillment ff = getFulfillment(id);    		
    	removeFulfillment(ff);
    }
        
    
    @Transient
    public OrderItem getOrderItem (UUID id) {
    	if (id == null) return null;
    	for (OrderItem oi : getOrderItems()) {
    		if (id.equals(oi.getId())) return oi;
    	}
    	return null;
    }
    
    @Transient
    public Fulfillment getFulfillment (UUID id) {
    	if (id == null) return null;
    	for (Fulfillment ff : getFulfillment()) {
    		if (id.equals(ff.getId())) return ff;
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
	
/*	
	select 'Всего' status, count(o.id) count_orders, sum(o.order_total) sum_order_total, 
	 sum(f.delivery_price) sum_delivery, '' as order_status
	 from customer_order o 
	      left join fulfillment f on f.order_id=o.id 
	      left outer join fulfillment f1 on f1.order_id=f.order_id and f.fulfillment_order<f1.fulfillment_order 
	  where f1.id is null 
	 union all 
	select s.short_name_r status, count(o.id) count_orders, sum(o.order_total) sum_order_total, 
	sum(f.delivery_price) sum_delivery, s.name as order_status
	 from customer_order o 
	      left join order_status s on s.name=o.order_status 
	      left join fulfillment f on f.order_id=o.id 
	      left outer join fulfillment f1 on f1.order_id=f.order_id and f.fulfillment_order<f1.fulfillment_order 
	  where f1.id is null 	      
	  group by 1,5	
*/	

}
