package com.example.android.homely.Tour;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.Data.TourData;
import com.example.android.homely.Home.HomeAdapter;
import com.example.android.homely.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PendingTourAdapter extends RecyclerView.Adapter<PendingTourAdapter.TourViewHolder> {

    private Context context;
    private ArrayList<TourData> tourDataList;
    private PendingTourAdapter.ItemClickListener itemClickListener;

    public PendingTourAdapter(Context context, ArrayList<TourData> tourDataArrayList, PendingTourAdapter.ItemClickListener itemClickListener) {
        this.context = context;
        this.tourDataList = tourDataArrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tour_item, parent, false);
        return new PendingTourAdapter.TourViewHolder(view);
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
