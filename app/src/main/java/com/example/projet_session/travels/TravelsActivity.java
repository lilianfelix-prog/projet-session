package com.example.projet_session.travels;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import 	android.view.WindowManager.LayoutParams;
import androidx.activity.EdgeToEdge;
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

public class TravelsActivity extends AppCompatActivity implements TravelsCallback.TravelsListener, View.OnClickListener {

    private RecyclerView recycleViewTravels;
    private TravelsAdapter travelsAdapter;



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


        recycleViewTravels = findViewById(R.id.recyclerView);
        setRecyclerView();
        fetchTravels();
    }

    private void setRecyclerView() {

        travelsAdapter = new TravelsAdapter();
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
    public void onClick(View view) {

    }

    @Override
    public void onTravelsSuccess(TravelsResponse response) {
        Log.d(TAG, "Data received, size: " + response.getTravels().size() + ". Calling submitList.");
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
}
