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

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailInput, passwordInput;
    private Button loginBtn;
    private TextView jumpToSignUp;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        emailInput = findViewById(R.id.login_user_email_input);
        passwordInput = findViewById(R.id.login_user_password_input);
        loginBtn = findViewById(R.id.login_login_button);
        jumpToSignUp = findViewById(R.id.jump_to_sign_up_page);

        jumpToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String pass = passwordInput.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){

                    dialog.setMessage("Logging");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    signInUser(email, pass);

                }else {
                    Toast.makeText(LoginActivity.this, "please fill all the credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signInUser(String email, String pass) {

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else {
                    dialog.hide();
                    Toast.makeText(LoginActivity.this, "error please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
