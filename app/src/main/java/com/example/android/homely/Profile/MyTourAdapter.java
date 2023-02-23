package com.example.android.homely.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.homely.Data.PropertyData;
import com.example.android.homely.Data.TourData;
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

public class MyTourAdapter extends RecyclerView.Adapter<MyTourAdapter.MyTourViewHolder> implements Filterable {

    private Context context;
    private ArrayList<TourData> list;
    private ArrayList<TourData>listAll;
    private DatabaseReference user, tours;
    private FirebaseUser firebaseUser;

    public MyTourAdapter(Context context, ArrayList<TourData> list) {
        this.context = context;
        this.list = list;
        this.listAll = list;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        user = FirebaseDatabase.getInstance().getReference("User/"+firebaseUser.getUid()+"/my_tour");
        tours = FirebaseDatabase.getInstance().getReference("Tour");
    }

    @NonNull
    @Override
    public MyTourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tour_item_card, parent, false);
        return new MyTourViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyTourViewHolder holder, int position) {
        TourData tourData = list.get(position);
        holder.tbname.setText(tourData.getPropertyName());
        holder.tdate.setText(tourData.getTourDate());
        holder.ttime.setText(tourData.getTourTime());
        holder.tstatus.setText(tourData.getStatus());
        if(!tourData.getStatus().equals("Pending")){
            holder.gname.setText(tourData.getAgentName());
            holder.gphone.setText(tourData.getAgentPhoneNumber());
            holder.gemail.setText(tourData.getAgentEmail());
            holder.desc.setText(tourData.getDescription());
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tourData.getStatus().equals("Accepted") && holder.guide.getVisibility()==View.GONE){
                    holder.guide.setVisibility(View.VISIBLE);
                    if (holder.description.getVisibility()==View.GONE && !tourData.getDescription().equals("null")){
                        holder.description.setVisibility(View.VISIBLE);
                    }
                }else if(tourData.getStatus().equals("Rejected") && holder.description.getVisibility()==View.GONE && !tourData.getDescription().equals("null")){
                    holder.description.setVisibility(View.VISIBLE);
                }else if(tourData.getStatus().equals("Pending") && holder.cancelTour.getVisibility()==View.GONE) {
                    holder.cancelTour.setVisibility(View.VISIBLE);
                }else{
                    holder.guide.setVisibility(View.GONE);
                    holder.description.setVisibility(View.GONE);
                    holder.cancelTour.setVisibility(View.GONE);
                }
            }
        });

        holder.cancelTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cancelTour.setVisibility(View.GONE);
                list.remove(holder.getAdapterPosition());
                tours.child(tourData.getTourID()).removeValue();
                user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue()!=null){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                if(dataSnapshot.getValue().equals(tourData.getTourID())){
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
            ArrayList<TourData> result = new ArrayList<TourData>();

            if (charSequence.toString().isEmpty()){
                result = listAll;
            }else{
                for (TourData tourData : listAll){
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
            list = (ArrayList<TourData>) filterResults.values;
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class MyTourViewHolder extends RecyclerView.ViewHolder {
        TextView tbname, tdate, ttime, tstatus, gname, gphone, gemail, desc;
        LinearLayout guide, description, card;
        MaterialButton cancelTour;
        public MyTourViewHolder(@NonNull View itemView) {
            super(itemView);
            tbname = itemView.findViewById(R.id.tbname);
            tdate = itemView.findViewById(R.id.tdateTxt);
            ttime = itemView.findViewById(R.id.ttimeTxt);
            tstatus = itemView.findViewById(R.id.tstatusTxt);
            gname = itemView.findViewById(R.id.gNameTxt);
            gphone = itemView.findViewById(R.id.gphoneTxt);
            gemail = itemView.findViewById(R.id.gEmailTxt);
            desc = itemView.findViewById(R.id.descTxt);
            guide = itemView.findViewById(R.id.guidDetails);
            description = itemView.findViewById(R.id.description1);
            card = itemView.findViewById(R.id.tourCard);
            cancelTour = itemView.findViewById(R.id.cancelTour);
        }
    }
}
