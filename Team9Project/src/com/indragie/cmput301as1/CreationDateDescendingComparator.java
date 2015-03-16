package com.indragie.cmput301as1;

import java.io.Serializable;
import java.util.Comparator;


public class CreationDateDescendingComparator implements Serializable, Comparator<ExpenseClaim>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1682251097861280267L;

	@Override
	public int compare(ExpenseClaim a, ExpenseClaim b)
	{

		return b.getCreationDate().compareTo(a.getCreationDate());
	}
}
