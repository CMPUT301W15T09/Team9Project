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
		assertEquals(observable.countObservers(), 0);
		
		observable.addObserver(new MockObserver<String>());
		assertEquals(observable.countObservers(), 1);
	}
	
	public void testAddingExistingObserverAgain() {
		assertEquals(observable.countObservers(), 0);
		
		MockObserver<String> observer = new MockObserver<String>();
		observable.addObserver(observer);
		observable.addObserver(observer);
		assertEquals(observable.countObservers(), 1);
	}
	
	public void testDeleteObserver() {
		assertEquals(observable.countObservers(), 0);
		
		MockObserver<String> observer = new MockObserver<String>();
		observable.addObserver(observer);
		assertEquals(observable.countObservers(), 1);
		
		observable.deleteObserver(observer);
		assertEquals(observable.countObservers(), 0);
	}
	
	public void testDeleteNonexistentObserver() {
		assertEquals(observable.countObservers(), 0);
		
		MockObserver<String> observer = new MockObserver<String>();
		observable.addObserver(observer);
		assertEquals(observable.countObservers(), 1);
		
		observable.deleteObserver(new MockObserver<String>());
		assertEquals(observable.countObservers(), 1);
	}
	
	public void testDeleteAllObservers() {
		assertEquals(observable.countObservers(), 0);
		
		observable.addObserver(new MockObserver<String>());
		observable.addObserver(new MockObserver<String>());
		observable.addObserver(new MockObserver<String>());
		assertEquals(observable.countObservers(), 3);
		
		observable.deleteObservers();
		assertEquals(observable.countObservers(), 0);
	}
	
	public void testSetClearChanged() {
		assertEquals(observable.hasChanged(), false);
		
		observable.setChanged();
		assertEquals(observable.hasChanged(), true);
		
		observable.clearChanged();
		assertEquals(observable.hasChanged(), false);
	}
	
	public void testNotifyObserversWithValue() {
		MockObserver<String> observer1 = new MockObserver<String>();
		MockObserver<String> observer2 = new MockObserver<String>();
		
		observable.addObserver(observer1);
		observable.addObserver(observer2);
		
		String value1 = "Hello world!";
		observable.notifyObservers(value1);
		assertEquals(observer1.getUpdateCalled(), false);
		assertEquals(observer2.getUpdateCalled(), false);
		
		observable.setChanged();
		observable.notifyObservers(value1);
		assertEquals(observer1.getUpdateValue(), value1);
		assertEquals(observer2.getUpdateValue(), value1);
		assertEquals(observable.hasChanged(), false);
		
		observable.deleteObserver(observer1);
		String value2 = "Hello universe!";
		observable.setChanged();
		observable.notifyObservers(value2);
		assertEquals(observer1.getUpdateValue(), value1);
		assertEquals(observer2.getUpdateValue(), value2);
		assertEquals(observable.hasChanged(), false);
	}
	
	public void testNotifyObserversWithoutValue() {
		MockObserver<String> observer1 = new MockObserver<String>();
		MockObserver<String> observer2 = new MockObserver<String>();
		
		observable.addObserver(observer1);
		observable.addObserver(observer2);
		
		observable.notifyObservers();
		assertEquals(observer1.getUpdateCalled(), false);
		assertEquals(observer2.getUpdateCalled(), false);
		
		observable.setChanged();
		observable.notifyObservers();
		assertEquals(observer1.getUpdateCalled(), true);
		assertEquals(observer2.getUpdateCalled(), true);
		assertEquals(observer1.getUpdateValue(), null);
		assertEquals(observer2.getUpdateValue(), null);
		assertEquals(observable.hasChanged(), false);
		
		observer1.setUpdateCalled(false);
		observer2.setUpdateCalled(false);
		observable.deleteObserver(observer1);
		observable.setChanged();
		observable.notifyObservers();
		
		assertEquals(observer1.getUpdateCalled(), false);
		assertEquals(observer2.getUpdateCalled(), true);
		assertEquals(observer1.getUpdateValue(), null);
		assertEquals(observer2.getUpdateValue(), null);
		assertEquals(observable.hasChanged(), false);
	}
}
