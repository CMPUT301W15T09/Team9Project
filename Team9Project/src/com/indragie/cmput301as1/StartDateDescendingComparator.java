package com.indragie.cmput301as1;

import java.io.Serializable;
import java.util.Comparator;


public class StartDateDescendingComparator implements Serializable, Comparator<ExpenseClaim>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4535122099977097227L;

	@Override
	public int compare(ExpenseClaim a, ExpenseClaim b)
	{
			
		return b.getStartDate().compareTo(a.getStartDate());
	}

}
