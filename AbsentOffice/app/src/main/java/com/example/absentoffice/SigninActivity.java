package com.example.absentoffice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.absentoffice.ExtraUtil.getCurrDay;
import static com.example.absentoffice.ExtraUtil.getCurrMonth;
import static com.example.absentoffice.ExtraUtil.getCurrYear;

public class SigninActivity extends AppCompatActivity {
    ArrayList<Employees> employees;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mRef;
    private Button signin;
    private Button signup;
    static final int PERMISSION_READ_STATE = 123;
    EditText loginPass;

    public SigninActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("employees");
        mRef = mFirebaseDatabase.getReference().child("submitabsent");
        employees = new ArrayList<Employees>();

        signin = findViewById(R.id.btn_signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinActivity();
            }
        });

        signup = findViewById(R.id.btn_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupActivity();
            }
        });
    }

    public void signinActivity() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat SDFSignInStart = new SimpleDateFormat("HH");
        String getTimeNow = SDFSignInStart.format(calendar.getTime());
        String strictTimeSignInStart = "06";
        String strictTimeSignInEnd = "12";
        int time = Integer.parseInt(getTimeNow);
        int signInStart = Integer.parseInt(strictTimeSignInStart);
        int signInEnd = Integer.parseInt(strictTimeSignInEnd);
        System.out.println(time);
        System.out.println(signInStart);
        System.out.println(signInEnd);

        if (TextUtils.isEmpty(((EditText) findViewById(R.id.loginPass)).getText().toString())){
            Toast.makeText(SigninActivity.this, "You did not enter your password", Toast.LENGTH_SHORT).show();
        } else if (time <= signInStart || time >= signInEnd){
            Toast.makeText(SigninActivity.this, "You can't submit your absent anymore. Contact the administrator.", Toast.LENGTH_SHORT).show();
            clean();
        } else {
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(getImei())){
                        String compPass = (String) dataSnapshot.child(getImei()).child("password").getValue();
//                        final String compName = (String) dataSnapshot.child(getImei()).child("name").getValue();
                        loginPass = findViewById(R.id.loginPass);
                        String password = loginPass.getText().toString().trim();
//                        String name = (String) dataSnapshot.child(getImei()).child("name").getValue();
                        final String currDate = getCurrDay() + " " + getCurrMonth() + " " + getCurrYear();
                        if (password.equals(compPass)) {
                            mRef.child(currDate).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(getImei())){
                                        Toast.makeText(SigninActivity.this, "You already submit absent!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(SigninActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SigninActivity.this, SubmitAbsActivity.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            Toast.makeText(SigninActivity.this, "Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    public void signupActivity(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
    private String getImei() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            return manager.getDeviceId();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_PHONE_STATE
            }, PERMISSION_READ_STATE);
            return "";
        }
    }
    private void clean(){
        loginPass.setText("");
        loginPass.requestFocus();
    }
}
