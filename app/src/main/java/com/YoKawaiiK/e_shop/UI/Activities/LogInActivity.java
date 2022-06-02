package com.YoKawaiiK.e_shop.UI.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.YoKawaiiK.e_shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    TextInputEditText mPassword;
    EditText mEmail;
    Button mlogin;
    Button mCreateBtn;
    TextView mforgerpassword, tvLogin;
    FirebaseAuth fauth;
    ProgressBar mprogresspar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.EmailLogin);
        mPassword = findViewById(R.id.PasswordLogin);
        fauth = FirebaseAuth.getInstance();
        tvLogin = findViewById(R.id.tvLogin);
        mlogin = (Button) findViewById(R.id.Login);
        mCreateBtn = (Button) findViewById(R.id.SignUpButton);
        mprogresspar = (ProgressBar) findViewById(R.id.progressBar1);
        mforgerpassword = (TextView) findViewById(R.id.ForgetPassword);
        // Checking if the User is logging in or log out ! ;
        if (fauth.getCurrentUser() != null) {
            if (fauth.getCurrentUser().getEmail().equals(getString(R.string.constantsAdminBaseEmail))) {
                startActivity(new Intent(LogInActivity.this, AdminActivity.class));
                finish();
            } else {
                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                finish();
            }

        }

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Email = mEmail.getText().toString().trim();
                final String Password = mPassword.getText().toString();
                if (TextUtils.isEmpty(Email)) {
                    mEmail.setError(getString(R.string.laEmailValidationRequired));
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    mPassword.setError(getString(R.string.laPasswordValidationRequired));
                    return;
                }
                if (Password.length() < 6) {
                    mPassword.setError(getString(R.string.laPasswordValidationMustBeBiggerLength));
                    return;
                }
                // progress in background and i make it here visible.
                mprogresspar.setVisibility(View.VISIBLE);

                // Authenticate
                fauth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (Email.equals(getString(R.string.constantsAdminBaseEmail)) && Password.equals(getString(R.string.constantsAdminBasePassword))) {
                                Toast.makeText(LogInActivity.this, R.string.laToastCreator, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LogInActivity.this, AdminActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LogInActivity.this, R.string.laToastBase, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LogInActivity.this, MainActivity.class));
                                finish();
                            }
                        } else {
                            Toast.makeText(LogInActivity.this, R.string.laToastError, Toast.LENGTH_SHORT).show();
                            mprogresspar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(tvLogin, "tvLogin");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LogInActivity.this, pairs);
                startActivity(intent, activityOptions.toBundle());
            }
        });

        mforgerpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here we will send a verification message
                startActivity(new Intent(LogInActivity.this, ForgetPasswordActivity.class));
            }
        });

    }


}