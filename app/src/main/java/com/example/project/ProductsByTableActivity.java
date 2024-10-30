package com.example.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.entity.Products;

import java.util.ArrayList;

public class ProductsByTableActivity extends AppCompatActivity implements OnRecyclerViewClickListener {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ArrayList<Products> productList;
    private ShoppingDatabase database;
    public static String tableName;
    Button btnBack,btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_by_table);

        // Lấy tên bảng được truyền từ intent
         tableName = getIntent().getStringExtra("TABLE_NAME");
         Log.d("ProductsByTable", "Table Name: " + tableName);

        // Khởi tạo database
        database = new ShoppingDatabase(this);

        btnBack=findViewById(R.id.button);
        btnBack.setOnClickListener(v -> finish());

        btnCreate=findViewById(R.id.button3);

        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddProductActivity.class);
            intent.putExtra("TABLE_NAME", tableName);
            startActivity(intent);
        });
        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerView);
        ImageView imgBack = findViewById(R.id.imgBack);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new ProductAdapter((Context) this,productList, this);
        recyclerView.setAdapter(adapter);

        // Load sản phẩm từ bảng được chọn
        loadProducts(tableName);

        // Xử lý sự kiện back
        imgBack.setOnClickListener(v -> finish());
    }

    @SuppressLint("Range")
    private void loadProducts(String tableName) {
        SQLiteDatabase db = database.getReadableDatabase();
        productList.clear();

        String query = "SELECT * FROM " + tableName;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Products products = new Products();
                products.setId(cursor.getInt(cursor.getColumnIndex("id")));
                products.setName(cursor.getString(cursor.getColumnIndex("name")));
                products.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                products.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                products.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
                products.setPieces(cursor.getInt(cursor.getColumnIndex("pieces")));
                products.setDiscount(cursor.getDouble(cursor.getColumnIndex("discount")));
                products.setImage(cursor.getInt(cursor.getColumnIndex("image")));
                productList.add(products);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnItemClick(int position) {
        Products product = productList.get(position);
        Log.d("ProductsByTable2", "Table Name: " + tableName);
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product_id", product.getId());
        intent.putExtra("product_name", product.getName());
        intent.putExtra("product_price", product.getPrice());
        intent.putExtra("product_brand", product.getBrand());
        intent.putExtra("product_pieces", product.getPieces());
        intent.putExtra("product_discount", product.getDiscount());
        intent.putExtra("product_description", product.getDescription());
        intent.putExtra("product_image", product.getImage());


        intent.putExtra("TABLE_NAME", tableName);

        // Start activity
        startActivity(intent);

    }
}