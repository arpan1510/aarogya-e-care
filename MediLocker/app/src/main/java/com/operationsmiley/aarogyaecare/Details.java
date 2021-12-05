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
import com.hbb20.CountryCodePicker;
import com.operationsmiley.aarogyaecare.module.Users;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Details extends AppCompatActivity {

    private EditText birthdate,fn,ln,ad;
    private TextView submit;
    private Users userdetails;
    private String country;
    final Calendar myCalendar = Calendar.getInstance();
    Spinner gender,bloodgroup;
    private Toolbar toolbar;
    private DatabaseReference detailref;
    private CountryCodePicker coun;
    private ProgressDialog loadingbar;
    String currentuser;
    GoogleSignInClient mGoogleSignInClient;
    TextView header;
    Typeface tfavv;
    DatePickerDialog datePickerDialog;
    ImageView backBtn;
//    Button check;

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();
        Intent intent=new Intent(Details.this,Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar = findViewById(R.id.save_details_toolbar);
        setSupportActionBar(toolbar);
        header = findViewById(R.id.simple_toolbar_header);
        header.setText("Add Details");
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);
        gender = findViewById(R.id.spinner1);
        userdetails=new Users();
        submit=findViewById(R.id.submitdetails_btn);
        coun=(CountryCodePicker)findViewById(R.id.ccpcountry);
        bloodgroup = findViewById(R.id.spinner2);
        fn=findViewById(R.id.first_name_details);
        ln=findViewById(R.id.last_name_details);
        ad=findViewById(R.id.address_details);
        loadingbar= new ProgressDialog(this);
        loadingbar.setTitle("Account Settings");
        loadingbar.setMessage("Saving your Details...");
        loadingbar.setCanceledOnTouchOutside(false);
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        detailref= FirebaseDatabase.getInstance().getReference().child("Users").child(currentuser).child("Self");
//      check = findViewById(R.id.checkBtn_toolbar);
//      check.setVisibility(View.VISIBLE);
        //create a list of items for the spinner.
            String[] items = new String[]{"Select Gender","Male", "Female", "Other"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items){
                public View getView(int position, View convertView, android.view.ViewGroup parent) {
                    tfavv = ResourcesCompat.getFont(Details.this,R.font.roboto_black);
                    TextView v = (TextView) super.getView(position, convertView, parent);
                    v.setTypeface(tfavv);
                    if(position==0)
                    {
                        v.setTextColor(Color.parseColor("#888888"));
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
                        v.setTextColor(Color.parseColor("#888888"));
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
            String[] items1 = new String[]{"Select Blood Group","A+","A-","B+","B-","AB+","AB-","O+","O-","Other"};
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1){
                public View getView(int position, View convertView, android.view.ViewGroup parent) {
                    tfavv = ResourcesCompat.getFont(Details.this,R.font.roboto_black);
                    TextView v = (TextView) super.getView(position, convertView, parent);
                    v.setTypeface(tfavv);
                    if(position==0)
                    {
                        v.setTextColor(Color.parseColor("#888888"));
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
                        v.setTextColor(Color.parseColor("#888888"));
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

        birthdate = findViewById(R.id.birthdate_details);
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
                datePickerDialog = new DatePickerDialog(Details.this, date, myCalendar
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
                    fn.setError("Please enter your First Name");
                }
                else if(ln.getText().toString().trim().length()==0)
                {
                    ln.setError("Please enter your Last Name");
                }
                else if(birthdate.getText().toString().trim().length()==0)
                {
                    Toast.makeText(Details.this, "Please Enter Your Date of Birth", Toast.LENGTH_SHORT).show();
                }
                else if(gender.getSelectedItem().toString().equals("Select Gender"))
                {
                    Toast.makeText(Details.this, "Please Select Your Gender", Toast.LENGTH_SHORT).show();
                }
                else if(bloodgroup.getSelectedItem().toString().equals("Select Blood Group"))
                {
                    Toast.makeText(Details.this, "Please Select Your Blood Group", Toast.LENGTH_SHORT).show();
                }
                else if(ad.getText().toString().trim().length()==0)
                {
                    ad.setError("Please enter your City");
                }
                else
                {
                    loadingbar.show();
                    detailref.child("blgrp").setValue(bloodgroup.getSelectedItem().toString());
                    detailref.child("dob").setValue(birthdate.getText().toString());
                    detailref.child("fname").setValue(fn.getText().toString().toUpperCase());
                    detailref.child("lname").setValue(ln.getText().toString().toUpperCase());
                    detailref.child("gender").setValue(gender.getSelectedItem().toString());
                    detailref.child("country").setValue(coun.getSelectedCountryName());
                    detailref.child("city").setValue(ad.getText().toString().toUpperCase());
                    detailref.child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                    detailref.child("topup").setValue("0");
                    detailref.child("usertype").setValue("FREE");
                    loadingbar.dismiss();
                    Toast.makeText(Details.this, "Details Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Details.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
