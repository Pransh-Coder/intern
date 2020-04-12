package com.example.intern.payment.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankTransferEntity {
	@SerializedName("entity")
	@Expose
	private String entity;
	@SerializedName("payment_id")
	@Expose
	private String payment_id;
	@SerializedName("amount")
	@Expose
	private long amount;
	@SerializedName("payer_bank_account")
	@Expose
	private PayerAccount payer_bank_account;
	
	public String getEntity() {
		return entity;
	}
	
	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	public String getPayment_id() {
		return payment_id;
	}
	
	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}
	
	public long getAmount() {
		return amount;
	}
	
	public void setAmount(long amount) {
		this.amount = amount;
	}
	
	public PayerAccount getPayer_bank_account() {
		return payer_bank_account;
	}
	
	public void setPayer_bank_account(PayerAccount payer_bank_account) {
		this.payer_bank_account = payer_bank_account;
	}
	
	public class PayerAccount{
		@SerializedName("account_number")
		@Expose
		private String account_number;
		
		public String getAccount_number() {
			return account_number;
		}
		
		public void setAccount_number(String account_number) {
			this.account_number = account_number;
		}
	}
}
