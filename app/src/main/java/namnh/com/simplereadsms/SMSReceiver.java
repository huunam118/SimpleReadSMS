package namnh.com.simplereadsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = SMSReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null) {
            return;
        }
        SmsMessage smsMessage;
        if (Build.VERSION.SDK_INT >= 19) { //KITKAT
            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            smsMessage = msgs[0];
        } else {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            if (pdus == null) return;
            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
        }
        if (smsMessage == null) return;
        String smsBody = smsMessage.getMessageBody();
        //strip flag
        while (smsBody.contains("FLAG")) {
            smsBody = smsBody.replace("FLAG", "");
        }
        Toast.makeText(context, smsBody, Toast.LENGTH_LONG).show();
        Log.i(TAG, "Received message: " + smsBody);
    }
}
