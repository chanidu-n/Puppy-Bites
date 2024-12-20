package com.example.puppybites;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "puppybites.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_CONDITION = "condition";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_IMAGE = "image";

    private static final String TABLE_CART = "cart";
    private static final String COLUMN_CART_ID = "id";
    private static final String COLUMN_CART_PRODUCT_ID = "product_id";
    private static final String COLUMN_CART_QUANTITY = "quantity";

    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_NAME = "name";

    private static final String TABLE_CONDITIONS = "conditions";
    private static final String COLUMN_CONDITION_NAME = "name";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the products table
        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_PRICE + " REAL, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_CONDITION + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_LOCATION + " TEXT, "
                + COLUMN_IMAGE + " BLOB)";
        db.execSQL(createProductsTable);

        // Create the cart table
        String createCartTable = "CREATE TABLE " + TABLE_CART + " ("
                + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CART_PRODUCT_ID + " INTEGER, "
                + COLUMN_CART_QUANTITY + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_ID + "))";
        db.execSQL(createCartTable);

        // Create categories and conditions tables
        String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CATEGORY_NAME + " TEXT)";
        db.execSQL(createCategoriesTable);

        String createConditionsTable = "CREATE TABLE " + TABLE_CONDITIONS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CONDITION_NAME + " TEXT)";
        db.execSQL(createConditionsTable);

        // Insert default values into categories table
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Puppy')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Male')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Female')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (" + COLUMN_CATEGORY_NAME + ") VALUES ('Adult')");

        // Insert default values into conditions table
        db.execSQL("INSERT INTO " + TABLE_CONDITIONS + " (" + COLUMN_CONDITION_NAME + ") VALUES ('Nutritional')");
        db.execSQL("INSERT INTO " + TABLE_CONDITIONS + " (" + COLUMN_CONDITION_NAME + ") VALUES ('Protein')");
        db.execSQL("INSERT INTO " + TABLE_CONDITIONS + " (" + COLUMN_CONDITION_NAME + ") VALUES ('Vitamin')");
        db.execSQL("INSERT INTO " + TABLE_CONDITIONS + " (" + COLUMN_CONDITION_NAME + ") VALUES ('Biscuit')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONDITIONS);
        onCreate(db);
    }

    // Product methods
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_CATEGORY, product.getCategory());
        values.put(COLUMN_CONDITION, product.getCondition());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_LOCATION, product.getLocation());
        values.put(COLUMN_IMAGE, product.getImage());
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public Product getLastAddedProduct() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") Product product = new Product(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CONDITION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                    cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))
            );
            cursor.close();
            return product;
        }
        return null;
    }

    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") Product product = new Product(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CONDITION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                    cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))
            );
            cursor.close();
            return product;
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Product product = new Product(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CONDITION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                        cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))
                );
                productList.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return productList;
    }

    public boolean updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_CATEGORY, product.getCategory());
        values.put(COLUMN_CONDITION, product.getCondition());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_LOCATION, product.getLocation());
        values.put(COLUMN_IMAGE, product.getImage());

        int rowsAffected = db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
        db.close();
        return rowsAffected > 0;
    }

    public void deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }

    // Cart methods
    public void addCartItem(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_PRODUCT_ID, cartItem.getProduct().getId());
        values.put(COLUMN_CART_QUANTITY, cartItem.getQuantity());
        db.insert(TABLE_CART, null, values);
        db.close();
    }

    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c." + COLUMN_CART_ID + ", c." + COLUMN_CART_PRODUCT_ID + ", c." + COLUMN_CART_QUANTITY +
                ", p." + COLUMN_NAME + ", p." + COLUMN_PRICE + ", p." + COLUMN_IMAGE +
                " FROM " + TABLE_CART + " c JOIN " + TABLE_PRODUCTS + " p ON c." + COLUMN_CART_PRODUCT_ID + " = p." + COLUMN_ID;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int cartId = cursor.getInt(cursor.getColumnIndex(COLUMN_CART_ID));
                @SuppressLint("Range") int productId = cursor.getInt(cursor.getColumnIndex(COLUMN_CART_PRODUCT_ID));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_CART_QUANTITY));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));

                Product product = new Product(productId, name, price, null, null, null, null, image);
                CartItem cartItem = new CartItem(cartId, product, quantity);
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return cartItems;
    }

    public void updateCartItem(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, cartItem.getQuantity());
        db.update(TABLE_CART, values, COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cartItem.getId())});
        db.close();
    }

    public void deleteCartItem(int cartItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cartItemId)});
        db.close();
    }
    public void addCartItem(int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_PRODUCT_ID, productId);
        values.put(COLUMN_CART_QUANTITY, quantity);
        db.insert(TABLE_CART, null, values);
        db.close();
    }

}
