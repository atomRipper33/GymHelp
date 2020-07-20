package com.example.android.gymhelp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private Context mCtx;
    private List<Member> memberList;
    private Dialog myDialog;
    private DatePickerDialog dpd;
    private String dateToBase2;
    private String dateToBase3;
    private Calendar calWithMonthAdded;
    private int day;
    private int monthh;
    private int yearr;
    private DatabaseHelper myDatabaseHelper;
    private DatabaseHelperStat statDatabaseHelper;
    private Calendar c;
    private int todayMonth;
    private int todayYear;
    private int todayDay;

    public MemberAdapter(Context mCtx, List<Member> memberList) {
        this.mCtx = mCtx;
        this.memberList = memberList;
    }

    //Parsing string date to Date and adding 1 month to it
    public Calendar addMonths(String dateAsString, int nbMonths) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        Date dateAsObj = sdf.parse(dateAsString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        cal.add(Calendar.MONTH, nbMonths);
        //Date dateAsObjAfterAMonth = cal.getTime() ;
        //System.out.println(sdf.format(dateAsObjAfterAMonth));
        //return dateAsObjAfterAMonth ;
        return cal;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.flist_layout, null);
        MemberViewHolder holder = new MemberViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder memberViewHolder, int i) {

        final Member member = memberList.get(i);
        memberViewHolder.textViewTitle.setText(member.getName());
        memberViewHolder.textViewDate.setText(member.geteDate());

    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    //Filtering searched characters
    public void setFilter(ArrayList<Member> newList) {
        memberList = new ArrayList<>();
        memberList.addAll(newList);
        notifyDataSetChanged();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewDate;
        TextView textViewExpires;
        ImageView imageViewDelete;
        public ConstraintLayout constraintLayout;


        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewExpires = itemView.findViewById(R.id.textViewExpires);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            myDialog = new Dialog(mCtx);
            myDialog.setContentView(R.layout.useradd_dialog);
            myDatabaseHelper = new DatabaseHelper(mCtx);
            statDatabaseHelper = new DatabaseHelperStat(mCtx);
            c = Calendar.getInstance();
            todayMonth = c.get(Calendar.MONTH);
            todayYear = c.get(Calendar.YEAR);
            todayDay=c.get(Calendar.DAY_OF_MONTH);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Member member = memberList.get(getAdapterPosition());
                    final TextView textView2 = myDialog.findViewById(R.id.textViewEmail);
                    final EditText nameE = myDialog.findViewById(R.id.editText2);
                    final EditText priceE = myDialog.findViewById(R.id.editText3);
                    final EditText dateE = myDialog.findViewById(R.id.editText4);
                    final EditText emailE = myDialog.findViewById(R.id.editText6);
                    Button addB = myDialog.findViewById(R.id.button);
                    myDialog.show();
                    textView2.setVisibility(View.GONE);
                    emailE.setVisibility(View.GONE);
                    nameE.setText(member.getName());
                    nameE.setKeyListener(null);
                    priceE.setText(Integer.toString(member.getPrice()));
                    dateE.setText(member.geteDate());
                    try {
                        calWithMonthAdded = addMonths(member.geteDate(), 1);
                    } catch (Exception e) {
                        Log.i("Error", e.toString());
                    }
                    day = calWithMonthAdded.get(Calendar.DAY_OF_MONTH);
                    monthh = calWithMonthAdded.get(Calendar.MONTH);
                    yearr = calWithMonthAdded.get(Calendar.YEAR);
                    dpd = new DatePickerDialog(mCtx, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            dateToBase2 = (dayOfMonth + "/" + Integer.toString(month + 1) + "/" + year);
                            String monthWithPrefix=Integer.toString(todayMonth+1);
                            String dayWithPrefix= Integer.toString(todayDay);
                            if (todayMonth + 1 < 10) {
                                monthWithPrefix= addPrefix(todayMonth+1);
                            }
                            if(todayDay<10){
                                dayWithPrefix= addPrefix(todayDay);
                            }
                            dateToBase3=(year + "-" +(monthWithPrefix) + "-" + dayWithPrefix);
                            dateE.setText(dateToBase2);
                        }
                    }, yearr, monthh, day);
                    dateE.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dpd.show();
                        }
                    });
                    addB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!priceE.getText().toString().isEmpty() && !dateE.getText().toString().isEmpty()) {
                                Toast.makeText(mCtx, "Renewed", Toast.LENGTH_SHORT).show();
                                myDatabaseHelper.renewMembership(member.getName(), Integer.parseInt(priceE.getText().toString()), dateToBase2);
                                statDatabaseHelper.addPriceToDb(dateToBase3,Integer.parseInt(priceE.getText().toString()));
                                myDialog.dismiss();
                            } else {
                                Toast.makeText(mCtx, "Please fill any empty field", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final Member member = memberList.get(getAdapterPosition());
                    Intent intent = new Intent(mCtx, UpdateActivity.class);
                intent.putExtra("Name", member.getName());
                intent.putExtra("Date", member.geteDate());
                intent.putExtra("Price", member.getPrice());
                intent.putExtra("Email", member.getEmail());
                mCtx.startActivity(intent);
                return true;
                }
            });
            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Member member = memberList.get(getAdapterPosition());
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mCtx);
                builder1.setMessage("Are you sure you want to delete this");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                myDatabaseHelper.deletebyName(member.getName());
                                dialog.cancel();
                                notifyDataSetChanged();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                }
            });
        }
        public String addPrefix(Integer number){
            return  "0"+number;
        }
    }

}
