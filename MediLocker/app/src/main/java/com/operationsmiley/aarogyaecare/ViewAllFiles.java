package com.operationsmiley.aarogyaecare;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.operationsmiley.aarogyaecare.module.Usage;
import com.operationsmiley.aarogyaecare.module.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.operationsmiley.aarogyaecare.R.drawable.ic_image_black_24dp;
import static com.operationsmiley.aarogyaecare.R.drawable.ic_picture_as_pdf_black_24dp;

//import androidx.annotation.Nullable;
//import android.annotation.SuppressLint;


public class ViewAllFiles extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int PERMISSION_STORAGE_CODE =1000 ;
    DatabaseReference databaseReference;
    androidx.appcompat.widget.SearchView search;
    String pastused;
    float pastused1,totalavai;
    LinearLayout nofiles;
    String usertype;
    List<String> usernamelist;
    ArrayAdapter<String> adapteruserwho;
    TextView rtext,buystorage;
    ImageView searchbtn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ChipNavigationBar bottomnavigation;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    private FirebaseAuth mAuth;
    TextView username,phonenumber,mlid;
    String userId;
    Usage use;
    private LinearLayout storagedetails;
    RecyclerView recycle;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference myref;
    View headerView;
    ImageView settings,sharetool,addtoolbar;
    String uid,totalstorage="0",storageused="0",nooffiles="0",noofimages="0",noofpdf="0",noofreports="0",noofprescriptions="0";
    int getrandom;
    List<String> reply;
    Spinner userwho;
//    Toolbar toolbar2;
    Toolbar toolbar;
    FirebaseUser currentUser;
    Dialog mDialog;
    LinearLayout upgradenow;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mRef;
    TextView storagepercent,storageoutof,usertypeshow;
    ProgressBar prog,progressCircle;
    ImageView imageView;
    LinearLayout linearLayout;
    DatabaseReference usageref,userwhoref,userref,sharingref;
    Usage use1;
    ProgressDialog loadingbar,loadingbar1,loadingbar2;
    StorageReference storef;
    String ofilename,extension,downurl;
    Typeface tfavv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_files);

        loadingbar= new ProgressDialog(this);
        loadingbar.setTitle("Rename file");
        loadingbar.setMessage("Updating...");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Records");
        loadingbar1.setMessage("Loading...");
        loadingbar1.setCanceledOnTouchOutside(false);
        nofiles=findViewById(R.id.nodatalinearlayoutviewallfiles);
        userwho=(Spinner)findViewById(R.id.spinner_selectusertype);
        userId = FirebaseAuth.getInstance().getUid();
//        search=findViewById(R.id.searchedittext2);
//        search.setQueryHint("Search Record");
        linearLayout = findViewById(R.id.linearlayoutviewallfiles);
        rtext=findViewById(R.id.textrecord);
        use1=new Usage();
        use=new Usage();
        loadingbar2= new ProgressDialog(this);
        loadingbar2.setTitle("Sharing Records");
        loadingbar2.setMessage("Loading...");
        loadingbar2.setCanceledOnTouchOutside(false);
        sharetool=findViewById(R.id.sharetoolbar);
        addtoolbar=findViewById(R.id.addrecordtoolbar);
        progressCircle = findViewById(R.id.progress_bar);
//        imageView = findViewById(R.id.records_image);
        searchbtn=findViewById(R.id.searchbtn);
//        cancel=findViewById(R.id.cancelbtn2);
        recycle=(RecyclerView)findViewById(R.id.recyclerfront);
        recycle.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,2);
        recycle.setLayoutManager(layoutManager);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();
        toolbar = findViewById(R.id.includeToll);
//        toolbar2 = findViewById(R.id.toolbar_search);
        userwhoref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        userref=FirebaseDatabase.getInstance().getReference().child("Users");
        sharingref=FirebaseDatabase.getInstance().getReference().child("Sharing");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Records").child(uid);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mAuth=FirebaseAuth.getInstance();
        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);
        reply = new ArrayList<>();
        reply.add("Wonderful!");
        reply.add("Sounds Great!");
        reply.add("Awesome!");
        reply.add("Exciting!");
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        navigationView.getMenu().getItem(0).setChecked(true);
        headerView = navigationView.inflateHeaderView(R.layout.header);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        storagedetails=headerView.findViewById(R.id.storagedetailslinearlayout);


//        storagedetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                String totalstorage,storageused,nooffiles,noofimages,noofpdf,noofreports,noofprescriptions;
//                DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                DatabaseReference usageref= FirebaseDatabase.getInstance().getReference().child("Usage").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//                usageref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        totalstorage=dataSnapshot.child("total").getValue(String.class);
//                        storageused=dataSnapshot.child("used").getValue(String.class);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                recordref.orderByChild("record_type").equalTo("Prescription").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        noofprescriptions= String.valueOf(dataSnapshot.getChildrenCount());
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                recordref.orderByChild("record_type").equalTo("Report").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        noofreports= String.valueOf(dataSnapshot.getChildrenCount());
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//                recordref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists())
//                        {
//                            nooffiles = "No of files : ".concat(String.valueOf(dataSnapshot.getChildrenCount()));
//                            for (DataSnapshot ds: dataSnapshot.getChildren()) {
////                                if(ds.child("record_type").getValue(String.class).equals("Prescription"))
////                                {
////                                    noofprescriptions=String.valueOf(Long.valueOf(noofprescriptions)+1);
////                                }
////                                else
////                                {
////                                    noofreports=String.valueOf(Long.valueOf(noofreports)+1);
////                                }
//                                if (ds.child("filetype").getValue(String.class).equals("PDF"))
//                                {
//                                    noofpdf=String.valueOf(Long.valueOf(noofpdf)+1);
//                                }
//                                else
//                                {
//                                    noofimages=String.valueOf(Long.valueOf(noofimages)+1);
//                                }
//
//                            }
//                        }
//                        else
//                        {
////                            noofprescriptions="0";
////                            noofreports="0";
//                            noofpdf="0";
//                            noofimages="0";
//                        }
//
//                        noofimages = "No. of Images : ".concat(noofimages);
//                        noofpdf  = "No. of PDFs : ".concat(noofpdf);
//                        noofreports = "No. of Reports: ".concat(noofreports);
//                        noofprescriptions = "No. of Prescriptions : ".concat(noofprescriptions);
//                        totalstorage = "Total Storage : ".concat(totalstorage);
//                        storageused="Storage used : ".concat(storageused);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//                String Message = totalstorage.concat("\n").concat(storageused).concat("\n").concat(nooffiles).concat("\n").concat(noofimages).concat("\n").concat(noofpdf).concat("\n").concat(noofreports).concat("\n").concat(noofprescriptions).concat("\n");
//
//                CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(ViewAllFiles.this)
//                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
//                        .setTitle("Storage Details")
//                        .setMessage(Message)
//                        .addButton("Buy Storage", Color.parseColor("#000000"), Color.parseColor("#ffffff"), CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.CENTER, (dialog, which) -> {
//                            dialog.dismiss();
//                        });
//                // Show the alert
//                builder5.show();
//            }
//        });


