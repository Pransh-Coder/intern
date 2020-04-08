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

public class MyAdapter_lifestyle extends RecyclerView.Adapter<MyAdapter_lifestyle.ViewHolder> {
    private String[] param;
    private Context context;

    public MyAdapter_lifestyle(Context context, String[] param) {
        this.param = param;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter_lifestyle.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_lifestyle, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter_lifestyle.ViewHolder holder, int position) {
        holder.recycle_lifestyle.setText(Html.fromHtml(param[position]));
    }

    @Override
    public int getItemCount() {
        return param.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recycle_lifestyle;
        CheckBox cb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recycle_lifestyle = itemView.findViewById(R.id.tv_recycle_lifestyle);
            cb = itemView.findViewById(R.id.checkBox);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (cb.isChecked()) {
                        String str = recycle_lifestyle.getText().toString();
                        int index = str.indexOf('\n');
                        str = str.substring(0, index);
                        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
