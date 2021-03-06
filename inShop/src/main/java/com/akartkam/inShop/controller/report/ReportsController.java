package com.akartkam.inShop.controller.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.akartkam.inShop.dao.ReportsDAO;
import com.akartkam.inShop.domain.product.Product;
import com.akartkam.inShop.domain.product.Sku;
import com.akartkam.inShop.service.PdfService;
import com.akartkam.inShop.service.extension.ProductDisplayNameModificator;
import com.akartkam.inShop.service.product.ProductService;

@Controller
@RequestMapping("/report")
public class ReportsController {
	
    @Value("#{appProperties['inShop.baseUrl']}")
	private String baseUrl;
	
	@Value("#{entityUrlPrefixes.getProperty(T(com.akartkam.inShop.util.Constants).PRODUCT_CLASS)}")
	private String productPrefix;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ReportsDAO reportsDAO;
	
	@Autowired
	private PdfService pdfService;
	
	@Autowired
	private ProductDisplayNameModificator productDisplayNameModificator;	
	
	@RequestMapping(value="/pdf/order-check/{orderId}")
	public ResponseEntity<byte[]> orderCheckPdf(@PathVariable("orderId") String orderId , HttpServletRequest request, 
			                                    HttpServletResponse response) throws MessagingException {
		Object[] order = reportsDAO.findOrdersCheck(UUID.fromString(orderId));
		List<Object[]> orderItems = reportsDAO.findOrderItemsCheck(UUID.fromString(orderId));
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("order", order);	
		vars.put("orderItems", orderItems);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		pdfService.generatePdf(out, request, response, "pdf/orderPdf", vars);		
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/pdf"));
	    String filename = "order_"+order[0]+".pdf";
	    headers.setContentDispositionFormData(filename, filename);
	    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	    ResponseEntity<byte[]> resp = new ResponseEntity<byte[]>(out.toByteArray(), headers, HttpStatus.OK);
	    return resp;
	}
	
	@RequestMapping(value="/csv/ym-price")
	public void downloadCSVYMlist(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String csvFileName = "ymList.csv";
        
        response.setContentType("text/csv");
 
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);	
        
        String[] header = { "id", "url", "price", "currencyId", "category", "picture",
        		            "name", "description"};
        
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.TAB_PREFERENCE);
        csvWriter.writeHeader(header); 

        List<Sku> skus = productService.getActiveSkuList();
        List<YmPriceCSV> csvs = new ArrayList<YmPriceCSV>();
        for (Sku sku : skus) {
        	List<String> lImgs = sku.lookupImages();
            String imgUrl = !lImgs.isEmpty()? lImgs.get(0): "";
            Product product = sku.lookupProduct();
            String skuUrl = product != null? product.getUrl(): "";
            String descr = sku.lookupSkuDescription();
            if (!StringUtils.isBlank(sku.getCode()) && !StringUtils.isBlank(imgUrl) && 
                !StringUtils.isBlank(descr)) {
            	
        	    StringBuilder sbSkuUrl = new StringBuilder(baseUrl);
            	YmPriceCSV csv = new YmPriceCSV();
                //csv.setAvailable(true);
            	csv.setId(Base64.getEncoder().encodeToString(Long.toString(sku.getId().getMostSignificantBits(), Character.MAX_RADIX).getBytes()).replaceAll("=", ""));
        	    csv.setUrl(sbSkuUrl.append("/").append(productPrefix).append(skuUrl).toString());
        	    csv.setPrice(sku.getPriceForPackage().setScale(2, RoundingMode.HALF_UP));
        	    csv.setCurrencyId("RUR");
        	    csv.setCategory(product.getCategory().getName());
        	    sbSkuUrl = new StringBuilder(baseUrl);
        	    csv.setPicture(sbSkuUrl.append(imgUrl).toString());
	        	productDisplayNameModificator.setSku(sku);
	        	String name = productDisplayNameModificator.getModifyedDisplayName(sku.lookupName());
	        	StringBuilder sbName = new StringBuilder(name);
	        	removeStringFromSB(sbName , "<span class='product-header-attribute'>");
	        	removeStringFromSB(sbName , "<span class='unit'>");
	        	removeStringFromSB(sbName , "</span>");
	        	removeStringFromSB(sbName , "&nbsp;");
        	    csv.setName(sbName.toString());
        	    csv.setDescription(descr);
        	    csvWriter.write(csv, header);
            }
        	
        }
        csvWriter.close();
	
	}
	
	private void removeStringFromSB(StringBuilder sb, String str) {
		int i = sb.indexOf(str);
		while (i!=-1) {
			sb.delete(i, i+str.length());
			i = sb.indexOf(str);
		}
	}
	
	public static class YmPriceCSV {
		String id;
		Boolean available;
		String url;
		BigDecimal price; 
		String currencyId;
		String category;
		String picture;
		String name;
		String description;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public Boolean getAvailable() {
			return available;
		}
		public void setAvailable(Boolean available) {
			this.available = available;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public BigDecimal getPrice() {
			return price;
		}
		public void setPrice(BigDecimal price) {
			this.price = price;
		}
		public String getCurrencyId() {
			return currencyId;
		}
		public void setCurrencyId(String currencyId) {
			this.currencyId = currencyId;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getPicture() {
			return picture;
		}
		public void setPicture(String picture) {
			this.picture = picture;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
		
	}
	
}
