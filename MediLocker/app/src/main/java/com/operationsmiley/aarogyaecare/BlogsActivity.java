package com.operationsmiley.aarogyaecare;

import android.app.Dialog;
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

public class BlogsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

//    BottomNavigationView bottomnavigation;
    ChipNavigationBar bottomnavigation;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    private FirebaseAuth mAuth;
    DatabaseReference adRef;
    TextView username,phonenumber,mlid,usertypeshow,buystorage;
    String userId;
    RecyclerView recycler11;
    RecyclerView.LayoutManager layoutManager1;
    DatabaseReference myref;
    View headerView;
    private LinearLayout storagedetails;
    String usertype,totalstorage="0",storageused="0",nooffiles="0",noofimages="0",noofpdf="0",noofreports="0",noofprescriptions="0";
    ImageView settings;
    Toolbar toolbar;
    LinearLayout upgradenow;
    ImageView threedots;
    Dialog mDialog;
    List<String> reply;

    public FirebaseDatabase mDatabase;
    public DatabaseReference mRef;
    TextView storagepercent,storageoutof;
    ProgressBar prog;
    DatabaseReference usageref;
    Usage use1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs);

        bottomnavigation = findViewById(R.id.bottom_nav);

//        bottomnavigation.setOnNavigationItemSelectedListener(navListner);
//        bottomnavigation.setSelectedItemId(R.id.nav_track);
        bottomnavigation.setOnItemSelectedListener(navLister);
        bottomnavigation.setItemSelected(R.id.nav_blogs,true);
        use1=new Usage();
        reply = new ArrayList<>();
        reply.add("Wonderful!");
        reply.add("Sounds Great!");
        reply.add("Awesome!");
        reply.add("Exciting!");
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.inflateHeaderView(R.layout.header);
//        settings = headerView.findViewById(R.id.setting);
        upgradenow = headerView.findViewById(R.id.upgrade_banner);
        buystorage=headerView.findViewById(R.id.buystorage);

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
//                CFAlertDialog.Builder builder5 = new CFAlertDialog.Builder(BlogsActivity.this)
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

        threedots=findViewById(R.id.menudots);
        threedots.setVisibility(View.GONE);
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                Intent intent03 = new Intent(BlogsActivity.this,SettingActivity.class);
//                startActivity(intent03);
//            }
//        });
        upgradenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(BlogsActivity.this);
                Random random = new Random();
                flatDialog.setTitle("Coming Soon!")
                        .setTitleColor(getResources().getColor(R.color.colorPrimaryDark))
                        .setBackgroundColor(Color.parseColor("#ffffff"))
                        .setIcon(R.drawable.crown2)
                        .setSubtitle("Premium Users will get special access to the Premium Features and Services on Aarogya E Care App.")
                        .setSubtitleColor(getResources().getColor(android.R.color.black))
                        .setFirstButtonText(reply.get(random.nextInt(4)))
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
        mDialog = new Dialog(this);
        userId = FirebaseAuth.getInstance().getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mRef =mDatabase.getReference("FeedSuggestions");
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mAuth=FirebaseAuth.getInstance();
        mGoogleSignInClient= GoogleSignIn.getClient(this, gso);


//                                  Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.includeToll);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(0).setChecked(true);


        usertypeshow = headerView.findViewById(R.id.type);
        username = headerView.findViewById(R.id.user_name);
        phonenumber = headerView.findViewById(R.id.phone_number);
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


        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawerOpen, R.string.navigation_drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

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
                    break;

                case R.id.nav_notifications :
                    startActivity(new Intent(getApplicationContext(),NotificationsActivity.class));
                    overridePendingTransition(0,0);
                    break;
            }
        }
    };
//
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
//                            Intent intent = new Intent(TrackActivity.this, ViewAllFiles.class);
//                            startActivity(intent);
//                            overridePendingTransition(0,0);
//                            return true;
//
//
////                        case R.id.nav_track :
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

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
                            onBackPressed();
                            final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(BlogsActivity.this);
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
                                final com.operationsmiley.aarogyaecare.FlatDialog flatDialog = new com.operationsmiley.aarogyaecare.FlatDialog(BlogsActivity.this);
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
                                Intent intent = new Intent(BlogsActivity.this,addfamilymember.class);
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
//
//                Intent intent01 = new Intent(this,AppInfoActivity.class);
//                startActivity(intent01);
//
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
//                                    Toast.makeText(BlogsActivity.this, "Please Enter Subject", Toast.LENGTH_SHORT).show();
//                                }
//                                else if (feedback.isEmpty())
//                                {
//                                    Toast.makeText(BlogsActivity.this, "Feedback can't be Empty!", Toast.LENGTH_SHORT).show();
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
//                                                    Toast.makeText(BlogsActivity.this, "You can send Feedback once a Day!", Toast.LENGTH_LONG).show();
//                                                    flatDialog.dismiss();
//                                                }
//                                            }
//                                            if(aman[0] == 1){
//                                                myref.child(userid).child(timestamp).updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        flatDialog.dismiss();
//                                                        Toast.makeText(BlogsActivity.this, "Feedback Submitted", Toast.LENGTH_SHORT).show();
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
                        .setMessage("Logout from Aarogya E care?")
                        .addButton("Yes", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                            mAuth.signOut();

                            // Google sign out
                            mGoogleSignInClient.signOut();
                            Intent intent = new Intent(BlogsActivity.this,Login.class);
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

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            overridePendingTransition(0,0);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser checkUser = FirebaseAuth.getInstance().getCurrentUser();
        if(checkUser == null)
        {
            Intent loginIntent = new Intent(BlogsActivity.this, Login.class);
            startActivity(loginIntent);
        }

        //Advertisement Recycler View
        adRef = FirebaseDatabase.getInstance().getReference().child("AdBanner");
        FirebaseRecyclerOptions<Advertisement> options =
                new FirebaseRecyclerOptions.Builder<Advertisement>()
                        .setQuery(adRef, Advertisement.class)
                        .build();
        recycler11 = findViewById(R.id.recycler_view_advertisement);
        layoutManager1 = new LinearLayoutManager(this);
        recycler11.setLayoutManager(layoutManager1);
        recycler11.setHasFixedSize(false);

        FirebaseRecyclerAdapter<Advertisement, AdViewHolder> adapter =
                new FirebaseRecyclerAdapter<Advertisement, AdViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull AdViewHolder holder, int position, @NonNull final Advertisement model) {
                        holder.ad_header.setText(model.getAd_header());
                        holder.ad_hashtag.setText(model.getAd_hashtag());
                        Picasso.get().load(model.getAd_image()).into(holder.ad_image);
                        String url = model.getAd_url();
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!url.isEmpty())
                                {
                                    Intent intent = new Intent(BlogsActivity.this,WebViewBlogs.class);
                                    intent.putExtra("url",url);
                                    intent.putExtra("name",model.getAd_header());
                                    startActivity(intent);
                                }
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item_layout, parent, false);
                        AdViewHolder holder = new AdViewHolder(view);
                        return holder;
                    }
                };
        adapter.startListening();
        recycler11.setAdapter(adapter);

    }
}
