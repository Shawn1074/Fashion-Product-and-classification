package com.example.mainproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mainproject.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signUpEmailId,signUpPasswordId,confirm_PasswordId,editTextRegisterFullName,editTextRegisterDoB, editTextRegisterMobile;
    private Button signUpButtonId;
    private TextView signInTextViewId;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private DatePickerDialog picker;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;
    ActivityMainBinding binding;
    FirebaseDatabase db;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appBarColor)));
        //this.setTitle("Sign Up Page");

        mAuth = FirebaseAuth.getInstance();


        signUpEmailId = findViewById(R.id.signUpEmailId);
        signUpPasswordId = findViewById(R.id.signUpPasswordId);
        confirm_PasswordId=findViewById(R.id.confirm_PasswordId);

        editTextRegisterFullName = findViewById(R.id.fullNameId);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();
        signUpButtonId = findViewById(R.id.signUpButtonId);
        signInTextViewId = findViewById(R.id.signInTextViewId);
        progressbar = findViewById(R.id.progressbarId);

        signUpButtonId.setOnClickListener(this);
        signInTextViewId.setOnClickListener(this);


        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date Picker Dialog
                picker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        editTextRegisterDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signUpButtonId:
                userResister();
                break;
            case R.id.signInTextViewId:
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

                break;

        }

    }


    private void userResister() {
        //obtain the entered data
        String fullName =editTextRegisterFullName.getText().toString().trim();
        String doB = editTextRegisterDoB.getText().toString();
        String mobile = editTextRegisterMobile.getText().toString();

        int selectGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
        radioButtonRegisterGenderSelected = findViewById(selectGenderId);
        String gender;     //can'T get the value if gender is not selected
        gender = radioButtonRegisterGenderSelected.getText().toString();

        String email=signUpEmailId.getText().toString().trim();
        String password=signUpPasswordId.getText().toString().trim();
        String confirmPassword = confirm_PasswordId.getText().toString();



          /*  if (!Fullname.isEmpty() && !DoB.isEmpty() && !Gender.isEmpty() && !Mobile.isEmpty())

            {

                ReadWriteUserDetails users = new ReadWriteUserDetails(Fullname,DoB,Gender,Mobile);
                db = FirebaseDatabase.getInstance();
                reference = db.getReference("ReadWriteUserDetails");

            }

          */

        if(fullName.isEmpty())
        {
            editTextRegisterFullName.setError("Enter your Full Name");
            editTextRegisterFullName.requestFocus();
            return;
        }

        if(doB.isEmpty())
        {
            editTextRegisterDoB.setError("Enter your Date of Birth");
            editTextRegisterDoB.requestFocus();
            return;
        }

        if(gender.isEmpty())
        {
            radioButtonRegisterGenderSelected.setError("Enter your Gender");
            radioButtonRegisterGenderSelected.requestFocus();
            return;
        }

        if(mobile.isEmpty())
        {
            editTextRegisterMobile.setError("Enter your mobile number");
            editTextRegisterMobile.requestFocus();
            return;
        }
       if(mobile.length()!=11)
        {
            editTextRegisterMobile.setError("Mobile number should be 11 digi");
            editTextRegisterMobile.requestFocus();
            return;
        }
       /*  if (!mobile.equals(11)) {
            editTextRegisterMobile.setError("Mobile number should be 11 digit");
            editTextRegisterMobile.requestFocus();
            return;
        }*/


        if(email.isEmpty())
        {
            signUpEmailId.setError("Enter an email address");
            signUpEmailId.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signUpEmailId.setError("Enter a valid email address");
            signUpEmailId.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            signUpPasswordId.setError("Enter a password");
            signUpPasswordId.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            signUpPasswordId.setError("Minimum length of a password should be 6");
            signUpPasswordId.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirm_PasswordId.setError("Passwords do not match");
            confirm_PasswordId.requestFocus();
            return;
        }




                progressbar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressbar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    //Enter user data into firevbase.
                                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(fullName,email,doB,gender,mobile);


                                    //User Reference from database for Register users
                                    // DatabaseReference referenceProfile =
                                    FirebaseDatabase.getInstance().getReference("Registered Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(),"User Register is successful",Toast.LENGTH_SHORT).show();
                                                        progressbar.setVisibility(View.GONE);

                                     /*   //open user profile after successful registration
                                        Intent intent=new Intent();
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        //finish();*/

                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(),"Failed to register ,Try again!",Toast.LENGTH_SHORT).show();
                                                        progressbar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });





                                } else {
                                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                    {
                                        Toast.makeText(getApplicationContext(),"User is already registered",Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Error:"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        });
            }
        }