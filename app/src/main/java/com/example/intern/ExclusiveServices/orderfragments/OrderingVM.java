package com.example.intern.ExclusiveServices.orderfragments;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.example.intern.database.SharedPrefUtil;

import java.sql.Struct;

public class OrderingVM extends ViewModel {
	private SharedPrefUtil prefUtil;
	private NavController navController;
	private Bitmap orderImageBitmap = null;
	private String orderDetailString = null;
	private ImageReceivedListener imageReceivedListener;
	private boolean hasChosenHomeDelivery;
	private String chosenVendorID;
	private String uid;
	
	public NavController getNavController() {
		return navController;
	}
	
	public void setNavController(NavController navController) {
		this.navController = navController;
	}
	
	public Bitmap getOrderImageBitmap() {
		return orderImageBitmap;
	}
	
	public void setOrderImageBitmap(Bitmap orderImageBitmap) {
		this.orderImageBitmap = orderImageBitmap;
	}
	
	public String getOrderDetailString() {
		return orderDetailString;
	}
	
	public void setOrderDetailString(String orderDetailString) {
		this.orderDetailString = orderDetailString;
	}
	
	public ImageReceivedListener getImageReceivedListener() {
		return imageReceivedListener;
	}
	
	public void setImageReceivedListener(ImageReceivedListener imageReceivedListener) {
		this.imageReceivedListener = imageReceivedListener;
	}
	
	public boolean isHasChosenHomeDelivery() {
		return hasChosenHomeDelivery;
	}
	
	public void setHasChosenHomeDelivery(boolean hasChosenHomeDelivery) {
		this.hasChosenHomeDelivery = hasChosenHomeDelivery;
	}
	
	public SharedPrefUtil getPrefUtil() {
		return prefUtil;
	}
	
	public void setPrefUtil(SharedPrefUtil prefUtil) {
		this.prefUtil = prefUtil;
	}
	
	public String getChosenVendorID() {
		return chosenVendorID;
	}
	
	public void setChosenVendorID(String chosenVendorID) {
		this.chosenVendorID = chosenVendorID;
	}


	public String getuID() {
		return uid;

	}
	public void setuID(String uid) {
		this.uid=uid;

	}

	public interface ImageReceivedListener{
		void hasReceivedImage(boolean b);
	}
}
