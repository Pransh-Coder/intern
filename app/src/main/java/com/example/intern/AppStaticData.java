package com.example.intern;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.intern.EditProfile.EditProfile;
import com.example.intern.ExclusiveServices.DoctorOnline;
import com.example.intern.ExclusiveServices.ExclusiveServices;
import com.example.intern.ExclusiveServices.HomeModification;
import com.example.intern.ExclusiveServices.TiffinService;
import com.example.intern.MedicalRecords.MedicalRecord;
import com.example.intern.ReduceExpenses.ReduceExpenses;
import com.example.intern.TotalDiscountReceived.TotalDiscountReceived;
import com.example.intern.askservices.DemandActivity;
import com.example.intern.askservices.EssentialServices;
import com.example.intern.fuel.MapsActivity;
import com.example.intern.shopping.ActivityShopping;
import com.example.intern.socialnetwork.Listactivity;
import com.example.intern.swabhiman.SwabhimanActivity;
import com.example.intern.tnc.TermsAndConditions;

import java.util.Arrays;
import java.util.List;

import save_money.Health;
import save_money.SaveMoney;

public abstract class AppStaticData {
	//TODO : Change to actual data on production
	public static final String VCURA_MAIL_ID = "chandanrajsingh19@gmail.com";
	public static final String PS_MAIL_ID = "ps@prarambhlife.com";
	public static final String TEST_MAIL_ID = "chandanrajsingh19@gmail.com";
	public static final List<String> demandProductTriggers = Arrays.asList("grocer", "vegetable","drinking","water","dairy", "curd","milk");
	public static final List<String> demandServicesTriggers = Arrays.asList("ask", "service","demand");
	public static final List<String> saveMoneyTriggers = Arrays.asList("save", "money", "offer");
	public static final List<String> healthTriggers = Arrays.asList("health", "dent", "teeth","pathology", "lab", "homeo", "diet", "eye", "physio");
	public static final List<String> giftTriggers = Arrays.asList("toy", "gift", "photo", "game");
	public static final List<String> lifestyleTriggers = Arrays.asList("life", "salon","fitness", "spa", "beauty", "parlor");
	public static final List<String> foodTriggers = Arrays.asList("food", "icecream");
	public static final List<String> servicesTrigger = Arrays.asList("mobile", "ro","bike", "wheeler");
	public static final List<String> holidayTriggers = Arrays.asList("holiday", "resort", "stay");
	public static final List<String> exclusiveServicesTriggers = Arrays.asList("auto","emergency","legal","financial","care");
	public static final List<String> tiffinTriggers = Arrays.asList("tiffin", "lunch");
	public static final List<String> homeModTriggers = Arrays.asList("home", "modification", "grab", "skid","toilet","riser","seat", "floor", "marking");
	public static final List<String> doctorOnlineTriggers = Arrays.asList("doctor", "ayurved","rishi","patient");
	public static final List<String> shoppingTriggers = Arrays.asList("shop","medicine","product","store","order");
	public static final List<String> swabhimanTriggers = Arrays.asList("employee", "job", "partner","business","invest","donate","donor", "swabhiman");
	public static final List<String> discountOnFuelTriggers = Arrays.asList("fuel", "petrol","diesel");
	public static final List<String> reduceExpensesTrigger = Arrays.asList("expenses", "monthly");
	public static final List<String> discountReceivedTriggers = Arrays.asList("discount", "history");
	public static final List<String> editProfileTriggers = Arrays.asList("edit", "profile", "me");
	public static final List<String> medicalRecordsTriggers = Arrays.asList("medical", "record", "vcura");
	public static final List<String> localNetworkTriggers = Arrays.asList("local", "network","friends", "freinds");
	public static final List<String> newsAndUpdateTriggers = Arrays.asList("news","update","notifi");
	public static final List<String> feedbackAndComplaintsTriggers = Arrays.asList("feedback", "complain","suggest");
	public static final List<String> rateShareTriggers = Arrays.asList("rate", "share", "like", "star");
	public static final List<String> tncTriggers = Arrays.asList("tnc", "terms", "conditions","privacy", "policy");
	
