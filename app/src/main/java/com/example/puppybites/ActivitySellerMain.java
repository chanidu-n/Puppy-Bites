package com.example.puppybites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivitySellerMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        CardView addProduct = findViewById(R.id.cardAddProduct);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySellerMain.this, MainActivity.class));
            }
        });

        CardView viewProduct = findViewById(R.id.cardAllProducts);
        viewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySellerMain.this, ActivityView.class));
            }
        });

        CardView UpdateProduct = findViewById(R.id.cardUpdateProduct);
        UpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySellerMain.this, ActivityUpdateProduct.class));
            }
        });

        CardView DeleteProduct = findViewById(R.id.cardDeleteProduct);
        DeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySellerMain.this, ActivityDeleteProduct.class));
            }
        });

    }

}