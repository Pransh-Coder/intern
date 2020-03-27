package com.example.intern.auth.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {
	private NavController navController;
	private GoogleSignInClient googleSignInClient;
	private FirebaseApp firebaseApp;
	private FirebaseAuth firebaseAuth;
	private FirebaseUser firebaseUser;
	
	public GoogleSignInClient getGoogleSignInClient() {
		return googleSignInClient;
	}
	
	public void setGoogleSignInClient(GoogleSignInClient googleSignInClient) {
		this.googleSignInClient = googleSignInClient;
	}
	public FirebaseAuth getFirebaseAuth() {
		return firebaseAuth;
	}
	
	public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
		this.firebaseAuth = firebaseAuth;
	}
	
	public FirebaseApp getFirebaseApp() {
		return firebaseApp;
	}
	
	public void setFirebaseApp(FirebaseApp firebaseApp) {
		this.firebaseApp = firebaseApp;
	}
	
	public FirebaseUser getFirebaseUser() {
		return firebaseUser;
	}
	
	public void setFirebaseUser(FirebaseUser firebaseUser) {
		this.firebaseUser = firebaseUser;
	}
	
	public NavController getNavController() {
		return navController;
	}
	
	public void setNavController(NavController navController) {
		this.navController = navController;
	}
}
