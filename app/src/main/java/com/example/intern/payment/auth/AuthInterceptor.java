package com.example.intern.payment.auth;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
	private String keyID;
	private  String key_secret;
	private String credentials;
	
	public AuthInterceptor(String keyID, String key_secret){
		this.credentials = Credentials.basic(keyID, key_secret);
	}
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request original = chain.request();
		Request.Builder builder = original.newBuilder()
				.header("Authorization" , credentials);
		Request request = builder.build();
		return chain.proceed(request);
	}
}
