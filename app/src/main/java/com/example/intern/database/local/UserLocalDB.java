package com.example.intern.database.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {UserEntity.class} , version = 1, exportSchema = false)
public abstract class UserLocalDB extends RoomDatabase {
	public static final ExecutorService executorService = Executors.newFixedThreadPool(4);
	private static volatile UserLocalDB INSTANCE;

	public static UserLocalDB getInstance(final Context context){
		if(INSTANCE == null){
			synchronized (UserLocalDB.class){
				if(INSTANCE == null){
					INSTANCE = Room.databaseBuilder(context, UserLocalDB.class, "user_data").build();
				}
			}
		}
		return INSTANCE;
	}

	public abstract UserDAO userDAO();
}
