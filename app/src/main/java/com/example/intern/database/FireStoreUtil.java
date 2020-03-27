package com.example.intern.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FireStoreUtil {
	//Firebase provides concurrent running of their commands. So no need for concurrency
	
	public static String QUERY_TYPE_UNRESOLVED = "0";
	public static String QUERY_TYPE_RESOLVED = "1";
	
	//Collection Names in the database
	public static String USER_COLLECTION_NAME = "Users";
	public static String USER_CLUSTER_COLLECTION_NAME = "uclust";
	public static String QUERY_COLLECTION_NAME = "queries";
	//Storage path names used
	private static String USER_IMAGE_STORAGE_NAME = "imgs";
	
	//Field Names used
	private static String USER_NAME = "un";
	private static String USER_EMAIL = "em";
	private static String USER_NICK_NAME = "nn";
	private static String USER_PS_NICK_NAME = "psnn";
	private static String USER_PHONE_NUMBER = "pn";
	private static String USER_DOB = "dob";
	private static String USER_PIN_CODE = "pc";
	private static String USER_PAY_ID = "pay";
	
	//References needs to be synchronised
	private static volatile FirebaseApp firebaseApp;
	private static volatile FirebaseUser firebaseUser;
	private static volatile FirebaseFirestore dbReference;
	private static volatile FirebaseStorage firebaseStorage;
	private static volatile DocumentReference userDocumentReference;
	private static volatile  DocumentReference userClusterReference;
	private static volatile StorageReference userStorageReference;
	private static volatile CollectionReference queryCollectionReference;
	
	private static FirebaseApp getFirebaseApp(Context context) {
		if(FirebaseApp.getApps(context).isEmpty()){
			firebaseApp = FirebaseApp.initializeApp(context);
		}
		return firebaseApp;
	}
	
	public static FirebaseUser getFirebaseUser(Context context){
		getFirebaseApp(context);
		return FirebaseAuth.getInstance().getCurrentUser();
	}
	
	public static FirebaseFirestore getDbReference(Context context){
		if(dbReference == null){
			synchronized (FireStoreUtil.class){
				if(dbReference == null){
					if(firebaseApp == null){
						getFirebaseApp(context);
					}
					dbReference = FirebaseFirestore.getInstance();
				}
			}
		}
		return dbReference;
	}
	
	private static FirebaseStorage getFirebaseStorage(Context context){
		if(firebaseStorage == null){
			synchronized (FireStoreUtil.class){
				firebaseStorage = FirebaseStorage.getInstance(getFirebaseApp(context));
			}
		}
		return firebaseStorage;
	}
	
	public static DocumentReference getUserDocumentReference(Context context, String userID){
		if(userDocumentReference == null){
			synchronized (FireStoreUtil.class){
				if(userDocumentReference == null){
					userDocumentReference = getDbReference(context).collection(USER_COLLECTION_NAME).document(userID);
				}
			}
		}
		return userDocumentReference;
	}
	
	public static DocumentReference getUserClusterReference(Context context, String pinCode){
		if(userClusterReference == null){
			synchronized (FireStoreUtil.class){
				if(userClusterReference == null){
					userClusterReference = getDbReference(context).collection(USER_CLUSTER_COLLECTION_NAME).document(pinCode);
				}
			}
		}
		return userClusterReference;
	}
	
	private static StorageReference getUserImageStorageReference(Context context, String UID){
		if(userStorageReference == null){
			synchronized (FireStoreUtil.class){
				if(userStorageReference == null){
					userStorageReference = getFirebaseStorage(context).getReference(UID).child(USER_IMAGE_STORAGE_NAME);
				}
			}
		}
		return userStorageReference;
	}
	
	//Methods to create new users or find existing ones
	public static Task<Void> makeUserWithUID(Context context, String UID, String userName, String eMail, String nickName, String psNickName, String phoneNumber, String DOB, String pinCode, String password){
		FireStoreUtil.PSUser user = new FireStoreUtil.PSUser(userName, eMail, nickName, psNickName, phoneNumber, DOB, pinCode, password);
		return getUserDocumentReference(context, UID).set(user);
	}
	
/*	public static boolean findUserWithUID(Context context, String UID){
		getUserDocumentReference(context, UID).get().addOnSuccessListener( snapshot -> {
			if(snapshot.exists()){
				//TODO: Login the user
			}else if(!snapshot.exists()){
				//TODO : Send to registration page
			}else{
				//TODO:Handle unknown error
			}
		});
	}*/
	
	//TODO: make a method to get user friends from the cluster
	//Creates a new cluster if not already present
	public static Task<Void> addToCluster(Context context, String pinCode, String UID){
		Map<String, Boolean> data = new HashMap<>();
		data.put(UID, Boolean.FALSE);
		return getUserClusterReference(context, pinCode).set(data);
	}
	
	public static List<String> getFriendsInContact(Context context){
		List<String> friends = new ArrayList<>();
		//TODO : Find user's friends from contact list
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if(cursor.moveToFirst()) {
			do {
				String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
					while (pCur.moveToNext()) {
						String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						friends.add(contactNumber);
						break;
					}
					pCur.close();
				}
			} while (cursor.moveToNext()) ;
		}
		cursor.close();
		return friends;
	}
	
	//TODO : Make updater methods
	//Updater methods for user fields
	public static Task<Void> uploadPayID(Context context, String payID){
		Map<String, Object> updata = new HashMap<>();
		updata.put(USER_PAY_ID, payID);
		return getUserDocumentReference(context, getFirebaseUser(context).getUid()).update(updata);
	}
	
	
	public static UploadTask uploadImage(Context context, String UID, Bitmap bitmap){
		StorageReference imagePathRef = getUserImageStorageReference(context, UID).child(Long.toString(System.currentTimeMillis()));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
		byte[] data = outputStream.toByteArray();
		return imagePathRef.putBytes(data);
	}
	
	public static UploadTask uploadImage(Context context, String UID, Uri uri){
		StorageReference imagePathRef = getUserImageStorageReference(context, UID).child(Long.toString(System.currentTimeMillis()));
		return imagePathRef.putFile(uri);
	}
	
	
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
		//Password
		public String pass;
		
		public PSUser(){}
		
		public PSUser(String name, String email, String nickName, String psNickName,
		              String phoneNumber, String DOB , String pinCode, String password){
			this.un = name;
			this.em = email;
			this.nn = nickName;
			this.psnn = psNickName;
			this.pn = phoneNumber;
			this.dob = DOB;
			this.pc = pinCode;
			this.pay = null;
			this.pass = password;
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
	}*/
}
