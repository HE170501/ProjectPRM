package com.example.project;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.project.entity.Products;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    private EditText etName, etPrice, etBrand, etPieces, etDescription, etDiscount;
    private ImageView ivProductImage;
    private Button btnAddProduct, btnSelectImage;
    private String tableName;
    private static final int PICK_IMAGE_REQUEST = 1;
    private int selectedImageResource = 0; // Để lưu resource ID của ảnh
    private List<Integer> imageResources = new ArrayList<>();
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Nhận tableName từ intent trước
        tableName = getIntent().getStringExtra("TABLE_NAME");
        ivProductImage = findViewById(R.id.ivProductImage);
        getAllDrawableResources();

        initializeViews();

        ivProductImage.setOnClickListener(v -> showImageSelectionDialog());
    }

    private void initializeViews() {
        etName = findViewById(R.id.etProductName);
        etPrice = findViewById(R.id.etProductPrice);
        etBrand = findViewById(R.id.etProductBrand);
        etPieces = findViewById(R.id.etProductPieces);
        etDescription = findViewById(R.id.etProductDescription);
        etDiscount = findViewById(R.id.etProductDiscount);
        ivProductImage = findViewById(R.id.ivProductImage);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnAddProduct.setOnClickListener(v -> {
            if (validateInputs()) {
                addProduct();
            }
        });
    }

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
        Log.d("Drawable Resources Count", String.valueOf(imageResources.size()));
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
                ivProductImage.setImageResource(resourceId);
                dialog.dismiss();
            });

            gridLayout.addView(imageView);
        }

        // Thêm ScrollView để có thể cuộn
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(gridLayout);
        layout.addView(scrollView);

        builder.setView(layout);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        dialog = builder.create();
        dialog.show();
    }

    private boolean validateInputs() {
        if (selectedImageResource == 0) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Name is required");
            return false;
        }
        if (etPrice.getText().toString().trim().isEmpty()) {
            etPrice.setError("Price is required");
            return false;
        }
        if (etBrand.getText().toString().trim().isEmpty()) {
            etBrand.setError("Brand is required");
            return false;
        }
        if (etPieces.getText().toString().trim().isEmpty()) {
            etPieces.setError("Pieces is required");
            return false;
        }
        return true;
    }

    private void addProduct() {
        try {
            String name = etName.getText().toString().trim();
            double price = Double.parseDouble(etPrice.getText().toString().trim());
            String brand = etBrand.getText().toString().trim();
            int pieces = Integer.parseInt(etPieces.getText().toString().trim());
            String description = etDescription.getText().toString().trim();
            double discount = etDiscount.getText().toString().trim().isEmpty() ?
                    0 : Double.parseDouble(etDiscount.getText().toString().trim());

            Products newProduct = new Products(
                    selectedImageResource,
                    name,
                    price,
                    brand,
                    pieces,
                    description,
                    discount
            );

            ShoppingDatabase db = new ShoppingDatabase(this);
            boolean success = db.insertProduct(newProduct, tableName);

            if (success) {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
                // Trả về result cho activity trước
                Intent resultIntent = new Intent();
                resultIntent.putExtra("product_added", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}