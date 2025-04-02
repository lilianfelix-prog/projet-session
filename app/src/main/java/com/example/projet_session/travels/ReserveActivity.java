package com.example.projet_session.travels;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projet_session.R;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class ReserveActivity extends AppCompatActivity {

    private int id;
    private String destination;
    private String description;
    private double price;
    private String imgUrl;
    Context context;


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

        id = getIntent().getIntExtra("id", -1);
        destination = getIntent().getStringExtra("destination");
        description = getIntent().getStringExtra("description");
        price = getIntent().getDoubleExtra("price", -1);
        imgUrl = getIntent().getStringExtra("imgUrl");

        TextView destinationTitle = findViewById(R.id.destinationTitle);
        TextView priceText = findViewById(R.id.priceText);
        TextView descriptionText = findViewById(R.id.descriptionText);
        ImageView destinationImage = findViewById(R.id.destinationImage);

        destinationTitle.setText(destination);
        priceText.setText("$" + String.format("%.2f", price));
        descriptionText.setText(description);

        if (imgUrl != null && !imgUrl.isEmpty()) {
            ImageLoader imageLoader = Coil.imageLoader(context);
            ImageRequest request = new ImageRequest.Builder(context)
                    .data(context)
                    .target(destinationImage)
                    .crossfade(true)
                    .build();
            imageLoader.enqueue(request);

            destinationImage.setVisibility(View.VISIBLE);
        } else {
            destinationImage.setVisibility(View.GONE);
        }

    }

}
