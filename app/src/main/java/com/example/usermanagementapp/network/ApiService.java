package com.example.usermanagementapp.network;
import com.example.usermanagementapp.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/users")
    Call<UserResponse> getUsers();
}
