package com.example.intern.database.repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.local.UserEntity;
import com.example.intern.database.local.UserLocalDB;

import java.util.Arrays;
import java.util.List;

public class UserRepository {
	private static String DB_NAME = "user_data";
	private UserEntity user_data;
	private MutableLiveData<List> friendUIDs = new MutableLiveData<>();
	private UserLocalDB localDB;
	private  UserEntity userEntity;
	private Context context;
	private String UID;
	public UserRepository(Context context, String UID){
		localDB = UserLocalDB.getInstance(context);
		user_data = localDB.userDAO().getUserWithUID(UID);
		this.context = context;
		this.UID = UID;
	}
	
	public MutableLiveData<List> getFriendUIDs(String pin_code){
		//TODO: Query firestore to return all users' UIDs
		FireStoreUtil.getUserClusterReference(context, pin_code).get().addOnSuccessListener(snapshot -> {
			friendUIDs.postValue(Arrays.asList(snapshot.get("u")));
		});
		return friendUIDs;
	}
	
	public UserEntity getUserEntity(String UID){
		UserLocalDB.executorService.execute(new Runnable() {
			@Override
			public void run() {
				userEntity = localDB.userDAO().getUserWithUID(UID);
			}
		});
		return userEntity;
	}
	
}
