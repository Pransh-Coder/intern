package com.example.intern.mailers;

import com.example.intern.AppStaticData;

import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender extends javax.mail.Authenticator {
	static {
		Security.addProvider(new JSSEProvider());
	}

	private String mailhost = "smtp.gmail.com"; //Hostname of the SMTP mail server which you want to connect for sending emails.
	private String user;
	private String password;
	private Session session;
	
	public MailSender(String user, String password) {
		this.user = user; //Your SMTP username. In case of GMail SMTP this is going to be your GMail email address.
		this.password = password; //Your SMTP password. In case of GMail SMTP this is going to be your GMail password.
		
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailhost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.quitwait", "false");
		
		session = Session.getDefaultInstance(props, this);
	}
	
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password);
	}
	
	public synchronized void sendVCuraMail(String subject, String body,
	                                       String sender) throws Exception {
		MimeMessage message = new MimeMessage(session);
		String finalBody = "User mail ID - " + sender + "\n" + body;
		DataHandler handler = new DataHandler(new ByteArrayDataSource(finalBody.getBytes(), "text/plain"));
		message.setFrom(new InternetAddress(AppStaticData.PS_MAIL_ID));
		message.setSubject(subject);
		message.setDataHandler(handler);
		//TODO : Edit method body to fabricated needs
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(AppStaticData.VCURA_MAIL_ID));
		message.setRecipient(Message.RecipientType.BCC, new InternetAddress(AppStaticData.TEST_MAIL_ID));
		Transport.send(message);
	}
	
	public synchronized void sendSwabhimanMail(String subject, String body, String sender) throws Exception{
		MimeMessage message = new MimeMessage(session);
		String finalBody = "User mail ID - " + sender + "\n" + body;
		DataHandler handler = new DataHandler(new ByteArrayDataSource(finalBody.getBytes(), "text/plain"));
		message.setSubject(subject);
		message.setDataHandler(handler);
		message.setFrom(new InternetAddress(AppStaticData.PS_MAIL_ID));
		//TODO : Edit method body to fabricated needs
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(AppStaticData.TEST_MAIL_ID));
		Transport.send(message);
		sendConfirmationMail(sender, subject);
	}
	
	public synchronized void sendConfirmationMail(String user_email, String subject) throws MessagingException {
		MimeMessage message = new MimeMessage(session);
		message.setSender(new InternetAddress(AppStaticData.PS_MAIL_ID));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(user_email));
		if(subject.contains(SwabhimanAutoMailer.SWABHIMAN_BUSS_ASSOCIATE_SUB)){
			String body = "Thank you for showing interest in being a business associate.\nOur team will contact you shorty" +
					"\nThank you!\nTeam,\nPS by Prarambh";
			DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
			message.setDataHandler(handler);
			message.setSubject(subject);
			Transport.send(message);
		}else if(subject.contains(SwabhimanAutoMailer.SWABHIMAN_EMPLOYEMENT_SUB_BASE)){
			String body = "Thank you for showing interest in employment opportunities.\nOur team will contact you shorty" +
					"\nThank you!\nTeam,\nPS by Prarambh";
			DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
			message.setDataHandler(handler);
			message.setSubject(subject);
			Transport.send(message);
		}else if(subject.contains(SwabhimanAutoMailer.SWABHIMAN_INVESTOR_SUB_BASE)) {
			String body = "Thank you for showing interest in being an investor.\nOur team will contact you shorty" +
					"\nThank you!\nTeam,\nPS by Prarambh";
			DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
			message.setDataHandler(handler);
			message.setSubject(subject);
			Transport.send(message);
		}
	}
}