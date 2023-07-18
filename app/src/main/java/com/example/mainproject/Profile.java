package com.example.mainproject;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class Profile extends AppCompatActivity {
    private TextView textViewWelcome,textViewFullName,textViewEmail,textviewDoB,textViewGender,textViewMobile;
    private ProgressBar progressBar;
    private  String fullName,email,doB,gender,mobile;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference reference;
    private FirebaseUser user;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        this.setTitle("Profile");


        user=FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Registered Users");
        userID = user.getUid();

        final TextView textViewWelcome=(TextView) findViewById(R.id.textView_show_welcome);
        final TextView textViewFullName=(TextView) findViewById(R.id.textView_show_full_name);
        final TextView textViewEmail=(TextView) findViewById(R.id.textView_show_email);
        final TextView textviewDoB=(TextView) findViewById(R.id.textView_show_dob);
        final TextView textViewGender=(TextView) findViewById(R.id.textView_show_gender);
        final TextView textViewMobile=(TextView)findViewById(R.id.textView_show_mobile);





        //set onclicklistener on imageView to open uploadprofilepicactivity
        imageView=findViewById(R.id.imageView_profile_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile.this,UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });



        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails userProfile=snapshot.getValue(ReadWriteUserDetails.class);

                if(userProfile !=null){
                   String fullName=userProfile.fullName;
                    String email=userProfile.email();
                    String doB =userProfile.doB;
                    String  gender=userProfile.gender;
                    String mobile=userProfile.mobile;


                    textViewWelcome.setText("Welcome "+fullName);
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textviewDoB.setText(doB);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);


                    /*set up Dp(after user has  uploaded
                    Uri uri=user.getPhotoUrl();
                    imageView setImageURI() should not used with reguar IUR.so we are useing Picaso
                    Picasso.get().load(uri).into(imageView);*/

                   /* String image=snapshot.getValue(String.class);
                    Picasso.get()
                            .load(image)
                            .into(imageView);*/

                }
               /* else {
                    Toast.makeText(Profile.this, "some thing happened", Toast.LENGTH_SHORT).show();

                }
                progressBar.setVisibility((View.GONE));*/


            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "some thing happened", Toast.LENGTH_SHORT).show();

            }
        });
        }





    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.menu_refresh){
            //Toast.makeText(this, "Feedback is selected.", Toast.LENGTH_SHORT).show();
            //openActivity();
            Intent intent=new Intent(this, Profile.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId()==R.id.menu_rofilePic){
            //Toast.makeText(this, "Feedback is selected.", Toast.LENGTH_SHORT).show();
            //openActivity();
            Intent intent=new Intent(this,ProfileUpdate.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}