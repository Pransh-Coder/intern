package com.example.intern.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.intern.R;
import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.databinding.ActivityAuthBinding;
import com.example.intern.mainapp.MainApp;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {
	private static String TAG = AuthActivity.class.getSimpleName();
	private ActivityAuthBinding binding;
	private AuthViewModel viewModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityAuthBinding.inflate(getLayoutInflater());
		viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
		setContentView(binding.getRoot());
		FirebaseApp firebaseApp = FirebaseApp.initializeApp(this);
		NavController navController = Navigation.findNavController(this, R.id.auth_nav_host_fr);
		viewModel.setNavController(navController);
		//TODO:Create splash screen
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();
		viewModel.setFirebaseApp(firebaseApp);
		viewModel.setGoogleSignInClient(GoogleSignIn.getClient(this, gso));
		viewModel.setFirebaseAuth(FirebaseAuth.getInstance(viewModel.getFirebaseApp()));
		viewModel.setFirebaseUser(viewModel.getFirebaseAuth().getCurrentUser());
		viewModel.setLoggedInListener(isLoggedIn -> {
			if(isLoggedIn){
				Intent intent = new Intent(AuthActivity.this, MainApp.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
