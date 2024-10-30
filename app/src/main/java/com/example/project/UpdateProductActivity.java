package com.example.project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.loader.content.CursorLoader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UpdateProductActivity extends AppCompatActivity {
    private EditText edtName, edtPrice, edtBrand, edtPieces, edtDiscount, edtDescription;
    private ImageView imgProduct;
    private Button btnSaveUpdate,btnBack;
    private ShoppingDatabase database;
    private int productId;
    private String tableName;
    private int currentImageResource;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String selectedImagePath;
    private List<Integer> imageResources = new ArrayList<>();
    private int selectedImageResource;
    private AlertDialog dialog;


    private void getAllDrawableResources() {
        Field[] fields = R.drawable.class.getFields();
        for (Field field : fields) {
            try {

                String name = field.getName().toLowerCase();
                Log.d("Drawable Resource Name", name);

                    imageResources.add(field.getInt(null));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("Drawable Resources Count", String.valueOf(imageResources.size())); // Log số lượng tài nguyên

    }
    private void showImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image");

        // Tạo layout cho dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);

        // Tạo GridLayout để hiển thị các ảnh
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(3); // Số cột

        // Thêm các ImageView vào GridLayout
        for (int resourceId : imageResources) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(resourceId);

            // Thiết lập kích thước và style cho ImageView
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 200;
            params.height = 200;
            params.setMargins(8, 8, 8, 8);
            imageView.setLayoutParams(params);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Xử lý sự kiện click
            imageView.setOnClickListener(v -> {
                selectedImageResource = resourceId;
                imgProduct.setImageResource(resourceId);
                dialog.dismiss();
            });

            gridLayout.addView(imageView);
        }

        // Thêm ScrollView để có thể cuộn nếu có nhiều ảnh
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(gridLayout);
        layout.addView(scrollView);

        builder.setView(layout);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

         dialog = builder.create();
        dialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        // Khởi tạo database
        database = new ShoppingDatabase(this);
        getAllDrawableResources();

        // Ánh xạ view
        initViews();

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        productId = intent.getIntExtra("product_id", -1);
        tableName = intent.getStringExtra("TABLE_NAME");
        String name = intent.getStringExtra("product_name");
        double price = intent.getDoubleExtra("product_price", 0.0);
        String brand = intent.getStringExtra("product_brand");
        int pieces = intent.getIntExtra("product_pieces", 0);
        double discount = intent.getDoubleExtra("product_discount", 0.0);
        currentImageResource = intent.getIntExtra("product_image", 0);
        String description = intent.getStringExtra("product_description");

        // Hiển thị dữ liệu hiện tại
        displayCurrentData(name, price, brand, pieces, discount, description);

        // Xử lý nút Save Update
        btnSaveUpdate.setOnClickListener(v -> updateProduct());
        imgProduct.setOnClickListener(v -> showImageSelectionDialog());
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtBrand = findViewById(R.id.edtBrand);
        edtPieces = findViewById(R.id.edtPieces);
        edtDiscount = findViewById(R.id.edtDiscount);
        edtDescription = findViewById(R.id.edtDescription);
        imgProduct = findViewById(R.id.imgProduct);
        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);
    }

    private void displayCurrentData(String name, double price, String brand,
                                    int pieces, double discount, String description) {
        edtName.setText(name);
        edtPrice.setText(String.valueOf(price));
        edtBrand.setText(brand);
        edtPieces.setText(String.valueOf(pieces));
        edtDiscount.setText(String.valueOf(discount));
        edtDescription.setText(description);
        imgProduct.setImageResource(currentImageResource);
    }

    private void updateProduct() {
        // Lấy dữ liệu từ các EditText
        String newName = edtName.getText().toString().trim();
        double newPrice = Double.parseDouble(edtPrice.getText().toString());
        String newBrand = edtBrand.getText().toString().trim();
        int newPieces = Integer.parseInt(edtPieces.getText().toString());
        double newDiscount = Double.parseDouble(edtDiscount.getText().toString());
        String newDescription = edtDescription.getText().toString().trim();

        // Kiểm tra dữ liệu
        if (newName.isEmpty() || newBrand.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật vào database
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("price", newPrice);
        values.put("brand", newBrand);
        values.put("pieces", newPieces);
        values.put("discount", newDiscount);
        values.put("description", newDescription);
        values.put("image", selectedImageResource);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(productId)};

        int result = db.update(tableName, values, whereClause, whereArgs);

        if (result > 0) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            // Quay về ProductDetailActivity với dữ liệu mới
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra("product_id", productId);
            intent.putExtra("TABLE_NAME", tableName);
            intent.putExtra("product_name", newName);
            intent.putExtra("product_price", newPrice);
            intent.putExtra("product_brand", newBrand);
            intent.putExtra("product_pieces", newPieces);
            intent.putExtra("product_discount", newDiscount);

            intent.putExtra("product_image", selectedImageResource);

            intent.putExtra("product_description", newDescription);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            try {
                Uri imageUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgProduct.setImageBitmap(bitmap);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}