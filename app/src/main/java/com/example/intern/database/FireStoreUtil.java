package com.example.intern.database;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;

public abstract class FireStoreUtil {
	public static String QUERY_TYPE_UNRESOLVED = "0";
	public static String QUERY_TYPE_RESOLVED = "1";
	public static String USER_COLLECTION_NAME = "Users";
	public static String QUERY_COLLECTION_NAME = "queries";
	//Field Names used
	private static String USER_NAME = "un";
	private static String USER_EMAIL = "em";
	private static String USER_NICK_NAME = "nn";
	private static String USER_PS_NICK_NAME = "psnn";
	private static String USER_PHONE_NUMBER = "pn";
	private static String USER_DOB = "dob";
	private static String USER_PIN_CODE = "pc";
	private static String USER_PAY_ID = "pay";
	
	private static volatile FirebaseFirestore dbReference;
	private static volatile DocumentReference userDocumentReference;
	private static volatile CollectionReference queryCollectionReference;
	
	private static FirebaseFirestore getDbReference(Context context){
		if(dbReference == null){
			synchronized (FireStoreUtil.class){
				if(dbReference == null){
					initialiseFirebase(context);
					dbReference = FirebaseFirestore.getInstance();
				}
			}
		}
		return dbReference;
	}
	
	private static void initialiseFirebase(Context context) {
		if(FirebaseApp.getApps(context).isEmpty()){
			FirebaseApp.initializeApp(context);
		}
	}
	
	private static DocumentReference getUserDocumentReference(Context context, String userID){
		if(userDocumentReference == null){
			synchronized (FireStoreUtil.class){
				if(userDocumentReference == null){
					userDocumentReference = getDbReference(context).collection(USER_COLLECTION_NAME).document(userID);
				}
			}
		}
		return userDocumentReference;
	}
	
	public static Task<Void> makeUserWithUID(Context context, String UID, String userName, String eMail, String nickName, String psNickName, String phoneNumber, String DOB, String pinCode){
		FireStoreUtil.PSUser user = new FireStoreUtil.PSUser(userName, eMail, nickName, psNickName, phoneNumber, DOB, pinCode);
		Task<Void> task = getUserDocumentReference(context, UID).collection(USER_COLLECTION_NAME).document(UID).set(user);
		return task;
	}
	
	//TODO : Make updater methods
	
	@IgnoreExtraProperties
	public static class PSUser{
		//User Name
		public String un;
		//E-Mail
		public String em;
		//Nick Name
		public String nn;
		//PS Nick Name
		public String psnn;
		//Phone Number
		public String pn;
		//DOB
		public String dob;
		//Pin Code
		public String pc;
		//Pay ID
		public String pay;
		
		public PSUser(){}
		
		public PSUser(String name, String email, String nickName, String psNickName,
		              String phoneNumber, String DOB , String pinCode){
			this.un = name;
			this.em = email;
			this.nn = nickName;
			this.psnn = psNickName;
			this.pn = phoneNumber;
			this.dob = DOB;
			this.pc = pinCode;
		}
	}
	
	//Redundant query related stuff
	/*public static CollectionReference getQueryCollectionReference(Context context, String UID){
		if(queryCollectionReference == null){
			userDocumentReference = initialiseUser(context, UID);
			synchronized (FireStoreUtil.class){
				if(queryCollectionReference == null){
					queryCollectionReference = userDocumentReference.collection(QUERY_COLLECTION_NAME);
				}
			}
		}
		return queryCollectionReference;
	}
	
	public static void sendQuery(String query){
		if(query != null) {
			//Send the query to the database along with the timestamp of the query
			Query buildQuery = new Query(QUERY_TYPE_UNRESOLVED, query);
			userDocumentReference.collection(QUERY_COLLECTION_NAME).add(buildQuery);
		}
	}*/
	
	@IgnoreExtraProperties
	static class Query{
		//Query Type
		private String qt;
		//Actual Query
		private String q;
		//Timestamp of query
		private String ts;
		public Query(){}
		
		public Query(String type, String query){
			this.qt = type;
			this.q = query;
			this.ts = Long.toString(System.currentTimeMillis());
		}
		//Getters and setters
		public String getQt() {
			return qt;
		}
		
		public void setQt(String qt) {
			this.qt = qt;
		}
		
		public String getTs() {
			return ts;
		}
		
		public void setTs(String ts) {
			this.ts = ts;
		}
		
		public String getQ() {
			return q;
		}
		
		public void setQ(String q) {
			this.q = q;
		}
	}
}
