package com.example.intern.database.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
	
	public List<EssentialOrderEntity> getOrders() {
		Callable<List<EssentialOrderEntity>> callable = () -> INSTANCE.orderDAO().getAllOrders();
		Future<List<EssentialOrderEntity>> result = databaseExecutor.submit(callable);
		try{return result.get();}catch (Exception ignored){return null;}
	}
	
	public void insertOrder(EssentialOrderEntity orderEntity){
		databaseExecutor.execute(() -> INSTANCE.orderDAO().insertOrder(orderEntity));
	}
	public List<String> getVid() {
		Callable<List<String>> callable = () -> INSTANCE.orderDAO().getAllVid();
		Future<List<String>> result = databaseExecutor.submit(callable);
		try{return result.get();}catch (Exception ignored){return null;}
	}
	public List<EssentialOrderEntity> getVidOrders(String vid) {
		Callable<List<EssentialOrderEntity>> callable = () -> INSTANCE.orderDAO().get_VidOrders(vid);
		Future<List<EssentialOrderEntity>> result = databaseExecutor.submit(callable);
		try{return result.get();}catch (Exception ignored){return null;}
	}

}
