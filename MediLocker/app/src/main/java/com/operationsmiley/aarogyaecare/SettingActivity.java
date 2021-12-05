package com.operationsmiley.aarogyaecare;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.operationsmiley.aarogyaecare.module.Users;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity{

    TextView header,phone,phone1,email,email1,address1,aecid1,aecid,coun1,coun,address,relationtext,passwordtext;
    ImageView delete,visibilityon,visibilityoff;
    Toolbar toolbar;
    private String country;
    EditText birthdate,relation;
    private FirebaseAuth mAuth;
    final Calendar myCalendar = Calendar.getInstance();
    Spinner gender,bloodgroup,userwho;
    EditText fname,lname,passwordedittext;
    ProgressDialog loadingbar;
    DatePickerDialog datePickerDialog;
    Button edit;
    DatabaseReference recordref;
    TextView save;
    Typeface tfavv;
    GoogleSignInClient mGoogleSignInClient;
    StorageReference storef;
    String UserId;
    DatabaseReference myref,userwhoref,myref1,usageref,databaseReference;
    FirebaseUser currentUser;
    private ProgressDialog loadingbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);
        relation=findViewById(R.id.setting_userrelation);
        relationtext=findViewById(R.id.relationtext);
        coun1=findViewById(R.id.setting_user_country1);
        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Loading");
        passwordtext=findViewById(R.id.passwordtext);
        passwordedittext=findViewById(R.id.setting_password);
//        visibilityoff=findViewById(R.id.settingvisibilityoff);
//        visibilityon=findViewById(R.id.settingvisibilityon);
        loadingbar1.setMessage("Please wait...");
        loadingbar1.setCanceledOnTouchOutside(false);
        loadingbar1.show();
        coun=findViewById(R.id.setting_user_country);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth=FirebaseAuth.getInstance();
        userwho=(Spinner)findViewById(R.id.spinner_selectusertypeforsettings);
        String UserId = currentUser.getUid();
        toolbar = findViewById(R.id.includeToll);
        UserId = currentUser.getUid();
        aecid=findViewById(R.id.setting_user_mlidtextview);
        aecid1=findViewById(R.id.setting_user_mlidtextview1);
        delete=findViewById(R.id.deletebtn_profile);
        gender = findViewById(R.id.spinner1);
        bloodgroup = findViewById(R.id.spinner2);
        fname = findViewById(R.id.setting_user_name);
        lname = findViewById(R.id.setting_user_lastname);
        phone = findViewById(R.id.setting_user_phone_number);
        phone1 = findViewById(R.id.setting_user_phone_number1);
        email1 = findViewById(R.id.setting_user_email1);
        email = findViewById(R.id.setting_user_email);
        gender = findViewById(R.id.spinner1);
        bloodgroup = findViewById(R.id.spinner2);
        userwhoref=FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);
        recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(UserId);
        myref1=FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);
        address = findViewById(R.id.Setting_address_details);
        address1 = findViewById(R.id.Setting_address_details1);
        save = findViewById(R.id.setting_change_save);

        myref = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);
        myref.child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loadingbar1.show();
                Users userDetails = dataSnapshot.getValue(Users.class);
                String gen = userDetails.getGender();
                String bg = userDetails.getBlgrp();
                Typeface typeface = ResourcesCompat.getFont(SettingActivity.this,R.font.roboto_black);
                ArrayAdapter myAdap = (ArrayAdapter) gender.getAdapter(); //cast to an ArrayAdapter
                int spinnerPosition = myAdap.getPosition(gen);
                ArrayAdapter myAdap2 = (ArrayAdapter) bloodgroup.getAdapter(); //cast to an ArrayAdapter
                int spinnerPosition2 = myAdap2.getPosition(bg);
                fname.setText(userDetails.getFname());
                lname.setText(userDetails.getLname());
                phone.setText(userDetails.getPhone());
                email.setText(currentUser.getEmail());
                passwordedittext.setText(userDetails.getMLID2());
                birthdate.setText(userDetails.getDob());
                gender.setSelection(spinnerPosition);
                bloodgroup.setSelection(spinnerPosition2);
                address.setText(userDetails.getCity());
                loadingbar1.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        passwordedittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus)
                {
                    passwordedittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    passwordedittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });
//        passwordedittext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(passwordedittext.getInputType()==InputType.TYPE_TEXT_VARIATION_PASSWORD)
//                {
//                    passwordedittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                }
//                else
//                {
//                    passwordedittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                }
//            }
//        });

