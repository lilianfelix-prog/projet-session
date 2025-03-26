package com.example.projet_session.auth;
import android.text.TextUtils;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static final String BASE_URL = "https://localhost/project-session";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(Class<S> serviceClass, String email, String password){
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            String token = Credentials.basic(email, password);
            return createService(serviceClass, token);
        }
        return createService(serviceClass, null);
    }

    private static <S> S createService(Class<S> serviceClass, final String token) {
        if (!TextUtils.isEmpty(token)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(token);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }
}


