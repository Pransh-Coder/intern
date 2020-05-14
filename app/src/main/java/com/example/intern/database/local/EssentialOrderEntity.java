package com.example.intern.database.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import java.util.List;

@Fts4(languageId = "lid")
@Entity
public class EssentialOrderEntity {
	@PrimaryKey
	public String user_ID;
	public String vendor_ID;
	public String order_ID;
	public Long timestamp;
	public List<String> items;
	public List<Integer> quantities;
	public List<Integer> prices;
	@ColumnInfo(name = "lid")
	public int languageID;
}