//        uploadPDFS = new ArrayList<>();
//        view_allFiles();
//        myPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                uploadFile uploadFile = uploadPDFS.get(position);
//                Intent intent = new Intent();
//                intent.setType(intent.ACTION_VIEW);
//                intent.setData(Uri.parse(uploadFile.getUrl()));
//                startActivity(intent);
//            }
//        });



        userwho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(userwho.getSelectedItem().toString().equals("All Members"))
                {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                progressCircle.setVisibility(View.GONE);
                                recycle.setVisibility(View.GONE);
                                nofiles.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                progressCircle.setVisibility(View.GONE);
                                recycle.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.VISIBLE);
                                nofiles.setVisibility(View.GONE);
                                FirebaseRecyclerOptions<uploadFile> options =
                                        new FirebaseRecyclerOptions.Builder<uploadFile>()
                                                .setQuery(databaseReference.orderByChild("date"),uploadFile.class)
                                                .build();

                                FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
                                        new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
                                            @Override
                                            protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
                                                String key=getRef(position).getKey();
                                                String furl=model.getUrl();
                                                progressCircle.setVisibility(View.GONE);
                                                holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
                                                holder.vhdate.setText(model.getDate());
                                                holder.vhrecordtype.setText(model.getRecord_type());
                                                loadingbar1.dismiss();
                                                holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ofilename=model.getOriginal_filename();
                                                        extension="."+model.getFiletype();
                                                        PopupMenu popupMenu = new PopupMenu(ViewAllFiles.this,holder.vhdropdown);
                                                        popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                            @Override
                                                            public boolean onMenuItemClick(MenuItem item) {
                                                                switch(item.getItemId()){
                                                                    case R.id.Rename:{
                                                                        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAllFiles.this);
                                                                        builder.setCancelable(false);
                                                                        View mview= getLayoutInflater().inflate(R.layout.dialogrename,null);
                                                                        final EditText fmobile;

                                                                        fmobile=mview.findViewById(R.id.dialog_rename);

                                                                        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                fmobile.setText(dataSnapshot.child("filename").getValue(String.class));
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {

                                                                            }
                                                                        });
                                                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                        builder.setView(mview);
                                                                        final AlertDialog dialog = builder.create();
                                                                        dialog.setCanceledOnTouchOutside(false);
                                                                        dialog.show();
                                                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                if(fmobile.getText().toString().trim().length()!=0){
                                                                                    dialog.dismiss();
                                                                                    loadingbar.show();
                                                                                    databaseReference.child(key).child("filename").setValue(fmobile.getText().toString().toUpperCase());
                                                                                    loadingbar.dismiss();



                                                                                }
                                                                                else
                                                                                {
                                                                                    fmobile.setError("Please enter filename!");
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                    return true;
                                                                    case R.id.share :{

                                                                        userwhoref.child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                String aecid=dataSnapshot.child("MLID2").getValue(String.class);
                                                                                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(ViewAllFiles.this);
                                                                                Random random = new Random();
                                                                                getrandom = random.nextInt(4);
                                                                                flatDialog.setTitle("Share Document!")
                                                                                        .setTitleColor(getResources().getColor(R.color.colorPrimary))
                                                                                        .setBackgroundColor(Color.parseColor("#ffffff"))
                                                                                        .setIcon(R.drawable.shareee)
                                                                                        .setSubtitle("Password : "+aecid+"\nPassword can be changed from Edit Profile")
                                                                                        .setSubtitleColor(getResources().getColor(android.R.color.black))
                                                                                        .setFirstButtonText("Share Document")
                                                                                        .withFirstButtonListner(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                flatDialog.dismiss();
                                                                                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                                                                shareIntent.setType("text/plain");
                                                                                                String shareBody = "Hey There!\n\nPlease check the document shared to you in the below link by entering the correct password!\n\nhttps://aarogyaecare19.web.app/share/share_view.html?userId="+uid+"&&key="+getRef(position).getKey();
                                                                                                String shareSub = "MediLocker App";
                                                                                                shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                                                                                                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                                                                                                startActivity(Intent.createChooser(shareIntent,"Share Using"));
                                                                                            }
                                                                                        })
                                                                                        .show();
                                                                                flatDialog.setCanceledOnTouchOutside(true);
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                    }
                                                                    return true;
                                                                    case R.id.Download : {
                                                                        storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
                                                                        storef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                            @Override
                                                                            public void onSuccess(Uri uri) {
                                                                                String urlfordownload=uri.toString();
                                                                                downurl=urlfordownload;
                                                                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                                                                                {
                                                                                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
                                                                                    {
                                                                                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                                                                        requestPermissions(permissions,PERMISSION_STORAGE_CODE);

                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
                                                                                        Toast.makeText(ViewAllFiles.this, "Download Started", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                                else
                                                                                {
                                                                                    downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
                                                                                    Toast.makeText(ViewAllFiles.this, "Download Started", Toast.LENGTH_SHORT).show();
                                                                                }

                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {

                                                                            }
                                                                        });



                                                                    }
                                                                    return true;
                                                                    case R.id.Delete : {
                                                                        CFAlertDialog.Builder builder1 = new CFAlertDialog.Builder(ViewAllFiles.this)
                                                                                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                                                                                .setTitle("Delete File")
                                                                                .setMessage("Are you sure you want to delete?")
                                                                                .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {


                                                                                    storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
                                                                                    storef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
                                                                                            databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                    String recordstorage=dataSnapshot.child("size").getValue(String.class);
                                                                                                    usageref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                            String totalused=dataSnapshot.child("used").getValue(String.class);
                                                                                                            usageref.child(uid).child("used").setValue(String.valueOf(Float.valueOf(totalused)-Float.valueOf(recordstorage)));
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                        }
                                                                                                    });

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });
                                                                                            databaseReference.child(key).removeValue();
                                                                                            Toast.makeText(ViewAllFiles.this, "File Deleted", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                                    dialog.dismiss();
                                                                                }).addButton("No",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
                                                                                    dialog.dismiss();
                                                                                });

                                                                        // Show the alert
                                                                        builder1.show();
                                                                    }
                                                                    return true;
                                                                    case R.id.Details : {
//                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
//                                                            intent.putExtra("key",key);
//                                                            startActivity(intent);
                                                                        // Create Alert using Builder
                                                                        DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
                                                                        recordref.addValueEventListener(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                uploadFile show = dataSnapshot.getValue(uploadFile.class);
                                                                                String filename = "File Name: ".concat(show.getFilename());
                                                                                String filetype = "File Type: ".concat(show.getRecord_type());
                                                                                String doctor = "Doctor: ".concat(show.getDoctorname());
                                                                                String clinic = "Clinic: ".concat(show.getHospital());
                                                                                String Notes = "Notes: ".concat(show.getNotes());
                                                                                String Date = "Created On: ".concat(show.getDate_o());
                                                                                String ext = "Extension: ".concat(show.getFiletype());
                                                                                String size = "File Size: ".concat(show.getSize()).concat(" MB");
                                                                                String subtype="Category: ".concat(show.getRecord_type1());
                                                                                String subsubtype="Sub Category: ".concat(show.getRecord_type2());
                                                                                String usersdata="Family Member: ".concat(show.getUserwho());
                                                                                String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
                                                                                        concat(ext).concat("\n").concat(size).concat("\n").concat(usersdata);
                                                                                CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(ViewAllFiles.this)
                                                                                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                                                                        .setTitle("Record Details")
                                                                                        .setMessage(Message)
                                                                                        .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
                                                                                            dialog.dismiss();
                                                                                        });


                                                                                // Show the alert
                                                                                builder5.show();
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                                    }
                                                                    return true;
                                                                }
                                                                return false;
                                                            }
                                                        });
                                                        popupMenu.show();
                                                    }
                                                });
                                                String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
                                                holder.vhfilesize.setText(numberAsString+"MB");
                                                if(model.getFiletype().equals("pdf"))
                                                {
                                                    holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
                                                }
                                                else
                                                {
                                                    holder.vhpdforimg.setImageResource(ic_image_black_24dp);
                                                }
                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if(model.getFiletype().equals("pdf"))
                                                        {
                                                            loadingbar1.show();
                                                            Intent intent = new Intent(ViewAllFiles.this,webview.class);
                                                            intent.putExtra("url",furl);
                                                            loadingbar1.dismiss();
                                                            startActivity(intent);
                                                        }
                                                        else
                                                        {
                                                            loadingbar1.show();
                                                            Intent intent = new Intent(ViewAllFiles.this,webviewforimage.class);
                                                            intent.putExtra("url",furl);
                                                            loadingbar1.dismiss();
                                                            startActivity(intent);
                                                        }

                                                    }
                                                });

                                            }

                                            @NonNull
                                            @Override
                                            public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
                                                recordviewholder holder = new recordviewholder(view);
                                                return holder;
                                            }
                                        };
                                recycle.setAdapter(adapter);
                                adapter.startListening();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                else
                {

                    databaseReference.orderByChild("userwho").equalTo(userwho.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(!dataSnapshot.exists()){
                                progressCircle.setVisibility(View.GONE);
                                recycle.setVisibility(View.GONE);
                                nofiles.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                progressCircle.setVisibility(View.GONE);
                                recycle.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.VISIBLE);
                                nofiles.setVisibility(View.GONE);
                                FirebaseRecyclerOptions<uploadFile> options =
                                        new FirebaseRecyclerOptions.Builder<uploadFile>()
                                                .setQuery(databaseReference.orderByChild("userwho").equalTo(userwho.getSelectedItem().toString()),uploadFile.class)
                                                .build();

                                Query query = databaseReference.orderByChild("userwho").equalTo(userwho.getSelectedItem().toString());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(! dataSnapshot.exists()){
//                                Toast.makeText(ViewAllFiles.this, "Please Upload Your First Record!", Toast.LENGTH_SHORT).show();
                                            progressCircle.setVisibility(View.GONE);
//                                            Toast.makeText(ViewAllFiles.this, "No Documents for ".concat(userwho.getSelectedItem().toString()), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter1=
                                        new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
                                            @Override
                                            protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
                                                String key=getRef(position).getKey();
                                                String furl=model.getUrl();
                                                progressCircle.setVisibility(View.GONE);
                                                holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
                                                holder.vhdate.setText(model.getDate());
                                                holder.vhrecordtype.setText(model.getRecord_type());
                                                loadingbar1.dismiss();
                                                holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        ofilename=model.getOriginal_filename();
                                                        extension="."+model.getFiletype();
                                                        PopupMenu popupMenu = new PopupMenu(ViewAllFiles.this,holder.vhdropdown);
                                                        popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                            @Override
                                                            public boolean onMenuItemClick(MenuItem item) {
                                                                switch(item.getItemId()){
                                                                    case R.id.Rename:{
                                                                        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAllFiles.this);
                                                                        builder.setCancelable(false);
                                                                        View mview= getLayoutInflater().inflate(R.layout.dialogrename,null);
                                                                        final EditText fmobile;

                                                                        fmobile=mview.findViewById(R.id.dialog_rename);

                                                                        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                fmobile.setText(dataSnapshot.child("filename").getValue(String.class));
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {

                                                                            }
                                                                        });
                                                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                        builder.setView(mview);
                                                                        final AlertDialog dialog = builder.create();
                                                                        dialog.setCanceledOnTouchOutside(false);
                                                                        dialog.show();
                                                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                if(fmobile.getText().toString().trim().length()!=0){
                                                                                    dialog.dismiss();
                                                                                    loadingbar.show();
                                                                                    databaseReference.child(key).child("filename").setValue(fmobile.getText().toString().toUpperCase());
                                                                                    loadingbar.dismiss();



                                                                                }
                                                                                else
                                                                                {
                                                                                    fmobile.setError("Please enter filename!");
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                    return true;
                                                                    case R.id.Download : {
                                                                        storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
                                                                        storef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                            @Override
                                                                            public void onSuccess(Uri uri) {
                                                                                String urlfordownload=uri.toString();
                                                                                downurl=urlfordownload;
                                                                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                                                                                {
                                                                                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
                                                                                    {
                                                                                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                                                                        requestPermissions(permissions,PERMISSION_STORAGE_CODE);
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        Toast.makeText(ViewAllFiles.this, "Download started", Toast.LENGTH_SHORT).show();
                                                                                        downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
                                                                                    }
                                                                                }
                                                                                else
                                                                                {
                                                                                    Toast.makeText(ViewAllFiles.this, "Download started", Toast.LENGTH_SHORT).show();
                                                                                    downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
                                                                                }

                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {

                                                                            }
                                                                        });



                                                                    }
                                                                    return true;
                                                                    case R.id.share :{

                                                                        userwhoref.child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                String aecid=dataSnapshot.child("MLID2").getValue(String.class);
                                                                                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(ViewAllFiles.this);
                                                                                Random random = new Random();
                                                                                getrandom = random.nextInt(4);
                                                                                flatDialog.setTitle("Share Document!")
                                                                                        .setTitleColor(getResources().getColor(R.color.colorPrimary))
                                                                                        .setBackgroundColor(Color.parseColor("#ffffff"))
                                                                                        .setIcon(R.drawable.shareee)
                                                                                        .setSubtitle("Password : "+aecid+"\nPassword can be changed from Edit Profile")
                                                                                        .setSubtitleColor(getResources().getColor(android.R.color.black))
                                                                                        .setFirstButtonText("Share Document")
                                                                                        .withFirstButtonListner(new View.OnClickListener() {
                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                flatDialog.dismiss();
                                                                                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                                                                shareIntent.setType("text/plain");
                                                                                                String shareBody = "Hey There!\n\nPlease check the document shared to you in the below link by entering the correct password!\n\nhttps://aarogyaecare19.web.app/share/share_view.html?userId="+uid+"&&key="+getRef(position).getKey();
                                                                                                String shareSub = "MediLocker App";
                                                                                                shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                                                                                                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                                                                                                startActivity(Intent.createChooser(shareIntent,"Share Using"));
                                                                                            }
                                                                                        })
                                                                                        .show();
                                                                                flatDialog.setCanceledOnTouchOutside(true);
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });

                                                                    }
                                                                    return true;
                                                                    case R.id.Delete : {
                                                                        CFAlertDialog.Builder builder1 = new CFAlertDialog.Builder(ViewAllFiles.this)
                                                                                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                                                                                .setTitle("Delete File")
                                                                                .setMessage("Are you sure you want to delete?")
                                                                                .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {


                                                                                    storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
                                                                                    storef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
                                                                                            databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                    String recordstorage=dataSnapshot.child("size").getValue(String.class);
                                                                                                    usageref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                        @Override
                                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                            String totalused=dataSnapshot.child("used").getValue(String.class);
                                                                                                            usageref.child(uid).child("used").setValue(String.valueOf(Float.valueOf(totalused)-Float.valueOf(recordstorage)));
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                        }
                                                                                                    });

                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                }
                                                                                            });
                                                                                            userwho.setSelection(adapteruserwho.getPosition(usernamelist.get(0)));
                                                                                            databaseReference.child(key).removeValue();
                                                                                            Toast.makeText(ViewAllFiles.this, "File Deleted", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                                    dialog.dismiss();
                                                                                }).addButton("No",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
                                                                                    dialog.dismiss();
                                                                                });

                                                                        // Show the alert
                                                                        builder1.show();
                                                                    }
                                                                    return true;
                                                                    case R.id.Details : {
//                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
//                                                            intent.putExtra("key",key);
//                                                            startActivity(intent);
                                                                        // Create Alert using Builder
                                                                        DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
                                                                        recordref.addValueEventListener(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                uploadFile show = dataSnapshot.getValue(uploadFile.class);
                                                                                String filename = "File Name: ".concat(show.getFilename());
                                                                                String filetype = "File Type: ".concat(show.getRecord_type());
                                                                                String doctor = "Doctor: ".concat(show.getDoctorname());
                                                                                String clinic = "Clinic: ".concat(show.getHospital());
                                                                                String Notes = "Notes: ".concat(show.getNotes());
                                                                                String Date = "Created On: ".concat(show.getDate_o());
                                                                                String ext = "Extension: ".concat(show.getFiletype());
                                                                                String size = "File Size: ".concat(show.getSize()).concat(" MB");
                                                                                String subtype="Category: ".concat(show.getRecord_type1());
                                                                                String subsubtype="Sub Category: ".concat(show.getRecord_type2());
                                                                                String usersdata="Family Member: ".concat(show.getUserwho());
                                                                                String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
                                                                                        concat(ext).concat("\n").concat(size).concat("\n").concat(usersdata);
                                                                                CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(ViewAllFiles.this)
                                                                                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                                                                        .setTitle("Record Details")
                                                                                        .setMessage(Message)
                                                                                        .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
                                                                                            dialog.dismiss();
                                                                                        });


                                                                                // Show the alert
                                                                                builder5.show();
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        });
                                                                    }
                                                                    return true;
                                                                }
                                                                return false;
                                                            }
                                                        });
                                                        popupMenu.show();
                                                    }
                                                });
                                                String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
                                                holder.vhfilesize.setText(numberAsString+"MB");
                                                if(model.getFiletype().equals("pdf"))
                                                {
                                                    holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
                                                }
                                                else
                                                {
                                                    holder.vhpdforimg.setImageResource(ic_image_black_24dp);
                                                }
                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if(model.getFiletype().equals("pdf"))
                                                        {
                                                            loadingbar1.show();
                                                            Intent intent = new Intent(ViewAllFiles.this,webview.class);
                                                            intent.putExtra("url",furl);
                                                            loadingbar1.dismiss();
                                                            startActivity(intent);
                                                        }
                                                        else
                                                        {
                                                            loadingbar1.show();
                                                            Intent intent = new Intent(ViewAllFiles.this,webviewforimage.class);
                                                            intent.putExtra("url",furl);
                                                            loadingbar1.dismiss();
                                                            startActivity(intent);
                                                        }

                                                    }
                                                });
                                            }

                                            @NonNull
                                            @Override
                                            public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
                                                recordviewholder holder = new recordviewholder(view);
                                                return holder;
                                            }
                                        };
                                recycle.setAdapter(adapter1);
                                adapter1.startListening();
                            }

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
//        ((EditText)  search.findViewById(R.id.search_src_text))
//                .setHintTextColor(getResources().getColor(android.R.color.darker_gray));
//        ((EditText)  search.findViewById(R.id.search_src_text))
//                .setTextColor(getResources().getColor(android.R.color.black));

