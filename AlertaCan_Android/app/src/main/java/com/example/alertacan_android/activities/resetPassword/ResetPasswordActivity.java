package com.example.alertacan_android.activities.resetPassword;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.alertacan_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ResetPasswordActivity";

    private FirebaseAuth mAuth;

    private MaterialToolbar appbar;
    private TextInputLayout emailLayout;
    private TextInputEditText emailInputEditText;
    private ConstraintLayout resetPasswordButtonLayout;
    private ProgressBar resetPasswordProgressBar;
    private Button resetPasswordButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
        appbar = findViewById(R.id.topAppBar);
        emailLayout = findViewById(R.id.emailLayout);
        emailInputEditText = findViewById(R.id.emailEditText);
        resetPasswordButtonLayout = findViewById(R.id.resetPasswordButtonLayout);
        resetPasswordProgressBar = findViewById(R.id.resetPasswordProgressBar);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        resetPasswordButton.setEnabled(false);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword(view);
            }
        });
        emailInputEditText.addTextChangedListener(resetPasswordTextWatcher);

    }

    private void resetPassword(View view){
        if(!validateEmail()) return;

        resetPasswordButton.setText("");
        resetPasswordButton.setEnabled(false);
        resetPasswordProgressBar.setVisibility(View.VISIBLE);

        String email = emailInputEditText.getText().toString().trim();

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        resetPasswordButton.setText("Reestablecer contraseña");
                        resetPasswordButton.setEnabled(true);
                        resetPasswordProgressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()){
                            Log.d(TAG,"Email sent succesfully");

                            Toast.makeText(ResetPasswordActivity.this, "Revisa tu buzón de correo",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            try{
                                throw task.getException();
                            } catch (FirebaseNetworkException e){
                                Toast.makeText(ResetPasswordActivity.this, "Comprueba tu conexión a internet",
                                        Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthInvalidUserException e){
                                emailLayout.setError("No existe una cuenta asociada a este correo");
                            } catch (Exception e){
                                Log.e(TAG,e.getMessage(),e);
                                Toast.makeText(ResetPasswordActivity.this, "RESET PASSWORD FAILED",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });


    }

    private boolean validateEmail(){
        String emailInput = emailInputEditText.getText().toString().trim();

        if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            emailLayout.setError("Introduce un correo electrónico válido");
            return false;
        }

        return true;
    }


    private TextWatcher resetPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            emailLayout.setError(null);

            String emailInput = emailInputEditText.getText().toString().trim();

            resetPasswordButton.setEnabled(!emailInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
