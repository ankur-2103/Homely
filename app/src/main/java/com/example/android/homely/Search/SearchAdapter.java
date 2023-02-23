package com.example.android.homely.Search;

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

import com.example.android.homely.PropertyProfile;
import com.example.android.homely.R;
import com.example.android.homely.Data.FilterData;
import com.example.android.homely.Data.PropertyData;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewholder> implements Filterable {

    private Context context;
    private ArrayList<PropertyData> propertyDataList;
    private ArrayList<PropertyData> propertyDataListAll;
    private FilterData filterData;

    public SearchAdapter(Context context, ArrayList<PropertyData> propertyDataList) {
        this.context = context;
        this.propertyDataList = propertyDataList;
        this.propertyDataListAll = propertyDataList;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_property_item, parent, false);
        return new SearchAdapter.SearchViewholder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewholder holder, int position) {
        PropertyData propertyData = propertyDataList.get(position);
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

    public class SearchViewholder extends RecyclerView.ViewHolder {
        TextView bname, bath, bed, area, price;
        ImageView imageView;
        MaterialCardView cardView;
        public SearchViewholder(@NonNull View itemView) {
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

//    if (charSequence.toString().isEmpty() && filterData==null){
//        result = propertyDataListAll;
//    }else if(charSequence.toString().isEmpty() && filterData!=null){
//        result = getFilteredData();
//    }else if (!charSequence.toString().isEmpty() && filterData==null){
//        for (PropertyData propertyData : propertyDataListAll){
//            if (propertyData.getFaddress().toLowerCase().contains(charSequence.toString().toLowerCase())){
//                Log.w("searchTxt", propertyData.getFaddress());
//                result.add(propertyData);
//            }
//        }
//    }else{
//        result = getFilteredDatas(charSequence);
//    }

//    private ArrayList<PropertyData> getFilteredDatas(CharSequence charSequence) {
//        ArrayList<PropertyData>result = new ArrayList<PropertyData>();
//        ArrayList<PropertyData>mockData = propertyDataListAll;
//        for (int i = 0; i < mockData.size(); i++) {
//            PropertyData propertyData = mockData.get(i);
//            if (propertyData.getFaddress().toLowerCase().contains(charSequence.toString().toLowerCase())){
//                result.add(propertyData);
//                mockData.remove(i);
//            }
//        }
//        if (filterData.getProperty()!=null && !filterData.getProperty().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                if (propertyData.getProperty().toLowerCase().contains(filterData.getProperty().toLowerCase())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getPriceMax()!=null && filterData.getPriceMin()!=null && !filterData.getPriceMax().isEmpty() && !filterData.getPriceMin().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                if (Integer.parseInt(propertyData.getMonthly_rent())>=Integer.parseInt(filterData.getAreaMin()) && Integer.parseInt(propertyData.getMonthly_rent())<=Integer.parseInt(filterData.getAreaMax())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getBed()!=null && filterData.getBed().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                if (propertyData.getBedrooms().toLowerCase().equals(filterData.getBed().toLowerCase())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getBath()!=null && filterData.getBath().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                if (propertyData.getBathrooms().toLowerCase().equals(filterData.getBath().toLowerCase())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getAreaMin()!=null && filterData.getAreaMin().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                float x = Float.parseFloat(propertyData.getArea_length());
//                float y = Float.parseFloat(propertyData.getArea_width());
//                if (x*y>=Float.parseFloat(filterData.getAreaMin())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getAreaMax()!=null && filterData.getAreaMax().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                float x = Float.parseFloat(propertyData.getArea_length());
//                float y = Float.parseFloat(propertyData.getArea_width());
//                if (x*y<=Float.parseFloat(filterData.getAreaMin())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getPropertyType()!=null && !filterData.getPropertyType().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                if (filterData.getPropertyType().contains(propertyData.getProperty_type())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        return result;
//    }
//
//    private ArrayList<PropertyData> getFilteredData() {
//        ArrayList<PropertyData>result = new ArrayList<PropertyData>();
//        ArrayList<PropertyData>mockData = propertyDataListAll;
//        if (filterData.getProperty()!=null && !filterData.getProperty().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                if (propertyData.getProperty().toLowerCase().contains(filterData.getProperty().toLowerCase())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getPriceMax()!=null && filterData.getPriceMin()!=null && !filterData.getPriceMax().isEmpty() && !filterData.getPriceMin().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                if (Integer.parseInt(propertyData.getMonthly_rent())>=Integer.parseInt(filterData.getAreaMin()) && Integer.parseInt(propertyData.getMonthly_rent())<=Integer.parseInt(filterData.getAreaMax())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getBed()!=null && filterData.getBed().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                if (propertyData.getBedrooms().toLowerCase().equals(filterData.getBed().toLowerCase())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getBath()!=null && filterData.getBath().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                if (propertyData.getBathrooms().toLowerCase().equals(filterData.getBath().toLowerCase())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getAreaMin()!=null && filterData.getAreaMin().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                float x = Float.parseFloat(propertyData.getArea_length());
//                float y = Float.parseFloat(propertyData.getArea_width());
//                if (x*y>=Float.parseFloat(filterData.getAreaMin())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getAreaMax()!=null && filterData.getAreaMax().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                float x = Float.parseFloat(propertyData.getArea_length());
//                float y = Float.parseFloat(propertyData.getArea_width());
//                if (x*y<=Float.parseFloat(filterData.getAreaMin())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        if (filterData.getPropertyType()!=null && !filterData.getPropertyType().isEmpty()){
//            for (int i = 0; i< mockData.size(); i++){
//                PropertyData propertyData = mockData.get(i);
//                if (filterData.getPropertyType().contains(propertyData.getProperty_type())){
//                    result.add(propertyData);
//                    mockData.remove(i);
//                }
//            }
//        }
//        return result;
//    }
}