//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                firebasesearch(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                firebasesearch(newText);
//                return false;
//            }
//        });

        addtoolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               fetchdata();
                Intent intent = new Intent(ViewAllFiles.this, SaveRecords.class);
//                intent.putExtra("useddata",pastused);
                startActivity(intent);
            }
        });
        sharetool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAllFiles.this);
                View mview= getLayoutInflater().inflate(R.layout.forgotpassdialog,null);
                final EditText femail;

                femail=mview.findViewById(R.id.addemail);
                builder.setTitle("Share all files to AEC user ");
                builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setView(mview);
                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(femail.getText().toString().trim().length()!=0){
                            dialog.dismiss();
                            loadingbar2.show();
                            userref.orderByChild("Self/email").equalTo(femail.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    if(dataSnapshot.exists())
                                    {
                                        if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(femail.getText().toString().trim()))
                                        {
                                            loadingbar2.dismiss();
                                            Toast.makeText(ViewAllFiles.this, "You have entered your own email id! Please enter another user's correct email id...", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            for(DataSnapshot ds: dataSnapshot.getChildren())
                                            {
                                                loadingbar2.dismiss();
                                                sharingref.child("SharebyUid").child(uid).child(ds.getKey()).child("access").setValue("1");
                                                sharingref.child("SharebyUid").child(uid).child(ds.getKey()).child("email").setValue(femail.getText().toString().trim());
                                                sharingref.child("SharedwithUid").child(ds.getKey()).child(uid).child("access").setValue("1");
                                                sharingref.child("SharedwithUid").child(ds.getKey()).child(uid).child("email").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                                Intent intent=new Intent(ViewAllFiles.this,sharedbyme.class);
                                                startActivity(intent);
                                            }

                                        }

                                    }
                                    else
                                    {
                                        loadingbar2.dismiss();
                                        Toast.makeText(ViewAllFiles.this, "No user with this email exists!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else
                        {
                            loadingbar2.dismiss();
                            femail.setError("Please enter email of user you want to share!");
                        }
                    }
                });
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                toolbar.setVisibility(View.INVISIBLE);
//                toolbar2.setVisibility(View.VISIBLE);
//                linearLayout.setVisibility(View.GONE);
////                search.requestFocus();
//                setSupportActionBar(toolbar2);

                Intent intent=new Intent(ViewAllFiles.this,searchactivity.class);
                startActivity(intent);

            }
        });

