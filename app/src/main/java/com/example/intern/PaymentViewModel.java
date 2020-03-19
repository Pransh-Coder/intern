package com.example.intern;

import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

public class PaymentViewModel extends ViewModel {
	//Needed to create customer in RazorPay, initialize from the
	private String userName;
	private String userPhone;
	private String userEmail;
	//Do a null check before proceeding with payment
	private JSONObject cardPaymentPayload = null;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserPhone() {
		return userPhone;
	}
	
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public void setCardPaymentPayload(String cardName, String cardNumber, String expMonth, String expYear, String cvv){
		//TODO: create payload for card payment
	}
	
	public JSONObject getCardPaymentPayload(){
		return this.cardPaymentPayload;
	}
}
