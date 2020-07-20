package com.example.android.gymhelp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;

import lib.folderpicker.FolderPicker;

public class SettingsActivity extends AppCompatActivity {

    Switch aSwitch;
    SharedPreferences stateOfSwitch;
    Button exportBut;
    Button importBut;
    int check=0;
    int FOLDERPICKER_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //aSwitch = findViewById(R.id.switch1);
        exportBut = findViewById(R.id.button3);
        importBut = findViewById(R.id.button5);
//        stateOfSwitch = getSharedPreferences("state",0);
//        boolean state = stateOfSwitch.getBoolean("switch",false);
//        aSwitch.setChecked(state);
//        aSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (aSwitch.isChecked()){Calendar calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(System.currentTimeMillis());
//                    calendar.set(Calendar.HOUR_OF_DAY, 9);
//                    calendar.set(Calendar.MINUTE, 30);
//                    calendar.set(Calendar.SECOND, 0);
//                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                    Intent emailIntent = new Intent(SettingsActivity.this,MyReceiver.class);
//                    PendingIntent emailAlarm = PendingIntent.getBroadcast(SettingsActivity.this,0,emailIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//                    alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,emailAlarm);
//                }else {
//                    if (!aSwitch.isChecked()) {
//                        boolean alarmUp = (PendingIntent.getBroadcast(SettingsActivity.this, 0,
//                                new Intent(SettingsActivity.this, MyReceiver.class),
//                                PendingIntent.FLAG_NO_CREATE) != null);
//
//                        if (alarmUp) {
//                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                            Intent myIntent = new Intent(SettingsActivity.this, MyReceiver.class);
//                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                                    SettingsActivity.this, 0, myIntent,
//                                    PendingIntent.FLAG_UPDATE_CURRENT);
//                            alarmManager.cancel(pendingIntent);
//                        }
//                    }
//                }
//                SharedPreferences.Editor editor = stateOfSwitch.edit();
//                editor.putBoolean("switch",aSwitch.isChecked());
//                editor.commit();
//            }
//        });
    }
    public void helpForExport(View view){   //members backup
        check=0;
        exportFun();
    }
    public void helpForExport2(View view){  //statistics backup
        check=1;
        exportFun();
    }

    public void exportFun(){
        Log.i("was","i was here");
        File direct = new File(Environment.getExternalStorageDirectory() + "/MembersBackUp");
        if(!direct.exists())
        {
            Log.i("was","i was here2");
            if(direct.mkdirs())
            {
                //directory is created;
            }

        }
        try {
            Log.i("was","i was here3");
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            String  currentDBPath;
            String backupDBPath;
            if(check==0){
                currentDBPath= "//data//" + "com.example.android.gymhelp"
                        + "//databases//" + "MembersGym";
                backupDBPath  = "/MembersBackUp/MembersGym";
            }else {
                currentDBPath = "//data//" + "com.example.android.gymhelp"
                        + "//databases//" + "StatisticsTableGym";
                backupDBPath = "/MembersBackUp/StatisticsTableGym";
            }
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);
            Log.i("was","i was here4");
            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
            Toast.makeText(getBaseContext(), backupDB.toString(),
                    Toast.LENGTH_LONG).show();


        } catch (Exception e) {

            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                    .show();

        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FOLDERPICKER_CODE && resultCode == Activity.RESULT_OK) {
            String folderLocation = intent.getExtras().getString("data");
            Log.i( "folderLocation", folderLocation );
            try {
                File sd = Environment.getExternalStorageDirectory();
                File data  = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String  currentDBPath;
                    //String backupDBPath  = "/BackupFolder/DatabaseName";
                    if(check==0){
                        currentDBPath= "//data//" + "com.example.android.gymhelp"
                                + "//databases//" + "MembersGym";
                    }else{
                        currentDBPath= "//data//" + "com.example.android.gymhelp"
                                + "//databases//" + "StatisticsTableGym";
                    }
                    File  backupDB= new File(data, currentDBPath);
                    File currentDB  = new File(folderLocation);
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(getBaseContext(), backupDB.toString(),
                            Toast.LENGTH_LONG).show();

                }
            } catch (Exception e) {

                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
                        .show();

            }

        }
    }



    //opens file explorer
    public void importFun(View view){ //members backup
        check=0;
        Intent intent = new Intent(this, FolderPicker.class);
        intent.putExtra("pickFiles", true);
        startActivityForResult(intent, FOLDERPICKER_CODE);
    }
    public void importFun2(View view){  //statistics backup
        check=1;
        Intent intent = new Intent(this, FolderPicker.class);
        intent.putExtra("pickFiles", true);
        startActivityForResult(intent, FOLDERPICKER_CODE);
    }



}
