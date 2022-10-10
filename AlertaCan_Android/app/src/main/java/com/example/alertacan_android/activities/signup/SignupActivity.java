package com.example.alertacan_android.activities.signup;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alertacan_android.activities.home.HomeActivity;
import com.example.alertacan_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private FirebaseAuth mAuth;
    private TextInputLayout emailLayout;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordLayout;
    private TextInputEditText passwordEditText;
    private TextInputLayout repeatPasswordLayout;
    private TextInputEditText repeatPasswordEditText;
    private Button signupButton;
    private ProgressBar signupProgressBar;
    private TextView loginTextView;
    private TextView privacyTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        emailLayout = findViewById(R.id.emailLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordLayout = findViewById(R.id.passwordLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        repeatPasswordLayout = findViewById(R.id.repeatPasswordLayout);
        repeatPasswordEditText = findViewById(R.id.repeatPasswordEditText);
        signupButton = findViewById(R.id.signupButton);
        signupProgressBar = findViewById(R.id.signupProgressBar);
        loginTextView = findViewById(R.id.loginTextView);
        privacyTextView = findViewById(R.id.privacyTextView);

        signupButton.setEnabled(false);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(view);
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        emailEditText.addTextChangedListener(signupTextWatcher);
        passwordEditText.addTextChangedListener(signupTextWatcher);
        repeatPasswordEditText.addTextChangedListener(signupTextWatcher);

    }

    private void signup(View view){
        if(!validateEmail()) return;

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String repeatPassword = repeatPasswordEditText.getText().toString().trim();

        System.out.println(password);
        System.out.println(repeatPassword);
        if(!repeatPassword.equals(password)){
            repeatPasswordLayout.setError("Las contraseñas no coinciden");
            return;
        }

        signupButton.setText("");
        signupButton.setEnabled(false);
        signupProgressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Log.d(TAG, "Signup succesful");

                    Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    signupButton.setText("Crear cuenta");
                    signupButton.setEnabled(true);
                    signupProgressBar.setVisibility(View.GONE);

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e){
                        emailLayout.setError("El correo ya está en uso");
                    } catch (FirebaseAuthWeakPasswordException e){
                        passwordLayout.setError("La contraseña debe tener al menos 8 caracteres");
                    } catch (FirebaseNetworkException e){
                        Toast.makeText(SignupActivity.this, "Comprueba tu conexión a internet",
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e(TAG,e.getMessage(),e);

                        Toast.makeText(SignupActivity.this, "Error, check console",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });




    }

    private boolean validateEmail(){
        String emailInput = emailEditText.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            emailLayout.setError("Please enter a valid email address");
            return false;
        }

        return true;
    }

    private TextWatcher signupTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            emailLayout.setError(null);
            passwordLayout.setError(null);
            repeatPasswordLayout.setError(null);

            String emailInput = emailEditText.getText().toString().trim();
            String passwordInput = passwordEditText.getText().toString().trim();
            String repeatPasswordInput = repeatPasswordEditText.getText().toString().trim();

            signupButton.setEnabled(!emailInput.isEmpty()
                    && !passwordInput.isEmpty()
                    && !repeatPasswordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}
