package com.example.calllogs;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallLogHelper extends CursorAdapter {

    public CallLogHelper(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Retrieve views
        TextView text1 = view.findViewById(android.R.id.text1);
        TextView text2 = view.findViewById(android.R.id.text2);

        // Retrieve column indices
        int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);
        int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
        int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);

        // Check if indices are valid
        if (numberIndex == -1 || durationIndex == -1 || dateIndex == -1 || typeIndex == -1) {
            // Handle case where columns are not found
            text1.setText("Call details unavailable");
            text2.setText("");
            return;
        }

        // Retrieve data from cursor
        String number = cursor.getString(numberIndex);
        String duration = cursor.getString(durationIndex);
        long timestamp = cursor.getLong(dateIndex);
        int type = cursor.getInt(typeIndex);

        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String date = dateFormat.format(new Date(timestamp));

        // Determine call type
        String callType = "";
        switch (type) {
            case CallLog.Calls.INCOMING_TYPE:
                callType = "Incoming";
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                callType = "Outgoing";
                break;
            case CallLog.Calls.MISSED_TYPE:
                callType = "Missed";
                break;
            case CallLog.Calls.REJECTED_TYPE:
                callType = "Outgoing (Not Connected)";
                break;
            default:
                callType = "Unknown";
        }

        // Display data in views
        text1.setText("Number: " + number);
        text2.setText("Type: " + callType + "\nDuration: " + duration + " seconds\nDate: " + date);
    }
}
