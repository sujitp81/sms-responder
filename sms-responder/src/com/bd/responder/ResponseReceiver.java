package com.bd.responder;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
//import android.widget.Toast;

public class ResponseReceiver extends BroadcastReceiver 
{
	ArrayList<String> settings;
	
	private void sendMessage(Context context, Intent intent, SmsMessage inMessage)
	{
		String sendData = getMessage(context);
		SmsManager mng = SmsManager.getDefault();
//		PendingIntent dummyEvent = PendingIntent.getBroadcast(context, 0, new Intent("bd.responder.IGNORE_ME"), 0);
		
		String addr = inMessage.getOriginatingAddress();
		
		try{
			mng.sendTextMessage(addr, null, sendData, null, null);
		}catch(Exception e){

		}
	}
	
	private String getMessage(Context context)
	{
		DBAdapter db = new DBAdapter(context);
		String message = "failed";
		db.open();
		message = db.getMessage(settings.get(1),"general");
		db.close();
		return message;
	}
	
	private SmsMessage[] getMessagesFromIntent(Intent intent)
	{
		SmsMessage retMsgs[] = null;
		Bundle bdl = intent.getExtras();
		Object pdus[] = (Object [])bdl.get("pdus");
		retMsgs = new SmsMessage[pdus.length];
		for(int n=0; n < pdus.length; n++)
		{
			byte[] byteData = (byte[])pdus[n];
			retMsgs[n] = SmsMessage.createFromPdu(byteData);
		}	
		return retMsgs;
	}
	
	public void onReceive(Context context, Intent intent) 
	{
		if(!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
		{
			return;
		}
		DBAdapter db = new DBAdapter(context);
		
		db.open();
		settings = db.getSettings();
		db.close();

		if(!settings.get(1).equals(""))
		{
			SmsMessage msg[] = getMessagesFromIntent(intent);

			for(int i=0; i < msg.length; i++)
			{
				String message = msg[i].getDisplayMessageBody();
				if(message != null && message.length() > 0)
				{
						sendMessage(context, intent, msg[i]);
				}
			}
		}
	}
}
