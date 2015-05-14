package android.project.followme;

import java.util.concurrent.ExecutionException;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends Activity {

	TextView register;
	TextView ForgotMyPW;
	EditText Usern;
	Button login;
	public static boolean SentFromLogin=false;
	public static boolean SentFromForgotPW=false;
	public static String dataFromLogin;
	public static String dataFromForgotPW;
	public static String Username;
	public static String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Usern = (EditText)findViewById(R.id.tbEmail);
        Usern.requestFocus();
        
        register = (TextView)findViewById(R.id.tRegister);
        
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),RegistrationActivity.class);
                startActivity(i);
            }
        });
        
        ForgotMyPW = (TextView)findViewById(R.id.tForgotPassword);
        
        ForgotMyPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	EditText editText = (EditText)findViewById(R.id.tbEmail);
				Username = editText.getText().toString();
				if(!Username.equals(""))
				{
					SentFromForgotPW=true;
					MyClientTask clientTask = new MyClientTask();
					try {
						clientTask.execute().get();
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					} catch (ExecutionException e) {
						
						e.printStackTrace();
					}
					SentFromForgotPW=false;
				}
				else
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.empty_login, Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				if(dataFromForgotPW.startsWith("O"))
				{
					String[] parts = dataFromForgotPW.split(" ");
					String number=parts[1];
					String message=parts[2];
					Send(number, message);
				}
				else
					if(dataFromForgotPW.startsWith("F"))
				{
						Toast t = Toast.makeText(getApplicationContext(), R.string.wrong_username, Toast.LENGTH_SHORT);
						t.show();
						return;
				}
            }
        });
        
        login = (Button)findViewById(R.id.bLogin);
        
        login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText editText = (EditText)findViewById(R.id.tbEmail);
				Username = editText.getText().toString();
				editText = (EditText)findViewById(R.id.tbPassword);
				Password = editText.getText().toString();
				if(!Username.equals("") && !Password.equals(""))
				{
					SentFromLogin=true;
					MyClientTask clientTask = new MyClientTask();
					try {
						clientTask.execute().get();
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					} catch (ExecutionException e) {
						
						e.printStackTrace();
					}	
					SentFromLogin=false;
				
				}
				else
				{
					Toast t = Toast.makeText(getApplicationContext(), R.string.empty_login_details, Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				
				if (dataFromLogin.equals("OK"))
				{
					Intent i=new Intent(getBaseContext(),MainActivity.class);
					i.putExtra("args",Username + ";" + Password );
					startActivity(i);
					finish();
				}
				else
					if (dataFromLogin.equals("UserErr"))
					{
						Toast t = Toast.makeText(getApplicationContext(), R.string.wrong_username, Toast.LENGTH_SHORT);
						t.show();
					}
					else
						if(dataFromLogin.equals("PassErr"))
						{
							Toast t = Toast.makeText(getApplicationContext(), R.string.wrong_password, Toast.LENGTH_SHORT);
							t.show();
						}
			}
		});
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
    private void Send(String number, String message) {
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(number, null, "You've Requested A Password From FollowMe Forgot Your Password.\nYour Password is:"+message, null, null);
        Toast t = Toast.makeText(getApplicationContext(), R.string.sms_sent, Toast.LENGTH_SHORT);
		t.show();
    }
    
    
}
