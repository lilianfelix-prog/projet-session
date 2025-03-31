package com.example.projet_session;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projet_session.auth.LoginCallback;
import com.example.projet_session.auth.LoginData;
import com.example.projet_session.auth.LoginRequest;
import com.example.projet_session.auth.LoginResponse;
import com.example.projet_session.auth.ServiceGenerator;

import retrofit2.Call;

public class CreateAccountActivity extends AppCompatActivity implements LoginCallback.LoginListener, View.OnClickListener {
    private Button registerBtn;
    private EditText emailInput;
    private EditText passwordInput;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerBtn = findViewById(R.id.register_button);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        login = findViewById(R.id.login);

        registerBtn.setOnClickListener(this);
        login.setOnClickListener(view -> {
            Intent createAccountView = new Intent(CreateAccountActivity.this, MainActivity.class);
            startActivity(createAccountView);
        });

    }

    @Override
    public void onClick(View view) {
        if (view==registerBtn) {
            login(new LoginData(emailInput.getText().toString().trim(), passwordInput.getText().toString().trim()));
        }
    }

    private void login(LoginData loginData) {
        LoginRequest loginRequest = ServiceGenerator.createService(LoginRequest.class, loginData.getEmail(), loginData.getPassword());
        Call<LoginResponse> call = loginRequest.login();
        call.enqueue(new LoginCallback(this));
    }
    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {
        System.out.println("login successful");
        Intent travelView = new Intent(this, TravelsActivity.class);
        startActivity(travelView);
    }

    @Override
    public void onLoginFailure(String errorMessage) {
        System.out.println(errorMessage);
    }

    @Override
    public void onNetworkError(Throwable t) {
        System.out.println("network error: " + t.getMessage());
    }
}
