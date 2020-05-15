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
	
	@Query("SELECT * FROM orders")
	List<EssentialOrderEntity> getAllOrders();
	
	@Query("INSERT INTO orders(prices) VALUES(:priceList)")
	void insertPrices(String priceList);
}
