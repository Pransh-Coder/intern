package com.example.intern.mainapp;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

public class MainAppViewModel extends ViewModel {
	public DrawerLayout drawerLayout;
	private NavController navController;
	
	public NavController getNavController() {
		return navController;
	}
	
	public void setNavController(NavController navController) {
		this.navController = navController;
	}
}
