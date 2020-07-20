package com.example.android.gymhelp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
    public Calendar c;
    public DatePickerDialog dpd;
    public int day;
    public int monthh;
    public int yearr;
    public List<Event> events;
    public String dateToBase;
    public String dateToBase2;
    public DatabaseHelper mDatabaseHelper;
    public Date dateForEvents;
    public Long miliForEvents;
    public DatabaseHelperStat myDatabaseForStat;
    private Toolbar toolbar;
    private CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    public static boolean isValidEmail(CharSequence email) {
        if (!email.equals("")) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        toolbar = findViewById(R.id.tool_bar);
        //textViewNo = findViewById(R.id.textViewNo);
        mDatabaseHelper = new DatabaseHelper(this);
        myDatabaseForStat= new DatabaseHelperStat(this);
        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(false);
        toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        setSupportActionBar(toolbar);
        getAllEvents();
        checkIfAlarmUp();
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabMode);
        tabLayout.setupWithViewPager(viewPager);

        try {
            checkIfMailWasSent();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                toolbar.setTitle(dateFormatForMonth.format(dateClicked));
                List<Event> bookingsFromMap = compactCalendarView.getEvents(dateClicked);
                Fragment1.fillRecyclerView(dateClicked,bookingsFromMap);
                }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                toolbar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void allAccountsButton(View view){
        Runnable r = new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 350); // will be delayed for 1.5 seconds
    }

    private void checkIfAlarmUp(){
        boolean alarmUp = (PendingIntent.getBroadcast(MainActivity.this, 0,
                new Intent(MainActivity.this, MyReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmUp){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent emailIntent = new Intent(MainActivity.this,MyReceiver.class);
            PendingIntent emailAlarm = PendingIntent.getBroadcast(MainActivity.this,0,emailIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,emailAlarm);
        }
    }

    private void checkIfMailWasSent() throws ParseException{        //starts sending mail if it was not sent that day
        boolean mailWasSe = mailWasSentToday();
        if (!mailWasSe){
            sendMail();
        }
    }
    private void sendMail(){
        Date d = new Date(new Date().getTime() + 86400000);
        final String tomorrowsDate = sdf.format(d);
        final String emails = mDatabaseHelper.getEmailsByDate(tomorrowsDate);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to sent the email ?");
        builder.setMessage("Mail was not sent today, do you want to send it ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mailSetup(emails,tomorrowsDate,MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("No",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void mailSetup(final String emails, final String tomorrowsDate, final Context context){            //sends mail
        final Date d = Calendar.getInstance().getTime();
        final SharedPreferences mailSent = context.getSharedPreferences("dateOfMail",0);
        if (TextUtils.isEmpty(emails)) {
            SharedPreferences.Editor editor = mailSent.edit();     //if string is empty mark email as it was sent that day
            editor.putString("dateLastSent",sdf.format(d));
            editor.apply();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BackgroundMail.newBuilder(context)
                            .withUsername("some@gmail.com")
                            .withPassword("somepass")
                            .withSenderName("Fitness center Jox")
                            .withMailBcc(emails)
                            .withType(BackgroundMail.TYPE_PLAIN)
                            .withSubject("Membership ending")
                            .withBody("Dear,\nyour memership is ending " + tomorrowsDate + " please renew it" +
                                    " if you want to use services from Fitness center 'Jox'." +
                                    "\nYour Jox\n\n\nThis is automated message, please do not reply.")
                            //.withSendingMessage(R.string.sending_email)
                            .withOnSuccessCallback(new BackgroundMail.OnSendingCallback() {
                                @Override
                                public void onSuccess() {
                                    SharedPreferences.Editor editor = mailSent.edit();
                                    editor.putString("dateLastSent",sdf.format(d));
                                    editor.apply();
                                    Toast.makeText(context, "Email was sent successfully", Toast.LENGTH_LONG).show();
                                    Log.i("tag", "succes");

                                }

                                @Override
                                public void onFail(Exception e) {
                                    Toast.makeText(context, "Email was not sent, check your internet connection", Toast.LENGTH_SHORT).show();
                                    Log.i("fail", "failed");
                                }
                            })
                            .send();
                }
            }).start();
        }
    }

    final public boolean  mailWasSentToday() throws ParseException{           //checks if mail was sent today

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today=calendar.getTime();
        SharedPreferences myPrefs = getSharedPreferences("dateOfMail",0);
        Date lastDate=sdf.parse(myPrefs.getString("dateLastSent","1/1/2000"));

        Log.i("test2","i: "+ (today)+" "+lastDate);
        return today.compareTo(lastDate)==0;
    }
    public String addPrefix(Integer number){
        return  "0"+number;
    }
    public void alertAdd(View view) {
        Runnable r = new Runnable() {

            @Override
            public void run() {

                c=Calendar.getInstance();
                final AlertDialog dialog;
                day = c.get(Calendar.DAY_OF_MONTH);
                monthh = c.get(Calendar.MONTH);
                yearr = c.get(Calendar.YEAR);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.useradd_dialog,null);
                final EditText nameE = mView.findViewById(R.id.editText2);
                final EditText priceE = mView.findViewById(R.id.editText3);
                final EditText dateE = mView.findViewById(R.id.editText4);
                final EditText emailE = mView.findViewById(R.id.editText6);
                Button addB = mView.findViewById(R.id.button);
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
                dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateToBase =(dayOfMonth+"/"+(month+1)+"/"+year);
                        String monthWithPrefix=Integer.toString(monthh+1);
                        String dayWithPrefix= Integer.toString(day);
                        if (monthh + 1 < 10) {
                            monthWithPrefix= addPrefix(monthh+1);
                        }
                        if(day<10){
                            dayWithPrefix= addPrefix(day);
                        }
                        dateToBase2 =(yearr+"-"+monthWithPrefix+"-"+dayWithPrefix);
                        dateE.setText(dateToBase);
                    }
                },yearr,monthh+1,day);

                dateE.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dpd.show();
                    }
                });
                addB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!nameE.getText().toString().isEmpty() && !priceE.getText().toString().isEmpty()
                                && !dateE.getText().toString().isEmpty() && isValidEmail(emailE.getText().toString())){
                            if (mDatabaseHelper.CheckIsDataAlreadyInDBorNot(nameE.getText().toString())){
                                Toast.makeText(MainActivity.this,"Member is already in database",Toast.LENGTH_SHORT).show();
                            }else {
                                myDatabaseForStat.addPriceToDb(dateToBase2,Integer.parseInt(priceE.getText().toString()));
                                Toast.makeText(MainActivity.this,"Added",Toast.LENGTH_SHORT).show();
                                mDatabaseHelper.addData(nameE.getText().toString(),Integer.parseInt(priceE.getText().toString()),dateToBase,emailE.getText().toString());
                            }

                            nameE.setText("");
                            priceE.setText("");
                            dateE.setText("");
                            emailE.setText("");
                            dialog.show();
                            nameE.setShowSoftInputOnFocus(true);
                        }else {
                            Toast.makeText(MainActivity.this,"Please fill any empty field or check if your email is valid",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 270); // will be delayed for 1.5 seconds
    }

    public void alertDelete(View view){
        Intent intent = new Intent(MainActivity.this,StatisticsActivity.class);
        startActivity(intent);
    }

    public void getAllEvents(){
        Cursor data = mDatabaseHelper.getData();
        events = new ArrayList<>();
        while (data.moveToNext()){
            try {
                dateForEvents = sdf.parse(data.getString(2));
            }catch (Exception e){
                Log.i("Error","Can not get events");
            }
            miliForEvents = dateForEvents.getTime();
            events.add(new Event(Color.RED,miliForEvents));
        }
        compactCalendarView.addEvents(events);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            Intent intentForSet = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intentForSet);
        }

        return super.onOptionsItemSelected(item);
    }
}
