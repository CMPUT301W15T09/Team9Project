package com.indragie.cmput301as1;

import java.util.Comparator;


public class CreationDateAscendingComparator implements Comparator<ExpenseClaim>
{

	@Override
	public int compare(ExpenseClaim a, ExpenseClaim b)
	{

		return a.getCreationDate().compareTo(b.getCreationDate());
	}

}