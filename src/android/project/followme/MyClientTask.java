package android.project.followme;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;



public class MyClientTask extends AsyncTask<Void, Void, Void> {
	public static String addr="54.148.59.83";
	//public static String addr="10.0.0.1";
	public static int port=44684;
	public String inMsg = "";
	String dstAddress;
	  int dstPort;
	  String response = "";
	  
	  MyClientTask(){
	   dstAddress = addr;
	   dstPort = port;
	  }

	  @Override
	  protected Void doInBackground(Void... arg0) {
	   
	   Socket socket = null;

	   try {
	    socket = new Socket(dstAddress, dstPort);
	    
	    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        if(LoginActivity.SentFromLogin==true)
        {
        	out.write("Login " + LoginActivity.Username + " Pass " + LoginActivity.Password);
        	out.flush();
        	inMsg = in.readLine();
        	LoginActivity.dataFromLogin = inMsg;
        }
        else
        	if(LoginActivity.SentFromForgotPW==true)
        	{
        		out.write("Forgot " + LoginActivity.Username);
            	out.flush();
            	inMsg = in.readLine();
            	LoginActivity.dataFromForgotPW = inMsg;
        	}
        else
        	if(RegistrationActivity.SentFromRegistration==true)
        	{
        		out.write("Register " + RegistrationActivity.Name + " " + RegistrationActivity.Email + " " + RegistrationActivity.Password + " " + RegistrationActivity.PhoneNumber);
                out.flush();
                inMsg = in.readLine();
                RegistrationActivity.dataFromServer = inMsg;
        	}
       	else
           	if(ContactsActivity.SentFromContactsGetList==true)
           	{
           		out.write("ContactsGet " + MainActivity.UserPublicEmail);
                out.flush();
                inMsg = in.readLine();
                ContactsActivity.InMsg = inMsg;
           	}
        else
           	if(MainActivity.SentFromMainGet==true)
           	{
            	out.write("MainGet " + MainActivity.UserPublicEmail);
                out.flush();
                inMsg = in.readLine();
                MainActivity.DataFromServer = inMsg;
            }
         else
            if(ContactsActivity.SentFromContactsGetNumber==true)
            {
               	out.write("ContactsNum " + MainActivity.UserPublicEmail + " " + ContactsActivity.tb1.getText().toString());
                out.flush();
                inMsg = in.readLine();
                ContactsActivity.InMsg = inMsg;
            }
        else
        	if (Settings.SentFromSettingsGet==true)
        	{
        		out.write("SettingsGet " + MainActivity.UserPublicEmail);
                out.flush();
                inMsg = in.readLine();
                Settings.InMsg = inMsg;
        	}
        else
            if (Settings.SentFromSettingsSet==true)
            {
            	out.write("SettingsSet " + MainActivity.UserPublicEmail + " " + Settings.SendInterval + " " + Settings.SendName + " " + Settings.SendPhone);
                out.flush();
                inMsg = in.readLine();
                Settings.InMsg = inMsg;
            }
        else
          	if(MainActivity.SentCheckSos==true)
           	{
          		out.write("MainCheckSos " + MainActivity.UserPublicEmail);
                out.flush();
                inMsg = in.readLine();
                MainActivity.DataFromServer = inMsg;
           	}
        else
           	if(MainActivity.SentSaveLocationSos==true)
           	{
           		out.write("MainSaveLocationSos " + MainActivity.UserPublicEmail + " " + MainActivity.latitude + " " + MainActivity.longitude);
                out.flush();
           	}
        else
          	if(SendRequest.SentFromRequestMaps==true)
           	{
          		out.write("SentFromRequestMaps " + SendRequest.GetName + " " + SendRequest.GetPhone);
                out.flush();
                inMsg = in.readLine();
                SendRequest.CoordinatsFromServer = inMsg;
          	}
        
        socket.close();
        
        
	   } catch (UnknownHostException e) {
	    e.printStackTrace();
	    response = "UnknownHostException: " + e.toString();
	   } catch (IOException e) {
	    e.printStackTrace();
	    response = "IOException: " + e.toString();
	   }finally{
	    if(socket != null){
	     try {
	      socket.close();
	     } catch (IOException e) {
	      e.printStackTrace();
	     }
	    }
	   }
	   return null;
	  }

	  @Override
	  protected void onPostExecute(Void result) {
	   //LoginActivity.dataFromLogin = inMsg;
	   super.onPostExecute(result);
	  }
}
