package com.example.puppybites;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ActivityCart extends AppCompatActivity {

    private ListView lstCart;
    private TextView txtTotal;
    private Button btnCheckout;
    private DbHelper dbHelper;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        lstCart = findViewById(R.id.lstCart);
        txtTotal = findViewById(R.id.txtTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        dbHelper = new DbHelper(this);

        loadCartItems();

        cartAdapter = new CartAdapter(this, cartItemList);
        lstCart.setAdapter(cartAdapter);

        updateTotal();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle checkout logic
                Toast.makeText(ActivityCart.this, "Checkout not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartItems() {
        cartItemList = dbHelper.getCartItems();
    }

    private void updateTotal() {
        double total = 0;
        for (CartItem item : cartItemList) {

            double itemTotal = item.getProduct().getPrice() * item.getQuantity();
            total += itemTotal;
        }
        txtTotal.setText("Total: $" + total);
    }

}
