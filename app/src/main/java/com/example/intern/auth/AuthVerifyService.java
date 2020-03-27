package com.example.intern.auth;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.intern.database.FireStoreUtil;
import com.example.intern.database.SharedPrefUtil;

public class AuthVerifyService extends IntentService {
	public static String USER_UID_INTENT_KEY;
	public static String KILL_APP_INTENT_KEY;
	public AuthVerifyService() {
		super("auth_verifier_ps_prarambh");
	}
	
	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		//TODO:Verify the user with firebase
		FireStoreUtil.getUserDocumentReference(this, intent.getStringExtra(USER_UID_INTENT_KEY)).get().addOnSuccessListener(snapshot -> {
			if(!snapshot.exists()){
				//TODO: User does not exist, Broadcast to kill app
				SharedPrefUtil prefUtil = new SharedPrefUtil(this);
				prefUtil.setLoginStatus(false);
				Intent stopAppIntent = new Intent();
				stopAppIntent.putExtra(KILL_APP_INTENT_KEY, true);
				sendBroadcast(stopAppIntent);
			}
			stopSelf();
		});
	}
}
