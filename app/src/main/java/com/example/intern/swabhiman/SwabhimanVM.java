package com.example.intern.swabhiman;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

public class SwabhimanVM extends ViewModel {
	private NavController navController;
	private boolean hasAcceptedTnC;
	
	public NavController getNavController() {
		return navController;
	}
	
	public void setNavController(NavController navController) {
		this.navController = navController;
	}
	
	public boolean isHasAcceptedTnC() {
		return hasAcceptedTnC;
	}
	
	public void setHasAcceptedTnC(boolean hasAcceptedTnC) {
		this.hasAcceptedTnC = hasAcceptedTnC;
	}
}
