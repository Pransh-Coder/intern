package com.example.intern;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter_local_networking extends RecyclerView.Adapter<MyAdapter_local_networking.ViewHolder> {
    private String [] param;
    private Context context;

    public MyAdapter_local_networking( Context context,String [] param ){
        this.param = param;
        this.context = context;
    }
    @NonNull
    @Override
    public MyAdapter_local_networking.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_local_networking,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull MyAdapter_local_networking.ViewHolder holder, int position) {
        holder.recycle_local_networking.setText(Html.fromHtml(param[position]));
    }
    @Override
    public int getItemCount() {
        return param.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recycle_local_networking;
        CheckBox cb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recycle_local_networking = itemView.findViewById(R.id.tv_recycle_local_networking);
            cb = itemView.findViewById(R.id.checkBox);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(cb.isChecked()) {
                        String str = recycle_local_networking.getText().toString();
                        int index = str.indexOf('\n');
                        str = str.substring(0,index);
                        Toast.makeText(context, str,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
