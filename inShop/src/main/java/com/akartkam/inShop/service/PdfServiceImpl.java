package com.akartkam.inShop.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring3.context.SpringWebContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

@Service("PdfService")
public class PdfServiceImpl implements PdfService {

	private static final Log LOG = LogFactory.getLog(PdfServiceImpl.class);
	
	@Autowired
    private ApplicationContext appContext;
	
	@Autowired
	private ServletContext servletContext;
	
    @Autowired 
    @Qualifier("emailTemplateEngine")
    private TemplateEngine templateEngine;
	
	@Override
	public void generatePdf(OutputStream outputStream, HttpServletRequest request, HttpServletResponse response, String templateName,
			                Map<String, Object> vars) {
		final SpringWebContext ctx = new SpringWebContext(request, response, servletContext, LocaleContextHolder.getLocale(),vars, appContext);
	    String htmlContent = templateEngine.process(templateName, ctx);
	    ITextRenderer renderer = new ITextRenderer();
/***
 * for test
 * FileOutputStream os = null;
   String fileName = UUID.randomUUID().toString();    
 */
		String fontPath = servletContext.getRealPath("/resources/css/fonts/arial.ttf");
/**/	    
	    try {
		    renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED); 
		    renderer.setDocumentFromString(htmlContent);
		    renderer.layout();

	    	/** for test
            final File outputFile = File.createTempFile(fileName, ".pdf");
            os = new FileOutputStream(outputFile);
            renderer.createPDF(os, false);
            */
			renderer.createPDF(outputStream);
		} catch (DocumentException | IOException e) {
			LOG.error("",e);
		}	    
	}

}
