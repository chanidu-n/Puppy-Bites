package com.example.puppybites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityDeleteProduct extends AppCompatActivity {

    private ListView listViewProducts;
    private DbHelper dbHelper;
    private ArrayList<Product> productList;
    private ArrayAdapter<Product> productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        listViewProducts = findViewById(R.id.listViewProducts);
        dbHelper = new DbHelper(this);

        loadProducts();

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product selectedProduct = productList.get(position);
                deleteProduct(selectedProduct.getId());
            }
        });
    }

    private void loadProducts() {
        productList = (ArrayList<Product>) dbHelper.getAllProducts(); // Fetch all products from the database

        productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        listViewProducts.setAdapter(productAdapter);
    }

    private void deleteProduct(int productId) {
        dbHelper.deleteProduct(productId);
        Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
        loadProducts(); // Refresh the list to reflect changes
    }
}
