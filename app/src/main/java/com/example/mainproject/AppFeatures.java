package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AppFeatures extends AppCompatActivity {
    FirebaseAuth mAuth;
    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_features);
        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));

        mainGrid=findViewById(R.id.mainGrid);

        setSingleEvent(mainGrid);
    }


    private void setSingleEvent(GridLayout mainGrid) {

        for(int i=0;i<mainGrid.getChildCount();i++){

            CardView cardView= (CardView) mainGrid.getChildAt(i);
            final int finalI= i;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(finalI==0){
                        Intent intent= new Intent( AppFeatures.this, Search_Image.class);
                        startActivity(intent);
                    }else if(finalI==1){
                        //Toast.makeText(MainActivity.this, "Videos is clicked", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(AppFeatures.this,Map.class);
                        startActivity(intent);

                    }else if(finalI==2){
                        Intent intent= new Intent(AppFeatures.this,  Date_Time.class);
                        startActivity(intent);

                    }else if(finalI==3){
                       // Toast.makeText(AppFeatures.this, "Map is clicked", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(AppFeatures.this,  Videos.class);
                        startActivity(intent);
                    }
                    else if(finalI==4){
                       // Toast.makeText(AppFeatures.this, "Here you can comment", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(AppFeatures.this, dashboard.class);
                        startActivity(intent);

                    }
                    else if(finalI==5) {
                        //Toast.makeText(AppFeatures.this, "Here you can see your profile.", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(AppFeatures.this,  Profile.class);
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(AppFeatures.this, "Nothing is Clicked", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //Menu



    //Menu Bar
    /* menuAdd menuObj= new menuAdd();
    menuObj.menu();
    //MenuSteup.SetMenu();
*/
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.reviewId){
            //Toast.makeText(this, "Feedback is selected.", Toast.LENGTH_SHORT).show();
            //openActivity();
            Intent intent=new Intent(this, ReviewPage.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId()==R.id.aboutusId){
            Intent intent=new Intent(this, AboutUs.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId()==R.id.logoutId){
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}