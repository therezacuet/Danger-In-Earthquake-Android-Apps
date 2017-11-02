package com.thereza.dangerinearthquake;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SMSReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle=intent.getExtras();
		SmsMessage[]allMessage=null;
		String number=null;
		String message="";
		Object[] object=(Object[]) bundle.get("pdus");
		allMessage=new SmsMessage[object.length];
		for(int i=0;i<object.length;i++){
			allMessage[i]=SmsMessage.createFromPdu((byte[]) object[i]);
			if(number==null) number=allMessage[i].getOriginatingAddress();
			message+=allMessage[i].getMessageBody();
			
			//Toast.makeText(context,message,Toast.LENGTH_LONG).show();
		}
		SmsManager manager=SmsManager.getDefault();
		manager.sendTextMessage(number,null,message,null,null);
		//Toast.makeText(context,"SMS recieved from "+number,Toast.LENGTH_LONG).show();
		if (number != null && number.equals("GP Internet")) {
            // Process our sms...
           // this.abortBroadcast();
			//Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();
			 Intent smsIntent=new Intent(context,MainActivity.class);
		     smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		     smsIntent.putExtra("sms_str", number);
		     context.startActivity(smsIntent);
			
			
			
        }
		
		
		
	}

}
