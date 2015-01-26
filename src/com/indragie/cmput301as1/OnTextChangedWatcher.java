package com.indragie.cmput301as1;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Abstract class that provides empty implementations for TextWatcher methods
 * that are unused.
 */
public abstract class OnTextChangedWatcher implements TextWatcher {
	@Override
	public void afterTextChanged(Editable arg0) {}
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public abstract void onTextChanged(CharSequence s, int start, int before, int count);
}
