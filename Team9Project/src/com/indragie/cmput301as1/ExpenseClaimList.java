/**
 * 
 */
package com.indragie.cmput301as1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

/**
 * Observable model object that contains a list of {@link ExpenseClaim} objects.
 * Observers will be notified when the list is modified by adding, removing, or
 * replacing existing expense claims.
 */
public class ExpenseClaimList extends TypedObservable<List<ExpenseClaim>> {
	//================================================================================
	// Properties
	//================================================================================
	
	private ArrayList<ExpenseClaim> claims;
	private String fileName;
	private Context context;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Create a new instance of {@link ExpenseClaimList}
	 * @param fileName Name of the file used to persist the expense claims.
	 * @param context The context used for I/O operations.
	 */
	public ExpenseClaimList(String fileName, Context context) {
		this.fileName = fileName;
		this.context = context;
		this.claims = loadExpenseClaims();
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return An unmodifiable list of expense claims.
	 */
	public List<ExpenseClaim> getExpenseClaims() {
		return Collections.unmodifiableList(claims);
	}
	
	//================================================================================
	// API
	//================================================================================
	
	/**
	 * Adds a new expense claim to the list of expense claims.
	 * @param claim The expense claim to add.
	 */
	public void addExpenseClaim(ExpenseClaim claim) {
		claims.add(claim);
		Collections.sort(claims);
		commitClaimsMutation();
	}
	
	/**
	 * Removes an existing expense claim from the list of expense claims.
	 * @param claim The expense claim to remove.
	 */
	public void removeExpenseClaim(ExpenseClaim claim) {
		if (claims.remove(claim)) {
			commitClaimsMutation();
		}
	}
	
	/**
	 * Removes an existing expense claim from the list of expense claims.
	 * @param index The index of the expense claim to remove.
	 */
	public void removeExpenseClaim(int index) {
		claims.remove(index);
		commitClaimsMutation();
	}
	
	/**
	 * Replaces an existing expense claim.
	 * @param index The index of the expense claim to replace.
	 * @param newClaim The expense claim to replace the existing expense claim with.
	 */
	public void setExpenseClaim(int index, ExpenseClaim newClaim) {
		claims.set(index, newClaim);
		Collections.sort(claims);
		commitClaimsMutation();
	}
	
	//================================================================================
	// Helpers
	//================================================================================
	
	private void commitClaimsMutation() {
		saveExpenseClaims(claims);
		setChanged();
		notifyObservers(claims);
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<ExpenseClaim> loadExpenseClaims() {
		try {
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			ois.close();
			fis.close();
			return (ArrayList<ExpenseClaim>)obj;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ExpenseClaim>();
		}
	}
	
	private void saveExpenseClaims(ArrayList<ExpenseClaim> claims) {
		try {
			FileOutputStream fos = context.openFileOutput(fileName, 0);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(claims);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
