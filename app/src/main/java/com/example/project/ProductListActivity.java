package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;

import com.example.project.entity.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView rvProductList;
    private ProductsAdapterAdmin adapter;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list); //

        rvProductList = findViewById(R.id.recyclerView);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));

        List<String> tableNames = new ArrayList<>();
        tableNames.add(ShoppingDatabase.TB_FASHION);
        tableNames.add(ShoppingDatabase.TB_BOOK);
        tableNames.add(ShoppingDatabase.TB_BEAUTY);
        tableNames.add(ShoppingDatabase.TB_ELECTRICS);
        tableNames.add(ShoppingDatabase.TB_GAME);
        tableNames.add(ShoppingDatabase.TB_HOME_COOKER);
        tableNames.add(ShoppingDatabase.TB_LAPTOP);
        tableNames.add(ShoppingDatabase.TB_MOBILE);
        tableNames.add(ShoppingDatabase.TB_SPORTS);
        tableNames.add(ShoppingDatabase.TB_CAR_TOOL);

        // Khởi tạo adapter
        adapter = new ProductsAdapterAdmin(this, tableNames);
        rvProductList.setAdapter(adapter);

         btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}