//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toolbar.setVisibility(View.VISIBLE);
//                toolbar2.setVisibility(View.GONE);
//                linearLayout.setVisibility(View.VISIBLE);
//
//                if(userwho.getSelectedItem().toString().equals("All Members"))
//                {
//
//                    databaseReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if(!dataSnapshot.exists()){
//                                progressCircle.setVisibility(View.GONE);
//                                recycle.setVisibility(View.GONE);
//                                nofiles.setVisibility(View.VISIBLE);
//                            }
//                            else
//                            {
//
//                                progressCircle.setVisibility(View.GONE);
//                                recycle.setVisibility(View.VISIBLE);
//                                linearLayout.setVisibility(View.VISIBLE);
//                                nofiles.setVisibility(View.GONE);
//                                FirebaseRecyclerOptions<uploadFile> options =
//                                        new FirebaseRecyclerOptions.Builder<uploadFile>()
//                                                .setQuery(databaseReference.orderByChild("date"),uploadFile.class)
//                                                .build();
//
//                                FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
//                                        new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
//                                            @Override
//                                            protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
//                                                String key=getRef(position).getKey();
//                                                String furl=model.getUrl();
//                                                progressCircle.setVisibility(View.GONE);
//                                                holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
//                                                holder.vhdate.setText(model.getDate());
//                                                holder.vhrecordtype.setText(model.getRecord_type());
//                                                loadingbar1.dismiss();
//                                                holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        ofilename=model.getOriginal_filename();
//                                                        extension="."+model.getFiletype();
//                                                        PopupMenu popupMenu = new PopupMenu(ViewAllFiles.this,holder.vhdropdown);
//                                                        popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
//                                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                                                            @Override
//                                                            public boolean onMenuItemClick(MenuItem item) {
//                                                                switch(item.getItemId()){
//                                                                    case R.id.Rename:{
//                                                                        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAllFiles.this);
//                                                                        builder.setCancelable(false);
//                                                                        View mview= getLayoutInflater().inflate(R.layout.dialogrename,null);
//                                                                        final EditText fmobile;
//
//                                                                        fmobile=mview.findViewById(R.id.dialog_rename);
//
//                                                                        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                            @Override
//                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                                fmobile.setText(dataSnapshot.child("filename").getValue(String.class));
//                                                                            }
//
//                                                                            @Override
//                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                            }
//                                                                        });
//
//                                                                        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
//                                                                            @Override
//                                                                            public void onClick(DialogInterface dialog, int which) {
//
//                                                                            }
//                                                                        });
//                                                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                                                            @Override
//                                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                                dialog.dismiss();
//                                                                            }
//                                                                        });
//                                                                        builder.setView(mview);
//                                                                        final AlertDialog dialog = builder.create();
//                                                                        dialog.setCanceledOnTouchOutside(false);
//                                                                        dialog.show();
//                                                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                                                                            @Override
//                                                                            public void onClick(View v) {
//                                                                                if(fmobile.getText().toString().trim().length()!=0){
//                                                                                    dialog.dismiss();
//                                                                                    loadingbar.show();
//                                                                                    databaseReference.child(key).child("filename").setValue(fmobile.getText().toString().toUpperCase());
//                                                                                    loadingbar.dismiss();
//
//
//
//                                                                                }
//                                                                                else
//                                                                                {
//                                                                                    fmobile.setError("Please enter filename!");
//                                                                                }
//                                                                            }
//                                                                        });
//                                                                    }
//                                                                    return true;
//                                                                    case R.id.share :{
//
//                                                                        userwhoref.child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                            @Override
//                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                                String aecid=dataSnapshot.child("MLID2").getValue(String.class);
//                                                                                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(ViewAllFiles.this);
//                                                                                Random random = new Random();
//                                                                                getrandom = random.nextInt(4);
//                                                                                flatDialog.setTitle("Share Document!")
//                                                                                        .setTitleColor(getResources().getColor(R.color.colorPrimary))
//                                                                                        .setBackgroundColor(Color.parseColor("#ffffff"))
//                                                                                        .setIcon(R.drawable.shareee)
//                                                                                        .setSubtitle("Password : "+aecid+"\nPassword can be changed from Edit Profile")
//                                                                                        .setSubtitleColor(getResources().getColor(android.R.color.black))
//                                                                                        .setFirstButtonText("Share Document")
//                                                                                        .withFirstButtonListner(new View.OnClickListener() {
//                                                                                            @Override
//                                                                                            public void onClick(View v) {
//                                                                                                flatDialog.dismiss();
//                                                                                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                                                                                                shareIntent.setType("text/plain");
//                                                                                                String shareBody = "Hey There!\n\nPlease check the document shared to you in the below link by entering the correct password!\n\nhttps://aarogyaecare19.web.app/share/share_view.html?userId="+uid+"&&key="+getRef(position).getKey();
//                                                                                                String shareSub = "MediLocker App";
//                                                                                                shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
//                                                                                                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
//                                                                                                startActivity(Intent.createChooser(shareIntent,"Share Using"));
//                                                                                            }
//                                                                                        })
//                                                                                        .show();
//                                                                                flatDialog.setCanceledOnTouchOutside(true);
//                                                                            }
//
//                                                                            @Override
//                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                            }
//                                                                        });
//
//                                                                    }
//                                                                    return true;
//                                                                    case R.id.Download : {
//                                                                        storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
//                                                                        storef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                                            @Override
//                                                                            public void onSuccess(Uri uri) {
//                                                                                String urlfordownload=uri.toString();
//                                                                                downurl=urlfordownload;
//                                                                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
//                                                                                {
//                                                                                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
//                                                                                    {
//                                                                                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                                                                                        requestPermissions(permissions,PERMISSION_STORAGE_CODE);
//                                                                                    }
//                                                                                    else
//                                                                                    {
//                                                                                        downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
//                                                                                        Toast.makeText(ViewAllFiles.this, "Download Started", Toast.LENGTH_SHORT).show();
//                                                                                    }
//                                                                                }
//                                                                                else
//                                                                                {
//                                                                                    downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
//                                                                                    Toast.makeText(ViewAllFiles.this, "Download Started", Toast.LENGTH_SHORT).show();
//                                                                                }
//
//                                                                            }
//                                                                        }).addOnFailureListener(new OnFailureListener() {
//                                                                            @Override
//                                                                            public void onFailure(@NonNull Exception e) {
//
//                                                                            }
//                                                                        });
//
//
//
//                                                                    }
//                                                                    return true;
//                                                                    case R.id.Delete : {
//                                                                        CFAlertDialog.Builder builder1 = new CFAlertDialog.Builder(ViewAllFiles.this)
//                                                                                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
//                                                                                .setTitle("Delete File")
//                                                                                .setMessage("Are you sure you want to delete?")
//                                                                                .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
//
//
//                                                                                    storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
//                                                                                    storef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                                        @Override
//                                                                                        public void onSuccess(Void aVoid) {
//                                                                                            usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
//                                                                                            databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                                                @Override
//                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                                                    String recordstorage=dataSnapshot.child("size").getValue(String.class);
//                                                                                                    usageref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                                                        @Override
//                                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                                                            String totalused=dataSnapshot.child("used").getValue(String.class);
//                                                                                                            usageref.child(uid).child("used").setValue(String.valueOf(Float.valueOf(totalused)-Float.valueOf(recordstorage)));
//                                                                                                        }
//
//                                                                                                        @Override
//                                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                                                        }
//                                                                                                    });
//
//                                                                                                }
//
//                                                                                                @Override
//                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                                                }
//                                                                                            });
//                                                                                            databaseReference.child(key).removeValue();
//                                                                                            Toast.makeText(ViewAllFiles.this, "File Deleted", Toast.LENGTH_SHORT).show();
//                                                                                        }
//                                                                                    });
//                                                                                    dialog.dismiss();
//                                                                                }).addButton("No",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
//                                                                                    dialog.dismiss();
//                                                                                });
//
//                                                                        // Show the alert
//                                                                        builder1.show();
//                                                                    }
//                                                                    return true;
//                                                                    case R.id.Details : {
////                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
////                                                            intent.putExtra("key",key);
////                                                            startActivity(intent);
//                                                                        // Create Alert using Builder
//                                                                        DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
//                                                                        recordref.addValueEventListener(new ValueEventListener() {
//                                                                            @Override
//                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                                uploadFile show = dataSnapshot.getValue(uploadFile.class);
//                                                                                String filename = "File Name: ".concat(show.getFilename());
//                                                                                String filetype = "File Type: ".concat(show.getRecord_type());
//                                                                                String doctor = "Doctor: ".concat(show.getDoctorname());
//                                                                                String clinic = "Clinic: ".concat(show.getHospital());
//                                                                                String Notes = "Notes: ".concat(show.getNotes());
//                                                                                String Date = "Created On: ".concat(show.getDate_o());
//                                                                                String ext = "Extension: ".concat(show.getFiletype());
//                                                                                String size = "File Size: ".concat(show.getSize()).concat(" MB");
//                                                                                String subtype="Category: ".concat(show.getRecord_type1());
//                                                                                String subsubtype="Sub Category: ".concat(show.getRecord_type2());
//                                                                                String usersdata="Family Member: ".concat(show.getUserwho());
//                                                                                String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
//                                                                                        concat(ext).concat("\n").concat(size).concat("\n").concat(usersdata);
//                                                                                CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(ViewAllFiles.this)
//                                                                                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
//                                                                                        .setTitle("Record Details")
//                                                                                        .setMessage(Message)
//                                                                                        .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
//                                                                                            dialog.dismiss();
//                                                                                        });
//
//
//                                                                                // Show the alert
//                                                                                builder5.show();
//                                                                            }
//
//                                                                            @Override
//                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                            }
//                                                                        });
//                                                                    }
//                                                                    return true;
//                                                                }
//                                                                return false;
//                                                            }
//                                                        });
//                                                        popupMenu.show();
//                                                    }
//                                                });
//                                                String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
//                                                holder.vhfilesize.setText(numberAsString+"MB");
//                                                if(model.getFiletype().equals("pdf"))
//                                                {
//                                                    holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
//                                                }
//                                                else
//                                                {
//                                                    holder.vhpdforimg.setImageResource(ic_image_black_24dp);
//                                                }
//                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//
//                                                        if(model.getFiletype().equals("pdf"))
//                                                        {
//                                                            loadingbar1.show();
//                                                            Intent intent = new Intent(ViewAllFiles.this,webview.class);
//                                                            intent.putExtra("url",furl);
//                                                            loadingbar1.dismiss();
//                                                            startActivity(intent);
//                                                        }
//                                                        else
//                                                        {
//                                                            loadingbar1.show();
//                                                            Intent intent = new Intent(ViewAllFiles.this,webviewforimage.class);
//                                                            intent.putExtra("url",furl);
//                                                            loadingbar1.dismiss();
//                                                            startActivity(intent);
//                                                        }
//
//                                                    }
//                                                });
//
//                                            }
//
//                                            @NonNull
//                                            @Override
//                                            public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
//                                                recordviewholder holder = new recordviewholder(view);
//                                                return holder;
//                                            }
//                                        };
//                                recycle.setAdapter(adapter);
//                                adapter.startListening();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//
//                }
//                else
//                {
//
//                    databaseReference.orderByChild("userwho").equalTo(userwho.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                            if(!dataSnapshot.exists()){
//                                progressCircle.setVisibility(View.GONE);
//                                recycle.setVisibility(View.GONE);
//                                nofiles.setVisibility(View.VISIBLE);
//                            }
//                            else
//                            {
//                                progressCircle.setVisibility(View.GONE);
//                                recycle.setVisibility(View.VISIBLE);
//                                linearLayout.setVisibility(View.VISIBLE);
//                                nofiles.setVisibility(View.GONE);
//                                FirebaseRecyclerOptions<uploadFile> options =
//                                        new FirebaseRecyclerOptions.Builder<uploadFile>()
//                                                .setQuery(databaseReference.orderByChild("userwho").equalTo(userwho.getSelectedItem().toString()),uploadFile.class)
//                                                .build();
//
//                                Query query = databaseReference.orderByChild("userwho").equalTo(userwho.getSelectedItem().toString());
//                                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        if(! dataSnapshot.exists()){
////                                Toast.makeText(ViewAllFiles.this, "Please Upload Your First Record!", Toast.LENGTH_SHORT).show();
//                                            progressCircle.setVisibility(View.GONE);
////                                            Toast.makeText(ViewAllFiles.this, "No Documents for ".concat(userwho.getSelectedItem().toString()), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//
//                                FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
//                                        new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
//                                            @Override
//                                            protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
//                                                String key=getRef(position).getKey();
//                                                String furl=model.getUrl();
//                                                progressCircle.setVisibility(View.GONE);
//                                                holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
//                                                holder.vhdate.setText(model.getDate());
//                                                holder.vhrecordtype.setText(model.getRecord_type());
//                                                loadingbar1.dismiss();
//                                                holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        ofilename=model.getOriginal_filename();
//                                                        extension="."+model.getFiletype();
//                                                        PopupMenu popupMenu = new PopupMenu(ViewAllFiles.this,holder.vhdropdown);
//                                                        popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
//                                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                                                            @Override
//                                                            public boolean onMenuItemClick(MenuItem item) {
//                                                                switch(item.getItemId()){
//                                                                    case R.id.Rename:{
//                                                                        final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAllFiles.this);
//                                                                        builder.setCancelable(false);
//                                                                        View mview= getLayoutInflater().inflate(R.layout.dialogrename,null);
//                                                                        final EditText fmobile;
//
//                                                                        fmobile=mview.findViewById(R.id.dialog_rename);
//
//                                                                        databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                            @Override
//                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                                fmobile.setText(dataSnapshot.child("filename").getValue(String.class));
//                                                                            }
//
//                                                                            @Override
//                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                            }
//                                                                        });
//
//                                                                        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
//                                                                            @Override
//                                                                            public void onClick(DialogInterface dialog, int which) {
//
//                                                                            }
//                                                                        });
//                                                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                                                            @Override
//                                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                                dialog.dismiss();
//                                                                            }
//                                                                        });
//                                                                        builder.setView(mview);
//                                                                        final AlertDialog dialog = builder.create();
//                                                                        dialog.setCanceledOnTouchOutside(false);
//                                                                        dialog.show();
//                                                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                                                                            @Override
//                                                                            public void onClick(View v) {
//                                                                                if(fmobile.getText().toString().trim().length()!=0){
//                                                                                    dialog.dismiss();
//                                                                                    loadingbar.show();
//                                                                                    databaseReference.child(key).child("filename").setValue(fmobile.getText().toString().toUpperCase());
//                                                                                    loadingbar.dismiss();
//
//
//
//                                                                                }
//                                                                                else
//                                                                                {
//                                                                                    fmobile.setError("Please enter filename!");
//                                                                                }
//                                                                            }
//                                                                        });
//                                                                    }
//                                                                    return true;
//                                                                    case R.id.Download : {
//                                                                        storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
//                                                                        storef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                                            @Override
//                                                                            public void onSuccess(Uri uri) {
//                                                                                String urlfordownload=uri.toString();
//                                                                                downurl=urlfordownload;
//                                                                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
//                                                                                {
//                                                                                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
//                                                                                    {
//                                                                                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                                                                                        requestPermissions(permissions,PERMISSION_STORAGE_CODE);
//                                                                                    }
//                                                                                    else
//                                                                                    {
//                                                                                        Toast.makeText(ViewAllFiles.this, "Download started", Toast.LENGTH_SHORT).show();
//                                                                                        downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
//                                                                                    }
//                                                                                }
//                                                                                else
//                                                                                {
//                                                                                    Toast.makeText(ViewAllFiles.this, "Download started", Toast.LENGTH_SHORT).show();
//                                                                                    downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
//                                                                                }
//
//                                                                            }
//                                                                        }).addOnFailureListener(new OnFailureListener() {
//                                                                            @Override
//                                                                            public void onFailure(@NonNull Exception e) {
//
//                                                                            }
//                                                                        });
//
//
//
//                                                                    }
//                                                                    return true;
//                                                                    case R.id.share :{
//
//                                                                        userwhoref.child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                            @Override
//                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                                String aecid=dataSnapshot.child("MLID2").getValue(String.class);
//                                                                                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(ViewAllFiles.this);
//                                                                                Random random = new Random();
//                                                                                getrandom = random.nextInt(4);
//                                                                                flatDialog.setTitle("Share Document!")
//                                                                                        .setTitleColor(getResources().getColor(R.color.colorPrimary))
//                                                                                        .setBackgroundColor(Color.parseColor("#ffffff"))
//                                                                                        .setIcon(R.drawable.shareee)
//                                                                                        .setSubtitle("Password : "+aecid+"\nPassword can be changed from Edit Profile")
//                                                                                        .setSubtitleColor(getResources().getColor(android.R.color.black))
//                                                                                        .setFirstButtonText("Share Document")
//                                                                                        .withFirstButtonListner(new View.OnClickListener() {
//                                                                                            @Override
//                                                                                            public void onClick(View v) {
//                                                                                                flatDialog.dismiss();
//                                                                                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                                                                                                shareIntent.setType("text/plain");
//                                                                                                String shareBody = "Hey There!\n\nPlease check the document shared to you in the below link by entering the correct password!\n\nhttps://aarogyaecare19.web.app/share/share_view.html?userId="+uid+"&&key="+getRef(position).getKey();
//                                                                                                String shareSub = "MediLocker App";
//                                                                                                shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
//                                                                                                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
//                                                                                                startActivity(Intent.createChooser(shareIntent,"Share Using"));
//                                                                                            }
//                                                                                        })
//                                                                                        .show();
//                                                                                flatDialog.setCanceledOnTouchOutside(true);
//                                                                            }
//
//                                                                            @Override
//                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                            }
//                                                                        });
//
//                                                                    }
//                                                                    return true;
//                                                                    case R.id.Delete : {
//                                                                        CFAlertDialog.Builder builder1 = new CFAlertDialog.Builder(ViewAllFiles.this)
//                                                                                .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
//                                                                                .setTitle("Delete File")
//                                                                                .setMessage("Are you sure you want to delete?")
//                                                                                .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
//
//
//                                                                                    storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
//                                                                                    storef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                                        @Override
//                                                                                        public void onSuccess(Void aVoid) {
//                                                                                            usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
//                                                                                            databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                                                @Override
//                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                                                    String recordstorage=dataSnapshot.child("size").getValue(String.class);
//                                                                                                    usageref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                                                        @Override
//                                                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                                                            String totalused=dataSnapshot.child("used").getValue(String.class);
//                                                                                                            usageref.child(uid).child("used").setValue(String.valueOf(Float.valueOf(totalused)-Float.valueOf(recordstorage)));
//                                                                                                        }
//
//                                                                                                        @Override
//                                                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                                                        }
//                                                                                                    });
//
//                                                                                                }
//
//                                                                                                @Override
//                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                                                }
//                                                                                            });
//                                                                                            userwho.setSelection(adapteruserwho.getPosition(usernamelist.get(0)));
//                                                                                            databaseReference.child(key).removeValue();
//                                                                                            Toast.makeText(ViewAllFiles.this, "File Deleted", Toast.LENGTH_SHORT).show();
//                                                                                        }
//                                                                                    });
//                                                                                    dialog.dismiss();
//                                                                                }).addButton("No",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
//                                                                                    dialog.dismiss();
//                                                                                });
//
//                                                                        // Show the alert
//                                                                        builder1.show();
//                                                                    }
//                                                                    return true;
//                                                                    case R.id.Details : {
////                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
////                                                            intent.putExtra("key",key);
////                                                            startActivity(intent);
//                                                                        // Create Alert using Builder
//                                                                        DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
//                                                                        recordref.addValueEventListener(new ValueEventListener() {
//                                                                            @Override
//                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                                uploadFile show = dataSnapshot.getValue(uploadFile.class);
//                                                                                String filename = "File Name: ".concat(show.getFilename());
//                                                                                String filetype = "File Type: ".concat(show.getRecord_type());
//                                                                                String doctor = "Doctor: ".concat(show.getDoctorname());
//                                                                                String clinic = "Clinic: ".concat(show.getHospital());
//                                                                                String Notes = "Notes: ".concat(show.getNotes());
//                                                                                String Date = "Created On: ".concat(show.getDate_o());
//                                                                                String ext = "Extension: ".concat(show.getFiletype());
//                                                                                String size = "File Size: ".concat(show.getSize()).concat(" MB");
//                                                                                String subtype="Category: ".concat(show.getRecord_type1());
//                                                                                String subsubtype="Sub Category: ".concat(show.getRecord_type2());
//                                                                                String usersdata="Family Member: ".concat(show.getUserwho());
//                                                                                String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
//                                                                                        concat(ext).concat("\n").concat(size).concat("\n").concat(usersdata);
//                                                                                CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(ViewAllFiles.this)
//                                                                                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
//                                                                                        .setTitle("Record Details")
//                                                                                        .setMessage(Message)
//                                                                                        .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
//                                                                                            dialog.dismiss();
//                                                                                        });
//
//
//                                                                                // Show the alert
//                                                                                builder5.show();
//                                                                            }
//
//                                                                            @Override
//                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                            }
//                                                                        });
//                                                                    }
//                                                                    return true;
//                                                                }
//                                                                return false;
//                                                            }
//                                                        });
//                                                        popupMenu.show();
//                                                    }
//                                                });
//                                                String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
//                                                holder.vhfilesize.setText(numberAsString+"MB");
//                                                if(model.getFiletype().equals("pdf"))
//                                                {
//                                                    holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
//                                                }
//                                                else
//                                                {
//                                                    holder.vhpdforimg.setImageResource(ic_image_black_24dp);
//                                                }
//                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//
//                                                        if(model.getFiletype().equals("pdf"))
//                                                        {
//                                                            loadingbar1.show();
//                                                            Intent intent = new Intent(ViewAllFiles.this,webview.class);
//                                                            intent.putExtra("url",furl);
//                                                            loadingbar1.dismiss();
//                                                            startActivity(intent);
//                                                        }
//                                                        else
//                                                        {
//                                                            loadingbar1.show();
//                                                            Intent intent = new Intent(ViewAllFiles.this,webviewforimage.class);
//                                                            intent.putExtra("url",furl);
//                                                            loadingbar1.dismiss();
//                                                            startActivity(intent);
//                                                        }
//
//                                                    }
//                                                });
//                                            }
//
//                                            @NonNull
//                                            @Override
//                                            public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
//                                                recordviewholder holder = new recordviewholder(view);
//                                                return holder;
//                                            }
//                                        };
//                                recycle.setAdapter(adapter);
//                                adapter.startListening();
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//                }
//            }
//        });
        mDialog = new Dialog(this);

        mDatabase = FirebaseDatabase.getInstance();
        mRef =mDatabase.getReference("FeedSuggestions");


        drawerLayout = findViewById(R.id.drawer_layout);



