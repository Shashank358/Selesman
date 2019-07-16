package com.agdevelopment.selesman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText firstName, emailId, password, lastName;
    private Button createAccBtn;
    private TextView jumpToLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private String currentUserId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();


        firstName = findViewById(R.id.sign_up_first_name);
        lastName = findViewById(R.id.sign_up_last_name);
        password = findViewById(R.id.sign_up_password);
        emailId = findViewById(R.id.sign_up_email_id);
        createAccBtn = findViewById(R.id.sign_up_create_account_btn);
        jumpToLogin = findViewById(R.id.jump_to_login_page);

        jumpToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String first_name = firstName.getText().toString();
                String last_name = lastName.getText().toString();
                String email = emailId.getText().toString();
                String pass = password.getText().toString();

                if (!TextUtils.isEmpty(first_name) && !TextUtils.isEmpty(email) &&
                        !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(last_name)){
                    dialog.setMessage("Creating account...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    creatingAccount(email, pass, first_name, last_name);

                }else {
                    Toast.makeText(SignUpActivity.this, "please fill all the credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void creatingAccount(final String email, String pass, final String first_name, final String last_name) {

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    currentUserId = mAuth.getCurrentUser().getUid();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("first_name", first_name);
                    hashMap.put("profile_pic", "default");
                    hashMap.put("user_email", email);
                    hashMap.put("last_name", last_name);
                    hashMap.put("uId", currentUserId);


                    db.collection("Salesman").document(currentUserId).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                dialog.dismiss();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else {
                                dialog.hide();
                                Toast.makeText(SignUpActivity.this, "something is wrong please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
