package com.indragie.cmput301as1;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GeolocationActivity extends Activity {

	//================================================================================
	// Constants
	//================================================================================

	public static final String EXTRA_LOCATION = "com.indragie.cmput301as1.EXTRA_LOCATION";
	
	//================================================================================
	// Properties
	//================================================================================
	
	private Location location;
	
	private GoogleMap map;
	
	//================================================================================
	// Activity Callbacks
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geolocation);
		
		if (!isNetworkAvailable(this)) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setCancelable(false);
			alert.setMessage("No Connection Available");
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					finish();
				}
			});
			alert.show();
		} else {
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		}
		
		Intent intent = getIntent();
		location = (Location) intent.getSerializableExtra(EXTRA_LOCATION);
		if (location != null) {
			LatLng lastLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
			MarkerOptions markerOpts = new MarkerOptions();
			markerOpts.position(lastLocationLatLng);
			map.animateCamera(CameraUpdateFactory.newLatLng(lastLocationLatLng));
			map.addMarker(markerOpts);
		}
		
		map.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				MarkerOptions markerOpts = new MarkerOptions();
				markerOpts.position(point);
				map.clear();
				map.animateCamera(CameraUpdateFactory.newLatLng(point));
				map.addMarker(markerOpts);		
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.geolocation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	@Override
	public void onBackPressed() {
		
	}
}
