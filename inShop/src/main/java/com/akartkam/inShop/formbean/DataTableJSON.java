package com.akartkam.inShop.formbean;

import java.io.Serializable;

public class DataTableJSON implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6920338550271202557L;
	private Integer draw;
	private Long recordsTotal, recordsFiltered;
	private String[][] data;
	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	public Long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public Long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(Long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public String[][] getData() {
		return data;
	}
	public void setData(String[][] data) {
		this.data = data;
	}
	
	
	
}
