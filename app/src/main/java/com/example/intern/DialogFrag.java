package com.example.intern;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogFrag extends DialogFragment {
    View view;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.dialog,new LinearLayout(getActivity()),false);
        TextView tv = view.findViewById(R.id.tv_pop3);
        tv.setText(Html.fromHtml("&#8226 4 services per year<br>&#8226 Free pick and drop service<br>&#8226 Roadside Assistance service<br>&#8226 Insurance Support"));
        tv = view.findViewById(R.id.tv_pop7);
        tv.setText(Html.fromHtml("&#8226 4 services per year<br>&#8226 Free pick and drop service<br>&#8226 Roadside Assistance service<br>&#8226 Insurance Support<br>&#8226 10% discount on labour<br>&#8226 2 Lubricants free"));

        CheckBox cb1 = view.findViewById(R.id.rb_1);
        CheckBox cb2 = view.findViewById(R.id.rb_2);
        Button bt = view.findViewById(R.id.bt_pop);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cb1.isChecked() && !cb2.isChecked()){
                    Toast.makeText(getActivity(), "Please select any one plan", Toast.LENGTH_SHORT).show();
                }
                else if(cb1.isChecked() && cb2.isChecked()){
                    Toast.makeText(getActivity(), "Please select only one plan", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Directing you to the payment gateway", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        builder.setContentView(view);
        return builder;
    }
}
