package com.finanscepte.mobile.api;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:8080";
    private static final String API_KEY = "dev-key";
    private static Retrofit retrofit;

    private ApiClient() {
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            Interceptor apiKeyInterceptor = chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("X-API-KEY", API_KEY)
                        .build();
                return chain.proceed(request);
            };
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(apiKeyInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
