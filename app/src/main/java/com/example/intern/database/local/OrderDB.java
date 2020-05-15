package com.example.intern.database.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {EssentialOrderEntity.class}, version = 1, exportSchema = false)
public abstract class OrderDB extends RoomDatabase {
	private static final int THREAD_COUNT = 4;
	private static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(THREAD_COUNT);
	private static volatile OrderDB INSTANCE;

	public static OrderDB getInstance(final Context context){
		if(INSTANCE == null){
			synchronized (OrderDB.class){
				if(INSTANCE == null){
					INSTANCE = Room.databaseBuilder(context, OrderDB.class, "order_db").build();
				}
			}
		}
		return INSTANCE;
	}
	
	//Exposes the DAO for the Room database
	abstract OrderDAO orderDAO();
	
	public List<EssentialOrderEntity> getOrders(){
		return INSTANCE.orderDAO().getAllOrders();
	}
	
	public void insertOrder(EssentialOrderEntity orderEntity){
		databaseExecutor.execute(() -> INSTANCE.orderDAO().insertOrder(orderEntity));
	}
	
	public void insertPrices(String priceList){
		databaseExecutor.execute(()-> INSTANCE.orderDAO().insertPrices(priceList));
	}
}
