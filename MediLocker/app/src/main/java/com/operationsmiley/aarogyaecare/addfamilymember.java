package com.operationsmiley.aarogyaecare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.operationsmiley.aarogyaecare.module.Users;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class addfamilymember extends AppCompatActivity {

    private EditText birthdate,fn,ln,relation;
    private TextView submit;
    ImageView backBtn;
    private Users userdetails;
    private String country;
    final Calendar myCalendar = Calendar.getInstance();
    Spinner gender,bloodgroup;
    DatePickerDialog datePickerDialog;
    private DatabaseReference detailref;
    Toolbar toolbar;
    private ProgressDialog loadingbar;
    String currentuser;
    GoogleSignInClient mGoogleSignInClient;
    TextView headerr;
    Typeface tfavv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfamilymember);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);
        gender = findViewById(R.id.spinner1_add);
        userdetails=new Users();
        headerr=findViewById(R.id.simple_toolbar_header);
        headerr.setText("Add Family Member");
        toolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        submit=findViewById(R.id.submit_add_btn);
        relation=findViewById(R.id.realation_add);
        bloodgroup = findViewById(R.id.spinner2_add);
        fn=findViewById(R.id.first_name_add);
        ln=findViewById(R.id.last_name_add);

        loadingbar= new ProgressDialog(this);
        loadingbar.setTitle("Adding Family Member");
        loadingbar.setMessage("Please wait..");
        loadingbar.setCanceledOnTouchOutside(false);
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        detailref= FirebaseDatabase.getInstance().getReference().child("Users").child(currentuser);
//      check = findViewById(R.id.checkBtn_toolbar);

//        check.setVisibility(View.VISIBLE);
        //create a list of items for the spinner.
        String[] items = new String[]{"Select Gender","Male", "Female", "Other"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items){
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                tfavv = ResourcesCompat.getFont(addfamilymember.this,R.font.roboto_black);
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTypeface(tfavv);
                if(position==0)
                {
                    v.setTextColor(Color.parseColor("#808080"));
                }
                else
                {
                    v.setTextColor(Color.BLACK);
                }
                v.setTextSize(14);
                return v;
            }

            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTypeface(tfavv);
                if(position==0)
                {
                    v.setTextColor(Color.parseColor("#808080"));
                }
                else
                {
                    v.setTextColor(Color.BLACK);
                }
                v.setTextSize(14);
                return v;
            }
        };
        //set the spinners adapter to the previously created one.
        String[] items1 = new String[]{"Select Blood group","A+","A-","B+","B-","AB+","AB-","O+","O-","Other"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1){
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                tfavv = ResourcesCompat.getFont(addfamilymember.this,R.font.roboto_black);
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTypeface(tfavv);
                if(position==0)
                {
                    v.setTextColor(Color.parseColor("#808080"));
                }
                else
                {
                    v.setTextColor(Color.BLACK);
                }
                v.setTextSize(14);
                return v;
            }

            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setTypeface(tfavv);
                if(position==0)
                {
                    v.setTextColor(Color.parseColor("#808080"));
                }
                else
                {
                    v.setTextColor(Color.BLACK);
                }
                v.setTextSize(14);
                return v;
            }
        };
        gender.setAdapter(adapter);
        bloodgroup.setAdapter(adapter1);

        birthdate = findViewById(R.id.birthdate_add);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                birthdate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        birthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(addfamilymember.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fn.getText().toString().trim().length()==0)
                {
                    fn.setError("Please Enter First Name");
                }
                else if(ln.getText().toString().trim().length()==0)
                {
                    ln.setError("Please Enter Last Name");
                }
                else if(birthdate.getText().toString().trim().length()==0)
                {
                    Toast.makeText(addfamilymember.this, "Please Enter Date of Birth", Toast.LENGTH_SHORT).show();
                }
                else if(gender.getSelectedItem().toString().equals("Select Gender"))
                {
                    Toast.makeText(addfamilymember.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }
                else if(bloodgroup.getSelectedItem().toString().equals("Select Blood group"))
                {
                    Toast.makeText(addfamilymember.this, "Please Select Blood Group", Toast.LENGTH_SHORT).show();
                }
                else if(relation.getText().toString().trim().length()==0)
                {
                    relation.setError("Please Enter Your Relation");
                }
                else
                {
                    loadingbar.show();
                    detailref.child(fn.getText().toString().toUpperCase()).child("blgrp").setValue(bloodgroup.getSelectedItem().toString());
                    detailref.child(fn.getText().toString().toUpperCase()).child("dob").setValue(birthdate.getText().toString());
                    detailref.child(fn.getText().toString().toUpperCase()).child("fname").setValue(fn.getText().toString().toUpperCase());
                    detailref.child(fn.getText().toString().toUpperCase()).child("lname").setValue(ln.getText().toString().toUpperCase());
                    detailref.child(fn.getText().toString().toUpperCase()).child("gender").setValue(gender.getSelectedItem().toString());
                    detailref.child(fn.getText().toString().toUpperCase()).child("relation").setValue(relation.getText().toString().toUpperCase());

                    loadingbar.dismiss();
                    Toast.makeText(addfamilymember.this, "Family Member Added Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(addfamilymember.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
