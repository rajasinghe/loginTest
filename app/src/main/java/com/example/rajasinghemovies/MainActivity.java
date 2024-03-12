package com.example.rajasinghemovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
Button login;
final int GOOGLE_LOGIN=10;
String TAG="MainActivity";
    Intent google_sign_in_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.login);
        Button signOutButton=findViewById(R.id.sign_out);
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                        .build();

        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);

        GoogleSignInAccount googleAccount=GoogleSignIn.getLastSignedInAccount(this);

        if(googleAccount==null){
           google_sign_in_intent=googleSignInClient.getSignInIntent();
            startActivityForResult(MainActivity.this.google_sign_in_intent,GOOGLE_LOGIN);

        }else{

            Log.d(TAG, "onCreate: google sign in account exist email:"+googleAccount.getEmail());
        }
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: accocunr signed out");
                    }
                });
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                google_sign_in_intent=googleSignInClient.getSignInIntent();
                startActivityForResult(MainActivity.this.google_sign_in_intent,GOOGLE_LOGIN);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GOOGLE_LOGIN){
            Log.d(TAG, "onActivityResult: google login intiation");
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                Log.d(TAG, "onActivityResult: "+account.getEmail());
            }catch (ApiException e){
                Log.e(TAG, "onActivityResult: "+e.getStatusCode(),e);
            }
        }
    }
}