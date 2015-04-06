package com.indragie.cmput301as1;

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
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class GeolocationActivity extends Activity {

	//================================================================================
	// Constants
	//================================================================================

	public static final String EXTRA_LOCATION = "com.indragie.cmput301as1.EXTRA_LOCATION";
	
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
		setContentView(R.layout.activity_geolocation);
		
		/*
		isNetworkAvailable(getActivity());
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		*/
		
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
		Location location = (Location) intent.getSerializableExtra(EXTRA_LOCATION);
		if (location != null) {
			setUpLastLocation(location);
			Toast toast = Toast.makeText(this, "Loading last location", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			// TODO: set this location to current location
		}
		
		marker = map.addMarker(new MarkerOptions().position(new LatLng(0,0))); // TODO: change to current location
		map.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				marker.setPosition(point);
				map.animateCamera(CameraUpdateFactory.newLatLng(point));
			}
		});
	}
	
	public void setUpLastLocation(Location location) {
		LatLng lastLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		MarkerOptions markerOpts = new MarkerOptions();
		markerOpts.position(lastLocationLatLng);
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(lastLocationLatLng)
		.zoom(17)
		.build();
		 map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));		
		 map.addMarker(markerOpts);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.geolocation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
	/*
	private void isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetworkInfo != null || (activeNetworkInfo.isConnectedOrConnecting())); {
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
		
	}
	*/
	
	//================================================================================
	// Subclass Overrides
	//================================================================================
	
	public Intent getResultIntent() {
		LatLng locationLatLng = marker.getPosition();
		Location location = new Location("");
		location.setLatitude(locationLatLng.latitude);
		location.setLongitude(locationLatLng.longitude);
		
		Intent intent = new Intent();
		
		Bundle bundle = new Bundle();
		bundle.putParcelable(EXTRA_LOCATION, location);
		intent.putExtras(bundle);
		
		//intent.putExtra(EXTRA_LOCATION, location);
		return intent;
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_OK, getResultIntent());
		finish();
	}
}
