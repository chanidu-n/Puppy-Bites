package com.example.puppybites;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CartAdapter extends ArrayAdapter<CartItem> {

    private Context context;
    private List<CartItem> cartItems;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        super(context, R.layout.cart_item, cartItems);
        this.context = context;
        this.cartItems = cartItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        }

        CartItem cartItem = cartItems.get(position);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtQuantity = convertView.findViewById(R.id.txtQuantity);
        TextView txtPrice = convertView.findViewById(R.id.txtPrice);
        ImageView imgProduct = convertView.findViewById(R.id.imgProduct);

        txtName.setText(cartItem.getProduct().getName());
        txtQuantity.setText("Quantity: " + cartItem.getQuantity());
        txtPrice.setText("Price: $" + cartItem.getProduct().getPrice() * cartItem.getQuantity());
        imgProduct.setImageBitmap(BitmapFactory.decodeByteArray(cartItem.getProduct().getImage(), 0, cartItem.getProduct().getImage().length));

        return convertView;
    }
}
