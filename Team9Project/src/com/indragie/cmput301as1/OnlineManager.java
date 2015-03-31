package com.indragie.cmput301as1;

import java.util.LinkedList;

import android.content.Context;

public class OnlineManager {

	private static LinkedList stack = new LinkedList(); 

	public static LinkedList getStack() {
		return stack;
	}

	public static void setStack(LinkedList stack) {
		OnlineManager.stack = stack;
	}
	
	public static void addToStack(ElasticSearchAPIClient.APICall<ElasticSearchDocument> call) {
		stack.add(call);
		checkForNetwork();
	}

	private static Context context;
	
	private static void checkForNetwork() {
		if (NetworkStateReceiver.isNetworkAvailable(context) == true) {
			// we are connected so then we want to go through with our stack calls
			stack.getFirst();
		}
	}
}
