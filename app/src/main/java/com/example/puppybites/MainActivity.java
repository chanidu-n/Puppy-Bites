package com.example.puppybites;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for image picker

    private EditText edtName, edtPrice, edtDescription, edtLocation;
    private Spinner spinnerCategory, spinnerCondition;
    private ImageView imgProduct;
    private Button btnUploadImage, btnAdd, btnView;
    private DbHelper DbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        edtLocation = findViewById(R.id.edtLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerCondition = findViewById(R.id.spinnerCondition);
        imgProduct = findViewById(R.id.imgProduct);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);

        DbHelper = new DbHelper(this);

        // Populate Spinners
        populateSpinners();

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewProducts();
            }
        });
    }

    private void populateSpinners() {
        SQLiteDatabase db = DbHelper.getReadableDatabase();

        // Populate Category Spinner
        String[] categoriesArray = getResources().getStringArray(R.array.category_array);
        ArrayList<String> categories = new ArrayList<>(Arrays.asList(categoriesArray));

// Set up ArrayAdapter
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set the adapter to the Spinner
        spinnerCategory.setAdapter(categoryAdapter);


        // Populate Condition Spinner
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
                // Load the selected image into ImageView
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgProduct.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addProduct() {
        String name = edtName.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String condition = spinnerCondition.getSelectedItem().toString();
        String description = edtDescription.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();

        // Validation
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

        Product product = new Product(0, name, price, category, condition, description, location, image);
        DbHelper.addProduct(product);
        Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();

        // Optionally clear the fields
        clearFields();
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    private void clearFields() {
        edtName.setText("");
        edtPrice.setText("");
        edtDescription.setText("");
        edtLocation.setText("");
        imgProduct.setImageDrawable(null);
        spinnerCategory.setSelection(0);
        spinnerCondition.setSelection(0);
    }

    private void viewProducts() {
        Intent intent = new Intent(MainActivity.this, ActivityView.class);
        startActivity(intent);
    }

}