//        visibilityon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                visibilityon.setVisibility(View.GONE);
//                visibilityoff.setVisibility(View.VISIBLE);
//                passwordedittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//            }
//        });
//
//        visibilityoff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                visibilityoff.setVisibility(View.GONE);
//                visibilityon.setVisibility(View.VISIBLE);
//
//            }
//        });

        List<String> usernamelist = new ArrayList<>();

        userwhoref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usernamelist.add("Select Family Member");
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    usernamelist.add(key);

                }
//                Toast.makeText(SettingActivity.this, "array :"+ usernamelist.get(1), Toast.LENGTH_SHORT).show();

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_spinner_dropdown_item, usernamelist){
                    public View getView(int position, View convertView, android.view.ViewGroup parent) {
                        tfavv = ResourcesCompat.getFont(SettingActivity.this,R.font.roboto_black);
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
                        v.setTextSize(16);
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
                        v.setTextSize(16);
                        return v;
                    }
                };
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userwho.setAdapter(adapter1);
//                int spinnerPositionSelf = adapter1.getPosition(usernamelist.get(1));
//                userwho.setSelection(spinnerPositionSelf);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });


        userwho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(userwho.getSelectedItem().toString().equals("Self")||userwho.getSelectedItem().toString().equals("Select Family Member"))
                {

                    String UserId = currentUser.getUid();
                    myref = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);
                    myref.child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            loadingbar1.show();
                            relationtext.setVisibility(View.GONE);
                            relation.setVisibility(View.GONE);
                            phone.setVisibility(View.VISIBLE);
                            email.setVisibility(View.VISIBLE);
                            address.setVisibility(View.VISIBLE);
                            address1.setVisibility(View.VISIBLE);
                            delete.setVisibility(View.GONE);
                            phone1.setVisibility(View.VISIBLE);
                            email1.setVisibility(View.VISIBLE);
                            coun1.setVisibility(View.VISIBLE);
                            coun.setVisibility(View.VISIBLE);
                            aecid.setVisibility(View.VISIBLE);
                            aecid1.setVisibility(View.VISIBLE);
                            passwordedittext.setVisibility(View.VISIBLE);
                            passwordtext.setVisibility(View.VISIBLE);
