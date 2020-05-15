package com.example.intern.database.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class EssentialOrderEntity {
	@Ignore
	public String user_ID;
	@PrimaryKey
	@NonNull
	public String order_ID;
	public String items;
	public String vendor_ID;
	public String quantities;
	public Long timestamp;
	public String prices;
	public EssentialOrderEntity(){}
	//Constructor
	public EssentialOrderEntity(String user_ID, String vendor_ID, String order_ID, Long timestamp,
	                            String items, String quantities){
		this.user_ID = user_ID;
		this.vendor_ID = vendor_ID;
		this.items = items;
		this.quantities = quantities;
		this.timestamp = timestamp;
		this.order_ID = order_ID;
	}
}
