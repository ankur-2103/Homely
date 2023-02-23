package com.example.android.homely.Tour;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.homely.Data.TourData;
import com.example.android.homely.R;

import java.util.ArrayList;

public class AdminTourAdapter extends RecyclerView.Adapter<AdminTourAdapter.TourViewHolder> implements Filterable {

    private Context context;
    private ArrayList<TourData> tourDataList, tourDataListAll;
    private AdminTourAdapter.ItemClickListener itemClickListener;

    public AdminTourAdapter(Context context, ArrayList<TourData> tourDataArrayList, AdminTourAdapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.tourDataList = tourDataArrayList;
        this.tourDataListAll = tourDataArrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tour_item, parent, false);
        return new AdminTourAdapter.TourViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        TourData tourData = tourDataList.get(position);
        holder.bname.setText(tourData.getPropertyName());
        holder.date.setText(tourData.getTourDate());
        holder.time.setText(tourData.getTourTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(tourDataList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tourDataList.size();
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<TourData> result = new ArrayList<TourData>();

            if (charSequence.toString().isEmpty()){
                result = tourDataListAll;
            }else{
                for (TourData tourData : tourDataListAll){
                    if(tourData.getPropertyLoc().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        result.add(tourData);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = result;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            tourDataList = (ArrayList<TourData>) filterResults.values;
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return null;
    }

    public interface ItemClickListener{
        void onItemClick(TourData tourData, int position);
    }

    public class TourViewHolder extends RecyclerView.ViewHolder {
        TextView bname, date, time;
        ImageView imageView;
        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            bname = itemView.findViewById(R.id.bname);
            date = itemView.findViewById(R.id.dateTxt);
            time = itemView.findViewById(R.id.timeTxt);
        }
    }
}
