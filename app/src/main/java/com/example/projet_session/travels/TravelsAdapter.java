package com.example.projet_session.travels;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_session.R;
import com.example.projet_session.data.remote.DTO.TravelDTO;

import java.util.ArrayList;
import java.util.List;

public class TravelsAdapter extends RecyclerView.Adapter<TravelViewHolder>{
    private List<TravelDTO> travelList = new ArrayList<>();

    public void setTravelList(List<TravelDTO> newTravelList){
        this.travelList.clear();
        if(newTravelList != null){
            this.travelList.addAll(newTravelList);
        }
        Log.d("TravelsAdapter", "submitList called. New size: " + this.travelList.size()); // Add log
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.travel_item, parent, false);
        return new TravelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        TravelDTO travel = travelList.get(position);
        holder.bind(travel);
    }

    @Override
    public int getItemCount() {
        int count = travelList == null ? 0 : travelList.size();
        Log.d("TravelsAdapter", "getItemCount called, returning: " + count);
        return count;
    }
}
