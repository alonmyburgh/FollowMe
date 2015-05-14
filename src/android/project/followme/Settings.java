package android.project.followme;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Settings extends Activity {
	
	Button settingsBack;
	Button settingsSave;
	EditText settingsTimeInterval;
	EditText settingsName;
	EditText settingsPhone;
	Button settingsSearch;
	public static Boolean Flag;
	public static String SendInterval;
	public static String SendName;
	public static String SendPhone;
	public static String GetName="";
	public static String GetPhone="";
	public static boolean SentFromSettingsGet=false;
	public static boolean SentFromSettingsSet=false;
	public static String InMsg="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		GetName="";
		GetPhone="";
		Flag = false;
		settingsTimeInterval = (EditText)findViewById(R.id.etSettingsTimeInterval);
		settingsName = (EditText)findViewById(R.id.etSettingsName);
		settingsPhone = (EditText)findViewById(R.id.etSettingsPhone);
		settingsBack = (Button)findViewById(R.id.bSettingsBack);
		
		SentFromSettingsGet=true;
		MyClientTask clientTask = new MyClientTask();
		try {
			clientTask.execute().get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			
			e.printStackTrace();
		}
		SentFromSettingsGet=false;
		if(InMsg.equals(";"))
		{
			Toast t = Toast.makeText(getApplicationContext(), R.string.settings_no_settings_found, Toast.LENGTH_SHORT);
			t.show();
		}
		else
		{
			String[] details;
			details = InMsg.split(";");
			settingsTimeInterval.setText(details[0]);
			settingsName.setText(details[1]);
			settingsPhone.setText(details[2]);
		}
		
		settingsBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		settingsSave = (Button)findViewById(R.id.bSettingsSave);
		settingsSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(CheckNumber(settingsTimeInterval.getText().toString())==false)
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.settings_incorrect_time, Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				if(settingsName.getText().toString().equals(""))
				{
					
					Toast t = Toast.makeText(getApplicationContext(), R.string.settings_incorrect_name, Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				SendInterval = settingsTimeInterval.getText().toString();
				SendName = settingsName.getText().toString();
				SendPhone = settingsPhone.getText().toString();
				SentFromSettingsSet=true;
				MyClientTask clientTask = new MyClientTask();
				try {
					clientTask.execute().get();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (ExecutionException e) {
					
					e.printStackTrace();
				}
				SentFromSettingsSet=false;
				
				if(InMsg.equals("OK"))
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.settings_save_success, Toast.LENGTH_SHORT);
					t.show();
				}
				else
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.settings_save_failed, Toast.LENGTH_SHORT);
					t.show();
				}
			}
		});
		
		settingsSearch = (Button)findViewById(R.id.ibSettingsSearch);
		settingsSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(getBaseContext(),ContactsActivity.class);
				i.putExtra("caller", "Settings");
				startActivity(i);
			}
		});
		
	}
	
	@Override
	public void onResume(){
	    super.onResume();
	    if(Flag==true)
	    {
	    	settingsName.setText(GetName);
	    	settingsPhone.setText(GetPhone);
	    }
	    Flag=false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
	
	public boolean CheckNumber (String num)
	{
		if(!num.equals(""))
		{
		if(Integer.parseInt(num)==0)
		{
			return false;
		}
		if(num.length()>4)
		{
			return false;
		}
		for(int i=0;i<num.length();i++)
		{
			if((num.charAt(i)<'0') || (num.charAt(i)>'9'))
			{
				return false;
			}
		}
		return true;
		}
		return false;
	}
}
