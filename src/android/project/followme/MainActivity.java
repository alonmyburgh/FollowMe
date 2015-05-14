package android.project.followme;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Timer sosTimer;
	Timer shareTimer;
	GPS gps;
	public static String UserPublicEmail;
	public static String UserPublicPass;
	public static String UserPublicName;
	public static String UserPublicPhone;
	public static boolean SentFromMainGet=false;
	public static String DataFromServer;
	public static String SosTimerPhoneNumber;
	Button follow;
	Button settings;
	Button contacts;
	Button exit;
	Button startstop;
	Button shareLocation;
	public static double latitude;
	public static double longitude;
	ImageView sosBackground;
	ImageView sosShout;
	AnimationDrawable shoutAnimation;
	boolean StartStopFlag=false;
	boolean StartStopShareFlag=false;
	public static boolean SentCheckSos=false;
	public static boolean SentSaveLocationSos=false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		shareLocation = (Button)findViewById(R.id.bShareLocation);
		sosBackground = (ImageView)findViewById(R.id.ivStart);
		sosShout = (ImageView)findViewById(R.id.ivShout);
		//shoutAnimation = (AnimationDrawable)sosShout.getBackground();
		sosShout.setVisibility(View.INVISIBLE);
		startstop = (Button)findViewById(R.id.btnSosStart);
		shareLocation.setTag("Off");
		startstop.setTag("Off");
		startstop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(StartStopFlag==false)
				{
					if(shareLocation.getTag().equals("On"))
					{
						Toast.makeText(getApplicationContext(),R.string.share_location_is_on , Toast.LENGTH_LONG).show();
						return;
					}
					SentCheckSos=true;
					MyClientTask clientTask = new MyClientTask();
					try {
						clientTask.execute().get();
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					} catch (ExecutionException e) {
						
						e.printStackTrace();
					}
					SentCheckSos=false;
					if(DataFromServer.equals("Error"))
					{
						Toast.makeText(getApplicationContext(),R.string.choose_sos_first , Toast.LENGTH_LONG).show();
						return;
					}
					startstop.setTag("On");
					String[] ans = DataFromServer.split(";");
					int SosTimerInterval = Integer.parseInt(ans[1].toString());
					SosTimerInterval = SosTimerInterval * 1000 * 60;
					SosTimerPhoneNumber = ans[0].toString(); 
					
					StartStopFlag=true;
					startstop.setBackgroundResource(R.drawable.stopbtn);
					sosBackground.setImageResource(R.drawable.stopbck);
					sosShout.setVisibility(View.VISIBLE);
					//shoutAnimation.start();
					
					sosTimer = new Timer();
					sosTimer.schedule(new TimerTask() {          
				        @Override
				        public void run() {
				        	SosTimerTask();
				        }

				    }, 0, SosTimerInterval);
					
				}
				else
				{
					StartStopFlag=false;
					startstop.setTag("Off");
					startstop.setBackgroundResource(R.drawable.startbtn);
					sosBackground.setImageResource(R.drawable.startbck);
					//shoutAnimation.stop();
					sosShout.setVisibility(View.INVISIBLE);
					sosTimer.cancel();
					gps.stopUsingGPS();
				}
				
			}
		});

		String args = getIntent().getStringExtra("args");
		String[] details = args.split(";");
		UserPublicEmail = details[0];
		UserPublicPass = details[1];
		
		SentFromMainGet=true;
		MyClientTask clientTask = new MyClientTask();
		try {
			clientTask.execute().get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			
			e.printStackTrace();
		}
		SentFromMainGet=false;
		details = DataFromServer.split(";");
		UserPublicName=details[0];
		UserPublicPhone=details[1];
		
		follow = (Button)findViewById(R.id.btnfollow);
        
        follow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
						
				Intent i=new Intent(getBaseContext(),SendRequest.class);
				startActivity(i);
			}
        });
        
        shareLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(StartStopShareFlag==false)
				{
					if(startstop.getTag().equals("On"))
					{
						Toast.makeText(getApplicationContext(),R.string.sos_is_on , Toast.LENGTH_LONG).show();
						return;
					}
					SentCheckSos=true;
					MyClientTask clientTask = new MyClientTask();
					try {
						clientTask.execute().get();
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					} catch (ExecutionException e) {
						
						e.printStackTrace();
					}
					SentCheckSos=false;
					if(DataFromServer.equals("Error"))
					{
						Toast.makeText(getApplicationContext(),R.string.choose_sos_first , Toast.LENGTH_LONG).show();
						return;
					}
					shareLocation.setTag("On");
					String[] ans = DataFromServer.split(";");
					int ShareTimerInterval = Integer.parseInt(ans[1].toString());
					ShareTimerInterval = ShareTimerInterval * 1000 * 60;
					
					StartStopShareFlag=true;
					shareLocation.setBackgroundResource(R.drawable.stopsharebtn);
					shareTimer = new Timer();
					shareTimer.schedule(new TimerTask() {          
				        @Override
				        public void run() {
				        	ShareTimerTask();
				        }

				    }, 0, ShareTimerInterval);
					
				}
				else
				{
					StartStopShareFlag=false;
					shareLocation.setTag("Off");
					shareLocation.setBackgroundResource(R.drawable.startsharebtn);
					shareTimer.cancel();
					gps.stopUsingGPS();
				}
				
			}
		});
        
        settings = (Button)findViewById(R.id.btnSettings);
        
        settings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
						
				Intent i=new Intent(getBaseContext(),Settings.class);
				startActivity(i);
			}
        });
        
        contacts = (Button)findViewById(R.id.bContacts);
        contacts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
						
				Intent i=new Intent(getBaseContext(),ContactsActivity.class);
				i.putExtra("caller", "MainActivity");
				startActivity(i);
			}
        });
        
        exit = (Button)findViewById(R.id.btnClose);
        exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.exit(0);
			}
		});
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	private void SosTimerTask()
	{
		this.runOnUiThread(Timer_Tick);
	}
	private Runnable Timer_Tick = new Runnable() {
	    public void run() {
	    	gps = new GPS(MainActivity.this);
			if(gps.canGetLocation()){
	            latitude = gps.getLatitude();
	            longitude = gps.getLongitude();
	            
	            SentSaveLocationSos=true;
				MyClientTask clientTask = new MyClientTask();
				try {
					clientTask.execute().get();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (ExecutionException e) {
					
					e.printStackTrace();
				}
				SentSaveLocationSos=false;
	            
	            SendSosMessage(SosTimerPhoneNumber, latitude, longitude); 
	            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
	        }
			else{
	            // can't get location
	            // GPS or Network is not enabled
	            // Ask user to enable GPS/network in settings
	            gps.showSettingsAlert();
	        }	
	    }
	};
	
	private void ShareTimerTask()
	{
		this.runOnUiThread(Timer_Tick_Share);
	}
	private Runnable Timer_Tick_Share = new Runnable() {
	    public void run() {
	    	gps = new GPS(MainActivity.this);
			if(gps.canGetLocation()){
	            latitude = gps.getLatitude();
	            longitude = gps.getLongitude();
	            
	            SentSaveLocationSos=true;
				MyClientTask clientTask = new MyClientTask();
				try {
					clientTask.execute().get();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (ExecutionException e) {
					
					e.printStackTrace();
				}
				SentSaveLocationSos=false;
	            
	            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
	        }
			else{
	            // can't get location
	            // GPS or Network is not enabled
	            // Ask user to enable GPS/network in settings
	            gps.showSettingsAlert();
	        }	
	    }
	};
	
    private void SendSosMessage(String number, double Lat, double Long) {
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(number, null,"The user: " + MainActivity.UserPublicName + " (" + MainActivity.UserPublicPhone + ") Have Activated S.O.S.\n His Location is:\nLat: " + Lat + "\nLong: " + Long +".\nMap view is available in FollowMe.", null, null);
    }
}
