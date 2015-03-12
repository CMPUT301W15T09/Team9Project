package com.indragie.cmput301as1;

import java.util.Comparator;


public class StartDateComparator implements Comparator<ExpenseClaim>
{

	@Override
	public int compare(ExpenseClaim a, ExpenseClaim b)
	{
			
		return a.getStartDate().compareTo(b.getStartDate());
	}

}
