package com.example.intern.mailers;

import android.util.Log;

import com.example.intern.ExecutorProvider;

public abstract class VCuraAutoMailer {
	public static void sendMail(String subject, String body, String user_email){
		Thread t = new Thread(() -> {
			try {
				MailSender sender = new MailSender("ps@prarambhlife.com",
						"Aditi@1881");
				sender.sendVCuraMail(subject, body, user_email );
			} catch (Exception e) {
				Log.e("SendMail", e.getMessage(), e);
			}
		});
		ExecutorProvider.getExecutorService().submit(t);
	}
}
