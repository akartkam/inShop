package com.akartkam.inShop.app;

import org.springframework.util.StringUtils;



public class DelSubstring {

	public static void main(String[] args) {
		String s = "yuirjrjrnrb<script>alert('qwer')</script>fgdhhfghfghf";
		int startIndex = s.indexOf("<script>");
		int endIndex = s.indexOf("</script>");
		String toBeReplaced = s.substring(startIndex , endIndex+9);
		System.out.println(s.replace(toBeReplaced, ""));
		s = StringUtils.delete(s, "<script>");
		s = StringUtils.delete(s, "</script>");
		System.out.println(s);

	}
}
