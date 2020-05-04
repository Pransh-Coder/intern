package com.example.intern.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FireStoreUtil {
	//Firebase provides concurrent running of their commands. So no need for concurrency
	
	//Collection Names in the database
	public static String USER_COLLECTION_NAME = "Users";
	public static String USER_CLUSTER_COLLECTION_NAME = "uclust";
	private static String USER_PHONE_LIST_COLLECTION_NAME = "uph";
	public static String USER_RELATIVE_PHONE_NUMBER = "rph";
	
	
	//Field Names used
	public static String USER_NAME = "un";
	public static String USER_EMAIL = "em";
	public static String USER_NICK_NAME = "nn";
	public static String USER_PS_NICK_NAME = "psnn";
	public static String USER_PHONE_NUMBER = "pn";
	//REQUEST TYPES
	public static int REQUESTS_SERVICE = 2;
	//Storage path names used
	private static String USER_PROFILE_IMAGE_FILE_NAME = "pp.jpg";
	public static String USER_DOB = "dob";
	public static String USER_PIN_CODE = "pc";
	public static String USER_PAY_ID = "pay";
	public static String USER_STATE= "LS";
	public static String USER_OCCUPATION = "occ";
	public static String USER_HOUSE_NO_KEY = "uhn";
	public static String USER_STREET_KEY = "ustr";
	public static String USER_AREA_KEY = "uarea";
	
	//References needs to be synchronised
	private static volatile FirebaseApp firebaseApp;
	private static volatile FirebaseUser firebaseUser;
	private static volatile FirebaseFirestore dbReference;
	private static volatile FirebaseStorage firebaseStorage;
	private static volatile DocumentReference userDocumentReference;
	private static volatile  DocumentReference userClusterReference;
	private static volatile StorageReference userStorageReference;
	private static volatile CollectionReference userPhoneCollectionReference;
	private static String ASK_THINGS_COLLECTION_NAME = "asked";
	private static String ESSENTIAL_SERVICE_REQUEST_COLLECTION_NAME = "essential";
	public static String PETROL_PRICE_KEY = "petropr";
	private static String FUEL_REFUND_COLLECTION_NAME = "bpclref";
	public static String STATIC_DATA_COLLECTION_NAME = "psdata";
	public static String EXCLUSIVE_SERVICES_COLLECTION_NAME = "exserv";
	public static String SWABHIMAN_SERVICE = "swabhiman";
	public static String DONOR = "donor";
	public static String INVESTOR = "investor";
	public static String HOME_MODIFICATION_SERVICES = "homemod";
	public static String TIFFIN_SERVICES_SERVICES = "tiffin";
	public static String AUTO_SERVICES_SERVICES = "auto";
	public static String EMERGENCY_CARE_SERVICES = "emerg";
	public static String LEGAL_FINANCIAL_SERVICES = "legalnfinancial";
	public static String EDUCATION_CLASSES_SERVICES = "edu";
	
	public static Task<DocumentSnapshot> getStaticDataSnapshot(){
		return FirebaseFirestore.getInstance().collection(STATIC_DATA_COLLECTION_NAME).document("static").get();
	}
	
	private static FirebaseApp getFirebaseApp(Context context) {
		if(FirebaseApp.getApps(context).isEmpty()){
			firebaseApp = FirebaseApp.initializeApp(context);
		}
		return firebaseApp;
	}
	
	public static FirebaseUser getFirebaseUser(Context context){
		if(firebaseUser == null){
			synchronized (FireStoreUtil.class){
				if(firebaseUser==null){
					firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
				}
			}
		}
		return firebaseUser;
	}
	
	private static FirebaseStorage getFirebaseStorage(Context context){
		if(firebaseStorage == null){
			synchronized (FireStoreUtil.class){
				if(firebaseStorage==null){
					firebaseStorage = FirebaseStorage.getInstance();
				}
			}
		}
		return firebaseStorage;
	}
	
	public static Task<DocumentReference> uploadServiceRequest(String userUID, String serviceType, String description){
		Map<String, Object> data = new HashMap<>();
		data.put("type", "service");data.put("user", userUID);
		data.put("cat", serviceType);data.put("desc", description);
		return FirebaseFirestore.getInstance().collection(ASK_THINGS_COLLECTION_NAME).add(data);
	}
	
	public static Task<DocumentReference> uploadDoctorRequest(String UID, String type, String name, String age, String description){
		Map<String, Object> data = new HashMap<>();
		data.put("uid", UID);data.put("cat", "doctoronline");
		data.put("type", type);data.put("name", name);
		data.put("age", age);data.put("desc", description);
		return  FirebaseFirestore.getInstance().collection(ASK_THINGS_COLLECTION_NAME).document("aaaaDoctorOnline").collection("DoctorOnlineReqs").add(data);
	}
	
	public static Task<DocumentReference> uploadProductRequest(String userUID, String productName, @Nullable String productPathInFirebaseStorage){
		Map<String, Object> data = new HashMap<>();
		data.put("type", "product");data.put("user", userUID);
		data.put("name", productName);data.put("image", productPathInFirebaseStorage);
		return FirebaseFirestore.getInstance().collection(ASK_THINGS_COLLECTION_NAME).add(data);
	}
	
	public static Task<DocumentReference> uploadEssentialServiceRequest(String UID, String needDetails, @Nullable String imagePath){
		Map<String, Object> data = new HashMap<>();
		data.put("user" , UID); data.put("need", needDetails);
		data.put("list", imagePath);
		return FirebaseFirestore.getInstance().collection(ESSENTIAL_SERVICE_REQUEST_COLLECTION_NAME).add(data);
	}
	
	public static Task<DocumentReference> uploadFuelRefundRequest(String UID, String invoiceNo, Double amountReq, String firebaseStorageUrl){
		Map<String, Object> data = new HashMap<>();
		data.put("user", UID);data.put("inv", invoiceNo);
		data.put("discamt", amountReq);data.put("invimg",firebaseStorageUrl);
		data.put("verstat", false);
		return FirebaseFirestore.getInstance().collection(FUEL_REFUND_COLLECTION_NAME).document(Integer.toString(Calendar.getInstance().get(Calendar.DATE))).collection(UID).add(data);
	}
	
	public static DocumentReference getUserDocumentReference(Context context, String userID){
		if(userDocumentReference == null){
			synchronized (FireStoreUtil.class){
				if(userDocumentReference == null){
					userDocumentReference = FirebaseFirestore.getInstance().collection(USER_COLLECTION_NAME).document(userID);
				}
			}
		}
		return userDocumentReference;
	}
	
	public static DocumentReference getUserClusterReference(Context context, String pinCode){
		if(userClusterReference == null){
			synchronized (FireStoreUtil.class){
				if(userClusterReference == null){
					userClusterReference = FirebaseFirestore.getInstance().collection(USER_CLUSTER_COLLECTION_NAME).document(pinCode);
				}
			}
		}
		return userClusterReference;
	}
	
	private static StorageReference getUserImageStorageReference(Context context, String UID){
		if(userStorageReference == null){
			synchronized (FireStoreUtil.class){
				if(userStorageReference == null){
					userStorageReference = FirebaseStorage.getInstance().getReference().child("users").child(UID);
				}
			}
		}
		return userStorageReference;
	}
	
	private static CollectionReference getUserPhoneCollectionReference(Context context){
		if(userPhoneCollectionReference == null){
			synchronized (FireStoreUtil.class){
				if(userPhoneCollectionReference == null){
					userPhoneCollectionReference = FirebaseFirestore.getInstance().collection(USER_PHONE_LIST_COLLECTION_NAME);
				}
			}
		}
		return userPhoneCollectionReference;
	}
	
	//Methods to create new users or find existing ones
	public static Task<Void> makeUserWithUID(Context context, String UID, String userName, String eMail, String nickName, String psNickName, String phoneNumber, String DOB, String pinCode, String relative_number, String logINStatus, Double memberShipFee){
		FireStoreUtil.PSUser user = new FireStoreUtil.PSUser(userName, eMail, nickName, psNickName, phoneNumber, DOB, pinCode,relative_number, logINStatus, memberShipFee);
		return FirebaseFirestore.getInstance().collection(USER_COLLECTION_NAME).document(UID).set(user);
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
		return getUserClusterReference(context, pinCode).set(data, SetOptions.merge());
	}
	
	public static Task<DocumentReference> addToPhoneNumberList(Context context, String phoneNumber, String UID){
		Map<String, String> data = new HashMap<>();
		data.put(phoneNumber, UID);
		return getUserPhoneCollectionReference(context).add(data);
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
	
	public static Task<Void> uploadMeta(Context context, String name, String email, String occupation, String house_no, String street, String area , String phoneNo, String relativePhoneNo){
		Map<String, Object> metadata = new HashMap<>();
		metadata.put(USER_NAME, name);
		metadata.put(USER_EMAIL, email);
		metadata.put(USER_OCCUPATION, occupation);
		metadata.put(USER_HOUSE_NO_KEY, house_no);
		metadata.put(USER_STREET_KEY, street);
		metadata.put(USER_AREA_KEY, area);
		metadata.put(USER_PHONE_NUMBER, phoneNo);
		metadata.put(USER_RELATIVE_PHONE_NUMBER, relativePhoneNo);
		return getUserDocumentReference(context, getFirebaseUser(context).getUid()).set(metadata, SetOptions.merge());
	}
	
	
	public static UploadTask uploadImage(Context context, String UID, Bitmap bitmap){
		StorageReference imagePathRef = getUserImageStorageReference(context, UID).child(System.currentTimeMillis() +".jpg");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
		byte[] data = outputStream.toByteArray();
		return imagePathRef.putBytes(data);
	}
	
	public static UploadTask uploadFuelInvoice(Context context, String UID, String invoice, Bitmap bitmap){
		StorageReference invoiceStRef = getUserImageStorageReference(context, UID).child("invoices").child(invoice+".jpg");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
		byte[] data = outputStream.toByteArray();
		return invoiceStRef.putBytes(data);
	}
	
	
	public static UploadTask uploadProfilePic(Context context, String UID, Bitmap bitmap){
		StorageReference imagePathRef = getUserImageStorageReference(context, UID).child(USER_PROFILE_IMAGE_FILE_NAME);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
		byte[] data = outputStream.toByteArray();
		return imagePathRef.putBytes(data);
	}
	
	public static FileDownloadTask getProfilePicInLocal(Context context, String UID) throws Exception {
		StorageReference ppRef = getUserImageStorageReference(context, UID).child(USER_PROFILE_IMAGE_FILE_NAME);
		File rootPath = new File(Environment.getExternalStorageDirectory(), "PSData");
		if(!rootPath.exists()){
			rootPath.mkdirs();
		}
		final File file = new File(rootPath, "pp.jpg");
		return ppRef.getFile(file);
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
		public String LS;
		//Relative Number
		public String rph;
		//Membership fee
		public Double memfee;
		//Wallet Balance
		public Double wallet;
		
		public PSUser(){}
		
		public PSUser(String name, String email, String nickName, String psNickName,
		              String phoneNumber, String DOB , String pinCode
				, String relative_number, String logINStatus, Double membership_fee){
			this.un = name;
			this.em = email;
			this.nn = nickName;
			this.psnn = psNickName;
			this.pn = phoneNumber;
			this.dob = DOB;
			this.pc = pinCode;
			this.pay = null;
			this.rph = relative_number;
			this.LS = logINStatus;
			this.memfee = membership_fee;
			this.wallet = 0d;
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
