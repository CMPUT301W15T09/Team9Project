package com.indragie.cmput301as1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
	public void onLocationChanged(Location location) {
		LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
		marker.setPosition(point);
		map.animateCamera(CameraUpdateFactory.newLatLng(point));		
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


	
	private void goToCurrentLocation() {
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!enabled) {
			startGpsUnavailabeDialog();
		}
		
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			onLocationChanged(location);
		} else {
			Toast.makeText(this, "GPS is not available at the moment", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void startNetworkUnavailableDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setCancelable(false);
		alert.setMessage("No Connection Available");
		alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});
		alert.show();
	}
	
	private void startGpsUnavailabeDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("GPS is disabled for your device. Would you like to enable it?");
		alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		});
		alert.setNegativeButton(android.R.string.cancel, null);
		alert.show();
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
