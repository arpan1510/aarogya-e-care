package com.operationsmiley.aarogyaecare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.Random;

import static com.operationsmiley.aarogyaecare.R.drawable.ic_image_black_24dp;
import static com.operationsmiley.aarogyaecare.R.drawable.ic_picture_as_pdf_black_24dp;

public class searchactivity extends AppCompatActivity {

    private Toolbar toolbar2;
    private ImageView cancel;
    androidx.appcompat.widget.SearchView search;
    private RecyclerView recycle;
    private static final int PERMISSION_STORAGE_CODE =1000 ;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private ProgressDialog loadingbar1,loadingbar;
    private FirebaseUser currentUser;
    DatabaseReference usageref,userwhoref;
    StorageReference storef;
    int getrandom;
    String ofilename,extension,downurl;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchactivity);
        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Loading");
        loadingbar1.setMessage("Please wait...");
        loadingbar1.setCanceledOnTouchOutside(false);
        loadingbar= new ProgressDialog(this);
        loadingbar.setTitle("Rename file");
        loadingbar.setMessage("Updating...");
        loadingbar.setCanceledOnTouchOutside(false);
        toolbar2 = findViewById(R.id.toolbar_search1);
        search= findViewById(R.id.searchedittext2);
        search.setQueryHint("Search Record");
        recycle=(RecyclerView)findViewById(R.id.recyclersearchactivity);
        recycle.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,2);
        recycle.setLayoutManager(layoutManager);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();

        ((EditText)  search.findViewById(R.id.search_src_text))
                .setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        ((EditText)  search.findViewById(R.id.search_src_text))
                .setTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar2);
        userwhoref=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Records").child(uid);
        cancel=findViewById(R.id.cancelbtn2);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebasesearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebasesearch(newText);
                return false;
            }
        });
    }

    private void firebasesearch(String searchtext) {

        String query = searchtext.toUpperCase();
        Query firebasesearchquery = databaseReference.orderByChild("filename").startAt(query).endAt(query + "\uf8ff");

                    FirebaseRecyclerOptions<uploadFile> options =
                            new FirebaseRecyclerOptions.Builder<uploadFile>()
                                    .setQuery(firebasesearchquery,uploadFile.class)
                                    .build();

                    FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
                            new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
                                    String key=getRef(position).getKey();
                                    String furl=model.getUrl();
                                    holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
                                    holder.vhdate.setText(model.getDate());
                                    holder.vhrecordtype.setText(model.getRecord_type());
                                    loadingbar1.dismiss();
                                    holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ofilename=model.getOriginal_filename();
                                            extension="."+model.getFiletype();
                                            PopupMenu popupMenu = new PopupMenu(searchactivity.this,holder.vhdropdown);
                                            popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch(item.getItemId()){
                                                        case R.id.Rename:{
                                                            final AlertDialog.Builder builder = new AlertDialog.Builder(searchactivity.this);
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
                                                                public void onClick(DialogInterface dialog, int which)
                                                                {

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
                                                                    final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(searchactivity.this);
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
                                                            storef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
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
                                                                            downloadFile(searchactivity.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
                                                                            Toast.makeText(searchactivity.this, "Download Started", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        downloadFile(searchactivity.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
                                                                        Toast.makeText(searchactivity.this, "Download Started", Toast.LENGTH_SHORT).show();
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
                                                            CFAlertDialog.Builder builder1 = new CFAlertDialog.Builder(searchactivity.this)
                                                                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                                                                    .setTitle("Delete File")
                                                                    .setMessage("Are you sure you want to delete?")
                                                                    .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                                                        storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
                                                                        storef.delete().addOnSuccessListener(new OnSuccessListener<Void>(){
                                                                            @Override
                                                                            public void onSuccess(Void aVoid)
                                                                            {
                                                                                usageref= FirebaseDatabase.getInstance().getReference().child("Usage");
                                                                                databaseReference.child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener()
                                                                                {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                                                                    {
                                                                                        String recordstorage=dataSnapshot.child("size").getValue(String.class);
                                                                                        usageref.child(uid).addListenerForSingleValueEvent(new ValueEventListener()
                                                                                        {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                                                                            {
                                                                                                String totalused=dataSnapshot.child("used").getValue(String.class);
                                                                                                usageref.child(uid).child("used").setValue(String.valueOf(Float.valueOf(totalused)-Float.valueOf(recordstorage)));
                                                                                            }
                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError databaseError)
                                                                                            {

                                                                                            }
                                                                                        });
                                                                                    }
                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError databaseError)
                                                                                    {

                                                                                    }
                                                                                });
                                                                                databaseReference.child(key).removeValue();
                                                                                Toast.makeText(searchactivity.this, "File Deleted", Toast.LENGTH_SHORT).show();
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
                                                                    CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(searchactivity.this)
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
                                                Intent intent = new Intent(searchactivity.this,webview.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(searchactivity.this,webviewforimage.class);
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    downloadFile(searchactivity.this,ofilename,extension,"/Aarogya E Care",downurl);
                    Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(searchactivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
    protected void onStart() {
        super.onStart();


                    FirebaseRecyclerOptions<uploadFile> options =
                            new FirebaseRecyclerOptions.Builder<uploadFile>()
                                    .setQuery(databaseReference,uploadFile.class)
                                    .build();

                    FirebaseRecyclerAdapter<uploadFile, recordviewholder> adapter=
                            new FirebaseRecyclerAdapter<uploadFile, recordviewholder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull recordviewholder holder, int position, @NonNull uploadFile model) {
                                    String key=getRef(position).getKey();
                                    String furl=model.getUrl();
                                    holder.vhfilename.setText(model.getFilename()+"."+model.getFiletype());
                                    holder.vhdate.setText(model.getDate());
                                    holder.vhrecordtype.setText(model.getRecord_type());
                                    loadingbar1.dismiss();
                                    holder.vhdropdown.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            ofilename=model.getOriginal_filename();
                                            extension="."+model.getFiletype();
                                            PopupMenu popupMenu = new PopupMenu(searchactivity.this,holder.vhdropdown);
                                            popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                    switch(item.getItemId()){
                                                        case R.id.Rename:{
                                                            final AlertDialog.Builder builder = new AlertDialog.Builder(searchactivity.this);
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
                                                                public void onClick(DialogInterface dialog, int which)
                                                                {

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
                                                                    final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(searchactivity.this);
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
                                                            storef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
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
                                                                            downloadFile(searchactivity.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
                                                                            Toast.makeText(searchactivity.this, "Download Started", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        downloadFile(searchactivity.this,ofilename,extension,"/Aarogya E Care",urlfordownload);
                                                                        Toast.makeText(searchactivity.this, "Download Started", Toast.LENGTH_SHORT).show();
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
                                                            CFAlertDialog.Builder builder1 = new CFAlertDialog.Builder(searchactivity.this)
                                                                    .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
                                                                    .setTitle("Delete File")
                                                                    .setMessage("Are you sure you want to delete?")
                                                                    .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                                                                        storef= FirebaseStorage.getInstance().getReference().child("Records/"+uid+"/"+ofilename);
                                                                        storef.delete().addOnSuccessListener(new OnSuccessListener<Void>(){
                                                                            @Override
                                                                            public void onSuccess(Void aVoid)
                                                                            {
                                                                                usageref= FirebaseDatabase.getInstance().getReference().child("Usage");
                                                                                databaseReference.child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener()
                                                                                {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                                                                    {
                                                                                        String recordstorage=dataSnapshot.child("size").getValue(String.class);
                                                                                        usageref.child(uid).addListenerForSingleValueEvent(new ValueEventListener()
                                                                                        {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                                                                            {
                                                                                                String totalused=dataSnapshot.child("used").getValue(String.class);
                                                                                                usageref.child(uid).child("used").setValue(String.valueOf(Float.valueOf(totalused)-Float.valueOf(recordstorage)));
                                                                                            }
                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError databaseError)
                                                                                            {

                                                                                            }
                                                                                        });
                                                                                    }
                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError databaseError)
                                                                                    {

                                                                                    }
                                                                                });
                                                                                databaseReference.child(key).removeValue();
                                                                                Toast.makeText(searchactivity.this, "File Deleted", Toast.LENGTH_SHORT).show();
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
                                                                    CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(searchactivity.this)
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
                                                Intent intent = new Intent(searchactivity.this,webview.class);
                                                intent.putExtra("url",furl);
                                                loadingbar1.dismiss();
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                loadingbar1.show();
                                                Intent intent = new Intent(searchactivity.this,webviewforimage.class);
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
