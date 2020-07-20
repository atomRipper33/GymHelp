package com.example.android.gymhelp;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.util.Log;
        import android.widget.Toast;

        import java.util.Calendar;
        import java.util.Date;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        DatabaseHelper mDatabaseHelper=new DatabaseHelper(context);
        Date d = new Date(new Date().getTime() + 86400000);
        final String tomorrowsDate = MainActivity.sdf.format(d);
        final String emails = mDatabaseHelper.getEmailsByDate(tomorrowsDate);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today=calendar.getTime();
        SharedPreferences myPrefs = context.getSharedPreferences("dateOfMail",0);
        try {
            Date lastDate=MainActivity.sdf.parse(myPrefs.getString("dateLastSent","1/1/2000"));
            Log.i("test2","i: "+ (today)+" "+lastDate);
            if(today.compareTo(lastDate)==0) {
                Toast.makeText(context, "Danas je mail već bio poslan.", Toast.LENGTH_SHORT).show();
            }else {
                MainActivity.mailSetup(emails, tomorrowsDate, context);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
