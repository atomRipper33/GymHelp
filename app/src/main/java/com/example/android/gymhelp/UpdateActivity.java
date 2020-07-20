package com.example.android.gymhelp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    EditText nameEditText;
    EditText dateEditText;
    EditText priceEditText;
    EditText emailEditText;
    DatabaseHelper myDatabaseHelper;
    String memberName;
    String memberDate;
    Integer memberPrice;
    String memberEmail;
    Button button2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        myDatabaseHelper = new DatabaseHelper(this);
        button2 = findViewById(R.id.button2);
        nameEditText = findViewById(R.id.editText);
        dateEditText = findViewById(R.id.editText5);
        priceEditText = findViewById(R.id.editText7);
        emailEditText = findViewById(R.id.editText8);
        getIncomingIntent();
    }

    private void getIncomingIntent(){
        if (getIntent().hasExtra("Name") && getIntent().hasExtra("Date")){
             memberName = getIntent().getStringExtra("Name");
             memberDate = getIntent().getStringExtra("Date");
             memberPrice = getIntent().getIntExtra("Price",0);
             memberEmail = getIntent().getStringExtra("Email");
            setActivity(memberName,memberDate,memberPrice,memberEmail);
        }
    }

    private void setActivity(String name, String date, Integer price,String email){
        nameEditText.setText(name);
        dateEditText.setText(date);
        priceEditText.setText(Integer.toString(price));
        emailEditText.setText(email);

    }

    public void UpdateMember(String newName,String newDate,Integer newPrice, String newEmail){
        if (!newName.equals(memberName) || !newDate.equals(memberDate) || !newEmail.equals(memberEmail) ||!newPrice.equals(memberPrice)){
            myDatabaseHelper.updateMember(newName,memberName,newDate,newPrice,newEmail);
            Toast.makeText(UpdateActivity.this,"Updated",Toast.LENGTH_SHORT).show();
        }

    }

    public void saveOnClick(View view){
        if (!nameEditText.getText().toString().isEmpty() && !dateEditText.getText().toString().isEmpty() && !priceEditText.getText().toString().isEmpty()){
            Toast.makeText(UpdateActivity.this,"Saved",Toast.LENGTH_SHORT).show();
            UpdateMember(nameEditText.getText().toString(),dateEditText.getText().toString(),Integer.parseInt(priceEditText.getText().toString()),
                    emailEditText.getText().toString());

        }else {
            Toast.makeText(UpdateActivity.this,"Please fill any empty field",Toast.LENGTH_SHORT).show();
        }

    }
}
