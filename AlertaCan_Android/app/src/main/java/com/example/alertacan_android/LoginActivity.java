package com.example.alertacan_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private TextInputLayout emailLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordLayout;
    private TextInputEditText passwordEditText;
    private TextView forgotPasswordTextView;
    private Button loginButton;
    private ProgressBar loginProgressBar;
    private TextView signupTextView;
    private TextView privacyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(this, HomeActivity.class));
        }


        emailLayout = findViewById(R.id.emailLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordLayout = findViewById(R.id.passwordLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        loginButton = findViewById(R.id.loginButton);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        signupTextView = findViewById(R.id.signupTextView);
        privacyTextView = findViewById(R.id.privacyTextView);

        loginButton.setEnabled(false);

        emailEditText.addTextChangedListener(loginTextWatcher);
        passwordEditText.addTextChangedListener(loginTextWatcher);


    }

    public void login(View view){
        if(!validateEmail()) return;

        loginButton.setText("");
        loginButton.setEnabled(false);
        loginProgressBar.setVisibility(View.VISIBLE);


        String email = emailEditText.getText().toString().trim();
        System.out.println(email);
        String password = passwordEditText.getText().toString().trim();
        System.out.println(password);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Log.d(TAG,"Login succesful");
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));

                        } else {
                            loginButton.setText("Login");
                            loginButton.setEnabled(true);
                            loginProgressBar.setVisibility(View.GONE);

                            try{
                                throw task.getException();
                            } catch (FirebaseNetworkException e){
                                Toast.makeText(LoginActivity.this, "Check your network connection",
                                        Toast.LENGTH_SHORT).show();

                            } catch (FirebaseAuthInvalidCredentialsException e){
                                Toast.makeText(LoginActivity.this, "Invalid user or password",
                                        Toast.LENGTH_SHORT).show();

                            } catch (FirebaseAuthInvalidUserException e){
                                Toast.makeText(LoginActivity.this, "Invalid user or password",
                                        Toast.LENGTH_SHORT).show();

                            } catch (Exception e){
                                Log.e(TAG,e.getMessage());
                                Toast.makeText(LoginActivity.this, "LOGIN FAILED",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

    }


    private boolean validateEmail(){
        String emailInput = emailEditText.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            emailLayout.setError("Please Enter a valid email address");
            return false;
        }

        return true;
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            emailLayout.setError(null);

            String emailInput = emailEditText.getText().toString().trim();
            String passwordInput = passwordEditText.getText().toString().trim();

            loginButton.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}