//                            visibilityon.setVisibility(View.VISIBLE);
                            Users userDetails = dataSnapshot.getValue(Users.class);
                            String gen = userDetails.getGender();
                            String bg = userDetails.getBlgrp();
                            Typeface typeface = ResourcesCompat.getFont(SettingActivity.this,R.font.ubuntu_bold);
                            ArrayAdapter myAdap = (ArrayAdapter) gender.getAdapter(); //cast to an ArrayAdapter
                            int spinnerPosition = myAdap.getPosition(gen);
                            ArrayAdapter myAdap2 = (ArrayAdapter) bloodgroup.getAdapter(); //cast to an ArrayAdapter
                            int spinnerPosition2 = myAdap2.getPosition(bg);
                            fname.setText(userDetails.getFname());
                            lname.setText(userDetails.getLname());
                            phone.setText(userDetails.getPhone());
                            aecid.setText(userDetails.getMLID());
                            coun.setText(userDetails.getCountry());

                            email.setText(currentUser.getEmail());
                            birthdate.setText(userDetails.getDob());
                            gender.setSelection(spinnerPosition);
                            bloodgroup.setSelection(spinnerPosition2);
                            address.setText(userDetails.getCity());
                            loadingbar1.dismiss();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {

                    String UserId = currentUser.getUid();
                    myref = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);
                    myref.child(userwho.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            loadingbar1.show();
                            Users userDetails = dataSnapshot.getValue(Users.class);
                            String gen = userDetails.getGender();
                            String bg = userDetails.getBlgrp();
                            Typeface typeface = ResourcesCompat.getFont(SettingActivity.this,R.font.ubuntu_bold);
                            ArrayAdapter myAdap = (ArrayAdapter) gender.getAdapter(); //cast to an ArrayAdapter
                            int spinnerPosition = myAdap.getPosition(gen);
                            ArrayAdapter myAdap2 = (ArrayAdapter) bloodgroup.getAdapter(); //cast to an ArrayAdapter
                            int spinnerPosition2 = myAdap2.getPosition(bg);
                            fname.setText(userDetails.getFname());
                            lname.setText(userDetails.getLname());
                            relation.setText(userDetails.getRelation());
                            relation.setVisibility(View.VISIBLE);
                            relationtext.setVisibility(View.VISIBLE);
                            phone.setVisibility(View.GONE);
                            delete.setVisibility(View.VISIBLE);
                            passwordedittext.setVisibility(View.GONE);
                            passwordtext.setVisibility(View.GONE);
//                            visibilityon.setVisibility(View.GONE);
                            phone1.setVisibility(View.GONE);
                            email1.setVisibility(View.GONE);
                            aecid.setVisibility(View.GONE);
                            aecid1.setVisibility(View.GONE);
                            email.setVisibility(View.GONE);
                            coun.setVisibility(View.GONE);
                            coun1.setVisibility(View.GONE);
                            birthdate.setText(userDetails.getDob());
                            gender.setSelection(spinnerPosition);
                            bloodgroup.setSelection(spinnerPosition2);
                            address.setVisibility(View.GONE);
                            address1.setVisibility(View.GONE);
                            loadingbar1.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        String[] items = new String[]{"Select Gender","Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items){
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                tfavv = ResourcesCompat.getFont(SettingActivity.this,R.font.roboto_black);
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
        String[] items1 = new String[]{"Select Blood Group","A+","A-","B+","B-","AB+","AB-","O+","O-","Other"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1){
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                tfavv = ResourcesCompat.getFont(SettingActivity.this,R.font.roboto_black);
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
        loadingbar= new ProgressDialog(this);
        loadingbar.setTitle("Account Sttings");
        loadingbar.setMessage("Saving your Details");
        header = findViewById(R.id.headertool);
        header.setText("Account Settings");
        birthdate = findViewById(R.id.setting_birthdate);
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
                 datePickerDialog = new DatePickerDialog(SettingActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                 datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(userwho.getSelectedItem().toString().equals("Self")||userwho.getSelectedItem().toString().equals("Select User"))
//                {
//                    String UserId = currentUser.getUid();
//                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(SettingActivity.this)
//                            .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
//                            .setTitle("Delete")
//                            .setMessage("Delete your account? Your all data will be erased permanently!")
//                            .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
//
//                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Records").child(UserId);
//                                storef= FirebaseStorage.getInstance().getReference().child("Records/"+UserId);
//                                usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
//                                usageref.child(UserId).removeValue();
//                                databaseReference.orderByChild("userwho").addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        for(DataSnapshot ds: dataSnapshot.getChildren())
//                                        {
//                                            storef.child(ds.child("original_filename").getValue(String.class)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    databaseReference.child(ds.getKey()).removeValue();
//                                                }
//                                            });
//                                        }
//
//
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//                                myref1.removeValue();
//                                currentUser.delete();
//                                mGoogleSignInClient.signOut();
//                                mAuth.signOut();
//
//
//                                Intent intent=new Intent(SettingActivity.this,Login.class);
//                                startActivity(intent);
//
//
//                                dialog.dismiss();
//                            }).addButton("No",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
//                                dialog.dismiss();
//                            });
//
//                    // Show the alert
//                    builder.show();
//                }
//                else
//                {
                    String UserId = currentUser.getUid();
                    CFAlertDialog.Builder builder = new CFAlertDialog.Builder(SettingActivity.this)
                            .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                            .setTitle("Delete")
                            .setMessage("Delete " +userwho.getSelectedItem().toString()+" ? User's all records will be deleted!")
                            .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {

                                storef= FirebaseStorage.getInstance().getReference().child("Records/"+UserId);
                                usageref=FirebaseDatabase.getInstance().getReference().child("Usage").child(UserId);
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Records").child(UserId);
                                databaseReference.orderByChild("userwho").equalTo(userwho.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot ds: dataSnapshot.getChildren())
                                        {
                                            String recordstorage=ds.child("size").getValue(String.class);
                                            storef.child(ds.child("original_filename").getValue(String.class)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    usageref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                                            String totalused=dataSnapshot1.child("used").getValue(String.class);
                                                            usageref.child("used").setValue(String.valueOf(Float.valueOf(totalused)-Float.valueOf(recordstorage)));
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                    databaseReference.child(ds.getKey()).removeValue();

                                                }
                                            });
                                        }
                                        myref1.child(userwho.getSelectedItem().toString()).removeValue();
                                        Toast.makeText(SettingActivity.this, userwho.getSelectedItem().toString()+" got deleted", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(SettingActivity.this,HomeActivity.class);
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                dialog.dismiss();
                            }).addButton("No",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
                                dialog.dismiss();
                            });

                    // Show the alert
                    builder.show();
                }
