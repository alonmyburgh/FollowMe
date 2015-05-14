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

public class SendRequest extends Activity {
	
	public static String GetName;
	public static String GetPhone;
	public static String CoordinatsFromServer;
	Button searchContact;
	Button back;
	Button send;
	EditText Name;
	EditText Phone;
	GPS gps;
	public static boolean SentFromRequestMaps=false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_send_request);
		GetPhone="";
		GetName="";
		Name = (EditText)findViewById(R.id.etName);
		Phone = (EditText)findViewById(R.id.etPhone);
		back = (Button)findViewById(R.id.btnbackreq);
		
		back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
		
		send = (Button)findViewById(R.id.btnSndRqst);
        
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
				if((GetName.equals("")) || (GetPhone.equals("")))
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.empty_request_details, Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				
				
				if((GetName.contains(" ")) || (GetPhone.contains(" ")) )
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.no_spaces_allawed, Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				
				
				SentFromRequestMaps=true;
				MyClientTask clientTask = new MyClientTask();
				try {
					clientTask.execute().get();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (ExecutionException e) {
					
					e.printStackTrace();
				}
				SentFromRequestMaps=false;
				
				if(CoordinatsFromServer.equals("Error"))
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.no_last_update, Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				
				Intent i=new Intent(getBaseContext(),ViewMap.class);
				i.putExtra("coordinates", CoordinatsFromServer);
				startActivity(i);
				
				
				
            }
        });
        
        searchContact = (Button)findViewById(R.id.ibContacts);
        searchContact.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i=new Intent(getBaseContext(),ContactsActivity.class);
				i.putExtra("caller", "Follow");
				startActivity(i);
			}
		});

	}
	
	@Override
	public void onResume(){
	    super.onResume();
	    Name.setText(GetName);
	    Phone.setText(GetPhone);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_request, menu);
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
