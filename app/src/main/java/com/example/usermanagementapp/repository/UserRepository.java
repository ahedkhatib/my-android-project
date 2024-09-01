package com.example.usermanagementapp.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.usermanagementapp.database.UserDatabase;
import com.example.usermanagementapp.database.UserDao;
import com.example.usermanagementapp.model.User;
import com.example.usermanagementapp.model.UserResponse;
import com.example.usermanagementapp.network.ApiService;
import com.example.usermanagementapp.network.RetrofitClient;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.UUID;
import android.util.Log;


public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private ExecutorService executorService;

    public UserRepository(Application application) {
        UserDatabase database = UserDatabase.getInstance(application);
        userDao = database.userDao();
        allUsers = userDao.getAllUsers();
        executorService = Executors.newFixedThreadPool(2);
    }

    public void insert(User user) {
        executorService.execute(() -> {
            User existingUser = userDao.getUserById(user.getId());
            if (existingUser == null) {
                userDao.insert(user);
            } else {
                userDao.update(user);
            }
        });
    }


    public void update(User user) {
        executorService.execute(() ->
        {
            Log.d("UserRepository", "Updating user: " + user.getId());
            userDao.update(user);});
    }



    public void delete(User user) {
        executorService.execute(() -> userDao.delete(user));
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public void fetchDataFromApi(ApiService apiService) {
        Call<UserResponse> call = apiService.getUsers();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body().getData();
                    for (User user : users) {
                        insert(user); // המשתמש יוכנס רק אם לא קיים
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

}
