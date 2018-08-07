package namnh.com.simplereadsms

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var textSmsMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSmsMessage = findViewById(R.id.text_sms_message)
        findViewById<View>(R.id.btn_read_sms).setOnClickListener { readAllSMS() }
    }

    override fun onResume() {
        super.onResume()
        if (!hasSMSPermission()) {
            requestSMSPermission()
        }
    }

    private fun readAllSMS() {
        val cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null,
            null) ?: return

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            val msgData = StringBuilder()
            do {
                msgData.append("")
                    .append(cursor.getColumnName(2))
                    .append(":")
                    .append(cursor.getString(2))
                    .append("\n")
                    .append(cursor.getColumnName(12))
                    .append(":")
                    .append(cursor.getString(12))
                    .append("\n")
                // use msgData
            } while (cursor.moveToNext())
            textSmsMessage.text = msgData.toString()
        } else {
            Log.i(TAG, "No SMS in data")
        }

        cursor.close()
    }

    private fun requestSMSPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS),
            REQUEST_SMS_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty()) return
        if (requestCode == REQUEST_SMS_PERMISSION) {
            grantResults
                .filter { it != PackageManager.PERMISSION_GRANTED }
                .forEach {
                    Toast.makeText(this, "Permissions is not Granted !", Toast.LENGTH_SHORT).show()
                }

            Toast.makeText(this, "Permissions Granted !", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasSMSPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        val REQUEST_SMS_PERMISSION = 1000
        private val TAG = MainActivity::class.java.simpleName
    }
}
