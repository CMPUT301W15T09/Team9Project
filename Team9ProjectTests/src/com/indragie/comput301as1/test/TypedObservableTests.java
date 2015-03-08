package com.indragie.comput301as1.test;

import com.indragie.cmput301as1.TypedObservable;
import com.indragie.cmput301as1.TypedObserver;

import junit.framework.TestCase;

public class TypedObservableTests extends TestCase {
	class MockObserver<T> implements TypedObserver<T> {
		private boolean updateCalled;
		private T updateValue;
		
		@Override
		public void update(TypedObservable<T> o, T arg) {
			updateCalled = true;
			updateValue = arg;
		}
		
		public boolean getUpdateCalled() {
			return updateCalled;
		}
		
		public void setUpdateCalled(boolean called) {
			updateCalled = called;
		}
		
		public T getUpdateValue() {
			return updateValue;
		}
	}
	
	class MockObservable<T> extends TypedObservable<T> {
		public void setChanged() {
			super.setChanged();
		}
		
		public void clearChanged() {
			super.clearChanged();
		}
	}
	
	private MockObservable<String> observable;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		observable = new MockObservable<String>();
	}
	
	public void testAddObserver() {
		assertEquals(0,observable.countObservers());
		
		observable.addObserver(new MockObserver<String>());
		assertEquals(1, observable.countObservers());
	}
	
	public void testAddingExistingObserverAgain() {
		assertEquals(0, observable.countObservers());
		
		MockObserver<String> observer = new MockObserver<String>();
		observable.addObserver(observer);
		observable.addObserver(observer);
		assertEquals(1, observable.countObservers());
	}
	
	public void testDeleteObserver() {
		assertEquals(0, observable.countObservers());
		
		MockObserver<String> observer = new MockObserver<String>();
		observable.addObserver(observer);
		assertEquals(1, observable.countObservers());
		
		observable.deleteObserver(observer);
		assertEquals(0, observable.countObservers());
	}
	
	public void testDeleteNonexistentObserver() {
		assertEquals(0, observable.countObservers());
		
		MockObserver<String> observer = new MockObserver<String>();
		observable.addObserver(observer);
		assertEquals(1, observable.countObservers());
		
		observable.deleteObserver(new MockObserver<String>());
		assertEquals(1, observable.countObservers());
	}
	
	public void testDeleteAllObservers() {
		assertEquals(0, observable.countObservers());
		
		observable.addObserver(new MockObserver<String>());
		observable.addObserver(new MockObserver<String>());
		observable.addObserver(new MockObserver<String>());
		assertEquals(3, observable.countObservers());
		
		observable.deleteObservers();
		assertEquals(0, observable.countObservers());
	}
	
	public void testSetClearChanged() {
		assertEquals(false, observable.hasChanged());
		
		observable.setChanged();
		assertEquals(true, observable.hasChanged());
		
		observable.clearChanged();
		assertEquals(false, observable.hasChanged());
	}
	
	public void testNotifyObserversWithValue() {
		MockObserver<String> observer1 = new MockObserver<String>();
		MockObserver<String> observer2 = new MockObserver<String>();
		
		observable.addObserver(observer1);
		observable.addObserver(observer2);
		
		String value1 = "Hello world!";
		observable.notifyObservers(value1);
		assertEquals(false, observer1.getUpdateCalled());
		assertEquals(false, observer2.getUpdateCalled());
		
		observable.setChanged();
		observable.notifyObservers(value1);
		assertEquals(value1, observer1.getUpdateValue());
		assertEquals(value1, observer2.getUpdateValue());
		assertEquals(false, observable.hasChanged());
		
		observable.deleteObserver(observer1);
		String value2 = "Hello universe!";
		observable.setChanged();
		observable.notifyObservers(value2);
		assertEquals(value1, observer1.getUpdateValue());
		assertEquals(value2, observer2.getUpdateValue());
		assertEquals(false, observable.hasChanged());
	}
	
	public void testNotifyObserversWithoutValue() {
		MockObserver<String> observer1 = new MockObserver<String>();
		MockObserver<String> observer2 = new MockObserver<String>();
		
		observable.addObserver(observer1);
		observable.addObserver(observer2);
		
		observable.notifyObservers();
		assertEquals(false, observer1.getUpdateCalled());
		assertEquals(false, observer2.getUpdateCalled());
		
		observable.setChanged();
		observable.notifyObservers();
		assertEquals(true, observer1.getUpdateCalled());
		assertEquals(true, observer2.getUpdateCalled());
		assertEquals(null, observer1.getUpdateValue());
		assertEquals(null, observer2.getUpdateValue());
		assertEquals(false, observable.hasChanged());
		
		observer1.setUpdateCalled(false);
		observer2.setUpdateCalled(false);
		observable.deleteObserver(observer1);
		observable.setChanged();
		observable.notifyObservers();
		
		assertEquals(false, observer1.getUpdateCalled());
		assertEquals(true, observer2.getUpdateCalled());
		assertEquals(null, observer1.getUpdateValue());
		assertEquals(null, observer2.getUpdateValue());
		assertEquals(false, observable.hasChanged());
	}
}