	//Logic to search from the main page
	@Nullable
	public static Intent getSearchResultIntentFromMain(Context context, String searchWord){
		String keyWord = searchWord.toLowerCase();
		Intent intent;
		//TODO :
		for(String word : demandProductTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, EssentialServices.class);
				return intent;
			}
		}
		for(String word : demandServicesTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, DemandActivity.class);
				return intent;
			}
		}
		for(String word : saveMoneyTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, SaveMoney.class);
				return intent;
			}
		}
		for(String word : healthTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, Health.class);
				return intent;
			}
		}
		for(String word : giftTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, Gifts.class);
				return intent;
			}
		}
		for(String word : lifestyleTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, Lifestyle.class);
				return intent;
			}
		}
		for(String word : foodTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, FoodActivity.class);
				return intent;
			}
		}
		for(String word : servicesTrigger){
			if(keyWord.contains(word)) {
				intent = new Intent(context, ServicesActivity.class);
				return intent;
			}
		}
		for(String word : holidayTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, Holiday.class);
				return intent;
			}
		}
		for(String word : exclusiveServicesTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, ExclusiveServices.class);
				return intent;
			}
		}
		for(String word : tiffinTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, TiffinService.class);
				return intent;
			}
		}
		for(String word : homeModTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, HomeModification.class);
				return intent;
			}
		}
		for(String word : doctorOnlineTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, DoctorOnline.class);
				return intent;
			}
		}
		for(String word : shoppingTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, ActivityShopping.class);
				return intent;
			}
		}
		for(String word : swabhimanTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, SwabhimanActivity.class);
				return intent;
			}
		}
		for(String word : discountOnFuelTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, MapsActivity.class);
				return intent;
			}
		}
		for(String word : reduceExpensesTrigger){
			if(keyWord.contains(word)) {
				intent = new Intent(context, ReduceExpenses.class);
				return intent;
			}
		}
		for(String word : discountReceivedTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, TotalDiscountReceived.class);
				return intent;
			}
		}
		for(String word : editProfileTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, EditProfile.class);
				return intent;
			}
		}
		for(String word : medicalRecordsTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, MedicalRecord.class);
				return intent;
			}
		}
		for(String word : localNetworkTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, Listactivity.class);
				return intent;
			}
		}
		for(String word : newsAndUpdateTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, NewsAndUpdatesACT.class);
				return intent;
			}
		}
		for(String word : feedbackAndComplaintsTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, FeedBackOrComplaintACT.class);
				return intent;
			}
		}
		for(String word : rateShareTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, Rating.class);
				return intent;
			}
		}
		for(String word : tncTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, TermsAndConditions.class);
				return intent;
			}
		}
		//No keyword matches
		return null;
	}
	
	public static Intent searchSaveMoney(Context context, String searchWord){
		String keyWord = searchWord.toLowerCase();
		Intent intent;
		for(String word : healthTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, Health.class);
				return intent;
			}
		}
		for(String word : giftTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, Gifts.class);
				return intent;
			}
		}
		for(String word : lifestyleTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, Lifestyle.class);
				return intent;
			}
		}
		for(String word : foodTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, FoodActivity.class);
				return intent;
			}
		}
		for(String word : servicesTrigger){
			if(keyWord.contains(word)) {
				intent = new Intent(context, ServicesActivity.class);
				return intent;
			}
		}
		for(String word : holidayTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, Holiday.class);
				return intent;
			}
		}
		return null;
	}
	
	public static Intent searchExclusiveServices(Context context, String searchWord){
		String keyWord = searchWord.toLowerCase();
		Intent intent = null;
		for(String word : exclusiveServicesTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, ExclusiveServices.class);
				return intent;
			}
		}
		for(String word : tiffinTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, TiffinService.class);
				return intent;
			}
		}
		for(String word : homeModTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, HomeModification.class);
				return intent;
			}
		}
		for(String word : doctorOnlineTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, DoctorOnline.class);
				return intent;
			}
		}
		for(String word : demandProductTriggers){
			if(keyWord.contains(word)) {
				intent = new Intent(context, EssentialServices.class);
				return intent;
			}
		}
		return intent;
	}
}
