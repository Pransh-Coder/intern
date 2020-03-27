package com.example.intern.database.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserEntity {
	@PrimaryKey
	@NonNull
			public String UID;
	@NonNull
	public String user_name;
	public String ps_nick_name;
	public String nick_name;
	public String phone_number;
	@NonNull
	public String user_DOB;
	@NonNull
	public String user_pin_code;
	@NonNull
	public boolean isLoggedIn;
	public UserEntity(String UID, String user_name, String user_DOB, String user_pin_code){
		this.UID = UID;
		this.user_DOB = user_DOB;
		this.user_name = user_name;
		this.user_pin_code = user_pin_code;
	}
}
