package com.example.projet_session.travels;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_session.R;
import com.example.projet_session.data.remote.DTO.TravelDTO;

public class TravelViewHolder extends RecyclerView.ViewHolder {
    TextView textViewDestinationName;
    ImageView imageViewTravel;
    TextView textViewDestinationPrice;

    public TravelViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewDestinationName = itemView.findViewById(R.id.textViewDestinationName);
        textViewDestinationPrice = itemView.findViewById(R.id.textViewDestinationPrice);
        imageViewTravel = itemView.findViewById(R.id.imageViewDestination);
    }

    public void bind(TravelDTO travel){
        textViewDestinationName.setText(travel.getDestination());
        textViewDestinationPrice.setText("$" + travel.getPrice());

        if (travel.getImgUrl() != null && !travel.getImgUrl().isEmpty()) {
            ImageLoader imageLoader = Coil.imageLoader(itemView.getContext());
            ImageRequest request = new ImageRequest.Builder(itemView.getContext())
                    .data(travel.getImgUrl())
                    .target(imageViewTravel)
                    .crossfade(true)
                    .build();
            imageLoader.enqueue(request);

            imageViewTravel.setVisibility(View.VISIBLE);
        } else {
            imageViewTravel.setVisibility(View.GONE);
        }

    }
}
