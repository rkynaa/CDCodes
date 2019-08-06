package com.example.absentoffice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.absentoffice.ExtraUtil.getCurrDay;
import static com.example.absentoffice.ExtraUtil.getCurrMonth;
import static com.example.absentoffice.ExtraUtil.getCurrYear;
import static com.example.absentoffice.SignupActivity.PERMISSION_READ_STATE;

public class SubmitAbsActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mRef;

    private FusedLocationProviderClient mFusedClient;

    Button submitBtn;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitabs);
        mRef = FirebaseDatabase.getInstance().getReference().child("employees");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("submitabsent");
        mFusedClient = LocationServices.getFusedLocationProviderClient(this);

        requestPermission();

        submitBtn = (Button) findViewById(R.id.btn_submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String currDate = getCurrDay() + " " + getCurrMonth() + " " + getCurrYear();
                        System.out.println(currDate);
                        final String trackName = (String) dataSnapshot.child(currDate).child("name").getValue();
                        submitAbsent();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void submitAbsent() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SubmitAbsActivity.this);
        builder.setTitle("Submit absent");
        builder.setMessage("Do you want to submit now?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSubmitAbsent();
                Toast.makeText(SubmitAbsActivity.this, "Submission successful!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveSubmitAbsent() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd MMM yyyy");
        final String date = mdformat.format(calendar.getTime());
        SimpleDateFormat HourFormat = new SimpleDateFormat("HH:mm:ss");
        final String hour = HourFormat.format(calendar.getTime());


        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String name = (String) dataSnapshot.child(getImei()).child("name").getValue();
                System.out.println(name);
                if (ActivityCompat.checkSelfPermission(SubmitAbsActivity.this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedClient.getLastLocation().addOnSuccessListener(SubmitAbsActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null){
                            SubmitAbsent absent = new SubmitAbsent(name, date, hour, Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
                            mDatabaseReference.child(date + "/" + getImei()).setValue(absent);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
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
}
