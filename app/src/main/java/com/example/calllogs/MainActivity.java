package com.example.calllogs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CALL_LOG = 1;
    private ListView listViewCallLogs;
    private TextView tvCallLogType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewCallLogs = findViewById(R.id.listViewCallLogs);
        tvCallLogType = findViewById(R.id.tvCallLogType);

        // Check permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG},
                    REQUEST_READ_CALL_LOG);
        } else {
            displayCallLogs(null); // Display all call logs initially
        }
    }

    private void displayCallLogs(String filter) {
        String selection = null;
        String[] selectionArgs = null;
        String logType = "All Calls";

        if (filter != null) {
            switch (filter) {
                case "Incoming":
                    selection = CallLog.Calls.TYPE + " = ?";
                    selectionArgs = new String[]{String.valueOf(CallLog.Calls.INCOMING_TYPE)};
                    logType = "Incoming Calls";
                    break;
                case "Outgoing":
                    selection = CallLog.Calls.TYPE + " = ?";
                    selectionArgs = new String[]{String.valueOf(CallLog.Calls.OUTGOING_TYPE)};
                    logType = "Outgoing Calls";
                    break;
                case "Missed":
                    selection = CallLog.Calls.TYPE + " = ?";
                    selectionArgs = new String[]{String.valueOf(CallLog.Calls.MISSED_TYPE)};
                    logType = "Missed Calls";
                    break;
                case "Rejected":
                    selection = CallLog.Calls.TYPE + " = ?";
                    selectionArgs = new String[]{String.valueOf(CallLog.Calls.REJECTED_TYPE)};
                    logType = "Outgoing(Not connected) Calls";
                    break;
                default:
                    // Handle other cases or unknown types
                    break;
            }
        }

        tvCallLogType.setText(logType);

        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                CallLog.Calls.DATE + " DESC");

        // Set up adapter with the filtered cursor
        CallLogHelper adapter = new CallLogHelper(this, cursor);
        listViewCallLogs.setAdapter(adapter);
    }

    public void onFilterButtonClick(View view) {
        // Handle filter button click
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.filter_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_all) {
                displayCallLogs(null); // Display all call logs
                return true;
            } else if (itemId == R.id.menu_incoming) {
                displayCallLogs("Incoming");
                return true;
            } else if (itemId == R.id.menu_outgoing) {
                displayCallLogs("Outgoing");
                return true;
            } else if (itemId == R.id.menu_missed) {
                displayCallLogs("Missed");
                return true;
            } else if (itemId == R.id.menu_rejected) {
                displayCallLogs("Outgoing(Not connected)");
                return true;
            }
            return false;
        });
        popup.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_CALL_LOG) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayCallLogs(null); // Display all call logs after permission granted
            } else {
                Toast.makeText(this, "Permission denied to read call logs", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
