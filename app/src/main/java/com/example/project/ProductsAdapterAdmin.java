package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.entity.Products;
import java.util.ArrayList;
import java.util.List;

public class ProductsAdapterAdmin extends RecyclerView.Adapter<ProductsAdapterAdmin.TableViewHolder> {

    private List<String> tableNames;
    private Context context;

    public ProductsAdapterAdmin(Context context, List<String> tableNames) {
        this.context = context;
        this.tableNames = tableNames;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_table, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        String tableName = tableNames.get(position);
        holder.textViewTableName.setText(tableName);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductsByTableActivity.class);
            intent.putExtra("TABLE_NAME", tableName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tableNames.size();
    }

    static class TableViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTableName;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTableName = itemView.findViewById(R.id.textViewTableName);
        }
    }

    private String getDisplayName(String tableName) {
        // Chuyển đổi tên bảng thành tên hiển thị đẹp hơn
        switch (tableName) {
            case ShoppingDatabase.TB_FASHION:
                return "Fashion";
            case ShoppingDatabase.TB_BOOK:
                return "Books";
            case ShoppingDatabase.TB_BEAUTY:
                return "Beauty";
            // Thêm các case khác tương ứng
            default:
                return tableName;
        }
    }
}