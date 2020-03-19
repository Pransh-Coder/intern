package com.example.intern;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;

public class FireStoreUtil {
	public static String QUERY_TYPE_UNRESOLVED = "0";
	public static String QUERY_TYPE_RESOLVED = "1";
	public static String USER_COLLECTION_NAME = "users";
	public static String QUERY_COLLECTION_NAME = "queries";
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
	
	public static DocumentReference initialiseUser(Context context, String userID){
		if(userDocumentReference == null){
			synchronized (FireStoreUtil.class){
				if(userDocumentReference == null){
					userDocumentReference = getDbReference(context).collection(USER_COLLECTION_NAME).document(userID);
				}
			}
		}
		return userDocumentReference;
	}
	
	public static CollectionReference getQueryCollectionReference(Context context, String UID){
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
	}
	
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
