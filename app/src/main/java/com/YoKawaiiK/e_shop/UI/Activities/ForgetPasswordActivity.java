package com.YoKawaiiK.e_shop.UI.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.YoKawaiiK.e_shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText emailTxt;
    private Button sendForgetBtn;
    private ProgressBar progressbar;
    private FirebaseAuth mfirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        emailTxt = findViewById(R.id.editTextEmailAddress);
        sendForgetBtn = findViewById(R.id.forgetBtn);
        progressbar = findViewById(R.id.progressBar);
        mfirebaseAuth = FirebaseAuth.getInstance();

        sendForgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!emailTxt.getText().toString().equals(""))
                {
                    progressbar.setVisibility(View.VISIBLE);
                    mfirebaseAuth.sendPasswordResetEmail(emailTxt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressbar.setVisibility(View.GONE);
                            if(task.isSuccessful())
                                Toast.makeText(ForgetPasswordActivity.this, "Password has been sent to your email please check your inbox", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(ForgetPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                    emailTxt.setError(getString(R.string.fpaFieldIsEmpty));
            }
        });
    }
}