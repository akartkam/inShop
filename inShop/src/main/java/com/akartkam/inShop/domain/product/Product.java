package com.akartkam.inShop.domain.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import com.akartkam.inShop.domain.AbstractWebDomainObject;
import com.akartkam.inShop.domain.Instruction;
import com.akartkam.inShop.domain.product.attribute.AbstractAttribute;
import com.akartkam.inShop.domain.product.attribute.AbstractAttributeValue;
import com.akartkam.inShop.domain.product.attribute.SimpleAttributeFactory;
import com.akartkam.inShop.domain.product.option.ProductOption;
import com.akartkam.inShop.domain.product.option.ProductOptionValue;
import com.akartkam.inShop.formatter.CurrencyFormat;
import com.akartkam.inShop.presentation.admin.AdminPresentation;
import com.akartkam.inShop.presentation.admin.EditTab;


@SqlResultSetMappings({
    @SqlResultSetMapping(name = "findAllProductUrlsRSM", columns = {
        @ColumnResult(name = "url")
    }),
    @SqlResultSetMapping(name = "findFilteredProductByCategoryRSM", columns = {
            @ColumnResult(name = "type"), @ColumnResult(name = "f1"), @ColumnResult(name = "f2") 
        })    
})
    
@NamedNativeQueries({
	@NamedNativeQuery(
			name = "findAllProductUrls",
			query = "select url from product where enabled=true",
			resultSetMapping = "findAllProductUrlsRSM"),	
    @NamedNativeQuery(
			name = "findFilteredProductByCategory",
			query = "WITH RECURSIVE r AS ( "+
			"	select id "+
			"	  from category "+  
			"	  where cast(id as varchar) = :c "+
			"	union all "+
			"	select c.id "+
			"	  from category c "+
			"	       join r on parent_id=r.id "+
			" ) "+
			" select distinct 0 as type, cast(b.id as varchar) f1, b.name f2 from product p, brand b , r "+ 
			"   where p.category_id=r.id and b.id=p.brand_id and p.enabled = true and "+
			"         (select count(distinct b.id) from product p, brand b , r "+
			"   		  where p.category_id=r.id and b.id=p.brand_id and p.enabled = true) > 1 "+      
			" union "+
			" select distinct 1 as type, cast(null as varchar),  trim(p.model) from product p, r "+ 
			"   where p.category_id=r.id and coalesce(trim(p.model),'') != ''  and p.enabled = true and "+
			"   (select count(distinct trim(p.model)) from product p, r "+
			"      where p.category_id=r.id and coalesce(trim(p.model),'') != ''  and p.enabled = true) > 1 "+
			" union "+
			" select q1.type, q1.f1, q1.f2 "+ 
			"  from "+
			"	( "+
			"		select 2 as type, f1, f2, count(f1) over (partition by f1) c "+
			"		  from	"+
			"			(select distinct trim(a.name) f1, "+  
			"			         case when coalesce(round(cast(adv.attributevalue as numeric), 2),0) != 0 then "+ 
			"			                   cast(round(cast(adv.attributevalue as numeric), 2) as varchar) "+
			"			              when coalesce(aiv.attributevalue,0) != 0 then cast(aiv.attributevalue as varchar) "+    
			"			              when coalesce(trim(alv.attributevalue),'') != '' then trim(alv.attributevalue) "+
			"			              when coalesce(trim(asv.attributevalue),'') != '' then trim(asv.attributevalue) end f2 "+ 
			"			     from product p "+
			"			          inner join r on p.category_id=r.id "+
			"			          left join sku s on s.product_id=p.id and s.enabled=true "+ 
			"			          left join attribute_value av on (av.product_id=p.id or av.sku_id=s.id) "+
			"			          left join attribute_decimal_value adv on adv.id=av.id "+
			"			          left join attribute_int_value aiv on aiv.id=av.id "+
			"			          left join attribute_slist_value alv on alv.id=av.id "+
			"			          left join attribute_string_value asv on asv.id=av.id "+
			"			          left join attribute a on a.id=av.attribute_id "+         
			"				where p.enabled = true and coalesce(trim(a.name),'') != '' "+
			"			union "+
			"		     select distinct trim(po.label) f1, trim(pov.option_value) f2 "+
			"			     from product p "+
			"			          inner join r on p.category_id=r.id "+
			"			          inner join sku s on s.product_id=p.id and s.enabled=true "+ 
			"			          inner join lnk_sku_option_value lsov on lsov.sku_id=s.id "+
			"			          inner join product_option_value pov on pov.id=lsov.product_option_value_id "+
			"			          inner join product_option po on po.id=pov.productoption_id "+
			"				where p.enabled = true and coalesce(trim(po.label), '') != '' "+
			"			) q "+   
			"	) q1 "+
			" where q1.c > 1 "+
			" order by 1, 2, 3",
		   resultSetMapping = "findFilteredProductByCategoryRSM")			
})

