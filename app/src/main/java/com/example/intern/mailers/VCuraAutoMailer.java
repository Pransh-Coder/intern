package com.example.intern.mailers;

import android.util.Log;

import com.example.intern.ExecutorProvider;

public class VCuraAutoMailer {
	public static void sendMail(String subject, String body, String recipients){
		Thread t = new Thread(() -> {
			try {
				MailSender sender = new MailSender("ps@prarambhlife.com",
						"Aditi@1881");
				sender.sendMail(subject, body,
						"ps@prarambhlife.com", recipients);
			} catch (Exception e) {
				Log.e("SendMail", e.getMessage(), e);
			}
		});
		ExecutorProvider.getExecutorService().submit(t);
	}
}
