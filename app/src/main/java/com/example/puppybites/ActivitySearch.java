package com.example.puppybites;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivitySearch extends AppCompatActivity {

    private SearchView searchView;
    private ListView lstSearchResults;
    private DbHelper dbHelper;
    private ProductAdapter productAdapter;
    private List<Product> searchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        lstSearchResults = findViewById(R.id.lstSearchResults);
        dbHelper = new DbHelper(this);

        searchResultsList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, searchResultsList);
        lstSearchResults.setAdapter(productAdapter);

        lstSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = searchResultsList.get(position);
                Intent intent = new Intent(ActivitySearch.this, ActivityViewDetail.class);
                intent.putExtra("PRODUCT_ID", product.getId());
                intent.putExtra("name", product.getName());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("category", product.getCategory());
                intent.putExtra("condition", product.getCondition());
                intent.putExtra("description", product.getDescription());
                intent.putExtra("location", product.getLocation());
                intent.putExtra("image", product.getImage());
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProducts(newText);
                return true;
            }
        });
    }

    private void searchProducts(String searchTerm) {
        searchResultsList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        if (searchTerm.isEmpty()) {
            cursor = db.rawQuery("SELECT * FROM products", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM products WHERE name LIKE ?", new String[]{"%" + searchTerm + "%"});
        }

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
                    searchResultsList.add(product);
                } else {
                    Toast.makeText(this, "Error: One or more columns are missing.", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Error: Could not retrieve data.", Toast.LENGTH_SHORT).show();
        }

        productAdapter.notifyDataSetChanged();
    }
}
