package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.entity.Users;
import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<Users> userList;

    public UserAdapter(ArrayList<Users> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }


    @Override

    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = userList.get(position);

        // Thiết lập giá trị cho các TextView
        holder.textViewUserNameValue.setText(user.getUserName()); // Tên người dùng
        holder.textViewFullNameValue.setText(user.getFullName()); // Họ và tên người dùng
        holder.textViewEmailValue.setText(user.getEmail()); // Email người dùng
        holder.textViewPhoneNumberValue.setText(user.getPhone()); // Số điện thoại người dùng


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUserName; // Để hiển thị tiêu đề "Tên: "
        public TextView textViewUserNameValue; // Để hiển thị giá trị tên người dùng
        public TextView textViewFullName; // Để hiển thị tiêu đề "Họ và Tên: "
        public TextView textViewFullNameValue; // Để hiển thị giá trị họ và tên người dùng
        public TextView textViewEmail; // Để hiển thị tiêu đề "Email: "
        public TextView textViewEmailValue; // Để hiển thị giá trị email
        public TextView textViewPhoneNumber; // Để hiển thị tiêu đề "Số điện thoại: "
        public TextView textViewPhoneNumberValue; // Để hiển thị giá trị số điện thoại


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUserNameValue = itemView.findViewById(R.id.textViewUserNameValue); // ID cho giá trị tên

            textViewFullNameValue = itemView.findViewById(R.id.textViewFullNameValue); // ID cho giá trị họ và tên

            textViewEmailValue = itemView.findViewById(R.id.textViewEmailValue); // ID cho giá trị email

            textViewPhoneNumberValue = itemView.findViewById(R.id.textViewPhoneNumberValue); // ID cho giá trị số điện thoại
        }

    }
}