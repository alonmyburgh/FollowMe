package android.project.followme;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegistrationActivity extends Activity {
	
	Button back;
	Button register;
	public static String Name;
	public static String Email;
	public static String Password;
	public static String PhoneNumber;
	public static String dataFromServer;
	public static boolean SentFromRegistration=false;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		back = (Button)findViewById(R.id.btnBack);
        
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        register = (Button)findViewById(R.id.btnRegister);
        
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	EditText etValues = (EditText)findViewById(R.id.tbNameReg);
				Name = etValues.getText().toString();
				etValues = (EditText)findViewById(R.id.tbEmailReg);
				Email = etValues.getText().toString();
				etValues = (EditText)findViewById(R.id.tbPasswordReg);
				Password = etValues.getText().toString();
				etValues = (EditText)findViewById(R.id.tbPhoneNumberReg);
				PhoneNumber = etValues.getText().toString();
				
				if((Name.equals("")) || (Email.equals("")) || (Password.equals("")) || (PhoneNumber.equals("")))
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.empty_registration_details, Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				if(!Email.contains("@"))
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.email_not_contain, Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				if((Name.contains(" ")) || (Email.contains(" ")) || (Password.contains(" ")) || (PhoneNumber.contains(" ")))
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.no_spaces_allawed, Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				SentFromRegistration=true;
				MyClientTask clientTask = new MyClientTask();
				try {
					clientTask.execute().get();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (ExecutionException e) {
					
					e.printStackTrace();
				}
				SentFromRegistration=false;
				if(dataFromServer.startsWith("O"))
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.registration_complete, Toast.LENGTH_SHORT);
					t.show();
					register.setClickable(false);
				}
				else
					if(dataFromServer.startsWith("E"))
					{
						Toast t = Toast.makeText(getApplicationContext(), R.string.email_exists, Toast.LENGTH_SHORT);
						t.show();
						return;
					}	
            }
        });
		
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
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
