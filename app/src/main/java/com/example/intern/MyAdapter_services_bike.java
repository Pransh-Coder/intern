package com.example.intern;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter_services_bike extends RecyclerView.Adapter<MyAdapter_services_bike.ViewHolder>{
    private String[] param;
    private Context context;

    public interface OnItemClickListener{
        void onItemClicked(View v);
    }

    private OnItemClickListener listener;

    public MyAdapter_services_bike(Context context, String[] param, OnItemClickListener listener) {
        this.param = param;
        this.context = context;
        this.listener = listener;
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
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(v);
                }
            });
        }
    }
}

