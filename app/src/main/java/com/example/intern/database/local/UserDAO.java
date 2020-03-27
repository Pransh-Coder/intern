package com.example.intern.database.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDAO {
	@Query("SELECT * FROM UserEntity WHERE UID LIKE :UID")
	UserEntity getUserWithUID(String UID);
	
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(UserEntity userEntity);
}
