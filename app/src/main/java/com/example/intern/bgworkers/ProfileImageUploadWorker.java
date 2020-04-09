package com.example.intern.bgworkers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.intern.database.FireStoreUtil;

public class ProfileImageUploadWorker extends Worker {
	public static final String KEY_UID = "uid";
	public static final String KEY_IMG_PATH = "path";
	private static volatile Result result = Result.retry();
	Context context;
	public ProfileImageUploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
		super(context, workerParams);
		this.context = context;
	}
	
	@NonNull
	@Override
	public Result doWork() {
		String UID = getInputData().getString(KEY_UID);
		String filePath = getInputData().getString(KEY_IMG_PATH);
		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		FireStoreUtil.uploadProfilePic(context,UID, bitmap).addOnSuccessListener(taskSnapshot -> setResultSuccess());
		return result;
	}
	private void setResultSuccess(){
		result = Result.success();
	}
}
