package com.example.intern.mailers;

import android.util.Log;

import com.example.intern.ExecutorProvider;

public abstract class SwabhimanAutoMailer {
	public static String SWABHIMAN_BUSS_ASSOCIATE_SUB = "Interest in being Business Associate";
	public static String SWABHIMAN_EMPLOYEMENT_SUB_BASE = "Interest in employment opportunities";
	public static String SWABHIMAN_INVESTOR_SUB_BASE = "Interest in investing";
	public static void sendSwabhimanMail(String subject, String body, String user_email){
		Thread t = new Thread(() -> {
			try {
				MailSender sender = new MailSender("ps@prarambhlife.com",
						"Aditi@1881");
				sender.sendSwabhimanMail(subject, body, user_email);
			} catch (Exception e) {
				Log.e("SendMail", e.getMessage(), e);
			}
		});
		ExecutorProvider.getExecutorService().submit(t);
	}
}
