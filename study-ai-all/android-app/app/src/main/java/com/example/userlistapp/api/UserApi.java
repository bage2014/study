package com.example.userlistapp.api;

import com.example.userlistapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserApi {
    @GET("/api/users")
    Call<List<User>> getUsers();
}