package com.example.puppybites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ActivityUpdateProduct extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText edtName, edtPrice, edtDescription, edtLocation;
    private Spinner spinnerCategory, spinnerCondition, spinnerProduct;
    private ImageView imgProduct;
    private Button btnUpdate, btnCancel;

    private DbHelper dbHelper;
    private ArrayAdapter<Product> productAdapter;
    private ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        edtLocation = findViewById(R.id.edtLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerCondition = findViewById(R.id.spinnerCondition);
        spinnerProduct = findViewById(R.id.spinnerProduct);
        imgProduct = findViewById(R.id.imgProduct);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);

        dbHelper = new DbHelper(this);
        productList = new ArrayList<>();

        populateSpinners();
        loadProducts();

        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Product selectedProduct = (Product) parent.getSelectedItem();
                if (selectedProduct != null) {
                    populateFields(selectedProduct);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
    }

    private void populateSpinners() {
        // Populate Category Spinner
        String[] categoriesArray = getResources().getStringArray(R.array.category_array);
        ArrayList<String> categories = new ArrayList<>(Arrays.asList(categoriesArray));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Populate Condition Spinner
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor conditionCursor = db.rawQuery("SELECT * FROM conditions", null);
        ArrayList<String> conditions = new ArrayList<>();
        while (conditionCursor.moveToNext()) {
            conditions.add(conditionCursor.getString(1)); // Assuming condition name is in the second column
        }
        conditionCursor.close();
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, conditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCondition.setAdapter(conditionAdapter);
    }

    private void loadProducts() {
        // Fetch products from the database and populate the product spinner
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);
        productList.clear();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
            @SuppressLint("Range") String condition = cursor.getString(cursor.getColumnIndex("condition"));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
            @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("location"));
            @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));

            Product product = new Product(id, name, price, category, condition, description, location, image);
            productList.add(product);
        }
        cursor.close();

        productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productList);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduct.setAdapter(productAdapter);
    }

    private void populateFields(Product product) {
        edtName.setText(product.getName());
        edtPrice.setText(String.valueOf(product.getPrice()));
        edtDescription.setText(product.getDescription());
        edtLocation.setText(product.getLocation());

        // Set category spinner
        ArrayAdapter<String> categoryAdapter = (ArrayAdapter<String>) spinnerCategory.getAdapter();
        int categoryPosition = categoryAdapter.getPosition(product.getCategory());
        spinnerCategory.setSelection(categoryPosition);

        // Set condition spinner
        ArrayAdapter<String> conditionAdapter = (ArrayAdapter<String>) spinnerCondition.getAdapter();
        int conditionPosition = conditionAdapter.getPosition(product.getCondition());
        spinnerCondition.setSelection(conditionPosition);

        // Set image
        if (product.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
            imgProduct.setImageBitmap(bitmap);
        } else {
            imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private void updateProduct() {
        Product selectedProduct = (Product) spinnerProduct.getSelectedItem();
        if (selectedProduct == null) {
            Toast.makeText(this, "Please select a product", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = selectedProduct.getId();
        String name = edtName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String condition = spinnerCondition.getSelectedItem().toString();
        String description = edtDescription.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || category.isEmpty() || condition.isEmpty() || description.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] image = null;
        if (imgProduct.getDrawable() != null) {
            imgProduct.setDrawingCacheEnabled(true);
            Bitmap bitmap = imgProduct.getDrawingCache();
            image = bitmapToByteArray(bitmap);
            imgProduct.setDrawingCacheEnabled(false);
        }

        Product product = new Product(id, name, price, category, condition, description, location, image);
        boolean updated = dbHelper.updateProduct(product);

        if (updated) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgProduct.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
}
