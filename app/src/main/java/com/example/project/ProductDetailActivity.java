package com.example.project;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productPrice, productPriceAfterDiscount;
    private TextView productBrand, productPieces, productDescription;
    private Button btnUpdate,btnBack,btnDelete;
    private String tableName,imageUrl;
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        // Ánh xạ các view
        initViews();
        Intent intent = getIntent();
        int productId = intent.getIntExtra("product_id", -1);
        String name = intent.getStringExtra("product_name");
        double price = intent.getDoubleExtra("product_price", 0.0);
        String brand = intent.getStringExtra("product_brand");
        int pieces = intent.getIntExtra("product_pieces", 0);
        double discount = intent.getDoubleExtra("product_discount", 0.0);
        int imageResource = intent.getIntExtra("product_image",0);
        String description = intent.getStringExtra("product_description");
        tableName = intent.getStringExtra("TABLE_NAME");
        Log.d("Table Name", String.valueOf(tableName));
        // Hiển thị dữ liệu
        productName.setText(name);
        productPrice.setText(price + "$");
        productBrand.setText("Brand: " + brand);
        productPieces.setText("Available: " + pieces + " pieces");
        productDescription.setText(description);
        productImage.setImageResource(imageResource);

        if (discount > 0) {
            double priceAfter = price - (price * (discount/100));
            productPriceAfterDiscount.setText(priceAfter + "$");
            productPrice.setPaintFlags(productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            productPrice.setTextColor(Color.GRAY);
        }
        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog(productId));
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(v -> {
            Intent updateIntent = new Intent(ProductDetailActivity.this, UpdateProductActivity.class);
            // Gửi tất cả dữ liệu cần thiết sang UpdateProductActivity
            updateIntent.putExtra("product_id", productId);
            updateIntent.putExtra("TABLE_NAME", tableName);
            updateIntent.putExtra("product_name", name);
            updateIntent.putExtra("product_price", price);
            updateIntent.putExtra("product_brand", brand);
            updateIntent.putExtra("product_pieces", pieces);
            updateIntent.putExtra("product_discount", discount);
            updateIntent.putExtra("product_image", imageResource);
            updateIntent.putExtra("product_description", description);
            Log.d("Table Name", String.valueOf(tableName));
            startActivity(updateIntent);
        });
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productPriceAfterDiscount = findViewById(R.id.productPriceAfterDiscount);
        productBrand = findViewById(R.id.productBrand);
        productPieces = findViewById(R.id.productPieces);
        productDescription = findViewById(R.id.productDescription);
        btnDelete = findViewById(R.id.btnDelete);


    }
    private void showDeleteConfirmationDialog(int productId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Yes", (dialog, which) -> deleteProduct(productId))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteProduct(int productId) {
        try {
            // Mở database
            SQLiteDatabase db = openOrCreateDatabase("shopping_db", MODE_PRIVATE, null);
            Log.d("Table Name", String.valueOf(tableName));
            Log.d("DeleteProduct", "Product ID: " + productId);
            Log.d("DeleteProduct", "Database Path: " + db.getPath());

            // Thực hiện xóa
            int rowsDeleted = db.delete(tableName, "id = ?",
                    new String[]{String.valueOf(productId)});

            if (rowsDeleted > 0) {
                Toast.makeText(this, "Product deleted successfully",
                        Toast.LENGTH_SHORT).show();

                // Quay về màn hình trước
                Intent resultIntent = new Intent();
                resultIntent.putExtra("product_deleted", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Failed to delete product",
                        Toast.LENGTH_SHORT).show();
            }

            // Đóng database
            db.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            Log.e("DeleteProduct", "Error deleting product: " + e.getMessage());
        }
    }

}