package com.operationsmiley.aarogyaecare;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.operationsmiley.aarogyaecare.module.Usage;
import com.operationsmiley.aarogyaecare.module.Users;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotificationsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    ChipNavigationBar bottomnavigation;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView username,phonenumber,mlid, usertypeshow;
    private LinearLayout storagedetails;
    String userId, totalstorage="0",storageused="0",nooffiles="0",noofimages="0",noofpdf="0",noofreports="0",noofprescriptions="0";
    DatabaseReference myref;
    View headerView;
    String usertype;
    ImageView threedots;
    Toolbar toolbar;
    ImageView imageView,settings;
    ImageView imageView2;
    private RecyclerView recycler11;
    LinearLayoutManager layoutManager1;
    Dialog mDialog;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mRef;
    LinearLayout upgradenow;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar1;
    TextView storagepercent,storageoutof,buystorage;
    ProgressBar prog;
    int getrandom;
    List<String> reply;
    DatabaseReference usageref;
    Usage use1;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        bottomnavigation = findViewById(R.id.bottom_nav);
        bottomnavigation.setOnItemSelectedListener(navLister);
        bottomnavigation.setItemSelected(R.id.nav_notifications,true);

        onStart();

        use1=new Usage();
//      bottomnavigation.setOnNavigationItemSelectedListener(navListner);
//      bottomnavigation.setSelectedItemId(R.id.nav_notifications);
        loadingbar1= new ProgressDialog(this);
        loadingbar1.setTitle("Save your Records!");
        loadingbar1.setMessage("Please wait...");
        loadingbar1.setCanceledOnTouchOutside(false);
        reply = new ArrayList<>();
        reply.add("Wonderful!");
        reply.add("Sounds Great!");
        reply.add("Awesome!");
        reply.add("Exciting!");
        mDialog = new Dialog(this);
        mDatabase = FirebaseDatabase.getInstance();
        threedots=findViewById(R.id.menudots);
        threedots.setVisibility(View.GONE);
        mRef =mDatabase.getReference("FeedSuggestions");
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);
        mAuth=FirebaseAuth.getInstance();
        imageView2=findViewById(R.id.imageView2);
        imageView=findViewById(R.id.imageView);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.inflateHeaderView(R.layout.header);
        username = headerView.findViewById(R.id.user_name);
        phonenumber = headerView.findViewById(R.id.phone_number);
        usertypeshow = headerView.findViewById(R.id.type);
        buystorage=headerView.findViewById(R.id.buystorage);
        storagepercent=headerView.findViewById(R.id.progresspercent);
        storageoutof=headerView.findViewById(R.id.progressvalue);
        prog=(ProgressBar)headerView.findViewById(R.id.progressbarstorage);
        usageref=FirebaseDatabase.getInstance().getReference().child("Usage");
        upgradenow = headerView.findViewById(R.id.upgrade_banner);
        usageref.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                use1=dataSnapshot.getValue(Usage.class);
                assert use1 != null;
                prog.setProgress((int)((100*Float.parseFloat(use1.getUsed()))/Float.parseFloat(use1.getTotal())));
                String numberAsString = String.format ("%.2f", (100*Float.valueOf(use1.getUsed()))/Float.valueOf(use1.getTotal()));
                storagepercent.setText("Storage ("+numberAsString+"% used)");
                String numberAsString1 = String.format ("%.2f", Float.valueOf(use1.getUsed()));
                storageoutof.setText(numberAsString1+"MB used out of "+use1.getTotal()+"MB");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        upgradenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = "https://aarogyaecare.com/user/payment1.php?id=2&&email=";
//                String Email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//                loadingbar1.show();
//                Intent intent = new Intent(NotificationsActivity.this,WebViewBlogs.class);
//                intent.putExtra("url",url.concat(Email));
//                intent.putExtra("name","Payment Link for Aarogya E Care");
//                loadingbar1.dismiss();
//                startActivity(intent);
                onBackPressed();
                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(NotificationsActivity.this);
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
//                CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(NotificationsActivity.this)
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
        buystorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://aarogyaecare.com/user/payment.php"));
                startActivity(browserIntent);

            }
        });

            myref = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("Self");
            myref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users userDetails = dataSnapshot.getValue(Users.class);
                    String fname = userDetails.getFname().substring(0,1).toUpperCase()+userDetails.getFname().substring(1).toLowerCase();

                    username.setText(fname);
                    phone = userDetails.getPhone();
                    phonenumber.setText(phone);
                    usertypeshow.setText("AEC_ID : "+userDetails.getMLID());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            toolbar = findViewById(R.id.includeToll);
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawerOpen, R.string.navigation_drawerClose);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("DirectNotification");
            FirebaseRecyclerOptions<NotificationModel> options =
                    new FirebaseRecyclerOptions.Builder<NotificationModel>()
                            .setQuery(notificationRef, NotificationModel.class)
                            .build();

            recycler11 = findViewById(R.id.notification_layout);
            layoutManager1 = new LinearLayoutManager(this);
