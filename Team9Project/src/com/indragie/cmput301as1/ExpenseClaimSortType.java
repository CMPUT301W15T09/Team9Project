package com.indragie.cmput301as1;

public class ExpenseClaimSortType {
	
	private static String sort_time;
	private static String sort_type;
	
	public ExpenseClaimSortType(String sort_time, String sort_type) {
		this.sort_time = sort_time;
		this.sort_type = sort_type;
	}
	
	public static String getSortTime() {
		return sort_time;
	}
	
	public static String getSortType() {
		return sort_type;
	}

}
