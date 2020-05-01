package com.example.intern.ReduceExpenses;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.intern.NewsAndUpdatesACT;
import com.example.intern.R;
import com.example.intern.databinding.ActivityReduceExpensesBinding;
import com.example.intern.mainapp.MainApp;

public class ReduceExpenses extends AppCompatActivity {
    private static String rupeeSymbol = "&#8377;";
    private static int healthInsuranceExpense , utilityBillExpense, otherExpense;
    ActivityReduceExpensesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReduceExpensesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.back.setOnClickListener(v-> onBackPressed());
        binding.home.setOnClickListener(v->{
            Intent intent = new Intent(this, MainApp.class);
            startActivity(intent);
            finish();
        });
        binding.notifi.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewsAndUpdatesACT.class);
            startActivity(intent);
        });
        //Set Dialogs for the last two
        binding.etHealthinsurance.setOnClickListener(v -> {
            CustomReduceExpenseDialog reduceExpenseDialog = new CustomReduceExpenseDialog("Health Insurance Expense", Html.fromHtml(rupeeSymbol).toString(), text -> {
                healthInsuranceExpense = text;
                binding.etHealthinsurance.setText(Integer.toString(text));
            }, healthInsuranceExpense);
            reduceExpenseDialog.show(getSupportFragmentManager(), "TEST1");
        });
        binding.etUtilitybills.setOnClickListener(v -> {
            CustomReduceExpenseDialog reduceExpenseDialog = new CustomReduceExpenseDialog("Utility Expense", Html.fromHtml(rupeeSymbol).toString(), text -> {
                utilityBillExpense = text;
                binding.etUtilitybills.setText(Integer.toString(text));
            }, utilityBillExpense);
            reduceExpenseDialog.show(getSupportFragmentManager(), "TEST2");
        });
        binding.etOthers.setOnClickListener(v -> {
            CustomReduceExpenseDialog reduceExpenseDialog = new CustomReduceExpenseDialog("Other Expense", Html.fromHtml(rupeeSymbol).toString(), text -> {
                otherExpense = text;
                binding.etOthers.setText(Integer.toString(text));
            }, otherExpense);
            reduceExpenseDialog.show(getSupportFragmentManager(), "TEST3");
        });
    }
    
    interface textListener{
        void setText(int text);
    }

    public static class CustomReduceExpenseDialog extends DialogFragment{
        private String title, hint;
        private int previousText = 0;
        private TextView mTitle;
        private  EditText mDetails;
        private TextView mOK;
        private textListener textListener;
    
        public CustomReduceExpenseDialog(String title, String hint , textListener textListener, int previousText){
            this.textListener = textListener;
            this.title = title;
            this.hint = hint;
            if(previousText != 0)this.previousText = previousText;
        }
    
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
    
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.enter_number_dialog, container, false);
            return v;
        }
    
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mTitle = view.findViewById(R.id.tv_title);
            mDetails = view.findViewById(R.id.et_detail);
            mOK = view.findViewById(R.id.ok_button);
            mOK.setOnClickListener(v -> dismiss());
            mTitle.setText(title);
            mDetails.setHint(hint);
            if(previousText!=0)mDetails.setText(Integer.toString(previousText));
        }
    
        @Override
        public void onStart() {
            super.onStart();
            mDetails.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
                }
    
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
        
                }
    
                @Override
                public void afterTextChanged(Editable s) {
                    int result = 0;
                    try{
                        result = Integer.parseInt(s.toString());
                    }catch (Exception ignored){}
                    textListener.setText(result);
                }
            });
        }
    }
}
