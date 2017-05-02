package com.akartkam.inShop.controller.report;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.akartkam.inShop.dao.ReportsDAO;
import com.akartkam.inShop.domain.order.Order;
import com.akartkam.inShop.service.PdfService;

@Controller
@RequestMapping("/report")
public class ReportsController {
	
	@Autowired
	private ReportsDAO reportsDAO;
	
	@Autowired
	private PdfService pdfService;
	
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
}