//        settings = headerView.findViewById(R.id.setting);
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                Intent intent03 = new Intent(ViewAllFiles.this, SettingActivity.class);
//                startActivity(intent03);
//            }
//        });
        username = headerView.findViewById(R.id.user_name);
        phonenumber = headerView.findViewById(R.id.phone_number);
        usertypeshow = headerView.findViewById(R.id.type);
//        usertypeshow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                Intent intent = new Intent(ViewAllFiles.this,UserTypes.class);
//                startActivity(intent);
//            }
//        });

        storagepercent=headerView.findViewById(R.id.progresspercent);
        storageoutof=headerView.findViewById(R.id.progressvalue);
        prog=(ProgressBar)headerView.findViewById(R.id.progressbarstorage);
        usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
        usageref.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                use1=dataSnapshot.getValue(Usage.class);
                prog.setProgress((int)((100*Float.valueOf(use1.getUsed()))/Float.valueOf(use1.getTotal())));
                String numberAsString = String.format ("%.2f", (100*Float.valueOf(use1.getUsed()))/Float.valueOf(use1.getTotal()));
                storagepercent.setText("Storage ("+numberAsString+"% used)");
                String numberAsString1 = String.format ("%.2f", Float.valueOf(use1.getUsed()));
                storageoutof.setText(numberAsString1+"MB used out of "+use1.getTotal()+"MB");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        upgradenow = headerView.findViewById(R.id.upgrade_banner);
        buystorage=headerView.findViewById(R.id.buystorage);

        buystorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://aarogyaecare.com/user/payment.php"));
                startActivity(browserIntent);

            }
        });
        upgradenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "https://aarogyaecare.com/user/payment1.php?id=2&&email=";
