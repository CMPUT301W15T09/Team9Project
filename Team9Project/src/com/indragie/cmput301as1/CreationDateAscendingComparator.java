package com.indragie.cmput301as1;

import java.io.Serializable;
import java.util.Comparator;


public class CreationDateAscendingComparator implements Serializable, Comparator<ExpenseClaim>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6116366099743950145L;

	@Override
	public int compare(ExpenseClaim a, ExpenseClaim b)
	{

		return a.getCreationDate().compareTo(b.getCreationDate());
	}

}