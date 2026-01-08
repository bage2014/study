package com.example.userlistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.userlistapp.adapter.UserAdapter;
import com.example.userlistapp.api.RetrofitClient;
import com.example.userlistapp.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private List<User> users = new ArrayList<>();
    
    private LinearLayout loadingLayout;
    private LinearLayout errorLayout;
    private Button retryButton;
    private TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化UI组件
        initUI();
        
        // 初始化RecyclerView
        initRecyclerView();
        
        // 加载用户列表
        loadUsers();
        
        // 设置重试按钮点击事件
        retryButton.setOnClickListener(v -> loadUsers());
    }

    private void initUI() {
        userRecyclerView = findViewById(R.id.user_recycler_view);
        loadingLayout = findViewById(R.id.loading_layout);
        errorLayout = findViewById(R.id.error_layout);
        retryButton = findViewById(R.id.retry_button);
        errorText = findViewById(R.id.error_text);
    }

    private void initRecyclerView() {
        userAdapter = new UserAdapter(users);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userRecyclerView.setAdapter(userAdapter);
    }

    private void loadUsers() {
        // 显示加载状态
        showLoading();
        
        // 调用API获取用户列表
        Call<List<User>> call = RetrofitClient.getUserApi().getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    users = response.body();
                    userAdapter.setUsers(users);
                    showUsers();
                } else {
                    showError("API请求失败: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                showError("网络连接失败: " + t.getMessage());
            }
        });
    }

    private void showLoading() {
        userRecyclerView.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    private void showUsers() {
        loadingLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        userRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showError(String message) {
        loadingLayout.setVisibility(View.GONE);
        userRecyclerView.setVisibility(View.GONE);
        errorText.setText(message);
        errorLayout.setVisibility(View.VISIBLE);
    }
}