//            layoutManager1.setReverseLayout(true);
            recycler11.setLayoutManager(layoutManager1);
            recycler11.setHasFixedSize(false);


        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    recycler11.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.GONE);
                    FirebaseRecyclerAdapter<NotificationModel, NotificationViewHolder> adapter =
                            new FirebaseRecyclerAdapter<NotificationModel, NotificationViewHolder>(options) {

                                @Override
                                protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull final NotificationModel model) {

                                    if(model.getNotification_image().isEmpty())
                                    {
                                        holder.notification_image.setVisibility(View.GONE);
                                    }
                                    else {
                                        holder.notification_icon.setVisibility(View.VISIBLE);
                                        Picasso.get().load(model.getNotification_image()).into(holder.notification_image);
                                    }
                                    if(model.getNotification_icon().isEmpty())
                                    {
                                        holder.notification_icon.setVisibility(View.GONE);
                                    }
                                    else
                                        {
                                        holder.notification_icon.setVisibility(View.VISIBLE);
                                        Picasso.get().load(model.getNotification_icon()).into(holder.notification_icon);
                                    }
                                    holder.notification_name.setText(model.getNotification_name());
                                    holder.notification_desc.setText(model.getNotification_desc());
                                    holder.notification_time.setText(model.getNotification_time());
                                    String url = model.getLink();

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(!url.isEmpty())
                                            {
                                                Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                startActivity(link);
                                            }
                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                                {
                                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout2, parent, false);
                                    return new NotificationViewHolder(view);
                                }
                            };
                    adapter.startListening();
                    recycler11.setAdapter(adapter);
//                  recycler11.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            recycler11.scrollToPosition(adapter.getItemCount()-1);
//                        }
//                    });
                }
                else
                {
                    recycler11.setVisibility(View.GONE);
                    imageView2.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.nav_add_family:
                DatabaseReference usertyperef;

                usertyperef=FirebaseDatabase.getInstance().getReference().child("Users");
                usertyperef.child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usertype=dataSnapshot.child("Self").child("topup").getValue(String.class);

                        if(usertype.equals("0"))
                        {
//                            Toast.makeText(HomeActivity.this, "Please upgrade your account to access this feature!", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                            final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(NotificationsActivity.this);
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
                                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(NotificationsActivity.this);
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
                                Intent intent = new Intent(NotificationsActivity.this,addfamilymember.class);
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
//                                    Toast.makeText(NotificationsActivity.this, "Please Enter Subject", Toast.LENGTH_SHORT).show();
//                                }
//                                else if (feedback.isEmpty())
//                                {
//                                    Toast.makeText(NotificationsActivity.this, "Feedback can't be Empty!", Toast.LENGTH_SHORT).show();
//                                }
//                                else {
//                                    final int[] aman = {1};
//                                    Query query = myref.child(userid).orderByChild("uid");
//                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for (DataSnapshot delSnapshot: dataSnapshot.getChildren()) {
//                                                AdImageSliderModel model = delSnapshot.getValue(AdImageSliderModel.class);
//                                                    if (model.getTimestamp().contains(timecheck)) {
//                                                        aman[0] = 0;
//                                                        Toast.makeText(NotificationsActivity.this, "You can send Feedback once a Day!", Toast.LENGTH_LONG).show();
//                                                        flatDialog.dismiss();
//                                                    }
//                                                }
//                                            if(aman[0] == 1){
//                                                    myref.child(userid).child(timestamp).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            flatDialog.dismiss();
//                                                            Toast.makeText(NotificationsActivity.this, "Feedback Submitted", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
//                                                }
//                                            }
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
                        .setMessage("Logout from Aarogya E care?")
                        .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            mAuth.signOut();

                            // Google sign out
                            mGoogleSignInClient.signOut();
                            Intent intent = new Intent(NotificationsActivity.this,Login.class);
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

    private ChipNavigationBar.OnItemSelectedListener navLister = new ChipNavigationBar.OnItemSelectedListener() {
        @Override
        public void onItemSelected(int i) {
            switch (i){
                case R.id.nav_home :
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    overridePendingTransition(0,0);
                    break;

                case R.id.nav_records :
                    startActivity(new Intent(getApplicationContext(),ViewAllFiles.class));
//                    Intent intent = new Intent(NotificationsActivity.this, ViewAllFiles.class);
//                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;

                case R.id.nav_blogs :
                     startActivity(new Intent(getApplicationContext(), BlogsActivity.class));
                     overridePendingTransition(0,0);
                     break;

                case R.id.nav_notifications :
                    break;
            }
        }
    };

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
//                            startActivity(new Intent(getApplicationContext(),ViewAllFiles.class));
//                            Intent intent = new Intent(NotificationsActivity.this, ViewAllFiles.class);
//                            startActivity(intent);
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
//                            return true;
//
//                    }
//                    return false;
//                }
//            };

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            startActivity(new Intent(NotificationsActivity.this,HomeActivity.class));
            overridePendingTransition(0,0);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser checkUser = FirebaseAuth.getInstance().getCurrentUser();
        if(checkUser == null)
        {
            Intent loginIntent = new Intent(NotificationsActivity.this, Login.class);
            startActivity(loginIntent);
            finish();
        }
    }
}
