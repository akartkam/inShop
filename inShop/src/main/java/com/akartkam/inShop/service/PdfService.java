package com.akartkam.inShop.service;

import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PdfService {
	void generatePdf(OutputStream outputStream, HttpServletRequest request,
			HttpServletResponse response, String templateName,
			Map<String, Object> vars);
}
