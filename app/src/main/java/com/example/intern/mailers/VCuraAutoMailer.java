package com.example.intern.mailers;

public abstract class VCuraAutoMailer {
	public static void sendMail(String subject, String body, String user_email){
		try {
			MailSender sender = new MailSender("ps@prarambhlife.com",
					"Aditi@1881");
			sender.sendVCuraMail(subject, body, user_email );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
