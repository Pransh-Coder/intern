package com.example.intern.payment.auth;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
	private static Retrofit retrofit;
	private static String VIRTUAL_ACC_ID = "va_ETDhYriSHSC4s1";

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
	
	public static void getBankPayments(String accountNumber, BankVerifierInterface verifierInterface){
		RazorPayAPI razorPayAPI = RazorPayAuthAPI.getRetrofit().create(RazorPayAPI.class);
		Call<PaymentCollectionEntity> call = razorPayAPI.paymentsMadeToVirtualAccount(VIRTUAL_ACC_ID);
		Log.d(TAG, "RazorPayAuthoriser: URL called" + call.request().url());
		List<String> foundPayIDs = new ArrayList<>();
		Callback<PaymentCollectionEntity> callback = new Callback<PaymentCollectionEntity>() {
			@Override
			public void onResponse(Call<PaymentCollectionEntity> call, Response<PaymentCollectionEntity> response) {
				if(response.body().getItems() != null){
					for(PaymentCollectionEntity.PaymentItem item : response.body().getItems()){
						//TODO : Check with the id now
						if(item.getMethod().equals("bank_transfer") && item.getStatus().equals("captured")){
							foundPayIDs.add(item.getId());
						}
					}
					checkWithPayIDs(foundPayIDs, accountNumber, verifierInterface);
				}
			}
			@Override
			public void onFailure(Call<PaymentCollectionEntity> call, Throwable t) {
				verifierInterface.isVerified(false, null);
			}
		};
		call.enqueue(callback);
	}
	
	private static void checkWithPayIDs(List<String> payIDs, String account_number, BankVerifierInterface verifierInterface){
		for(String payID : payIDs){
			RazorPayAPI razorPayAPI = RazorPayAuthAPI.getRetrofit().create(RazorPayAPI.class);
			Call<BankTransferEntity> call = razorPayAPI.bankTransferInfo(payID);
			Log.d(TAG, "checkWithPayIDs: url called : " + call.request().url());
			Callback<BankTransferEntity> callback = new Callback<BankTransferEntity>() {
				@Override
				public void onResponse(Call<BankTransferEntity> call, Response<BankTransferEntity> response) {
					if(response.body().getAmount()==51000){
						if(response.body().getPayer_bank_account().getAccount_number().equals(account_number)){
							verifierInterface.isVerified(true, payID);
						}
					}
				}
				
				@Override
				public void onFailure(Call<BankTransferEntity> call, Throwable t) {
					verifierInterface.isVerified(false, null);
				}
			};
			call.enqueue(callback);
		}
	}
	
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
	
	public  interface  BankVerifierInterface{
		void isVerified(boolean verificationStatus, String payID);
	}
}
