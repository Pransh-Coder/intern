package com.example.intern.database.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDAO{
	
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insertOrder(EssentialOrderEntity orderEntity);
	
	@Query("SELECT * FROM orders_v2")
	List<EssentialOrderEntity> getAllOrders();
	@Query("SELECT vendor_ID FROM orders_v2")
	List<String>getAllVid();
	@Query("SELECT * FROM orders_v2 WHERE vendor_ID LIKE :vid")
	List<EssentialOrderEntity> get_VidOrders(String vid);

	
}
