package com.example.puppybites;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityViewDetail extends AppCompatActivity {

    private DbHelper dbHelper;
    private ImageView imgProduct;
    private TextView txtName, txtPrice, txtCategory, txtCondition, txtDescription, txtLocation;
    private Button btnAddToCart;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);

        imgProduct = findViewById(R.id.imgProduct);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtCategory = findViewById(R.id.txtCategory);
        txtCondition = findViewById(R.id.txtCondition);
        txtDescription = findViewById(R.id.txtDescription);
        txtLocation = findViewById(R.id.txtLocation);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        dbHelper = new DbHelper(this);

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {
            loadProductDetails(productId);
            setupAddToCartButton(productId);
        }
    }

    private void loadProductDetails(int productId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM products WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("name");
            int priceIndex = cursor.getColumnIndex("price");
            int categoryIndex = cursor.getColumnIndex("category");
            int conditionIndex = cursor.getColumnIndex("condition");
            int descriptionIndex = cursor.getColumnIndex("description");
            int locationIndex = cursor.getColumnIndex("location");
            int imageIndex = cursor.getColumnIndex("image");

            if (nameIndex != -1 && priceIndex != -1 && categoryIndex != -1 &&
                    conditionIndex != -1 && descriptionIndex != -1 &&
                    locationIndex != -1 && imageIndex != -1) {

                String name = cursor.getString(nameIndex);
                double price = cursor.getDouble(priceIndex);
                String category = cursor.getString(categoryIndex);
                String condition = cursor.getString(conditionIndex);
                String description = cursor.getString(descriptionIndex);
                String location = cursor.getString(locationIndex);
                byte[] image = cursor.getBlob(imageIndex);

                txtName.setText(name);
                txtPrice.setText(String.format("$%.2f", price));
                txtCategory.setText(category);
                txtCondition.setText(condition);
                txtDescription.setText(description);
                txtLocation.setText(location);

                if (image != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    imgProduct.setImageBitmap(bitmap);
                }
            } else {
                Toast.makeText(this, "Error: One or more columns are missing.", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Error: Could not retrieve data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupAddToCartButton(final int productId) {
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming quantity is 1 for simplicity
                dbHelper.addCartItem(productId, 1);
                Toast.makeText(ActivityViewDetail.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
