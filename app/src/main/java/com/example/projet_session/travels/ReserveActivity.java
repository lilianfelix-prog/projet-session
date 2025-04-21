package com.example.projet_session.travels;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projet_session.R;
import com.example.projet_session.auth.ServiceGenerator;
import com.example.projet_session.data.local.DatabaseHelper;
import com.example.projet_session.data.remote.DTO.ReserveRequestDTO;
import com.example.projet_session.data.remote.DTO.ReserveResponse;
import com.example.projet_session.data.remote.ReserveCallback;
import com.example.projet_session.data.remote.ReserveRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReserveActivity extends AppCompatActivity implements ReserveCallback.ReserveListener{

    private String id;
    private String destination;
    private String description;
    private String price;
    private String imgUrl;
    private String dureeJours;
    private String typeDeVoyage;
    private String activitesIncluses;
    private ArrayList<String> availableDates;
    private ArrayList<String> availablePlaces;
    private TextInputEditText placesInput;
    private TextInputEditText fullNameInput;
    private TextInputEditText emailInput;
    private TextInputEditText phoneInput;
    private MaterialButton reserveButton;
    private DatabaseHelper databaseHelper;
    private Spinner dateSpinner;
    private ArrayAdapter<String> dateAdapter;
    private Map<String, String> dateToPlacesMap;
    private Float totalMontant = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.reserve_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reserve), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialise database helper
        databaseHelper = new DatabaseHelper(this);

        id = getIntent().getStringExtra("id");
        destination = getIntent().getStringExtra("destination");
        description = getIntent().getStringExtra("description");
        price = getIntent().getStringExtra("price");
        imgUrl = getIntent().getStringExtra("imgUrl");
        dureeJours = getIntent().getStringExtra("dureeJours");
        typeDeVoyage = getIntent().getStringExtra("typeDeVoyage");
        activitesIncluses = getIntent().getStringExtra("activitesIncluses");
        availableDates = getIntent().getStringArrayListExtra("dates");
        availablePlaces = getIntent().getStringArrayListExtra("places");

        initializeViews();

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        reserveButton.setOnClickListener(v -> handleReservation());
    }

    private void initializeViews() {
        TextView destinationTitle = findViewById(R.id.destinationTitle);
        TextView priceText = findViewById(R.id.priceText);
        TextView descriptionText = findViewById(R.id.descriptionText);
        ImageView destinationImage = findViewById(R.id.destinationImage);
        TextView durationText = findViewById(R.id.durationText);
        TextView accommodationText = findViewById(R.id.accommodationText);
        
        placesInput = findViewById(R.id.placesInput);
        fullNameInput = findViewById(R.id.fullNameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        reserveButton = findViewById(R.id.reserveButton);
        dateSpinner = findViewById(R.id.dateSpinner);

        destinationTitle.setText(destination);
        priceText.setText("$" + price);
        descriptionText.setText(description);
        durationText.setText(dureeJours + " Days");
        accommodationText.setText(typeDeVoyage);

        if (imgUrl != null && !imgUrl.isEmpty()) {
            ImageLoader imageLoader = Coil.imageLoader(this);
            ImageRequest request = new ImageRequest.Builder(this)
                    .data(imgUrl)
                    .target(destinationImage)
                    .crossfade(true)
                    .build();
            imageLoader.enqueue(request);
            destinationImage.setVisibility(View.VISIBLE);
        } else {
            destinationImage.setVisibility(View.GONE);
        }

        ArrayList<String> dateWithPlaces = new ArrayList<>();
        dateToPlacesMap = new HashMap<>();
        if (availableDates != null && availablePlaces != null) {
            for (int i = 0; i < availableDates.size(); i++) {
                String date = availableDates.get(i);
                String places = availablePlaces.get(i);
                dateWithPlaces.add(date + " (" + places + " places libres)");
                dateToPlacesMap.put(date, places);
            }
        }
        dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateWithPlaces);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateAdapter.notifyDataSetChanged();
        dateSpinner.setAdapter(dateAdapter);

        placesInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePlaces();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private boolean validatePlaces() {
        String selectedDateWithPlaces = (String) dateSpinner.getSelectedItem();
        if (selectedDateWithPlaces == null) return false;
        
        String selectedDate = selectedDateWithPlaces.substring(0, selectedDateWithPlaces.indexOf(" ("));
        String availablePlaces = dateToPlacesMap.get(selectedDate);
        
        String inputPlaces = placesInput.getText().toString();
        if (!inputPlaces.isEmpty()) {
            int requestedPlaces = Integer.parseInt(inputPlaces);
            int available = Integer.parseInt(availablePlaces);
            
            if (requestedPlaces > available) {
                placesInput.setError("Only " + available + " places available for this date");
                reserveButton.setEnabled(false);
                return false;
            } else {
                placesInput.setError(null);
                reserveButton.setEnabled(true);
                totalMontant = requestedPlaces * Float.parseFloat(price);
                reserveButton.setText("Reserve $" + totalMontant);
                return true;
            }
        }
        return false;
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (TextUtils.isEmpty(fullNameInput.getText())) {
            fullNameInput.setError("Please enter full name");
            isValid = false;
        }

        if (TextUtils.isEmpty(emailInput.getText())) {
            emailInput.setError("Please enter email");
            isValid = false;
        }

        if (TextUtils.isEmpty(phoneInput.getText())) {
            phoneInput.setError("Please enter phone number");
            isValid = false;
        }

        if (dateSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (!validatePlaces()) {
            isValid = false;
        }

        return isValid;
    }


    String selectedDate;
    String nbPlaces;
    String fullName;
    String email;
    String phone;
    float montant;
    int requestedPlaces;

    /**
     * initialiser les variables de la reservation
     */
    private void setInputVariables(){
        if (!validateInputs()) {
            return;
        }
        try {
            String selectedDateWithPlaces = (String) dateSpinner.getSelectedItem();
            if (selectedDateWithPlaces == null) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedDate = selectedDateWithPlaces.substring(0, selectedDateWithPlaces.indexOf(" ("));
            nbPlaces = placesInput.getText().toString();
            fullName = fullNameInput.getText().toString();
            email = emailInput.getText().toString();
            phone = phoneInput.getText().toString();
            montant = totalMontant;
            requestedPlaces = Integer.parseInt(nbPlaces);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * envoyer la requete de reservation pour changer le nombre de places disponible
     */
    private void handleReservation() {

            setInputVariables();

            ReserveRequest reserveRequest = ServiceGenerator.createService(ReserveRequest.class);
            Call<ReserveResponse> call = reserveRequest.reserve(new ReserveRequestDTO(selectedDate, nbPlaces, id));
            call.enqueue(new ReserveCallback(this));


    }

    /**
     * Si la requete est reussi, ajouter la reservation dans la base de donnees local
     * @param response
     */
    @Override
    public void onReserveSuccess(ReserveResponse response) {
        databaseHelper.addReservation(
                destination,
                selectedDate,
                montant,
                "RESERVED",
                requestedPlaces,
                fullName,
                email,
                phone
        );
        Toast.makeText(this, "Reservation reussi!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onReserveFailure(String errorMessage) {
        Toast.makeText(this, "reservation non reussi...", Toast.LENGTH_SHORT).show();
        Log.e("Reservation Failure",errorMessage);
    }

    @Override
    public void onNetworkError(Throwable t) {
        Toast.makeText(this, "reservation non reussi...", Toast.LENGTH_SHORT).show();
        Log.e("Reservation Failure", t.getMessage());

    }
}
