package com.example.projet_session.travels;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_session.R;
import com.example.projet_session.auth.RegisterCallback;
import com.example.projet_session.auth.ServiceGenerator;
import com.example.projet_session.data.remote.DTO.TravelDTO;
import com.example.projet_session.data.remote.DTO.TravelsResponse;
import com.example.projet_session.data.remote.TravelsCallback;
import com.example.projet_session.data.remote.TravelsRequest;

import java.util.List;

import retrofit2.Call;

public class TravelsActivity extends AppCompatActivity implements TravelsCallback.TravelsListener, OnItemClickListener {

    private RecyclerView recycleViewTravels;
    private TravelsAdapter travelsAdapter;
    private EditText editTextSearch;
    private ImageView searchIcon;

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

        // Initialize search views
        editTextSearch = findViewById(R.id.editTextSearch);
        searchIcon = findViewById(R.id.searchIcon);

        // Set up search functionality
        setupSearch();

        recycleViewTravels = findViewById(R.id.recyclerView);
        setRecyclerView();
        fetchTravels();
    }

    private void setupSearch() {
        // Handle search icon click
        searchIcon.setOnClickListener(v -> performSearch());

        // Handle enter key press
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String searchQuery = editTextSearch.getText().toString().trim();
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

    private void fetchTravels() {
        //TODO pass the auth token
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
    }

    @Override
    public void onNetworkError(Throwable t) {
        Log.e("TravelsActivity", "Network error: " + t.getMessage(), t);
    }


    @Override
    public void onItemClick(View view, int position) {
        TextView textViewDescription = view.findViewById(R.id.textViewDescription);
        if (textViewDescription.getVisibility() == View.VISIBLE) {

            textViewDescription.setVisibility(View.GONE);

            Intent reservevationIntent = new Intent(this, ReserveActivity.class);
            TravelDTO clickedTravel = travelsAdapter.getItem(position);

            int id = clickedTravel.getId();
            String destination = clickedTravel.getDestination();
            String description = clickedTravel.getDescription();
            double price = clickedTravel.getPrice();
            String imgUrl = clickedTravel.getImgUrl();

            reservevationIntent.putExtra("id", id);
            reservevationIntent.putExtra("destination", destination);
            reservevationIntent.putExtra("description", description);
            reservevationIntent.putExtra("price", price);
            reservevationIntent.putExtra("imgUrl", imgUrl);

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
