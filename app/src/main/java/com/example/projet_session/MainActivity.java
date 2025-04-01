package com.example.projet_session;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
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
import com.example.projet_session.travels.TravelsActivity;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements LoginCallback.LoginListener, View.OnClickListener {

    private Button loginBtn;
    private EditText emailInput;
    private EditText passwordInput;
    private TextView createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginBtn = findViewById(R.id.login_button);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        createAccount = findViewById(R.id.create_account);

        loginBtn.setOnClickListener(this);
        createAccount.setOnClickListener(view -> {
            Intent createAccountView = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(createAccountView);
        });

    }

    @Override
    public void onClick(View view) {
        if (view==loginBtn) {
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
        System.out.println("login successful: " + loginResponse.getToken());
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