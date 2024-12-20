package com.example.puppybites;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityView extends AppCompatActivity {

    private ListView lstProduct;
    private DbHelper dbHelper;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private Button btnViewCart;
    private Button btnVieProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        lstProduct = findViewById(R.id.lstProduct);
        dbHelper = new DbHelper(this);

        productList = new ArrayList<>();
        loadProducts();

        productAdapter = new ProductAdapter(this, productList);
        lstProduct.setAdapter(productAdapter);

        lstProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = productList.get(position);
                Intent intent = new Intent(ActivityView.this, ActivityViewDetail.class);
                intent.putExtra("PRODUCT_ID", product.getId());
                startActivity(intent);
            }
        });


        btnViewCart = findViewById(R.id.btnViewCart);
        btnViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityView.this, ActivityCart.class);
                startActivity(intent);
            }
        });


        btnVieProfile = findViewById(R.id.btnViewProfile);
        btnVieProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityView.this, ProfileFragment.class);
                startActivity(intent);
            }
        });
    }

    private void loadProducts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);

        if (cursor != null) {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int priceIndex = cursor.getColumnIndex("price");
            int categoryIndex = cursor.getColumnIndex("category");
            int conditionIndex = cursor.getColumnIndex("condition");
            int descriptionIndex = cursor.getColumnIndex("description");
            int locationIndex = cursor.getColumnIndex("location");
            int imageIndex = cursor.getColumnIndex("image");

            while (cursor.moveToNext()) {
                if (idIndex != -1 && nameIndex != -1 && priceIndex != -1 &&
                        categoryIndex != -1 && conditionIndex != -1 &&
                        descriptionIndex != -1 && locationIndex != -1 &&
                        imageIndex != -1) {

                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);
                    double price = cursor.getDouble(priceIndex);
                    String category = cursor.getString(categoryIndex);
                    String condition = cursor.getString(conditionIndex);
                    String description = cursor.getString(descriptionIndex);
                    String location = cursor.getString(locationIndex);
                    byte[] image = cursor.getBlob(imageIndex);

                    Product product = new Product(id, name, price, category, condition, description, location, image);
                    productList.add(product);
                } else {
                    Toast.makeText(this, "Error: One or more columns are missing.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Error: Could not retrieve data.", Toast.LENGTH_SHORT).show();
        }
    }
}
