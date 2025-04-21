package com.example.projet_session.travels;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projet_session.R;
import com.example.projet_session.data.local.DatabaseHelper;
import com.google.android.material.button.MaterialButton;

public class ReservationsHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservationsAdapter adapter;
    private DatabaseHelper databaseHelper;
    private TextView emptyView;
    private MaterialButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservations_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.reservations_history), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.reservationsRecyclerView);
        emptyView = findViewById(R.id.emptyView);
        backButton = findViewById(R.id.backButton);

        databaseHelper = new DatabaseHelper(this);

        adapter = new ReservationsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(v -> finish());


        loadReservations();
    }

    /**
     * Charge les resrvations depuis la base de donnees local et les affiche dans le RecyclerView.
     */
    public void loadReservations() {
        Cursor cursor = databaseHelper.getReservations();
        adapter.swapCursor(cursor);

        if (cursor == null || cursor.getCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
} 