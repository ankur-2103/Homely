package com.example.android.homely.Booking;

import android.content.Context;
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
import com.example.android.homely.Data.TourData;
import com.example.android.homely.Profile.MyTourAdapter;
import com.example.android.homely.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyBookingsAdapter extends RecyclerView.Adapter<MyBookingsAdapter.MyBookingsViewHolder> implements Filterable {

    private Context context;
    private ArrayList<DealData> list, listAll;
    private DatabaseReference user, deals;
    private FirebaseUser firebaseUser;

    public MyBookingsAdapter(Context context, ArrayList<DealData> list) {
        this.context = context;
        this.list = list;
        this.listAll = list;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = FirebaseDatabase.getInstance().getReference("User/"+firebaseUser.getUid()+"/my_deals");
        deals = FirebaseDatabase.getInstance().getReference("Deals");
    }

    @NonNull
    @Override
    public MyBookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_bookings_item, parent, false);
        return new MyBookingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingsViewHolder holder, int position) {
        DealData dealData = list.get(position);
        holder.dbname.setText(dealData.getPropertyName());
        holder.dtime.setText(dealData.getDealTime());
        holder.ddate.setText(dealData.getDealDate());
        holder.dstatus.setText(dealData.getDealStatus());

        holder.bookingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dealData.getDealStatus().equals("Pending") && holder.cancelDeal.getVisibility()==View.GONE){
                    holder.cancelDeal.setVisibility(View.VISIBLE);
                }else{
                    holder.cancelDeal.setVisibility(View.GONE);
                }
            }
        });

        holder.cancelDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(holder.getAdapterPosition());
                deals.child(dealData.getDealID()).removeValue();
                user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue()!=null){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                if(dataSnapshot.getValue().equals(dealData.getDealID())){
                                    user.child(dataSnapshot.getKey()).removeValue();
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
            ArrayList<DealData> result = new ArrayList<DealData>();

            if (charSequence.toString().isEmpty()){
                result = listAll;
            }else {
                for (DealData dealData : listAll){
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

    public class MyBookingsViewHolder extends RecyclerView.ViewHolder {
        TextView dbname, ddate, dtime, dstatus;
        MaterialButton cancelDeal;
        LinearLayout bookingCard;
        public MyBookingsViewHolder(@NonNull View itemView) {
            super(itemView);
            dbname = itemView.findViewById(R.id.dbname);
            ddate = itemView.findViewById(R.id.ddateTxt);
            dtime = itemView.findViewById(R.id.dtimeTxt);
            dstatus = itemView.findViewById(R.id.dstatusTxt);
            cancelDeal = itemView.findViewById(R.id.cancelDeal);
            bookingCard = itemView.findViewById(R.id.bookingCard);
        }
    }
}
