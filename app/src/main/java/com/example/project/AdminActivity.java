package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

      BarChart barChart;
    Button viewUser, viewProduct,btnLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);


        btnLogOut = findViewById(R.id.button2);
        btnLogOut.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
        });



        viewUser = findViewById(R.id.buttonUserList);
        viewProduct = findViewById(R.id.buttonProductList);
        viewUser.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, UserListActivity.class));
        });
        viewProduct.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, ProductListActivity.class));
        });
        }


    private void setupBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ShoppingDatabase db = new ShoppingDatabase(this);

        String[] categories = {"Fashion", "Book", "Beauty", "Electrics", "Game", "Home", "Laptop", "Mobile", "Sports", "Car Tools"};
        String[] tableNames = {ShoppingDatabase.TB_FASHION, ShoppingDatabase.TB_BOOK, ShoppingDatabase.TB_BEAUTY,
                ShoppingDatabase.TB_ELECTRICS, ShoppingDatabase.TB_GAME, ShoppingDatabase.TB_HOME_COOKER,
                ShoppingDatabase.TB_LAPTOP, ShoppingDatabase.TB_MOBILE, ShoppingDatabase.TB_SPORTS,
                ShoppingDatabase.TB_CAR_TOOL};

        for (int i = 0; i < tableNames.length; i++) {
            int count = db.getProductCount(tableNames[i]);
            entries.add(new BarEntry(i + 1, count));
        }

        db.close();


    }

}