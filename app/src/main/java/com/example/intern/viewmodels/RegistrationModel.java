package com.example.intern.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

public class RegistrationModel extends ViewModel {
	private NavController appFlowController;
	private NavController registrationFlowController;
	
	public NavController getRegistrationFlowController(){
		return this.registrationFlowController;
	}
	
	public void setRegistrationFlowController(NavController registrationFlowController){
		this.registrationFlowController = registrationFlowController;
	}
	
	public NavController getAppFlowController(){
		return this.appFlowController;
	}
	
	public void setAppFlowController(NavController appFlowController){
		this.appFlowController = appFlowController;
	}
}
