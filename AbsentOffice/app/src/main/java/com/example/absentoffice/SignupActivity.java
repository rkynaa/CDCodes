package com.example.absentoffice;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    static final int PERMISSION_READ_STATE = 123;

    EditText signupName;
    EditText signupOccp;
    EditText signupEmail;
    EditText signupPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("employees");
        signupName = findViewById(R.id.signupName);
        signupOccp = findViewById(R.id.signupOccup);
        signupEmail = findViewById(R.id.signupID);
        signupPass = findViewById(R.id.signupPass);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signup_menu:
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(getImei())){
                            Toast.makeText(SignupActivity.this, "Data Already Exist!", Toast.LENGTH_LONG).show();
                        } else {
                            saveEmployee();
                            Toast.makeText(SignupActivity.this, "Sign Up Succeed!", Toast.LENGTH_LONG).show();
                        }
                        clean();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.signup_menu, menu);
        return true;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_READ_STATE:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    }
//                    String ImeiNumber = manager.getDeviceId();
//                    String phoneNumber = manager.getLine1Number();
                } else {
                    Toast.makeText(this, "You don't have required permission to make action", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void saveEmployee() {
        final String imei = getImei();
        String name = signupName.getText().toString();
        String occp = signupOccp.getText().toString();
        String email = signupEmail.getText().toString();
        String pass = signupPass.getText().toString();
        Employees employees = new Employees(name, occp, email, pass, "", imei);
        mDatabaseReference.child(imei).setValue(employees);
    }
    private void clean(){
        signupName.setText("");
        signupOccp.setText("");
        signupEmail.setText("");
        signupPass.setText("");
        signupName.requestFocus();
    }
}
