package com.example.absentoffice;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 123;
    private static Activity caller;
    public static ArrayList<SubmitAbsent> mAbsents;
    public static ArrayList<Employees> mEmployees;

    private FirebaseUtil(){};

    public static void openFbReference(String ref, final Activity callerActivity){
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            if (ref == "submitabsent"){
                mAbsents = new ArrayList<SubmitAbsent>();
            } else if (ref == "employees"){
                mEmployees = new ArrayList<Employees>();
            }
        }
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }
}
