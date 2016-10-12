package com.akartkam.inShop.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonUtil {
	  
	private static final Log LOG = LogFactory.getLog(CommonUtil.class);
	private CommonUtil () {};
	  
	public static int nullSafeIntegerToPrimitive(Integer value) {
		int res = 0;
		if (value != null) res = value;
		return res;
	}
	
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWithHeader = request.getHeader("X-Requested-With");
        boolean result = "XMLHttpRequest".equals(requestedWithHeader);
        
        if (LOG.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder()
                .append("Request URL: [").append(request.getServletPath()).append("]")
                .append(" - ")
                .append("X-Requested-With: [").append(requestedWithHeader).append("]")
                .append(" - ")
                .append("Returning: [").append(result).append("]");
            LOG.trace(sb.toString());
        }
        
        return result;
    }	
	
 
}
