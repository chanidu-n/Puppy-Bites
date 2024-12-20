package com.example.puppybites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ProductAdapter extends ArrayAdapter <Product> {
    private final Context context;
    private final List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        super(context, R.layout.list_item, products);
        this.context = context;
        this.products = products;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Product Product = products.get(position);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtPrice = convertView.findViewById(R.id.txtPrice);
        TextView txtCategory = convertView.findViewById(R.id.txtCategory);
        ImageView imgProductItem = convertView.findViewById(R.id.imgProductItem);

        txtName.setText(Product.getName());
        txtPrice.setText("$" + Product.getPrice());
        txtCategory.setText(Product.getCategory());

        if (Product.getImage() != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Product.getImage());
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imgProductItem.setImageBitmap(bitmap);
        } else {
            imgProductItem.setImageResource(R.drawable.item); // Ensure you have this placeholder image
        }

        return convertView;
    }
}