//                String Email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//                loadingbar1.show();
//                Intent intent = new Intent(ViewAllFiles.this,WebViewBlogs.class);
//                intent.putExtra("url",url.concat(Email));
//                intent.putExtra("name","Payment Link for Aarogya E Care");
//                loadingbar1.dismiss();
//                startActivity(intent);
                onBackPressed();
                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(ViewAllFiles.this);
                Random random = new Random();
                getrandom = random.nextInt(4);
                flatDialog.setTitle("Coming Soon!")
                        .setTitleColor(getResources().getColor(R.color.colorPrimary))
                        .setBackgroundColor(Color.parseColor("#ffffff"))
                        .setIcon(R.drawable.crown2)
                        .setSubtitle("Premium Users will get special access to the Premium Features and Services on Aarogya E Care App.")
                        .setSubtitleColor(getResources().getColor(android.R.color.black))
                        .setFirstButtonText(reply.get(getrandom))
                        .withFirstButtonListner(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                flatDialog.dismiss();
                            }
                        })
                        .show();
                flatDialog.setCanceledOnTouchOutside(true);
            }
        });

        myref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Self");
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users userDetails = dataSnapshot.getValue(Users.class);
                String fname = userDetails.getFname().substring(0,1).toUpperCase()+userDetails.getFname().substring(1).toLowerCase();
                username.setText(fname);
                phonenumber.setText(userDetails.getPhone());
                usertypeshow.setText("AEC_ID : "+userDetails.getMLID());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bottomnavigation = findViewById(R.id.bottom_nav);
        setSupportActionBar(toolbar);




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawerOpen, R.string.navigation_drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



//        bottomnavigation.setOnNavigationItemSelectedListener(navListner);
//        bottomnavigation.setSelectedItemId(R.id.nav_records);
        bottomnavigation.setOnItemSelectedListener(navLister);
        bottomnavigation.setItemSelected(R.id.nav_records,true);

    }

//    private void view_allFiles() {
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
//
//                    uploadFile uploadFile = postSnapshot.getValue(uploadFile.class);
//                    uploadPDFS.add(uploadFile);
//                }
//
//                String[] uploads = new String[uploadPDFS.size()];
//
//                for (int i=0 ; i<uploads.length ; i++){
//
//                    uploads[i] = uploadPDFS.get(i).getName();
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads){
//
//                    @Override
//                    public View getView(int position,  View convertView,ViewGroup parent) {
//
//                        View view = super.getView(position, convertView, parent);
//
//                        TextView myText= (TextView) view.findViewById(android.R.id.text1);
//                        myText.setTextColor(Color.BLACK);
//
//
//                        return view;
//                    }
//                };
//                myPDFListView.setAdapter(adapter);
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//
//            }
//        });
//    }


    @Override
    public void onBackPressed() {
        int a = toolbar.getVisibility();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
//        else if (a == View.INVISIBLE){
//            toolbar.setVisibility(View.VISIBLE);
//            toolbar2.setVisibility(View.GONE);
////                setSupportActionBar(toolbar);
////            firebasesearch("");
//        }
        else {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            overridePendingTransition(0,0);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadingbar1.show();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Records").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        if(! AppStatus.getInstance(this).isOnline()) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            finish();
        }

         usernamelist = new ArrayList<>();
        userwhoref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usernamelist.add("All Members");
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    String key = ds.getKey();
                    usernamelist.add(key);

                }
//                Toast.makeText(ViewAllFiles.this, "array :"+ usernamelist.get(1), Toast.LENGTH_SHORT).show();

                 adapteruserwho = new ArrayAdapter<String>(ViewAllFiles.this, android.R.layout.simple_spinner_dropdown_item, usernamelist){
                    public View getView(int position, View convertView, android.view.ViewGroup parent) {
                        tfavv = ResourcesCompat.getFont(ViewAllFiles.this,R.font.roboto_black);
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
                adapteruserwho.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                userwho.setAdapter(adapteruserwho);
//                userwho.setSelection(adapter1.getPosition(usernamelist.get(0)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            linearLayout.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
//            toolbar2.setVisibility(View.GONE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    loadingbar1.dismiss();
                    progressCircle.setVisibility(View.GONE);
                    recycle.setVisibility(View.GONE);
                    nofiles.setVisibility(View.VISIBLE);
                }
                else
                {
                    progressCircle.setVisibility(View.GONE);
                    recycle.setVisibility(View.VISIBLE);
                    nofiles.setVisibility(View.GONE);
                    FirebaseRecyclerOptions<uploadFile> options =
                            new FirebaseRecyclerOptions.Builder<uploadFile>()
                                    .setQuery(databaseReference.orderByChild("date"),uploadFile.class)
                                    .build();

                    FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
                            new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
                                    String key=getRef(position).getKey();
                                    String furl=model.getUrl();
                                    progressCircle.setVisibility(View.GONE);
                                    holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
                                    holder.vhdate.setText(model.getDate());
                                    holder.vhrecordtype.setText(model.getRecord_type());
                                    holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ofilename=model.getOriginal_filename();
                                            extension="."+model.getFiletype();
                                            PopupMenu popupMenu = new PopupMenu(ViewAllFiles.this,holder.vhdropdown);
                                            popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch(item.getItemId()){
                                                        case R.id.Rename:{
                                                            final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAllFiles.this);
                                                            builder.setCancelable(false);
                                                            View mview= getLayoutInflater().inflate(R.layout.dialogrename,null);
                                                            final EditText fmobile;

                                                            fmobile=mview.findViewById(R.id.dialog_rename);

                                                            databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    fmobile.setText(dataSnapshot.child("filename").getValue(String.class));
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                            builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            });
                                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                            builder.setView(mview);
                                                            final AlertDialog dialog = builder.create();
                                                            dialog.setCanceledOnTouchOutside(false);
                                                            dialog.show();
                                                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    if(fmobile.getText().toString().trim().length()!=0){
                                                                        dialog.dismiss();
                                                                        loadingbar.show();
                                                                        databaseReference.child(key).child("filename").setValue(fmobile.getText().toString().toUpperCase());
                                                                        loadingbar.dismiss();



                                                                    }
                                                                    else
                                                                    {
                                                                        fmobile.setError("Please enter filename!");
                                                                    }
                                                                }
                                                            });
                                                        }
                                                        return true;
                                                        case R.id.Download : {
                                                            storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
                                                            storef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    String urlfordownload=uri.toString();
                                                                    downurl=urlfordownload;
                                                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                                                                    {
                                                                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
                                                                        {
                                                                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                                                            requestPermissions(permissions,PERMISSION_STORAGE_CODE);

                                                                        }
                                                                        else
                                                                        {
                                                                            downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
                                                                            Toast.makeText(ViewAllFiles.this, "Download Started", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
                                                                        Toast.makeText(ViewAllFiles.this, "Download Started", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                }
                                                            });



                                                        }
                                                        return true;
                                                        case R.id.share :{

                                                            userwhoref.child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    String aecid=dataSnapshot.child("MLID2").getValue(String.class);
                                                                    final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(ViewAllFiles.this);
                                                                    Random random = new Random();
                                                                    getrandom = random.nextInt(4);
                                                                    flatDialog.setTitle("Share Document!")
                                                                            .setTitleColor(getResources().getColor(R.color.colorPrimary))
                                                                            .setBackgroundColor(Color.parseColor("#ffffff"))
                                                                            .setIcon(R.drawable.shareee)
                                                                            .setSubtitle("Password : "+aecid+"\nPassword can be changed from Edit Profile")
                                                                            .setSubtitleColor(getResources().getColor(android.R.color.black))
                                                                            .setFirstButtonText("Share Document")
                                                                            .withFirstButtonListner(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    flatDialog.dismiss();
                                                                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                                                    shareIntent.setType("text/plain");
                                                                                    String shareBody = "Hey There!\n\nPlease check the document shared to you in the below link by entering the correct password!\n\nhttps://aarogyaecare19.web.app/share/share_view.html?userId="+uid+"&&key="+getRef(position).getKey();
                                                                                    String shareSub = "MediLocker App";
                                                                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                                                                                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                                                                                    startActivity(Intent.createChooser(shareIntent,"Share Using"));
                                                                                }
                                                                            })
                                                                            .show();
                                                                    flatDialog.setCanceledOnTouchOutside(true);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });

                                                        }
                                                        return true;
                                                        case R.id.Delete : {
                                                            CFAlertDialog.Builder builder1 = new CFAlertDialog.Builder(ViewAllFiles.this)
                                                                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                                                                    .setTitle("Delete File")
                                                                    .setMessage("Are you sure you want to delete?")
                                                                    .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {


                                                                        storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
                                                                        storef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
                                                                                databaseReference.child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                        String recordstorage=dataSnapshot.child("size").getValue(String.class);
                                                                                        usageref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                String totalused=dataSnapshot.child("used").getValue(String.class);
                                                                                                usageref.child(uid).child("used").setValue(String.valueOf(Float.valueOf(totalused)-Float.valueOf(recordstorage)));
                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                            }
                                                                                        });

                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                    }
                                                                                });
                                                                                databaseReference.child(key).removeValue();
                                                                                Toast.makeText(ViewAllFiles.this, "File Deleted", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                        dialog.dismiss();
                                                                    }).addButton("No",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
                                                                        dialog.dismiss();
                                                                    });

                                                            // Show the alert
                                                            builder1.show();
                                                        }
                                                        return true;
                                                        case R.id.Details : {
//                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
//                                                            intent.putExtra("key",key);
//                                                            startActivity(intent);
                                                            // Create Alert using Builder
                                                            DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
                                                            recordref.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    uploadFile show = dataSnapshot.getValue(uploadFile.class);
                                                                    String filename = "File Name: ".concat(show.getFilename());
                                                                    String filetype = "File Type: ".concat(show.getRecord_type());
                                                                    String doctor = "Doctor: ".concat(show.getDoctorname());
                                                                    String clinic = "Clinic: ".concat(show.getHospital());
                                                                    String Notes = "Notes: ".concat(show.getNotes());
                                                                    String Date = "Created On: ".concat(show.getDate_o());
                                                                    String ext = "Extension: ".concat(show.getFiletype());
                                                                    String size = "File Size: ".concat(show.getSize()).concat(" MB");
                                                                    String subtype="Category: ".concat(show.getRecord_type1());
                                                                    String subsubtype="Sub Category: ".concat(show.getRecord_type2());
                                                                    String usersdata="Family Member: ".concat(show.getUserwho());
                                                                    String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
                                                                            concat(ext).concat("\n").concat(size).concat("\n").concat(usersdata);
                                                                    CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(ViewAllFiles.this)
                                                                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                                                                            .setTitle("Record Details")
                                                                            .setMessage(Message)
                                                                            .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
                                                                                dialog.dismiss();
                                                                            });


                                                                    // Show the alert
                                                                    builder5.show();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            });
                                            popupMenu.show();
                                        }
                                    });
                                    String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
                                    holder.vhfilesize.setText(numberAsString+"MB");
                                    if(model.getFiletype().equals("pdf"))
                                    {
                                        holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
                                    }
                                    else
                                    {
                                        holder.vhpdforimg.setImageResource(ic_image_black_24dp);
                                    }
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if(model.getFiletype().equals("pdf"))
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(ViewAllFiles.this,webview.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(ViewAllFiles.this,webviewforimage.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }

                                        }
                                    });

                                }

                                @NonNull
                                @Override
                                public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
                                    recordviewholder holder = new recordviewholder(view);
                                    return holder;
                                }
                            };
                    recycle.setAdapter(adapter);
                    adapter.startListening();
                    loadingbar1.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(destinationDirectory,fileName + fileExtension);
        downloadmanager.enqueue(request);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_add_family:
                DatabaseReference usertyperef;

                usertyperef=FirebaseDatabase.getInstance().getReference().child("Users");
                usertyperef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usertype=dataSnapshot.child("Self").child("topup").getValue(String.class);

                        if(usertype.equals("0"))
                        {
//                            Toast.makeText(HomeActivity.this, "Please upgrade your account to access this feature!", Toast.LENGTH_SHORT).show();
                            final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(ViewAllFiles.this);
                            flatDialog.setTitle("Add Family Member!")
                                    .setTitleColor(getResources().getColor(R.color.colorPrimaryDark))
                                    .setBackgroundColor(Color.parseColor("#ffffff"))
                                    .setIcon(R.drawable.crown2)
                                    .setSubtitle("Through this feature user can add three family members to store their data, Buy Storage once to get access to this amazing feature.")
                                    .setSubtitleColor(getResources().getColor(android.R.color.black))
                                    .setFirstButtonText("Buy Storage")
                                    .withFirstButtonListner(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            flatDialog.dismiss();
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://aarogyaecare.com/user/payment.php"));
                                            startActivity(browserIntent);
                                        }
                                    })
                                    .show();
                            flatDialog.setCanceledOnTouchOutside(true);
                        }
                        else
                        {
                            if(dataSnapshot.getChildrenCount()>=4)
                            {
//                                Toast.makeText(HomeActivity.this, "You have already added 3 members, to add unlimited members please upgrade to VIP account", Toast.LENGTH_SHORT).show();
                                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(ViewAllFiles.this);
                                flatDialog.setTitle("Add Family Member!")
                                        .setTitleColor(getResources().getColor(R.color.colorPrimaryDark))
                                        .setBackgroundColor(Color.parseColor("#ffffff"))
                                        .setIcon(R.drawable.crown2)
                                        .setSubtitle("You have reached to maximum limit of three family members, Aarogya E Care allows maximum three family members to store their data.")
                                        .setSubtitleColor(getResources().getColor(android.R.color.black))
                                        .setFirstButtonText("Alright")
                                        .withFirstButtonListner(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                flatDialog.dismiss();
                                            }
                                        })
                                        .show();
                                flatDialog.setCanceledOnTouchOutside(true);
                            }
                            else
                            {
                                Intent intent = new Intent(ViewAllFiles.this,addfamilymember.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                overridePendingTransition(0,0);
                break;

//            case R.id.nav_app_info:
//                Intent intent01 = new Intent(this,AppInfoActivity.class);
//                startActivity(intent01);
//                break;
//
//            case R.id.nav_feed_suggest:
//                final com.operationsmiley.medilocker.FlatDialog flatDialog = new com.operationsmiley.medilocker.FlatDialog(this);
//                flatDialog.setTitle("Feedback / Suggestion")
//                        .setTitleColor(getResources().getColor(android.R.color.black))
//                        .setBackgroundColor(Color.parseColor("#ffffff"))
//                        .setFirstTextFieldHint("Subject")
//                        .setFirstTextFieldBorderColor(getResources().getColor(android.R.color.darker_gray))
//                        .setFirstTextFieldHintColor(getResources().getColor(android.R.color.darker_gray))
//                        .setFirstTextFieldTextColor(getResources().getColor(android.R.color.black))
//                        .setLargeTextFieldHint("Write your Message here")
//                        .setLargeTextFieldBorderColor(getResources().getColor(android.R.color.darker_gray))
//                        .setLargeTextFieldHintColor(getResources().getColor(android.R.color.darker_gray))
//                        .setLargeTextFieldTextColor(getResources().getColor(android.R.color.black))
//                        .setFirstButtonText("Send")
//                        .withFirstButtonListner(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                DatabaseReference myref;
//                                String subject = flatDialog.getFirstTextField().trim();
//                                String feedback = flatDialog.getLargeTextField().trim();
//                                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                                Date currentTime = Calendar.getInstance().getTime();
//                                String timestamp = currentTime.toString();
//                                String timecheck = timestamp.substring(0,10);
//                                myref = FirebaseDatabase.getInstance().getReference().child("Feedback");
//                                HashMap<String, Object> dataMap = new HashMap<>();
//                                dataMap.put("subject",subject);
//                                dataMap.put("feedback",feedback);
//                                dataMap.put("timestamp",timestamp);
//                                dataMap.put("uid",userid);
//                                if(subject.isEmpty())
//                                {
//                                    Toast.makeText(ViewAllFiles.this, "Please Enter Subject", Toast.LENGTH_SHORT).show();
//                                }
//                                else if (feedback.isEmpty())
//                                {
//                                    Toast.makeText(ViewAllFiles.this, "Feedback can't be Empty!", Toast.LENGTH_SHORT).show();
//                                }
//                                else {
//                                    final int[] aman = {1};
//                                    Query query = myref.child(userid).orderByChild("uid");
//                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for (DataSnapshot delSnapshot: dataSnapshot.getChildren()) {
//                                                AdImageSliderModel model = delSnapshot.getValue(AdImageSliderModel.class);
//                                                if (model.getTimestamp().contains(timecheck)) {
//                                                    aman[0] = 0;
//                                                    Toast.makeText(ViewAllFiles.this, "You can send Feedback once a Day!", Toast.LENGTH_LONG).show();
//                                                    flatDialog.dismiss();
//                                                }
//                                            }
//                                            if(aman[0] == 1){
//                                                myref.child(userid).child(timestamp).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        flatDialog.dismiss();
//                                                        Toast.makeText(ViewAllFiles.this, "Feedback Submitted", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                });
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }
//                            }
//                        })
//                        .show();
//                flatDialog.setCanceledOnTouchOutside(true);
//                break;
//
//            case R.id.nav_invite_friend:
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                String shareBody = "Hey There! I am using  MediLocker App.\n\nYou can store your all previous Medical Records at your provided MediLocker.\n\nDownload the App Now!:\nplay.google.com/store/apps/details?id=com.whatsapp&hl=en";
//                String shareSub = "MediLocker App";
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
//                shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
//                startActivity(Intent.createChooser(shareIntent,"Share Using"));
//                break;

            case R.id.nav_sharedbyme:
                Intent intent04 = new Intent(this,sharedbyme.class);
                startActivity(intent04);
                break;

            case R.id.nav_sharedwithme:
                Intent intent05 = new Intent(this,sharedwithme.class);
                startActivity(intent05);
                break;

            case R.id.nav_setting:
                Intent intent03 = new Intent(this,SettingActivity.class);
                startActivity(intent03);
                break;

            case R.id.nav_payment:
                Intent intent10 = new Intent(this,payment.class);
                startActivity(intent10);
                break;

            case R.id.nav_logout:
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                        .setTitle("Signout")
                        .setMessage("Logout from Aarogya E Care ?")
                        .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            mAuth.signOut();

                            // Google sign out
                            mGoogleSignInClient.signOut();
                            Intent intent = new Intent(ViewAllFiles.this,Login.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }).addButton("No",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
                            dialog.dismiss();
                        });

                // Show the alert
                builder.show();
                break;

        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

