package com.example.android.homely.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.android.homely.PropertyProfile;
import com.example.android.homely.R;
import com.example.android.homely.Data.PropertyData;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> implements Filterable {

    private Context context;
    private ArrayList<PropertyData> propertyDataList;
    private ArrayList<PropertyData> propertyDataListAll;

    public HomeAdapter(Context context, ArrayList<PropertyData> propertyDataList){
        this.context = context;
        this.propertyDataList = propertyDataList;
        this.propertyDataListAll = propertyDataList;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_property_item, parent, false);
        return new HomeViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        PropertyData propertyData = propertyDataList.get(position);
        holder.bname.setText(propertyData.getBuilding_name());
        holder.propertyLoc.setText(propertyData.getCity_name()+", "+propertyData.getState());
        holder.price.setText("₹"+propertyData.getMonthly_rent());
        Uri uri = Uri.parse(propertyData.getFuri());
        try {
            Picasso.get().load(uri).into(holder.imageView);
        }catch (Exception e){
            Log.e("pic", "onBindViewHolder: "+e.toString());
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PropertyProfile.class);
                intent.putExtra("propertyData", propertyData);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return propertyDataList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected Filter.FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<PropertyData>result = new ArrayList<PropertyData>();

            if(charSequence.toString().isEmpty()){
                result = propertyDataListAll;
            }else{
                for (PropertyData propertyData : propertyDataListAll){
                    if (propertyData.getFaddress().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        result.add(propertyData);
                    }
                }
            }

            FilterResults filterResults = new Filter.FilterResults();
            filterResults.values = result;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            propertyDataList = (ArrayList<PropertyData>) filterResults.values;
            notifyDataSetChanged();
        }
    };

    public class HomeViewHolder extends ViewHolder {
        TextView bname, propertyLoc,price;
        ImageView imageView;
        MaterialCardView cardView;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            bname = itemView.findViewById(R.id.bname);
            propertyLoc = itemView.findViewById(R.id.propertyLoc);
            price = itemView.findViewById(R.id.price);
            cardView = itemView.findViewById(R.id.homeCardInfo);
        }
    }
}
