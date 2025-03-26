package com.example.projet_session;

import android.os.Bundle;

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

public class MainActivity extends AppCompatActivity implements LoginCallback.LoginListener{

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
        login(new LoginData("alice@gmail.com", "alice123"));
    }

    private void login(LoginData loginData) {
        LoginRequest loginRequest = ServiceGenerator.createService(LoginRequest.class, loginData.getEmail(), loginData.getPassword());
        Call<LoginResponse> call = loginRequest.login();
        call.enqueue(new LoginCallback(this));
    }
    @Override
    public void onLoginSuccess(LoginResponse loginResponse) {
        System.out.println("login successful");
    }

    @Override
    public void onLoginFailure(String errorMessage) {
        System.out.println("login failed: " + errorMessage );
    }

    @Override
    public void onNetworkError(Throwable t) {
        System.out.println("network error: " + t.getMessage());
    }
}