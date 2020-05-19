package com.example.intern.database.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders_v2")
public class EssentialOrderEntity {
	@Ignore
	public String user_ID;
	@PrimaryKey
	@NonNull
	public String order_ID;
	public String vendor_ID;
	public Long timestamp;
	public EssentialOrderEntity(){}
	//Constructor
	public EssentialOrderEntity(String user_ID, String vendor_ID, @NonNull String order_ID, Long timestamp){
		this.user_ID = user_ID;
		this.vendor_ID = vendor_ID;
		this.timestamp = timestamp;
		this.order_ID = order_ID;
	}
}
