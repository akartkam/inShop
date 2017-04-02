package com.akartkam.inShop.formbean;

import java.io.Serializable;
import java.util.List;

public class DataTableForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7558931821537115314L;
	private Integer draw, start, length;
	private Search search;
	private Column[] columns;
	private Order[] order;
	
	public Integer getDraw() {
		return draw;
	}

	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	public Column[] getColumns() {
		return columns;
	}

	public void setColumns(Column[] columns) {
		this.columns = columns;
	}

	public Order[] getOrder() {
		return order;
	}

	public void setOrder(Order[] order) {
		this.order = order;
	}
	

	public static class	Column{
		private String data, name, searchable, orderable;
		private Search search;
		public String getData() {
			return data;
		}
		public void setData(String data) {
			this.data = data;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSearchable() {
			return searchable;
		}
		public void setSearchable(String searchable) {
			this.searchable = searchable;
		}
		public String getOrderable() {
			return orderable;
		}
		public void setOrderable(String orderable) {
			this.orderable = orderable;
		}
		public Search getSearch() {
			return search;
		}
		public void setSearch(Search search) {
			this.search = search;
		}
		
		
	}
	
	public static class Search{
		private String value, regex;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getRegex() {
			return regex;
		}

		public void setRegex(String regex) {
			this.regex = regex;
		}
		
	}
	
	public static class Order{
		private String column, dir;

		public String getColumn() {
			return column;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public String getDir() {
			return dir;
		}

		public void setDir(String dir) {
			this.dir = dir;
		}
	}
}


