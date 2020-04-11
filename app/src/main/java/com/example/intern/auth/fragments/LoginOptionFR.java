package com.example.intern.auth.fragments;

import androidx.fragment.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.intern.R;
import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.databinding.FragmentRegistrationOptionsFRBinding;
import com.example.intern.mainapp.MainApp;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
public class LoginOptionFR extends Fragment {


    private static String TAG = RegistrationOptionsFR.class.getSimpleName();
    private int G_SIGN_IN_REQ_CODE = 12;
    private FragmentRegistrationOptionsFRBinding binding;
    private AuthViewModel viewModel;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private ProgressDialog progressDialog;

    public LoginOptionFR() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mCallbackManager = CallbackManager.Factory.create();
        progressDialog=new ProgressDialog(getContext());
        mAuth=FirebaseAuth.getInstance();
        binding = FragmentRegistrationOptionsFRBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //TODO:
        binding.btnGoogleSignup.setOnClickListener(v->{
            Intent intent = viewModel.getGoogleSignInClient().getSignInIntent();
            startActivityForResult(intent, G_SIGN_IN_REQ_CODE);
        });
        binding.btnSignupPhone.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_LoginOptionFR_to_LoginwithPhone);
        });
        binding.btnSignupFb.setOnClickListener(v->{
            LoginManager.getInstance().logInWithReadPermissions(LoginOptionFR.this, Arrays.asList("email"));
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("myfb", "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d("myfb", "facebook:onCancel");
                    // ...
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d("myfb", "facebook:onError", error);
                    // ...
                }
            });

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == G_SIGN_IN_REQ_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                Log.d(TAG, "onActivityResult: g sign in");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                Log.d(TAG, "onActivityResult: failed g sign in");
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        viewModel.getFirebaseAuth().signInWithCredential(credential).addOnSuccessListener(authResult -> {
            if(authResult.getUser() != null){
                Log.d(TAG, "firebaseAuthWithGoogle: success");
                checkExistence();
            }else{
                Log.d(TAG, "firebaseAuthWithGoogle: dismissed");
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("myfb", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkExistence();
                            // ...
                        } else {
                            Log.d(TAG, "firebaseAuthWithFacebook: dismissed");
                        }
                    }
                });

    }
    private void checkExistence() {
        String currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("We are Fetching Your Information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        //Toast.makeText(getContext(), "No such user exists", Toast.LENGTH_LONG).show();
        FirebaseFirestore.getInstance().collection("Users").document(currentuserid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    String state = document.getString("LS");
                    //Toast.makeText(getContext(),"before"+state,Toast.LENGTH_LONG).show();
                    if (state.equals("0")) {
                        //  Toast.makeText(getContext(),state,Toast.LENGTH_LONG).show();
                        Map<String, Object> data = new HashMap<>();
                        data.put("LS","1");
                        FirebaseFirestore.getInstance().collection("Users").document(currentuserid)
                                .update(data);
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),state,Toast.LENGTH_LONG).show();
                        viewModel.setFirebaseUser(viewModel.getFirebaseAuth().getCurrentUser());
                        Intent intent = new Intent(getContext(), MainApp.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if(state.equals("1")){
                        progressDialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        //Toast.makeText(getContext(),"User already Logged in from another device...Logout First!!",Toast.LENGTH_LONG).show();
                        viewModel.getNavController().navigate(R.id.action_LoginOptionFR_to_LoginResgister);

                    }
                    else
                        Toast.makeText(getContext(),"inelse"+state,Toast.LENGTH_LONG).show();

                }else{
                    progressDialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getContext(),"User does not exist...Signup First!!",Toast.LENGTH_LONG).show();
                    viewModel.getNavController().navigate(R.id.action_LoginOptionFR_to_LoginResgister);


                }
            }
        });

    }


}

