package com.example.android.homely.MyHome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.homely.R;
import com.example.android.homely.data.PropertyData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPropertyAdapter extends RecyclerView.Adapter<MyPropertyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<PropertyData>list;
    private ItemClickListener itemClickListener;

    public MyPropertyAdapter(Context context, ArrayList<PropertyData> list, ItemClickListener itemClickListener) {
        this.context = context;
        this.list = list;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_property_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PropertyData propertyData = list.get(position);
        holder.bname.setText(propertyData.getBuilding_name());
        holder.bath.setText(propertyData.getBathrooms());
        holder.bed.setText(propertyData.getBedrooms());
        holder.area.setText(propertyData.getArea_length()+" x "+propertyData.getArea_width());
        holder.price.setText("₹ "+propertyData.getMonthly_rent());
        Uri uri = Uri.parse(propertyData.getFuri());
        try {
            Picasso.get().load(uri).into(holder.imageView);
        }catch (Exception e){
            Log.e("pic", "onBindViewHolder: "+e.toString());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(list.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface ItemClickListener{
        void onItemClick(PropertyData propertyData, int position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bname, bath, bed, area, price;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            bname = itemView.findViewById(R.id.bname);
            bed = itemView.findViewById(R.id.bed);
            bath = itemView.findViewById(R.id.bath);
            area = itemView.findViewById(R.id.area);
            price = itemView.findViewById(R.id.price);
        }
    }
}