@NamedQueries({
@NamedQuery(
			name = "findAllProducts",
			query = "from Product as p order by ordering"),	
@NamedQuery(
		name = "findProductByProductStatus",
		query = "from Product as p where :productStatus in  elements(p.defaultSku.productStatus) and p.enabled=true order by ordering"), 
@NamedQuery(
		name = "findProductByBrand",
		query = "from Product as p where p.brand = :brand and p.enabled=true order by ordering") 
})
@Entity
@Table(name = "Product")
@Cache(region = "product", usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("rawtypes")
@BatchSize(size = 50)
public class Product extends AbstractWebDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -583044339566068826L;
	private Category category;
	private Brand brand;
	private String model;
	private List<AbstractAttributeValue> attributeValues = new ArrayList<AbstractAttributeValue>();
	private String url;
	private Sku defaultSku;
    private List<Sku> additionalSku = new ArrayList<Sku>();	
    private Set<ProductOption> productOptions = new HashSet<ProductOption>();
    private Boolean canSellWithoutOptions = true;
    private Instruction instruction;
    private Boolean isNotShowPriceForUnit = false;

    @Column(name = "is_not_show_price_for_unit")
	public Boolean getIsNotShowPriceForUnit() {
		return isNotShowPriceForUnit;
	}
	public void setIsNotShowPriceForUnit(Boolean isNotShowPriceForUnit) {
		this.isNotShowPriceForUnit = isNotShowPriceForUnit;
	}
    
	@Column(name = "can_sell_without_options")
	public Boolean isCanSellWithoutOptions() {
		return canSellWithoutOptions;
	}
	public void setCanSellWithoutOptions(Boolean canSellWithoutOptions) {
		this.canSellWithoutOptions = canSellWithoutOptions;
	}
	
	@ManyToOne(optional=true)
	@JoinColumn
	@ForeignKey(name="fk_product_instruction")
	public Instruction getInstruction() {
		return instruction;
	}
	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(nullable=false)
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Override
    @Column(name = "url")
    @Index(name="product_url_index", columnNames={"url"})	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
		
	@ManyToOne
	@JoinColumn
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	@Column(name = "model")
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public boolean canRemove() {
		return additionalSku.isEmpty();
	};
	
	@OneToMany(mappedBy="product", cascade = CascadeType.ALL, orphanRemoval=true)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@BatchSize(size = 20)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<AbstractAttributeValue> getAttributeValues() {
		return attributeValues;
	}
	public void setAttributeValues(List<AbstractAttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
		Collections.sort(attributeValues, new AbstractAttribute.AVComparer());
	}	
	
	public void addAttributeValue (AbstractAttributeValue attributeValue) {
		if (attributeValue == null) throw new IllegalArgumentException("Null attributeValue!");
		attributeValue.setProduct(this);
		getAttributeValues().add(attributeValue);		
	}
	
	public void addAttributeValue (AbstractAttributeValue attributeValue, AbstractAttribute attribute) {
		if (attributeValue == null) throw new IllegalArgumentException("Null attributeValue!");
		if (attribute == null) throw new IllegalArgumentException("Null attribute!");
		if (attribute.getAttributeType() != attributeValue.getAttributeValueType()) throw new IllegalArgumentException("The type of attribute and attributeValue is different!"); 
		attributeValue.setAttribute(attribute);
		addAttributeValue(attributeValue);
	}
	
	public void addAttributeValue (AbstractAttribute attribute) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (attribute == null) throw new IllegalArgumentException("Null attribute!");
		AbstractAttributeValue attributeValue = SimpleAttributeFactory.createAttributeValue(attribute.getAttributeType());
		addAttributeValue(attributeValue, attribute);
	}
	
	public void removeAttributeValue (AbstractAttributeValue attributeValue) {
		List<AbstractAttributeValue> lav =  getAttributeValues();
		if (lav!=null) {
			lav.remove(attributeValue);
		}
	}
	
	public void addAdditionalSku(Sku sku) {
		if (sku == null) throw new IllegalArgumentException("Null sku!");
		sku.setProduct(this);
		getAdditionalSku().add(sku);		
	}
	
	public void removeAdditionalSku(Sku sku) {
		if (sku == null) throw new IllegalArgumentException("Null sku!");
		sku.setProduct(null);
		getAdditionalSku().remove(sku);		
	}

	@AdminPresentation(tab=EditTab.LINKS)
	@Valid
	@ManyToMany
	@JoinTable(name = "lnk_product_option", joinColumns = { 
			@JoinColumn(name = "product_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "product_option_id", 
					nullable = false, updatable = false) })
	@BatchSize(size=50)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<ProductOption> getProductOptions() {
		return productOptions;
	}
	
	public void setProductOptions(Set<ProductOption> productOption) {
		this.productOptions = productOption;
	}
	
    public void addProductOption(ProductOption productOption) {
        if (productOption == null) throw new IllegalArgumentException("Null productOption!");
        productOptions.add(productOption);
    }
    public void removeProductOption(ProductOption productOption) {
        if (productOption == null) throw new IllegalArgumentException("Null productOption!");
        productOptions.remove(productOption);
    }
    
    @Valid
    @OneToOne(cascade={CascadeType.ALL})
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    @JoinColumn(name="default_sku_id")
	public Sku getDefaultSku() {
		return defaultSku;
	}

    public void setDefaultSku(Sku defaultSku) {
	       if (defaultSku != null) {
	            defaultSku.setDefaultProduct(this);

	        }
	        this.defaultSku = defaultSku;
	}
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy="product")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	@BatchSize(size = 50)
	@OrderBy("ordering")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Sku> getAdditionalSku() {
		return additionalSku;
	}
	public void setAdditionalSku(List<Sku> additionalSku) {
		this.additionalSku = additionalSku;
	}
	
    @Transient
    public List<Sku> getSkus() {
        List<Sku> skus = new ArrayList<Sku>();
        for (Sku sku : getAdditionalSku()) {
            if (sku.isEnabled()) {
                skus.add(sku);
            }
        }
        return skus;
    }
    
    
    @Transient
    public List<ProductOptionValue> getPOVByPO(ProductOption productOption) {
    	List<ProductOptionValue> pov = new ArrayList<ProductOptionValue>();
    	for (Sku sku: getSkus()) {
    		for(ProductOptionValue v: sku.getProductOptionValues()) {
    			if (v.getProductOption().equals(productOption) && !pov.contains(v)) pov.add(v);
    		}
    	}
    	return pov;
    }
    
    @Transient
    public List<String> getPOVAsStringsByPO(ProductOption productOption) {
    	List<ProductOptionValue> pol = getPOVByPO(productOption);
    	List<String> res = (List<String>) CollectionUtils.collect(pol, new Transformer<ProductOptionValue, String>() {
			@Override
			public String transform(ProductOptionValue input) {
				return input.getOptionValue();
			}
    		
    	});
    	return res;
    }
    
    @Transient
    public boolean hasAdditionalSkus() {
    	return !getSkus().isEmpty();
    }
	
	public void buildFullLink(String shortUrl) {
		if (shortUrl == null || "".equals(shortUrl)) return;
		if (shortUrl.startsWith("/")) shortUrl = shortUrl.substring(1);     
        StringBuilder linkBuffer = new StringBuilder(50);
        linkBuffer.append(this.getCategory().getUrl()).append("/").append(shortUrl);
        setUrl(linkBuffer.toString());		
	} 
	
	@Transient
	public List<String> getAllImages() {
		List<String> ret = new ArrayList<String>();
		for(String image: getDefaultSku().getImages()) ret.add(image);
		for(Sku sku: getSkus()) {
			for(String image: sku.getImages()) ret.add(image);
		}
		return ret;
	}
	
	
	@Transient
	public List<String> getSkuCodes() {
		List<String> ret = new ArrayList<String>();
		if (getDefaultSku().getCode() != null && !"".equals(getDefaultSku().getCode())) ret.add(getDefaultSku().getCode());
		for(Sku sku : getSkus()) {
			if (sku.getCode() != null && !"".equals(sku.getCode())) ret.add(sku.getCode());
		}
		return ret;
	}
	
	@Transient
	public String getDisplayName() {
		return getDefaultSku().getName();
	}
    
	@Transient
	public String getLongDisplayName() {
		return getDefaultSku().getDescription();
	}
		
	@Transient
	public String getInstructionContent() {
		Instruction instr = getInstruction();
		if (instr != null) return instr.getContent();
		List<Category> cts = getCategory().buildCategoryHierarchy(null, false);
		for (Category cat : cts) {
			instr = cat.getInstruction();
			if (instr != null) return instr.getContent();
		}
		return null;
	}
	
	@Override
	@Transient
	public Product clone() throws CloneNotSupportedException {
		Product product = (Product) super.clone();
		product.setId(UUID.randomUUID());
		product.setUrl(new String(getUrl()));
		product.setModel(new String(getModel()));
		product.setProductOptions(new HashSet<ProductOption>());
		product.setAttributeValues(new ArrayList<AbstractAttributeValue>());
		product.setDefaultSku(getDefaultSku().clone());
		product.setCreatedBy(null);
		product.setCreatedDate(null);
		product.setUpdatedBy(null);
		product.setUpdatedDate(null);
		return product;
	}

	@Override
	@Transient
	public String getName() {
		return defaultSku.getName();
	}
	
	@Transient
	@CurrencyFormat
	public BigDecimal[] getMinMaxPrice(){
		BigDecimal[] ret = new BigDecimal[]{null, null};
		BigDecimal price;
		if (!hasAdditionalSkus()) {
			price = getDefaultSku().getPrice();	
			ret[0] = price;
			ret[1] = price;
		} else {
			for (Sku sku : getSkus()) {
				price = sku.getPrice();
				if(price != null) {
					if (ret[0] == null || price.compareTo(ret[0]) < 0) ret[0]=price;
					if (ret[1] == null || price.compareTo(ret[1]) > 0) ret[1]=price;
				}
			}
		}
		if (ret[0] == null) ret[0]=ret[1];
		if (ret[1] == null) ret[1]=ret[0];
		return ret;
	}

	@Transient
	@CurrencyFormat
	public BigDecimal[] getMinMaxPriceForPackage(){
		BigDecimal[] ret = new BigDecimal[]{null, null};
		BigDecimal price;
		int quantity;
		if (!hasAdditionalSkus()) {
			quantity = getDefaultSku().getQuantityPerPackage() != null? getDefaultSku().getQuantityPerPackage(): 0;
			price = getDefaultSku().getPrice();
			if (quantity != 0 && price != null) {
				price = price.multiply(new BigDecimal(quantity));
				ret[0] = price;
				ret[1] = price;				
			}
		} else {
			for (Sku sku : getSkus()) {
				quantity = sku.getQuantityPerPackage() != null? getDefaultSku().getQuantityPerPackage(): 0;
				price = sku.getPrice();
				if(quantity != 0 && price != null) {
					price = price.multiply(new BigDecimal(quantity));
					if (ret[0] == null || price.compareTo(ret[0]) < 0) ret[0]=price;
					if (ret[1] == null || price.compareTo(ret[1]) > 0) ret[1]=price;
				}
			}
		}
		if (ret[0] == null) ret[0]=ret[1];
		if (ret[1] == null) ret[1]=ret[0];
		return ret;
	}
	
	@Transient
	public List<AbstractAttribute> getAdditionalSkuAttributes() {
		List<AbstractAttribute> ret = new ArrayList<AbstractAttribute>();
		for (Sku sku : getSkus()) {
			for (AbstractAttributeValue av : sku.getAttributeValues()){
				AbstractAttribute at = av.getAttribute();
				if (!ret.contains(at)) ret.add(at);
			}
		}
		Collections.sort(ret);
		return ret;
	}

	@Transient
	public List<AbstractAttributeValue> getAdditionalSkuAvByAt(AbstractAttribute attribute) {
		List<AbstractAttributeValue> avl = new ArrayList<AbstractAttributeValue>();
		for (Sku sku : getSkus()) {
			for (AbstractAttributeValue av : sku.getAttributeValues()){
				if (av.getAttribute().equals(attribute)) avl.add(av);
			}
		}
		return avl;
	}
	
	
	@Transient
	public List<String> getAdditionalSkuAvAsStringsByAt(AbstractAttribute attribute) {
    	List<AbstractAttributeValue> avl = getAdditionalSkuAvByAt(attribute);
    	List<String> res = (List<String>) CollectionUtils.collect(avl, new Transformer<AbstractAttributeValue, String>() {
			@Override
			public String transform(AbstractAttributeValue input) {
				return input.getStringValue();
			}
    		
    	});
    	return res;		
	}
}
