package com.example.projet_session.travels;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_session.R;
import com.example.projet_session.auth.ServiceGenerator;
import com.example.projet_session.data.remote.DTO.TravelDTO;
import com.example.projet_session.data.remote.DTO.TravelsResponse;
import com.example.projet_session.data.remote.TravelsCallback;
import com.example.projet_session.data.remote.TravelsRequest;
import com.example.projet_session.data.remote.DTO.TripDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class TravelsActivity extends AppCompatActivity implements TravelsCallback.TravelsListener, OnItemClickListener {

    private RecyclerView recycleViewTravels;
    private TravelsAdapter travelsAdapter;
    private EditText editTextSearch;
    private ImageView searchIcon;
    private ImageView filterIcon;
    private MaterialButton reservationsButton;

    private float currentMinBudget = 0f;
    private float currentMaxBudget = 500f;
    private static final float DEFAULT_MIN_BUDGET = 0f;
    private static final float DEFAULT_MAX_BUDGET = 500f;
    private boolean isOptionOneSelected = false;
    private boolean isOptionTwoSelected = false;
    private boolean isOptionThreeSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.travels_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.travels), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextSearch = findViewById(R.id.editTextSearch);
        searchIcon = findViewById(R.id.searchIcon);
        filterIcon = findViewById(R.id.filterIcon);
        reservationsButton = findViewById(R.id.reservationsButton);

        setupSearch();
        setupFilter();

        reservationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(TravelsActivity.this, ReservationsHistoryActivity.class);
            startActivity(intent);
        });

        recycleViewTravels = findViewById(R.id.recyclerView);
        setRecyclerView();
        fetchTravels();
    }

    private void setupSearch() {
        searchIcon.setOnClickListener(v -> performSearch(editTextSearch.getText().toString().trim(), false, null, null));

        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                performSearch(editTextSearch.getText().toString().trim(), false, null, null);
                return true;
            }
            return false;
        });
    }

    /**
     * Mettre en place le filtre de recherche avec les options
     * sur click de l'icone. Apply envoie une requete utilisant la meme
     * methode que la recherche normale.
     */
    private void setupFilter(){
        filterIcon.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View filterView = getLayoutInflater().inflate(R.layout.filter_popup, null);
            builder.setView(filterView);

            RangeSlider budgetSlider = filterView.findViewById(R.id.budget_slider);
            CheckBox optionOne = filterView.findViewById(R.id.option_one);
            CheckBox optionTwo = filterView.findViewById(R.id.option_two);
            CheckBox optionThree = filterView.findViewById(R.id.option_three);
            TextView sliderValueText = filterView.findViewById(R.id.slider_value_text);

            budgetSlider.setValues(currentMinBudget, currentMaxBudget);
            optionOne.setChecked(isOptionOneSelected);
            optionTwo.setChecked(isOptionTwoSelected);
            optionThree.setChecked(isOptionThreeSelected);

            budgetSlider.addOnChangeListener((slider, value, fromUser) -> {
                List<Float> values = slider.getValues();
                sliderValueText.setText("$" + values.get(0).intValue() + " - $" + values.get(1).intValue());
            });

            Button applyButton = filterView.findViewById(R.id.apply_button);
            Button resetButton = filterView.findViewById(R.id.reset_button);

            AlertDialog dialog = builder.create();

            applyButton.setOnClickListener(view -> {
                List<Float> values = budgetSlider.getValues();
                currentMinBudget = values.get(0);
                currentMaxBudget = values.get(1);
                isOptionOneSelected = optionOne.isChecked();
                isOptionTwoSelected = optionTwo.isChecked();
                isOptionThreeSelected = optionThree.isChecked();
                String query = isOptionOneSelected ? optionOne.getText().toString() : "";
                query += isOptionTwoSelected ? " " + optionTwo.getText().toString() : "";
                query += isOptionThreeSelected ? " " + optionThree.getText().toString() : "";
                performSearch(query, true, currentMinBudget, currentMaxBudget);
                dialog.dismiss();
            });

            resetButton.setOnClickListener(view -> {
                budgetSlider.setValues(DEFAULT_MIN_BUDGET, DEFAULT_MAX_BUDGET);
                optionOne.setChecked(false);
                optionTwo.setChecked(false);
                optionThree.setChecked(false);
            });

            dialog.show();
        });

    }

    /**
     * Effectue une recherche avec les paramètres spécifiés.
     * Envoie une requête avec les paramètres spécifiés.
     * @param searchQuery
     * @param hasBudget
     * @param minBudget
     * @param maxBudget
     */
    private void performSearch(String searchQuery, boolean hasBudget, @Nullable Float minBudget, @Nullable Float maxBudget) {
        if(hasBudget){
            searchQuery += " budget:" + minBudget + ":" + maxBudget;
            Log.d("TravelsActivity", "Search query: " + searchQuery);
        }
        if (!searchQuery.isEmpty()) {
            TravelsRequest travelRequest = ServiceGenerator.createService(TravelsRequest.class);
            Call<TravelsResponse> call = travelRequest.searchTravels(searchQuery);
            call.enqueue(new TravelsCallback(this));
        } else {
            fetchTravels();
        }
    }

    private void setRecyclerView() {
        travelsAdapter = new TravelsAdapter();
        travelsAdapter.setOnItemClickListener(this);
        recycleViewTravels.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "Setting Adapter. Adapter is null? " + (travelsAdapter == null));
        recycleViewTravels.setAdapter(travelsAdapter);
    }

    /**
     * Récupère toute la liste des voyages depuis le serveur.
     */
    private void fetchTravels() {
        
        TravelsRequest travelRequest = ServiceGenerator.createService(TravelsRequest.class);
        Call<TravelsResponse> call = travelRequest.getTravelsResponse();
        call.enqueue(new TravelsCallback(this));
    }


    @Override
    public void onTravelsSuccess(TravelsResponse response) {
        travelsAdapter.setTravelList(response.getTravels());
        Log.d("TravelsActivity", "Fetched travels");
    }

    @Override
    public void onTravelsFailure(String errorMessage) {
        Log.e("TravelsActivity", "Error fetching travels: " + errorMessage);
        editTextSearch.setError(errorMessage);
    }

    @Override
    public void onNetworkError(Throwable t) {
        Log.e("TravelsActivity", "Network error: " + t.getMessage(), t);
    }

    /**
     * Méthode appelee lorsque l'utilisateur clique sur un élément de la liste.
     * passer toute les donnees du voyage selectionne, et l'envoyer vers la page de reservation.
     * Recupere les donnees du voyage en fonction du format du json retournee par la requete
     * (list d'objet Trips dans chaque list d'objet voyage).
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        TextView textViewDescription = view.findViewById(R.id.textViewDescription);
        if (textViewDescription.getVisibility() == View.VISIBLE) {


            Intent reservevationIntent = new Intent(this, ReserveActivity.class);
            TravelDTO clickedTravel = travelsAdapter.getItem(position);


            reservevationIntent.putExtra("id", clickedTravel.getId());
            reservevationIntent.putExtra("destination", clickedTravel.getDestination());
            reservevationIntent.putExtra("description", clickedTravel.getDescription());
            reservevationIntent.putExtra("price", clickedTravel.getPrice());
            reservevationIntent.putExtra("imgUrl", clickedTravel.getImgUrl());
            reservevationIntent.putExtra("dureeJours", clickedTravel.getDureeJours());
            reservevationIntent.putExtra("typeDeVoyage", clickedTravel.getTypeDeVoyage());
            reservevationIntent.putExtra("activitesIncluses", clickedTravel.getActivitesIncluses());

            if (clickedTravel.getTrips() != null && !clickedTravel.getTrips().isEmpty()) {
                ArrayList<String> dates = new ArrayList<>();
                ArrayList<String> places = new ArrayList<>();
                
                for (TripDTO trip : clickedTravel.getTrips()) {
                    dates.add(trip.getDate());
                    places.add(trip.getNbPlacesDisponibles());
                }
                
                reservevationIntent.putStringArrayListExtra("dates", dates);
                reservevationIntent.putStringArrayListExtra("places", places);
            }

            startActivity(reservevationIntent);
        } else {
            textViewDescription.setVisibility(View.VISIBLE);
        }
        Log.d("TravelsActivity", "Item clicked at position: " + position);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        view.setVisibility(View.VISIBLE);
        Log.d("TravelsActivity", "Item long clicked at position: " + position);
    }
}
