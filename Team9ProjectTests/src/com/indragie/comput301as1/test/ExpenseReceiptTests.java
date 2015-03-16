package com.indragie.comput301as1.test;

import java.io.File;
import java.util.Date;

import org.joda.money.Money;

import com.indragie.cmput301as1.ExpenseItem;
import com.indragie.cmput301as1.ExpenseItemAddActivity;

import android.os.Environment;
import android.test.AndroidTestCase;

public class ExpenseReceiptTests extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		ExpenseItem item = new ExpenseItem("", "", "", Money.parse("CAD 0.00"), new Date());
		
		String folder = Environment.getExternalStorageDirectory() + "/tmp";
		File receiptFolder = new File(folder);
		if (!receiptFolder.exists()) {
			receiptFolder.mkdir();
		}
	}
	/*
	protected void testCalculateScale() {
		int maxSize = 65536;
		
	}
	
	protected void testResizeBitmap() {
		int 
	}
	*/
}
