package namnh.com.simplereadsms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast

class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null || intent.extras == null) {
            return
        }
        val smsMessage: SmsMessage?
        smsMessage = if (Build.VERSION.SDK_INT >= 19) { //KITKAT
            val msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            msgs[0]
        } else {
            val pdus = intent.extras!!.get("pdus") as Array<*>
            SmsMessage.createFromPdu(pdus[0] as ByteArray)
        }
        if (smsMessage == null) return
        var smsBody = smsMessage.messageBody
        //strip flag
        while (smsBody.contains("FLAG")) {
            smsBody = smsBody.replace("FLAG", "")
        }
        Toast.makeText(context, smsBody, Toast.LENGTH_LONG).show()
        Log.i(TAG, "Received message: " + smsBody)
    }

    companion object {
        private val TAG = SMSReceiver::class.java.simpleName
    }
}
