package com.example.hangout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = SMSReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String format = bundle.getString("format");
            Object[] pdus = (Object[]) bundle.get(pdu_type);
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
            }
            for (int i = 0; i < msgs.length; i++) {
                String number = msgs[i].getDisplayOriginatingAddress();
                Contact contact = ContactManager.instance().getContactByNumber(number);
                if (contact == null) {
                    Bitmap bm = BitmapFactory.decodeResource(AppManager.Instance().getResources(), R.drawable.mango_default);
                    contact = ContactManager.instance().newContact(number, number, "unknown", bm);
                }
                String strMessage = "";
                strMessage += "SMS from " + contact.getName();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
}
