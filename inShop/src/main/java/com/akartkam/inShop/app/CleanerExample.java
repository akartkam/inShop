package com.akartkam.inShop.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
 
/**
 *
 * @author John Yeary
 * @version 1.0
 */
public class CleanerExample {
 
    private static final String dirty = "<h2><span style='background-color: rgb(239, 239, 239);'>Миссия Sony</span> — вдохновлять вас на новые открытия и радовать достижениями.</h2> " +
    							        "<div><br></div><script>alert('qwer')</script>"+
    							        "<p style='line-height: 1.6;'><br><a href='http://www.mail.u' target='_blank'>www.mail.u</a></p>";
 
    public static void main(String[] args) {
         
    	/*Whitelist wl = Whitelist.relaxed().addAttributes("span", "style").addAttributes("img", "src");
        String clean = Jsoup.clean(dirty,wl);
         
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("DIRTY CODE");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(dirty);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("CLEAN CODE");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(clean);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        */
    	String s = "<img src='erty\\hhh'";
        Pattern scriptPattern = Pattern.compile(".*<[\\s]*img.*src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        boolean value = scriptPattern.matcher(s).matches();
        System.out.println(value);

    }
 
}
