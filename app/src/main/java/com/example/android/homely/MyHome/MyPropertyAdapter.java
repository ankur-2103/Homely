package com.example.android.homely.MyHome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.homely.R;
import com.example.android.homely.Data.PropertyData;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPropertyAdapter extends RecyclerView.Adapter<MyPropertyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<PropertyData>list;

    public MyPropertyAdapter(Context context, ArrayList<PropertyData> list) {
        this.context = context;
        this.list = list;
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
            Log.e("err", "Error : "+e);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PropertyActivity.class);
                intent.putExtra("propertyData", propertyData);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bname, bath, bed, area, price;
        ImageView imageView;
        MaterialCardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            bname = itemView.findViewById(R.id.bname);
            bed = itemView.findViewById(R.id.bed);
            bath = itemView.findViewById(R.id.bath);
            area = itemView.findViewById(R.id.area);
            price = itemView.findViewById(R.id.price);
            cardView = itemView.findViewById(R.id.myPropertyInfo);
        }
    }
}
