package com.example.android.homely.Booking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.homely.Data.DealData;
import com.example.android.homely.R;

import java.util.ArrayList;
import java.util.Locale;

public class AdminBookingsAdapter extends RecyclerView.Adapter<AdminBookingsAdapter.TodayBookingsViewHolder> implements Filterable {

    private Context context;
    private ArrayList<DealData> list;
    private ArrayList<DealData>listAll;

    public AdminBookingsAdapter(Context context, ArrayList<DealData> dealDataList) {
        this.context = context;
        this.list = dealDataList;
        this.listAll = dealDataList;
    }

    @NonNull
    @Override
    public TodayBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_bookings_item, parent, false);
        return new TodayBookingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayBookingsViewHolder holder, int position) {
        DealData dealData = list.get(position);
        holder.dbname.setText(dealData.getPropertyName());
        holder.dtime.setText(dealData.getDealTime());
        holder.ddate.setText(dealData.getDealDate());
        holder.dstatus.setText(dealData.getDealStatus());

        holder.bookingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminBookingActivity.class);
                intent.putExtra("dealData",dealData);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<DealData>result = new ArrayList<DealData>();

            if(charSequence.toString().isEmpty()){
                result = listAll;
            }else{
                for(DealData dealData : listAll){
                    if(dealData.getPropertyAddress().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        result.add(dealData);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = result;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list = (ArrayList<DealData>) filterResults.values;
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class TodayBookingsViewHolder extends RecyclerView.ViewHolder {
        TextView dbname, ddate, dtime, dstatus;
        LinearLayout bookingCard;
        public TodayBookingsViewHolder(@NonNull View itemView) {
            super(itemView);
            dbname = itemView.findViewById(R.id.dbname);
            ddate = itemView.findViewById(R.id.ddateTxt);
            dtime = itemView.findViewById(R.id.dtimeTxt);
            dstatus = itemView.findViewById(R.id.dstatusTxt);
            bookingCard = itemView.findViewById(R.id.bookingCard);
        }
    }
}
