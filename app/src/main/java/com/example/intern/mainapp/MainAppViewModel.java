package com.example.intern.mainapp;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.example.intern.database.SharedPrefUtil;

public class MainAppViewModel extends ViewModel {
	public DrawerLayout drawerLayout;
	private NavController navController;
	private SharedPrefUtil prefUtil;
	
	public NavController getNavController() {
		return navController;
	}
	
	public void setNavController(NavController navController) {
		this.navController = navController;
	}
	
	public SharedPrefUtil getPrefUtil() {
		return prefUtil;
	}
	
	public void setPrefUtil(SharedPrefUtil prefUtil) {
		this.prefUtil = prefUtil;
	}
}
