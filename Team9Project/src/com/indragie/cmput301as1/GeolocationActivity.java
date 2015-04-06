package com.indragie.cmput301as1;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class GeolocationActivity extends Activity implements LocationListener{

	//================================================================================
	// Constants
	//================================================================================

	public static final String EXTRA_LOCATION_LATITUDE = "com.indragie.cmput301as1.EXTRA_LOCATION_LATITUDE";
	public static final String EXTRA_LOCATION_LONGITUDE = "com.indragie.cmput301as1.EXTRA_LOCATION_LONGITUDE";
	
	//================================================================================
	// Properties
	//================================================================================
		
	private GoogleMap map;
	
	private Marker marker;
	
	//================================================================================
	// Activity Callbacks
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!isNetworkAvailable(this)) {
			startNetworkUnavailableDialog();
		}
		
		setContentView(R.layout.activity_geolocation);
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		Intent intent = getIntent();
		double latitude = intent.getExtras().getDouble(EXTRA_LOCATION_LATITUDE);
		double longitude = intent.getExtras().getDouble(EXTRA_LOCATION_LONGITUDE);
		LatLng location = new LatLng(latitude, longitude);
		marker = map.addMarker(new MarkerOptions().position(location));
		map.animateCamera(CameraUpdateFactory.newLatLng(location));
		
		map.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				marker.setPosition(point);
				map.animateCamera(CameraUpdateFactory.newLatLng(point));
			}
		});
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.geolocation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_current_location) {
			goToCurrentLocation();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}

	private void startNetworkUnavailableDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setCancelable(false);
		alert.setMessage("No Connection Available");
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});
		alert.show();
	}
	
	private void goToCurrentLocation() {
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
		marker.setPosition(point);
		map.animateCamera(CameraUpdateFactory.newLatLng(point));
	}
	
	//================================================================================
	// Subclass Overrides
	//================================================================================
	
	public Intent getResultIntent() {
		LatLng locationLatLng = marker.getPosition();
		Intent intent = new Intent();
		intent.putExtra(EXTRA_LOCATION_LATITUDE, locationLatLng.latitude);
		intent.putExtra(EXTRA_LOCATION_LONGITUDE, locationLatLng.longitude);
		return intent;
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK, getResultIntent());
		finish();
	}
}
