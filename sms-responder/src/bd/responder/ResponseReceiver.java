package bd.responder;

import java.io.*;

import android.content.*;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class ResponseReceiver extends BroadcastReceiver 
{
	private void sendMessage(Context context, Intent intent, SmsMessage inMessage)
	{
		String sendData = "Message Recieved";
		SmsManager mng = SmsManager.getDefault();
		PendingIntent dummyEvent = PendingIntent.getBroadcast(context, 0, new Intent("bd.responder.IGNORE_ME"), 0);
		
		String addr = inMessage.getOriginatingAddress();
		
		try{
			mng.sendTextMessage(addr, null, sendData, dummyEvent, dummyEvent);
		}catch(Exception e){
			
		}
	}
	
	private SmsMessage[] getMessagesFromIntent(Intent intent)
	{
		SmsMessage retMsgs[] = null;
		Bundle bdl = intent.getExtras();
		try{
			Object pdus[] = (Object [])bdl.get("pdus");
			retMsgs = new SmsMessage[pdus.length];
			for(int n=0; n < pdus.length; n++)
			{
				byte[] byteData = (byte[])pdus[n];
				retMsgs[n] = SmsMessage.createFromPdu(byteData);
			}	
			
		}catch(Exception e)
		{
			
		}
		return retMsgs;
	}
	
	public void onReceive(Context context, Intent intent) 
	{
		String FILENAME = "RRSettings";
		Boolean on = false;
		if(!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
		{
			return;
		}
		
		try{
			FileInputStream fis = context.openFileInput(FILENAME);
			byte[] settings = new byte[1];
			fis.read(settings);
			if(settings[0] == '1')
			{
				on = true;
			}
			
		}catch(Exception e)
		{
			try{
				FileOutputStream fos = context.openFileOutput(FILENAME, 0);
				String string = "1";
				fos.write(string.getBytes());
				fos.close();
				on = true;
			}catch(Exception Ex)
			{
				
			}			
		}
		
		if(on)
		{
			SmsMessage msg[] = getMessagesFromIntent(intent);
			
			for(int i=0; i < msg.length; i++)
			{
				String message = msg[i].getDisplayMessageBody();
				if(message != null && message.length() > 0)
				{
					if(message.startsWith("Test"))
					{
						sendMessage(context, intent, msg[i]);
					}
				}
			}
		}
	}
}
