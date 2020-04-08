package com.example.intern.payment.auth;

import android.util.Log;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RazorPayAuthAPI {
	private static final String TAG = RazorPayAuthAPI.class.getSimpleName();
	private static final String keyID = "rzp_test_9SFxBSOfMFPxyk";
	private static final String key_secret = "Qi9f1wEd1SSTeEprS64810cZ";
	private static final String BASE_URL = "https://api.razorpay.com/v1/";

	public static void isPaymentVerified(String paymentID, VerifierInterface verifierInterface){
		RazorPayAPI razorPayAPI = RazorPayAuthAPI.getRetrofit().create(RazorPayAPI.class);
		Call<PaymentEntity> call = razorPayAPI.paymentInfo(paymentID);
		Log.d(TAG, "RazorPayAuthoriser: URL called" + call.request().url());
		Callback<PaymentEntity> callback = new Callback<PaymentEntity>() {
			@Override
			public void onResponse(Call<PaymentEntity> call, Response<PaymentEntity> response) {
				if(response.body().getStatus().equals("authorized")){
					Log.d(TAG, "onResponse: Payment authorized");
					verifierInterface.isVerified(true);
				}
			}
			@Override
			public void onFailure(Call<PaymentEntity> call, Throwable t) {
				Log.d(TAG, "onFailure: call failure");
				verifierInterface.isVerified(false);
			}
		};
		call.enqueue(callback);
	}
	private static Retrofit retrofit;
	public static Retrofit getRetrofit(){
		if(retrofit == null){
			final OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new AuthInterceptor(keyID,key_secret)).build();
			final Retrofit.Builder builder = new Retrofit.Builder()
					.baseUrl(BASE_URL)
					.client(httpClient)
					.addConverterFactory(GsonConverterFactory.create());
			retrofit = builder.build();
			return retrofit;
		}else{
			return retrofit;
		}
	}
	
	public  interface  VerifierInterface{
		void isVerified(boolean verificationStatus);
	}
}
