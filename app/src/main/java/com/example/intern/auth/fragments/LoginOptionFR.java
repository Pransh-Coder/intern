package com.example.intern.auth.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.intern.databinding.LoginUiBinding;
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
public class LoginOptionFR extends Fragment {


    private static String TAG = RegistrationOptionsFR.class.getSimpleName();
    private int G_SIGN_IN_REQ_CODE = 12;
    private LoginUiBinding binding;
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
        binding = LoginUiBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //TODO:
        binding.googleSignIn.setOnClickListener(v->{
            Intent intent = viewModel.getGoogleSignInClient().getSignInIntent();
            startActivityForResult(intent, G_SIGN_IN_REQ_CODE);
        });
        binding.phoneSignIn.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_LoginOptionFR_to_LoginwithPhone);
        });
        binding.facebookSignIn.setOnClickListener(v->{
            new AlertDialog.Builder(getContext()).setTitle("Sorry for inconvenience").setMessage("Due to COVID-19 global pandemic and nationwide lock-downs, our vendors are not available. Stay tuned for further updates")
                    .setIcon(R.drawable.pslogotrimmed).setPositiveButton("I understand", null).show();
            /*LoginManager.getInstance().logInWithReadPermissions(LoginOptionFR.this, Arrays.asList("email"));
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

             */

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
                            viewModel.getFirebaseAuth().signInWithCredential(credential).addOnSuccessListener(authResult -> {
                                        if (authResult.getUser() != null) {
                                            /*FireStoreUtil.getUserDocumentReference(requireContext(), authResult.getUser().getUid()).addSnapshotListener((snapshot, e) -> {
                                                if (snapshot != null && snapshot.exists()) {
                                                    viewModel.getPrefUtil().updateSharedPrefsPostLogin(snapshot);
                                                    //viewModel.getLoggedInListener().isLoggedIn(true);
                                                }
                                            });*/
                                            checkExistence();
                                        }
                                    });
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
                  if(document.exists()){
                        //  Toast.makeText(getContext(),state,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        viewModel.setFirebaseUser(viewModel.getFirebaseAuth().getCurrentUser());
                        viewModel.getPrefUtil().updateSharedPrefsPostLogin(document);
                        Intent intent = new Intent(getContext(), MainApp.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getContext(),"Unable to fetch info...",Toast.LENGTH_LONG).show();

                }else{
                    progressDialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getContext(),"User does not exist...Signup First!!",Toast.LENGTH_LONG).show();
                    viewModel.getNavController().navigate(R.id.action_LoginOptionFR_to_LoginResgister);
                }
            }
        });

    }

    private void signoutUser() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        });
    }


}