//    private void firebasesearch(String searchtext) {
//        String query = searchtext.toUpperCase();
//
//        Query firebasesearchquery = databaseReference.orderByChild("filename").startAt(query).endAt(query + "\uf8ff");
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(!dataSnapshot.exists())
//                {
//                    progressCircle.setVisibility(View.GONE);
//                    recycle.setVisibility(View.GONE);
//                    linearLayout.setVisibility(View.GONE);
//                    nofiles.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    progressCircle.setVisibility(View.GONE);
//                    recycle.setVisibility(View.VISIBLE);
//                    linearLayout.setVisibility(View.GONE);
//                    nofiles.setVisibility(View.GONE);
//                    FirebaseRecyclerOptions<uploadFile> options =
//                            new FirebaseRecyclerOptions.Builder<uploadFile>()
//                                    .setQuery(firebasesearchquery,uploadFile.class)
//                                    .build();
//
//                    FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
//                            new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
//                                @Override
//                                protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
//                                    String key=getRef(position).getKey();
//                                    String furl=model.getUrl();
//                                    progressCircle.setVisibility(View.GONE);
//                                    holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
//                                    holder.vhdate.setText(model.getDate());
//                                    holder.vhrecordtype.setText(model.getRecord_type());
//                                    loadingbar1.dismiss();
//                                    holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            ofilename=model.getOriginal_filename();
//                                            extension="."+model.getFiletype();
//                                            PopupMenu popupMenu = new PopupMenu(ViewAllFiles.this,holder.vhdropdown);
//                                            popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
//                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                                                @Override
//                                                public boolean onMenuItemClick(MenuItem item) {
//                                                    switch(item.getItemId()){
//                                                        case R.id.Rename:{
//                                                            final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAllFiles.this);
//                                                            builder.setCancelable(false);
//                                                            View mview= getLayoutInflater().inflate(R.layout.dialogrename,null);
//                                                            final EditText fmobile;
//                                                            fmobile=mview.findViewById(R.id.dialog_rename);
//                                                            databaseReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                @Override
//                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                    fmobile.setText(dataSnapshot.child("filename").getValue(String.class));
//                                                                }
//
//                                                                @Override
//                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                }
//                                                            });
//
//                                                            builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(DialogInterface dialog, int which)
//                                                                {
//
//                                                                }
//                                                            });
//                                                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(DialogInterface dialog, int which) {
//                                                                    dialog.dismiss();
//                                                                }
//                                                            });
//                                                            builder.setView(mview);
//                                                            final AlertDialog dialog = builder.create();
//                                                            dialog.setCanceledOnTouchOutside(false);
//                                                            dialog.show();
//                                                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(View v) {
//                                                                    if(fmobile.getText().toString().trim().length()!=0){
//                                                                        dialog.dismiss();
//                                                                        loadingbar.show();
//                                                                        databaseReference.child(key).child("filename").setValue(fmobile.getText().toString().toUpperCase());
//                                                                        loadingbar.dismiss();
//                                                                    }
//                                                                    else
//                                                                    {
//                                                                        fmobile.setError("Please enter filename!");
//                                                                    }
//                                                                }
//                                                            });
//                                                        }
//                                                        return true;
//                                                        case R.id.share :{
//                                                            userwhoref.child("Self").addListenerForSingleValueEvent(new ValueEventListener() {
//                                                                @Override
//                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                    String aecid=dataSnapshot.child("MLID2").getValue(String.class);
//                                                                    final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(ViewAllFiles.this);
//                                                                    Random random = new Random();
//                                                                    getrandom = random.nextInt(4);
//                                                                    flatDialog.setTitle("Share Document!")
//                                                                            .setTitleColor(getResources().getColor(R.color.colorPrimary))
//                                                                            .setBackgroundColor(Color.parseColor("#ffffff"))
//                                                                            .setIcon(R.drawable.shareee)
//                                                                            .setSubtitle("Password : "+aecid+"\nPassword can be changed from Edit Profile")
//                                                                            .setSubtitleColor(getResources().getColor(android.R.color.black))
//                                                                            .setFirstButtonText("Share Document")
//                                                                            .withFirstButtonListner(new View.OnClickListener() {
//                                                                                @Override
//                                                                                public void onClick(View v) {
//                                                                                    flatDialog.dismiss();
//                                                                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                                                                                    shareIntent.setType("text/plain");
//                                                                                    String shareBody = "Hey There!\n\nPlease check the document shared to you in the below link by entering the correct password!\n\nhttps://aarogyaecare19.web.app/share/share_view.html?userId="+uid+"&&key="+getRef(position).getKey();
//                                                                                    String shareSub = "MediLocker App";
//                                                                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
//                                                                                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
//                                                                                    startActivity(Intent.createChooser(shareIntent,"Share Using"));
//                                                                                }
//                                                                            })
//                                                                            .show();
//                                                                    flatDialog.setCanceledOnTouchOutside(true);
//                                                                }
//
//                                                                @Override
//                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                }
//                                                            });
//                                                        }
//                                                        return true;
//                                                        case R.id.Download : {
//                                                            storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
//                                                            storef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
//                                                                @Override
//                                                                public void onSuccess(Uri uri) {
//                                                                    String urlfordownload=uri.toString();
//                                                                    downurl=urlfordownload;
//                                                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
//                                                                    {
//                                                                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
//                                                                        {
//                                                                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                                                                            requestPermissions(permissions,PERMISSION_STORAGE_CODE);
//                                                                        }
//                                                                        else
//                                                                        {
//                                                                            downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
//                                                                            Toast.makeText(ViewAllFiles.this, "Download Started", Toast.LENGTH_SHORT).show();
//                                                                        }
//                                                                    }
//                                                                    else
//                                                                    {
//                                                                        downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
//                                                                        Toast.makeText(ViewAllFiles.this, "Download Started", Toast.LENGTH_SHORT).show();
//                                                                    }
//                                                                }
//                                                            }).addOnFailureListener(new OnFailureListener() {
//                                                                @Override
//                                                                public void onFailure(@NonNull Exception e) {
//
//                                                                }
//                                                            });
//                                                        }
//                                                        return true;
//                                                        case R.id.Delete : {
//                                                            CFAlertDialog.Builder builder1 = new CFAlertDialog.Builder(ViewAllFiles.this)
//                                                                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
//                                                                    .setTitle("Delete File")
//                                                                    .setMessage("Are you sure you want to delete?")
//                                                                    .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
//                                                                        storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
//                                                                        storef.delete().addOnSuccessListener(new OnSuccessListener<Void>(){
//                                                                            @Override
//                                                                            public void onSuccess(Void aVoid)
//                                                                            {
//                                                                                usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
//                                                                                databaseReference.child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener()
//                                                                                {
//                                                                                    @Override
//                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//                                                                                    {
//                                                                                        String recordstorage=dataSnapshot.child("size").getValue(String.class);
//                                                                                        usageref.child(uid).addListenerForSingleValueEvent(new ValueEventListener()
//                                                                                        {
//                                                                                            @Override
//                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
//                                                                                            {
//                                                                                                String totalused=dataSnapshot.child("used").getValue(String.class);
//                                                                                                usageref.child(uid).child("used").setValue(String.valueOf(Float.valueOf(totalused)-Float.valueOf(recordstorage)));
//                                                                                            }
//                                                                                            @Override
//                                                                                            public void onCancelled(@NonNull DatabaseError databaseError)
//                                                                                            {
//
//                                                                                            }
//                                                                                        });
//                                                                                    }
//                                                                                    @Override
//                                                                                    public void onCancelled(@NonNull DatabaseError databaseError)
//                                                                                    {
//
//                                                                                    }
//                                                                                });
//                                                                                databaseReference.child(key).removeValue();
//                                                                                search.requestFocus();
//                                                                                firebasesearch("");
//                                                                                Toast.makeText(ViewAllFiles.this, "File Deleted", Toast.LENGTH_SHORT).show();
//                                                                            }
//                                                                        });
//                                                                        dialog.dismiss();
//                                                                    }).addButton("No",-1,-1,CFAlertDialog.CFAlertActionStyle.DEFAULT,CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,(dialog, which) -> {
//                                                                        dialog.dismiss();
//                                                                    });
//                                                            // Show the alert
//                                                            builder1.show();
//                                                        }
//                                                        return true;
//                                                        case R.id.Details : {
////                                                            Intent intent=new Intent(ViewAllFiles.this,detailsforrecord.class);
////                                                            intent.putExtra("key",key);
////                                                            startActivity(intent);
//                                                            // Create Alert using Builder
//                                                            DatabaseReference recordref= FirebaseDatabase.getInstance().getReference().child("Records").child(uid).child(key);
//                                                            recordref.addValueEventListener(new ValueEventListener() {
//                                                                @Override
//                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                                    uploadFile show = dataSnapshot.getValue(uploadFile.class);
//                                                                    String filename = "File Name: ".concat(show.getFilename());
//                                                                    String filetype = "File Type: ".concat(show.getRecord_type());
//                                                                    String doctor = "Doctor: ".concat(show.getDoctorname());
//                                                                    String clinic = "Clinic: ".concat(show.getHospital());
//                                                                    String Notes = "Notes: ".concat(show.getNotes());
//                                                                    String Date = "Created On: ".concat(show.getDate_o());
//                                                                    String ext = "Extension: ".concat(show.getFiletype());
//                                                                    String size = "File Size: ".concat(show.getSize()).concat(" MB");
//                                                                    String subtype="Category: ".concat(show.getRecord_type1());
//                                                                    String subsubtype="Sub Category: ".concat(show.getRecord_type2());
//                                                                    String usersdata="Family Member: ".concat(show.getUserwho());
//                                                                    String Message = filename.concat("\n").concat(filetype).concat("\n").concat(subtype).concat("\n").concat(subsubtype).concat("\n").concat(doctor).concat("\n").concat(clinic).concat("\n").concat(Notes).concat("\n").concat(Date).concat("\n").
//                                                                            concat(ext).concat("\n").concat(size).concat("\n").concat(usersdata);
//                                                                    CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(ViewAllFiles.this)
//                                                                            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
//                                                                            .setTitle("Record Details")
//                                                                            .setMessage(Message)
//                                                                            .addButton("Back", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
//                                                                                dialog.dismiss();
//                                                                            });
//
//
//                                                                    // Show the alert
//                                                                    builder5.show();
//                                                                }
//
//                                                                @Override
//                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                                                }
//                                                            });
//                                                        }
//                                                        return true;
//                                                    }
//                                                    return false;
//                                                }
//                                            });
//                                            popupMenu.show();
//                                        }
//                                    });
//                                    String numberAsString = String.format ("%.2f", Float.valueOf(model.getSize()));
//                                    holder.vhfilesize.setText(numberAsString+"MB");
//                                    if(model.getFiletype().equals("pdf"))
//                                    {
//                                        holder.vhpdforimg.setImageResource(ic_picture_as_pdf_black_24dp);
//                                    }
//                                    else
//                                    {
//                                        holder.vhpdforimg.setImageResource(ic_image_black_24dp);
//                                    }
//                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//
//                                            if(model.getFiletype().equals("pdf"))
//                                            {
//                                                loadingbar1.show();
//                                                Intent intent = new Intent(ViewAllFiles.this,webview.class);
//                                                intent.putExtra("url",furl);
//                                                loadingbar1.dismiss();
//                                                startActivity(intent);
//                                            }
//                                            else
//                                            {
//                                                loadingbar1.show();
//                                                Intent intent = new Intent(ViewAllFiles.this,webviewforimage.class);
//                                                intent.putExtra("url",furl);
//                                                loadingbar1.dismiss();
//                                                startActivity(intent);
//                                            }
//
//                                        }
//                                    });
//
//                                }
//
//                                @NonNull
//                                @Override
//                                public recordviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_document,parent,false);
//                                    recordviewholder holder = new recordviewholder(view);
//                                    return holder;
//                                }
//                            };
//                    recycle.setAdapter(adapter);
//                    adapter.startListening();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    downloadFile(ViewAllFiles.this,ofilename,extension,"/Aarogya E Care",downurl);
                    Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ViewAllFiles.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener navListner =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//
