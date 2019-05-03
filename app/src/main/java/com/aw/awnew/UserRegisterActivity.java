package com.aw.awnew;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegisterActivity extends AppCompatActivity {

    //Variables
    private Button buttonRegister;
    private EditText editTextRegisterPassword, editTextRegisterEmail, editTextRegisterPassword2, editTextUsername;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        InitalizeFields();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    //Accept user registry inputs, and create new account in the database.
    private void CreateAccount() {
        String email = editTextRegisterEmail.getText().toString();
        String password = editTextRegisterPassword.getText().toString();
        String password2 = editTextRegisterPassword2.getText().toString();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email...", Toast.LENGTH_SHORT);
        } else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT);
        } else if(!password.equals(password2)) {
            Toast.makeText(this, "Passwords do not match...", Toast.LENGTH_LONG);
        } else {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait, while new account is being created...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                String currentUserID = mAuth.getCurrentUser().getUid();
                                rootRef.child("Users").child(currentUserID).setValue("");
                                SendUserToSearchActivity();
                                Toast.makeText(UserRegisterActivity.this, "Account Created Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(UserRegisterActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    //Initialize all the editable widgets.
    private void InitalizeFields() {
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextRegisterPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextRegisterEmail = (EditText) findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        loadingBar = new ProgressDialog(this);
    }

    //Create intent for another activity.
    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(UserRegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    private void SendUserToSearchActivity() {
        Intent searchIntent = new Intent(UserRegisterActivity.this, SearchActivity.class);
        searchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(searchIntent);
        finish();
    }
}
