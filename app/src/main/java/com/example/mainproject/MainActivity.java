package com.example.mainproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signInEmailId,signInPasswordId;
    private Button signInButtonID;
    private TextView signUpTextViewId;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        this.setTitle(" FASHION  PRODUCT & CLASSIFICATION");
        mAuth=FirebaseAuth.getInstance();

        signInEmailId=findViewById(R.id.signInEmailId);
        signInPasswordId=findViewById(R.id.signInPasswordId);
        signInButtonID=findViewById(R.id.signInButtonID);
        signUpTextViewId=findViewById(R.id.signUpTextViewId);
        progressBar=findViewById(R.id.progressbarId);


        signInButtonID.setOnClickListener(this);
        signUpTextViewId.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signInButtonID:
                userLogin();

                break;
            case R.id.signUpTextViewId:
                Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);

                break;

        }

    }

    private void userLogin() {
        String email=signInEmailId.getText().toString().trim();
        String password=signInPasswordId.getText().toString().trim();

        if(email.isEmpty())
        {
            signInEmailId.setError("Enter an email address");
            signInEmailId.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signInEmailId.setError("Enter a valid email address");
            signInEmailId.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            signInPasswordId.setError("Enter a password");
            signInPasswordId.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            signInPasswordId.setError("Minimum length of a password should be 6");
            signInPasswordId.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful())
                {
                    FirebaseUser firebaseUser=mAuth.getCurrentUser();
                    finish();
                    Intent intent=new Intent(getApplicationContext(),AppFeatures.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Login Unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}