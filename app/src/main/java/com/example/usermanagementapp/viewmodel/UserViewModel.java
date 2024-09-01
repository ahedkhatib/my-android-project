package com.example.usermanagementapp.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.usermanagementapp.network.ApiService;
import androidx.lifecycle.LiveData;
import com.example.usermanagementapp.model.User;
import com.example.usermanagementapp.repository.UserRepository;
import java.util.List;
import android.util.Log;


public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
        Log.d("UserViewModel", "ViewModel initialized and data fetched from repository");
    }

    public void insert(User user) {
        repository.insert(user);
    }

    public void update(User user) {
        repository.update(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public LiveData<List<User>> getAllUsers() {
        Log.d("UserRepository", "Fetching all users from database");
        return allUsers;
    }

    public void fetchDataFromApi(ApiService apiService) {
        repository.fetchDataFromApi(apiService);
    }
}