//                    switch (item.getItemId()){
//                        case R.id.nav_home :
//                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                            overridePendingTransition(0,0);
//                            return true;
//
//                        case R.id.nav_records :
//                            overridePendingTransition(0,0);
//                            return true;
//
//
////                        case R.id.nav_track :
////                            startActivity(new Intent(getApplicationContext(),TrackActivity.class));
////                            overridePendingTransition(0,0);
////                            return true;
//
//                        case R.id.nav_notifications :
//                            startActivity(new Intent(getApplicationContext(),NotificationsActivity.class));
//                            overridePendingTransition(0,0);
//                            return true;
//
//                    }
//                    return false;
//                }
//            };

    private ChipNavigationBar.OnItemSelectedListener navLister = new ChipNavigationBar.OnItemSelectedListener() {
        @Override
        public void onItemSelected(int i) {
            switch (i){
                case R.id.nav_home :
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    overridePendingTransition(0,0);
                    break;

                case R.id.nav_records :
                    break;

                case R.id.nav_blogs :
                    startActivity(new Intent(getApplicationContext(), BlogsActivity.class));
                    overridePendingTransition(0,0);
                    break;

                case R.id.nav_notifications :
                    startActivity(new Intent(getApplicationContext(),NotificationsActivity.class));
                    overridePendingTransition(0,0);
                    break;
            }
        }
    };

//    private void fetchdata() {
//        loadingbar1.show();
//
//        usageref.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                use=dataSnapshot.getValue(Usage.class);
//                pastused=use.getUsed();
//                pastused1=Float.valueOf(pastused)+5;
//                totalavai=Float.valueOf(use.getTotal());
////                Paper.book().write(prevalent.pastusedpre,pastused);
//                loadingbar1.dismiss();
//                if(pastused1>=totalavai)
//                {
//                    Toast.makeText(ViewAllFiles.this, "Memory full", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Intent intent = new Intent(ViewAllFiles.this, SaveRecords.class);
//                    intent.putExtra("useddata",pastused);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

}
