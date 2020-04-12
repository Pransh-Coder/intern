package com.example.intern.payment.auth;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RazorPayAPI {
	//va_ETDhYriSHSC4s1
	@GET("payments/{paymentID}")
	Call<PaymentEntity> paymentInfo(@Path("paymentID") String paymentID);
	@GET("virtual_accounts/{virtual_account_id}/payments")
	Call<PaymentCollectionEntity> paymentsMadeToVirtualAccount(@Path("virtual_account_id") String virtualAccID);
	@GET("payments/{payID}/bank_transfer")
	Call<BankTransferEntity> bankTransferInfo(@Path("payID") String payID);
}

