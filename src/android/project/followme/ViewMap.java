package android.project.followme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class ViewMap extends Activity {
	private double latitude;
	private double longitude;
	private GoogleMap googleMap;
	private String coordinates;
	private String latitudeString;
	private String longitudeString;
	private String lastUpdate;
	private String address;
	private String reqName;
	TextView fLocation;
	Button close;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_map);
		
		
		try {
            
			SetAddressAndLocation();
 
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
		
		LatLng loc= new LatLng(latitude,longitude);


		
		try {
            // Loading map
            initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
		
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		
		
        Marker location = googleMap.addMarker(new MarkerOptions().position(loc).title(reqName +" location")
        		.icon(BitmapDescriptorFactory.fromResource(R.drawable.redpin)));
        
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        //googleMap.getUiSettings().setZoomControlsEnabled(true); 
        
        close = (Button)findViewById(R.id.btnClz);
		
		close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
				
	}
	
	private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.mapFragment)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        }
	
	private void SetAddressAndLocation()
	{
		reqName=SendRequest.GetName;
		Intent intent = getIntent();        
		coordinates = intent.getStringExtra("coordinates");
		
		if (coordinates != null)
		{
		
			String[] locInfo = coordinates.split(";");
			latitudeString = locInfo[0];
			longitudeString = locInfo[1];
			lastUpdate = locInfo[2];
			latitude = Double.parseDouble(latitudeString); 
			longitude = Double.parseDouble(longitudeString); 
			
			LocationAddress locationAddress = new LocationAddress();
	        address=locationAddress.getAddressFromLocation(latitude, longitude,
	                getApplicationContext());
	        
	        fLocation =(TextView) findViewById(R.id.tvLocation);
			fLocation.setText(reqName + "\n" + "last location from: " + lastUpdate + "\n" + address);

        
       
        } 
		else 
		{
			Toast.makeText(getApplicationContext(),
                    "Sorry! unable to show address", Toast.LENGTH_SHORT)
                    .show();
        }

		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_map, menu);
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
	
	
	
}
