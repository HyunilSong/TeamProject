package com.team.drawing_share.ui.read;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team.drawing_share.R;

import java.util.ArrayList;

public class ReadAdapter extends RecyclerView.Adapter<ReadAdapter.CustomViewHolder> {

    private ArrayList<Idea> arrayList;
    private Context context;


    public ReadAdapter(ArrayList<Idea> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image;
        TextView tv_title;
        TextView tv_writer;
        TextView tv_date;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_image = itemView.findViewById(R.id.iv_image);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_writer = itemView.findViewById(R.id.tv_writer);
            this.tv_date = itemView.findViewById(R.id.tv_date);
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_read_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImage())
                .into(holder.iv_image);
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_writer.setText(arrayList.get(position).getWriter());
        holder.tv_date.setText(arrayList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }


}
