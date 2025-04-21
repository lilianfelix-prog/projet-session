package com.example.projet_session.travels;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_session.R;
import com.example.projet_session.auth.ServiceGenerator;
import com.example.projet_session.data.local.DatabaseHelper;
import com.example.projet_session.data.remote.DTO.ReserveResponse;
import com.google.gson.JsonObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {

    private Context context;
    private Cursor cursor;
    private final NumberFormat currencyFormat;
    private final DatabaseHelper databaseHelper;

    public ReservationsAdapter(Context context) {
        this.context = context;
        this.currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(view);
    }

    /**
     * mettre a jour le ViewHolder avec les données de la reservation
     * si le boutton cancel est appuye, on envoie une requete de cancel
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            String destination = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINATION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
            float amount = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MONTANT));
            String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUT));
            int nbPlaces = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NB_PLACES));
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));


            holder.destinationText.setText(destination);
            holder.dateText.setText(date);
            holder.amountText.setText(currencyFormat.format(amount));
            holder.statusText.setText(status);
            holder.placesText.setText(nbPlaces + " places");

            if ("CANCELED".equals(status)) {
                holder.statusText.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
            } else {
                holder.statusText.setTextColor(context.getResources().getColor(android.R.color.holo_blue_light));
            }

            holder.itemView.setTag(id);

            if ("CANCELED".equals(status)) {
                holder.cancelButton.setVisibility(View.GONE);
            } else {
                holder.cancelButton.setVisibility(View.VISIBLE);
                holder.cancelButton.setOnClickListener(v -> {
                    databaseHelper.updateReservationStatus(id, "CANCELED");
                    refreshData();

                    sendCancelReserve(date, nbPlaces, id);

                    if (context instanceof ReservationsHistoryActivity) {
                        ((ReservationsHistoryActivity) context).loadReservations();
                    }
                });
            }

            holder.itemView.setOnClickListener(v -> {
                if (holder.cancelButton.getVisibility() == View.VISIBLE) {
                    holder.cancelButton.setVisibility(View.GONE);
                } else if (!"CANCELED".equals(status)) {
                    holder.cancelButton.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void refreshData() {
        if (context instanceof ReservationsHistoryActivity) {
            ((ReservationsHistoryActivity) context).loadReservations();
        }
    }

    public interface CancelReserveRequest{
        @POST("cancel")
        Call<JsonObject> cancelReserve(@Body Map<String, String> body);
    }

    /**
     * Methode abreviee pour envoyer une requete de cancel de reservation, sans
     * faire de class Callback et class reponse.
     * @param selectedDate
     * @param nbPlaces
     * @param id
     */
    public void sendCancelReserve(String selectedDate, int nbPlaces, String id) {
        CancelReserveRequest service = ServiceGenerator.createService(CancelReserveRequest.class);

        Map<String, String> body = new HashMap<>();
        body.put("date", selectedDate);
        body.put("nb_places_disponibles",String.valueOf(nbPlaces));
        body.put("id", id);

        service.cancelReserve(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject json = response.body();
                    Log.d("CancelReserve", "Response: " + json.toString());
                } else {
                    Log.e("CancelReserve", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("CancelReserve", "Error: " + t.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();

    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView destinationText;
        TextView dateText;
        TextView amountText;
        TextView statusText;
        TextView placesText;
        Button cancelButton;

        ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationText = itemView.findViewById(R.id.destinationTextView);
            dateText = itemView.findViewById(R.id.dateTextView);
            amountText = itemView.findViewById(R.id.montantTextView);
            statusText = itemView.findViewById(R.id.statutTextView);
            placesText = itemView.findViewById(R.id.nbPlacesTextView);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }
} 