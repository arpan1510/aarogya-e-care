package com.operationsmiley.aarogyaecare;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.operationsmiley.aarogyaecare.module.Usage;
import com.operationsmiley.aarogyaecare.prevalent.prevalent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SaveRecords extends AppCompatActivity {

    EditText editFileName,dname,hname,note;
    ImageView backbtn;
    TextView uploadBtn;
    LinearLayout recordtypetext,recordsubtypetext;
    private EditText birthdate;
    final Calendar myCalendar = Calendar.getInstance();
    Toolbar toolbar;
    TextView header;
    Usage use;
    float pastused1,totalavai;
    FirebaseUser checkUser;
    String userId;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    Spinner recordtype,userwho,rstype,rsstype;
    String updatedusedstr;
    float totalcheck,updatedused;
    String pastused;
    private StorageTask mUploadtask;
    Typeface tfavv;
    StorageReference storageReference;
    String useddata;
    FirebaseUser currentUser;
    DatabaseReference databaseReference,usageref,databaseReference1,userwhoref;
    String uid;
    long count;
    private ProgressDialog loadingbar1;
    private static final int PERMISSION_STORAGE_CODE =1000 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_records);
        Paper.init(this);
//        useddata=getIntent().getStringExtra("useddata");
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        backbtn=findViewById(R.id.backBtn);
        dname=findViewById(R.id.doctor_name_details);
        hname=findViewById(R.id.hospital_name_details);
        note=findViewById(R.id.filenotes_details);
        recordsubtypetext=findViewById(R.id.textrecordsubsubtype);
        recordtypetext=findViewById(R.id.textrecordsubtype);
        recordtype=(Spinner)findViewById(R.id.spinner_recordtype);
        userwho=(Spinner)findViewById(R.id.spinner_userwho);
        rstype=(Spinner)findViewById(R.id.spinner_recordsubtype);
        rsstype=(Spinner)findViewById(R.id.spinner_recordsubsubtype);
        editFileName = findViewById(R.id.filename_details);
        uploadBtn = findViewById(R.id.upload_btn);
        toolbar = findViewById(R.id.toolbar_upload);
        setSupportActionBar(toolbar);
        use=new Usage();
        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Loading");
        loadingbar1.setMessage("Please wait...");
        loadingbar1.setCanceledOnTouchOutside(false);
        loadingbar1.show();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
        userwhoref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        storageReference = FirebaseStorage.getInstance().getReference();
        usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Records").child(uid);
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Records");

        checkUser= FirebaseAuth.getInstance().getCurrentUser();
        userId = checkUser.getUid();

        if(! AppStatus.getInstance(this).isOnline()) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            usageref.child(checkUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    loadingbar1.show();
                    Toast.makeText(SaveRecords.this, "for viewallfiles", Toast.LENGTH_SHORT).show();
                    use=dataSnapshot.getValue(Usage.class);
                    pastused=use.getUsed();
                    pastused1=Float.valueOf(pastused)+5;
                    totalavai=Float.valueOf(use.getTotal());
//                Paper.book().write(prevalent.pastusedpre,pastused);
                    loadingbar1.dismiss();
                    if(pastused1>=totalavai)
                    {
                        Toast.makeText(SaveRecords.this, "Memory full", Toast.LENGTH_SHORT).show();
                        loadingbar1.dismiss();
                        finish();
                    }
                    loadingbar1.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

//        header = findViewById(R.id.simple_toolbar_header);
//        header.setText("Save a Document");
        date = dateFormat.format(calendar.getTime());
        birthdate = findViewById(R.id.recorddate_details);
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
                DatePickerDialog d= new DatePickerDialog(SaveRecords.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                d.getDatePicker().setMaxDate(new Date().getTime());
                d.show();
            }
        });
        List<String> usernamelist = new ArrayList<>();
        userwhoref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usernamelist.add("Select Family Member");
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    usernamelist.add(key);

                }

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SaveRecords.this, android.R.layout.simple_spinner_dropdown_item, usernamelist){
                    public View getView(int position, View convertView, android.view.ViewGroup parent) {
                        tfavv = ResourcesCompat.getFont(SaveRecords.this,R.font.roboto_black);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        databaseReference1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists())
//                {
//                    count=dataSnapshot.getChildrenCount();
//                }
//                else
//                {
//                    count=0;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        String[] items = new String[]{"Select Record Type","Report","Prescription"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items){
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                tfavv = ResourcesCompat.getFont(SaveRecords.this,R.font.roboto_black);
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
        recordtype.setAdapter(adapter);
        loadingbar1.dismiss();
//        String[] item1 = usernamelist.toArray(new String[usernamelist.size()]);
//            String[] item1=new String[]{"Arpan","Raju"};

        recordtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FirebaseDatabase.getInstance().getReference().child(recordtype.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            loadingbar1.show();
                            rstype.setVisibility(View.VISIBLE);
                            recordtypetext.setVisibility(View.VISIBLE);
                            recordsubtypetext.setVisibility(View.VISIBLE);
                            rsstype.setVisibility(View.VISIBLE);
                            List<String> recordstype = new ArrayList<>();
                            List<String> recordsstype = new ArrayList<>();
                            recordstype.add("Select type of "+recordtype.getSelectedItem().toString());
                            recordsstype.add("Select subtype of "+recordtype.getSelectedItem().toString());
                            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                String key = ds.getKey();
                                recordstype.add(key);

                            }

                            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SaveRecords.this, android.R.layout.simple_spinner_dropdown_item, recordstype){
                                public View getView(int position, View convertView, android.view.ViewGroup parent) {
                                    tfavv = ResourcesCompat.getFont(SaveRecords.this,R.font.roboto_black);
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
                            rstype.setAdapter(adapter1);
//                            rstype.setSelection(adapter1.getPosition(recordstype.get(0)));

                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SaveRecords.this, android.R.layout.simple_spinner_dropdown_item, recordsstype){
                                public View getView(int position, View convertView, android.view.ViewGroup parent) {
                                    tfavv = ResourcesCompat.getFont(SaveRecords.this,R.font.roboto_black);
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
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            rsstype.setAdapter(adapter2);
                            loadingbar1.dismiss();

                        }
                        else
                        {
                            rstype.setVisibility(View.GONE);
                            recordsubtypetext.setVisibility(View.GONE);
                            recordtypetext.setVisibility(View.GONE);
                            rsstype.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rstype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FirebaseDatabase.getInstance().getReference().child(recordtype.getSelectedItem().toString()).child(rstype.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            loadingbar1.show();
                            rstype.setVisibility(View.VISIBLE);
                            recordtypetext.setVisibility(View.VISIBLE);
                            recordsubtypetext.setVisibility(View.VISIBLE);
                            rsstype.setVisibility(View.VISIBLE);
                            List<String> recordsstype1 = new ArrayList<>();
                            recordsstype1.add("Select subtype of "+recordtype.getSelectedItem().toString());
                            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                String key = dataSnapshot.child(ds.getKey()).getValue(String.class);
                                recordsstype1.add(key);

                            }

                            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(SaveRecords.this, android.R.layout.simple_spinner_dropdown_item, recordsstype1){
                                public View getView(int position, View convertView, android.view.ViewGroup parent) {
                                    tfavv = ResourcesCompat.getFont(SaveRecords.this,R.font.roboto_black);
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
                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            rsstype.setAdapter(adapter3);
                            loadingbar1.dismiss();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(recordtype.getSelectedItem().toString().equals("Select Record Type"))
                {
                    Toast.makeText(SaveRecords.this, "Please select a record type", Toast.LENGTH_SHORT).show();
                }
                else if(userwho.getSelectedItem().toString().equals("Select User"))
                {
                    Toast.makeText(SaveRecords.this, "Please select a user", Toast.LENGTH_SHORT).show();
                }
                else if(dname.getText().toString().trim().length()==0)
                {
                    dname.setError("Please enter Doctor's name");
                }
                else if(hname.getText().toString().trim().length()==0)
                {
                    hname.setError("Please Enter Clinic's name");
                }
                else if(editFileName.getText().toString().trim().length()==0)
                {
                    editFileName.setError("Please enter file name");
                }
                else if(birthdate.getText().toString().length()==0)
                {
                    Toast.makeText(SaveRecords.this, "Please enter record date!", Toast.LENGTH_SHORT).show();
                }
                else if(note.getText().toString().trim().length()==0)
                {
                    note.setError("Please enter a note describing your document");
                }
                else if(mUploadtask !=null && mUploadtask.isInProgress()){
                    Toast.makeText(SaveRecords.this, "Upload in progress...", Toast.LENGTH_SHORT).show();
                }
                else {


                    if(rstype.getVisibility()==View.VISIBLE)
                    {
                        if(rstype.getSelectedItem().toString().equals("Select type of "+recordtype.getSelectedItem().toString()))
                        {
                            Toast.makeText(SaveRecords.this, "Please select type of "+recordtype.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        }
                        else if(rsstype.getSelectedItem().toString().equals("Select subtype of "+recordtype.getSelectedItem().toString()))
                        {
                            Toast.makeText(SaveRecords.this, "Please select subtype of "+recordtype.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                            {
                                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
                                {
                                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                    requestPermissions(permissions,PERMISSION_STORAGE_CODE);
                                }
                                else
                                {
                                    selectFile();
                                }
                            }
                            else
                            {
                                selectFile();
                            }
                        }
                    }
                    else
                    {
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
                            {
                                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permissions,PERMISSION_STORAGE_CODE);

                            }
                            else
                            {
                                selectFile();
                            }
                        }
                        else
                        {
                            selectFile();
                        }
                    }


                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
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
        else
        {
            usageref.child(checkUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    loadingbar1.show();
                    Toast.makeText(SaveRecords.this, "for viewallfiles", Toast.LENGTH_SHORT).show();
                    use=dataSnapshot.getValue(Usage.class);
                    pastused=use.getUsed();
                    pastused1=Float.valueOf(pastused)+5;
                    totalavai=Float.valueOf(use.getTotal());
//                Paper.book().write(prevalent.pastusedpre,pastused);
                    loadingbar1.dismiss();
                    if(pastused1>=totalavai)
                    {
                        Toast.makeText(SaveRecords.this, "Memory full", Toast.LENGTH_SHORT).show();
                        loadingbar1.dismiss();
                        finish();
                    }
                    loadingbar1.dismiss();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    }

    private void selectFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimetypes = {"application/pdf","image/*"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimetypes);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1  && data != null && data.getData() != null){

                uploadPDFFile(data.getData());


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    selectFile();
                }
                else
                {
                    Toast.makeText(SaveRecords.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadPDFFile(Uri data) {



        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Your Document");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        FirebaseUser user;
        String extension=GetFileExtension(data);
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String originalfilename="Aarogya E Care"+date;
        StorageReference reference = storageReference.child("Records/"+Uid+"/Aarogya E Care"+date);
        mUploadtask=reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        String pastused = Paper.book().read(prevalent.pastusedpre);
                        float total=taskSnapshot.getTotalByteCount();
                        total=total/1048576;
                        updatedused=total+Float.valueOf(pastused);
                        updatedusedstr=String.valueOf(updatedused);
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url = uri.getResult();

                            if(rstype.getVisibility()==View.VISIBLE)
                            {
                                uploadFile uploadFile = new uploadFile(editFileName.getText().toString().toUpperCase(),recordtype.getSelectedItem().toString(),dname.getText().toString().toUpperCase(),hname.getText().toString().toUpperCase(),note.getText().toString(),date,String.valueOf(total),userwho.getSelectedItem().toString(),extension,rstype.getSelectedItem().toString(),rsstype.getSelectedItem().toString(),originalfilename,url.toString(),birthdate.getText().toString());
                                databaseReference.child(databaseReference.push().getKey()).setValue(uploadFile);
                                usageref.child(uid).child("used").setValue(updatedusedstr);
                                Toast.makeText(SaveRecords.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            }
                            else
                            {
                                uploadFile uploadFile = new uploadFile(editFileName.getText().toString().toUpperCase(),recordtype.getSelectedItem().toString(),dname.getText().toString().toUpperCase(),hname.getText().toString().toUpperCase(),note.getText().toString(),date,String.valueOf(total),userwho.getSelectedItem().toString(),extension,"Other","Other",originalfilename,url.toString(),birthdate.getText().toString());
                                databaseReference.child(databaseReference.push().getKey()).setValue(uploadFile);
                                usageref.child(uid).child("used").setValue(updatedusedstr);
                                Toast.makeText(SaveRecords.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            }





                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
            {
                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded: "+(int)progress+"%");
            }
        });
    }
    public String GetFileExtension(Uri uri)
    {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        // Return file Extension
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


}
