package namnh.com.simplereadsms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_SMS_PERMISSION = 1000;
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView textSmsMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textSmsMessage = findViewById(R.id.text_sms_message);
        findViewById(R.id.btn_read_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readAllSMS();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasSMSPermission()) {
            requestSMSPermission();
        }
    }

    private void readAllSMS() {
        Cursor cursor =
                getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null,
                        null);

        if (cursor == null) return;
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            StringBuilder msgData = new StringBuilder();
            do {
                msgData.append("")
                        .append(cursor.getColumnName(2))
                        .append(":")
                        .append(cursor.getString(2))
                        .append("\n")
                        .append(cursor.getColumnName(12))
                        .append(":")
                        .append(cursor.getString(12))
                        .append("\n");
                // use msgData
            } while (cursor.moveToNext());
            textSmsMessage.setText(msgData.toString());
        } else {
            Log.i(TAG, "No SMS in data");
        }
        cursor.close();
    }

    private void requestSMSPermission() {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length <= 0) return;
        if (requestCode == REQUEST_SMS_PERMISSION) {
            for (int granted : grantResults) {
                if (granted != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions is not Granted !", Toast.LENGTH_SHORT).show();
                }
            }

            Toast.makeText(this, "Permissions Granted !", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasSMSPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }
}
