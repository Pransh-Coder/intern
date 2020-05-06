package com.example.intern;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intern.payment.BecomeAMember;

public class MyAdapter_services_bike extends RecyclerView.Adapter<MyAdapter_services_bike.ViewHolder> {
    private String[] param;
    private Context context;

    public interface OnItemClickListener {
        void onItemClicked(View v);
    }


    public MyAdapter_services_bike(Context context, String[] param) {
        this.param = param;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter_services_bike.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_two_wheeler, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter_services_bike.ViewHolder holder, int position) {
        holder.recycle_services.setText(Html.fromHtml(param[position]));
    }

    @Override
    public int getItemCount() {
        return param.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recycle_services;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recycle_services = itemView.findViewById(R.id.tv_recycle);
            LinearLayout ll = itemView.findViewById(R.id.ll_recycler);
            ll.setOnClickListener(v -> {
                createPopup();


            });
        }

        private void createPopup() {

            AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
            Button btn_pay=view.findViewById(R.id.bt_pop);
            TextView tv = view.findViewById(R.id.tv_pop3);
            tv.setText(Html.fromHtml("&#8226"));
            tv = view.findViewById(R.id.tv_new_pop31);
            tv.setText(Html.fromHtml("&#8226"));
            tv = view.findViewById(R.id.tv_new_pop33);
            tv.setText(Html.fromHtml("&#8226"));
            tv = view.findViewById(R.id.tv_new_pop35);
            tv.setText(Html.fromHtml("&#8226"));
            tv = view.findViewById(R.id.tv_pop71);
            tv.setText(Html.fromHtml("&#8226"));
            tv = view.findViewById(R.id.tv_pop73);
            tv.setText(Html.fromHtml("&#8226"));
            tv = view.findViewById(R.id.tv_pop75);
            tv.setText(Html.fromHtml("&#8226"));
            tv = view.findViewById(R.id.tv_pop77);
            tv.setText(Html.fromHtml("&#8226"));
            tv = view.findViewById(R.id.tv_pop79);
            tv.setText(Html.fromHtml("&#8226"));
            tv = view.findViewById(R.id.tv_pop711);
            tv.setText(Html.fromHtml("&#8226"));
            CheckBox cb1 = view.findViewById(R.id.rb_1);
            CheckBox cb2 = view.findViewById(R.id.rb_2);

            dialogbuilder.setView(view);
            AlertDialog dialog = dialogbuilder.create();
            dialog.show();
            btn_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!cb1.isChecked() && !cb2.isChecked()){
                        Toast.makeText(context, "Please select any one plan", Toast.LENGTH_SHORT).show();
                    }
                    else if(cb1.isChecked() && cb2.isChecked()){
                        Toast.makeText(context, "Please select only one plan", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Directing you to the payment gateway", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent=new Intent(context,BecomeAMember.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }

                }
            });

        }
    }
}




