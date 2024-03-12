package com.example.rajasinghemovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rajasinghemovies.databinding.ActivityFireBaseBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseActivity extends AppCompatActivity {
private FirebaseAuth firebaseAuth;
private FirebaseUser user;
private ActivityFireBaseBinding binding;

private SignInClient onTapClient;
private BeginSignInRequest signInRequest;
String TAG="firebase test";

private final int GOOGLE_LOGIN=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFireBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        if(user !=null){
            Log.d(TAG, "onCreate: fire account exists"+user.getEmail()+" "+user.getUid());
        }else{
            Log.d(TAG, "onCreate: fire base account doesent exist");
        }
        binding.createPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.createUserWithEmailAndPassword(FireBaseActivity.this.binding.email.getText().toString(),FireBaseActivity.this.binding.password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    user=firebaseAuth.getCurrentUser();
                                    Toast.makeText(FireBaseActivity.this, "user account created", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onComplete: "+user.getEmail()+"  "+user.getUid());
                                }else{
                                    Log.d(TAG, "onComplete: user accoutnt not created"+task.getResult().getUser().getUid()+" "+task.getResult());
                                }
                            }
                        });
            }
        });
        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signInWithEmailAndPassword(binding.email.getText().toString(),binding.password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    user=firebaseAuth.getCurrentUser();
                                    Toast.makeText(FireBaseActivity.this, "sign in success", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onComplete: "+user.getEmail()+" "+user.getUid());
                                }else{
                                    Toast.makeText(FireBaseActivity.this, "un authorized", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
        binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Toast.makeText(FireBaseActivity.this, "user signed out", Toast.LENGTH_SHORT).show();
            }
        });
            }
        });

    }

    private void initGoogleSignIn() {
        onTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.WEB_CLIENT_ID))
                        .setFilterByAuthorizedAccounts(false)
                        .build()
        ).build();

    }

    private void googleSignIn(){
        onTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult beginSignInResult) {
                        try {
                            startIntentSenderForResult(beginSignInResult.getPendingIntent().getIntentSender(),10,null,0,0,0 );

                        }catch (IntentSender.SendIntentException e){
                            Log.e(TAG, "onSuccess: "+e.getLocalizedMessage(),e);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: begin sign in failed",  e);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GOOGLE_LOGIN:

        }
    }
}