package com.example.intern.payment.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentCollectionEntity {
	@SerializedName("entity")
	@Expose
	private String entity;
	@SerializedName("items")
	@Expose
	private List<PaymentItem> items = null;
	
	public String getEntity() {
		return entity;
	}
	
	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	public List<PaymentItem> getItems() {
		return items;
	}
	
	public void setItems(List<PaymentItem> items) {
		this.items = items;
	}
	
	public class PaymentItem{
		@SerializedName("id")
		@Expose
		private String id;
		@SerializedName("method")
		@Expose
		private String method;
		@SerializedName("status")
		@Expose
		private String status;
		@SerializedName("amount")
		@Expose
		private int amount;
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public String getMethod() {
			return method;
		}
		
		public void setMethod(String method) {
			this.method = method;
		}
		
		public int getAmount() {
			return amount;
		}
		
		public void setAmount(int amount) {
			this.amount = amount;
		}
		
		public String getStatus() {
			return status;
		}
		
		public void setStatus(String status) {
			this.status = status;
		}
	}
}
