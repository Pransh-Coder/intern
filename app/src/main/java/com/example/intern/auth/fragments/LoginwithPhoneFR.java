package com.example.intern.auth.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intern.R;
import com.example.intern.auth.viewmodel.AuthViewModel;
import com.example.intern.databinding.PhoneLoginUiBinding;
import com.example.intern.mainapp.MainApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginwithPhoneFR  extends Fragment {

    private static String TAG = PhoneRegistrationFR.class.getSimpleName();
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog loadingbar;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private PhoneLoginUiBinding binding;
    private AuthViewModel viewModel;
    private ProgressDialog progressDialog;
    String phnno;


    public LoginwithPhoneFR() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = PhoneLoginUiBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        View view = binding.getRoot();
        progressDialog = new ProgressDialog(getContext());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadingbar = new ProgressDialog(requireContext());
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingbar.dismiss();
                Toast.makeText(requireContext(), "Invalid please enter correct phone number with your country code", Toast.LENGTH_LONG).show();
            }

            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                loadingbar.dismiss();
                Toast.makeText(requireContext(), "Code sent", Toast.LENGTH_LONG).show();
                binding.etPhoneNumber.setVisibility(View.INVISIBLE);
            }
        };
        binding.btnOtp.setOnClickListener(v -> {
            phnno = binding.etPhoneNumber.getText().toString();
            if (TextUtils.isEmpty(phnno)) {
                binding.etPhoneNumber.setError("Phone Number is invalid");
            } else {
                loadingbar.setTitle("Phone Verification");
                loadingbar.setMessage("Please wait,while we authenticate your phone");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                binding.btnOtp.setVisibility(View.INVISIBLE);
                binding.etPhoneNumber.setVisibility(View.INVISIBLE);
                binding.tvOTP.setVisibility(View.INVISIBLE);
                binding.code91.setVisibility(View.INVISIBLE);
                binding.etOtp.setVisibility(View.VISIBLE);
                binding.btnCancel.setVisibility(View.VISIBLE);
                binding.btnLogin.setVisibility(View.VISIBLE);
                binding.btnResendotp.setVisibility(View.VISIBLE);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + phnno,        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        requireActivity(),               // Activity (for callback binding)
                        callbacks);        // OnVerificationStateChangedCallbacks
            }
        });
        binding.btnResendotp.setOnClickListener(v ->
        {
            loadingbar.setTitle("Phone Verification");
            loadingbar.setMessage("Resending code...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + phnno,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    requireActivity(),               // Activity (for callback binding)
                    callbacks);
        });
        binding.btnLogin.setOnClickListener(v -> {
            String code = binding.etOtp.getText().toString();
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(requireContext(), "Please enter the code", Toast.LENGTH_LONG).show();
            } else {
                loadingbar.setTitle("Code Verification");
                loadingbar.setMessage("Please wait,while we are verifying");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        viewModel.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingbar.dismiss();
                            checkExistence();
                        } else {
                            String msg = task.getException().toString();
                            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show();
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
                        viewModel.getPrefUtil().updateSharedPrefsPostLogin(document);
                        Intent intent = new Intent(getContext(), MainApp.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if(state.equals("1")){
                        progressDialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getContext(),"User already Logged in from another device...Logout First!!",Toast.LENGTH_LONG).show();
                        viewModel.getNavController().navigate(R.id.action_LoginwithPhoneFR_toLoginRegister);

                    }
                    else
                        Toast.makeText(getContext(),"inelse"+state,Toast.LENGTH_LONG).show();

                }else{
                    progressDialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getContext(),"User does not exist...Signup First!!",Toast.LENGTH_LONG).show();
                    viewModel.getNavController().navigate(R.id.action_LoginwithPhoneFR_toLoginRegister);


                }
            }
        });

    }
}