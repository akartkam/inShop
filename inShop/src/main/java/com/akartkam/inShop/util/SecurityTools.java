package com.akartkam.inShop.util;

import java.util.regex.Pattern;

public class SecurityTools {
	
	  public static String stripXSS(String value) {
	        if (value != null) {
	            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
	            // avoid encoded attacks.
	            // value = ESAPI.encoder().canonicalize(value);

	        	//Лучше конечно юзать jsoup, но пока лень.
	        	
	            // Avoid null characters
	            value = value.replaceAll("", "");

	            // Avoid anything between script tags
	            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid anything in a src='...' type of expression except <img src='...' 
	            //Не работает если src= без img на одной строке вместе с src= с тэгом img
	            //scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            //data:image в начале src свидетельствует о том, что это img src='...'
	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'((?!data:image).)(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            //scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            //scriptPattern = Pattern.compile("^((?!<[\\s]*img).)*?src[\r\n]*?=[\r\n]*?\\\"(.*?)\\\"(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"((?!data:image).)(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");
	            
	            // Remove any lonesome </script> tag
	            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Remove any lonesome <script ...> tag
	            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid eval(...) expressions
	            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid expression(...) expressions
	            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid javascript:... expressions
	            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid vbscript:... expressions
	            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid onload= expressions
	            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");
	        }
	        return value;
	    }

}
