package com.indragie.cmput301as1;

import java.util.Comparator;


public class CreationDateDescendingComparator implements Comparator<ExpenseClaim>
{

	@Override
	public int compare(ExpenseClaim a, ExpenseClaim b)
	{

		return b.getCreationDate().compareTo(a.getCreationDate());
	}
}
