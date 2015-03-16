package com.indragie.cmput301as1;

import java.io.Serializable;
import java.util.Comparator;


public class StartDateAscendingComparator implements Serializable, Comparator<ExpenseClaim>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2123787414197460051L;

	@Override
	public int compare(ExpenseClaim a, ExpenseClaim b)
	{
			
		return a.getStartDate().compareTo(b.getStartDate());
	}

}