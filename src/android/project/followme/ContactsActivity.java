package android.project.followme;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsActivity extends Activity {
	
	Button contactsDone;
	public static boolean SentFromContactsGetList=false;
	public static boolean SentFromContactsGetNumber=false;
	public static String InMsg="";
	public static EditText tb1;
	public static String ReturnToFollowName;
	public static String ReturnToFollowPhone;
	public static final String RESULT_KEY = "result";
	String[] choosen;
	GridView dg;
	List<String> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		
		ReturnToFollowName="";
		ReturnToFollowPhone="";
		
		
		
		dg = (GridView)findViewById(R.id.gvContacts);
		list=new ArrayList<String>();
		
		SentFromContactsGetList=true;
		MyClientTask clientTask = new MyClientTask();
		try {
			clientTask.execute().get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			
			e.printStackTrace();
		}
		SentFromContactsGetList=false;
		if(InMsg.equals(";"))
		{
			Toast t = Toast.makeText(getApplicationContext(), R.string.no_contacts_found, Toast.LENGTH_SHORT);
			t.show();
		}
		if(!InMsg.equals(";"))
		{
			String[] items = InMsg.split(";");
			for (int i=0;i<items.length;i++)
			{
				items[i]=items[i].replaceAll("-", "\n");
				list.add(items[i]);
			}
		}
		ArrayAdapter<String> adp = new ArrayAdapter<String> (this,android.R.layout.simple_dropdown_item_1line,list);
    	dg.setAdapter(adp);

		String caller = getIntent().getStringExtra("caller");
		contactsDone = (Button)findViewById(R.id.bContactsDone);
		tb1 = (EditText)findViewById(R.id.tbContactsPhoneNum);
		if(!caller.equals("MainActivity"))
		{
			//contactsDone.setText(R.string.contacts_done);
			contactsDone.setBackgroundResource(R.drawable.donebtn);
			contactsDone.setTag("Done");
			tb1.setVisibility(View.INVISIBLE);
		}
		else
		{
			//contactsDone.setText(R.string.contacts_insert);
			contactsDone.setBackgroundResource(R.drawable.insertbtn);
			contactsDone.setTag("Insert");
			tb1.setVisibility(View.VISIBLE);
		}
		
		dg.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
			   Toast.makeText(getApplicationContext(),
				((TextView) v).getText(), Toast.LENGTH_SHORT).show();
			}
		});
        
		contactsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactsDone.getTag().equals("Insert"))
                {
                	if(CheckNumber(tb1.getText().toString())==false)
                	{
                		Toast t = Toast.makeText(getApplicationContext(), R.string.incorrect_phone_num_format, Toast.LENGTH_SHORT);
						t.show();
						return;
                	}
                	SentFromContactsGetNumber=true;
            		MyClientTask clientTask = new MyClientTask();
            		try {
            			clientTask.execute().get();
            		} catch (InterruptedException e) {
            			
            			e.printStackTrace();
            		} catch (ExecutionException e) {
            			
            			e.printStackTrace();
            		}
            		SentFromContactsGetNumber=false;
                	
                	if(InMsg.equals("OK"))
                	{
                		Toast t = Toast.makeText(getApplicationContext(), R.string.contacts_added, Toast.LENGTH_SHORT);
						t.show();
						finish();
                	}
                	
                	if(InMsg.equals("NO"))
                	{
                		Toast t = Toast.makeText(getApplicationContext(), R.string.contacts_not_added, Toast.LENGTH_SHORT);
						t.show();
                	}
                }
                if(contactsDone.getTag().equals("Done"))
                {
                	String caller = getIntent().getStringExtra("caller");
                	if(caller.equals("Settings"))
                	{
                		Settings.GetName = ReturnToFollowName;
                		Settings.GetPhone = ReturnToFollowPhone;
                		Settings.Flag=true;
                	}
                	else
                	{
                		SendRequest.GetName = ReturnToFollowName;
                    	SendRequest.GetPhone = ReturnToFollowPhone;
                	}
                	finish();
                }
            }
        });
		
		dg.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v,
	                int position, long id) {
	        	String res = list.get(position).toString();
	            choosen = res.split("\n");
	            ReturnToFollowName=choosen[0];
	            ReturnToFollowPhone=choosen[1];
	            Toast.makeText(ContactsActivity.this, res, Toast.LENGTH_SHORT).show();
	        }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contacts, menu);
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
		if(num.charAt(0)!='0')
		{
			return false;
		}
		if(num.length()!=10)
		{
			return false;
		}
		for(int i=0;i<10;i++)
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
