package com.indragie.cmput301as1;

import java.util.Comparator;


public class StartDateComparator2 implements Comparator<ExpenseClaim>
{

	@Override
	public int compare(ExpenseClaim a, ExpenseClaim b)
	{
			
		return b.getStartDate().compareTo(a.getStartDate());
	}

}
