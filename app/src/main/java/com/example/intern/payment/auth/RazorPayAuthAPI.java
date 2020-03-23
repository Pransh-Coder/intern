package com.example.intern.payment.auth;

/*import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RazorPayAuthAPI {
	private static final String keyID = "rzp_test_9SFxBSOfMFPxyk";
	private static final String key_secret = "Qi9f1wEd1SSTeEprS64810cZ";
	private static final String BASE_URL = "https://api.razorpay.com/v1/";
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
}
 */