//            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userwho.getSelectedItem().toString().equals("Select Family Member"))
                {
                    Toast.makeText(SettingActivity.this, "Please Select Member", Toast.LENGTH_SHORT).show();
                }
                else if(fname.getText().toString().trim().length()==0)
                {
                    fname.setError("Please Enter Your First Name");
                }
                else if(lname.getText().toString().trim().length()==0)
                {
                    lname.setError("Please Enter Your Last Name");
                }
                else if(gender.getSelectedItem().toString().equals("Select Gender"))
                {
                    Toast.makeText(SettingActivity.this, "Please Select Your Gender", Toast.LENGTH_SHORT).show();
                }
                else if(bloodgroup.getSelectedItem().toString().equals("Select Blood Group"))
                {
                    Toast.makeText(SettingActivity.this, "Please Select Your Blood Group", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingbar.show();
                    if(userwho.getSelectedItem().toString().equals("Self"))
                    {
                        if(passwordedittext.getText().toString().length()==0)
                        {
                            Toast.makeText(SettingActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            myref.child("Self").child("blgrp").setValue(bloodgroup.getSelectedItem().toString());
                            myref.child("Self").child("dob").setValue(birthdate.getText().toString());
                            myref.child("Self").child("fname").setValue(fname.getText().toString().toUpperCase());
                            myref.child("Self").child("lname").setValue(lname.getText().toString().toUpperCase());
                            myref.child("Self").child("gender").setValue(gender.getSelectedItem().toString());
                            myref.child("Self").child("MLID2").setValue(passwordedittext.getText().toString());
                            loadingbar.dismiss();
                            Toast.makeText(SettingActivity.this, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                            finish();
//                        myref.child("Self").child("city").setValue(address.getText().toString().toUpperCase());
                        }

                    }
                    else
                    {
                        if(relation.getText().toString().trim().length()==0)
                        {
                            relation.setError("Please enter relation");
                        }
                        else
                        {
                            myref.child(userwho.getSelectedItem().toString()).removeValue();
                            myref.child(fname.getText().toString().toUpperCase()).child("blgrp").setValue(bloodgroup.getSelectedItem().toString());
                            myref.child(fname.getText().toString().toUpperCase()).child("dob").setValue(birthdate.getText().toString());
                            myref.child(fname.getText().toString().toUpperCase()).child("fname").setValue(fname.getText().toString().toUpperCase());
                            myref.child(fname.getText().toString().toUpperCase()).child("lname").setValue(lname.getText().toString().toUpperCase());
                            myref.child(fname.getText().toString().toUpperCase()).child("gender").setValue(gender.getSelectedItem().toString());
                            myref.child(fname.getText().toString().toUpperCase()).child("relation").setValue(relation.getText().toString().trim().toUpperCase());
                            recordref.orderByChild("userwho").equalTo(userwho.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                        recordref.child(ds.getKey()).child("userwho").setValue(fname.getText().toString());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            loadingbar.dismiss();
                            Toast.makeText(SettingActivity.this, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

//                    myref.child(userwho.getSelectedItem().toString()).child("city").setValue(address.getText().toString().toUpperCase());

                }
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(! AppStatus.getInstance(this).isOnline()) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            finish();
        }

//        myref = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);
//        myref.child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Users userDetails = dataSnapshot.getValue(Users.class);
//                String gen = userDetails.getGender();
//                String bg = userDetails.getBlgrp();
//                Typeface typeface = ResourcesCompat.getFont(SettingActivity.this,R.font.ubuntu_bold);
//                ArrayAdapter myAdap = (ArrayAdapter) gender.getAdapter(); //cast to an ArrayAdapter
//                int spinnerPosition = myAdap.getPosition(gen);
//                ArrayAdapter myAdap2 = (ArrayAdapter) bloodgroup.getAdapter(); //cast to an ArrayAdapter
//                int spinnerPosition2 = myAdap2.getPosition(bg);
//                fname.setText(userDetails.getFname());
//                lname.setText(userDetails.getLname());
//                phone.setText(userDetails.getPhone());
//                email.setText(currentUser.getEmail());
//                birthdate.setText(userDetails.getDob());
//                gender.setSelection(spinnerPosition);
//                bloodgroup.setSelection(spinnerPosition2);
//                address.setText(userDetails.getCity());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
}
