package com.example.usermanagementapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.usermanagementapp.adapter.UserAdapter;
import com.example.usermanagementapp.model.User;
import com.example.usermanagementapp.network.ApiService;
import com.example.usermanagementapp.network.RetrofitClient;
import com.example.usermanagementapp.viewmodel.UserViewModel;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText avatarUrlEditText;
    private Button addUserButton;
    private Button updateUserButton;

    private User selectedUser; // This will hold the user selected for updating

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        avatarUrlEditText = findViewById(R.id.avatarUrlEditText);
        addUserButton = findViewById(R.id.addUserButton);
        updateUserButton = findViewById(R.id.updateUserButton);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        UserAdapter adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        userViewModel.fetchDataFromApi(apiService);

        userViewModel.getAllUsers().observe(this, users -> {
            adapter.setUsers(users);
            Log.d("MainActivity", "Users observed: " + users.size());
        });


        // Set a click listener on the RecyclerView items to select a user for updating
        adapter.setOnItemClickListener(user -> {
            selectedUser = user;
            firstNameEditText.setText(user.getFirstName());
            lastNameEditText.setText(user.getLastName());
            avatarUrlEditText.setText(user.getAvatar());
        });

        // Add user button functionality
        addUserButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String avatarUrl = avatarUrlEditText.getText().toString().trim();

            if (!firstName.isEmpty() && !lastName.isEmpty() && !avatarUrl.isEmpty()) {
                boolean userExists = false;

                // בדוק אם המשתמש כבר קיים
                for (User user : adapter.getUsers()) {
                    if (user.getFirstName().equals(firstName) && user.getLastName().equals(lastName)) {
                        userExists = true;
                        break;
                    }
                }

                if (userExists) {
                    // הצגת הודעה למשתמש שהמשתמש כבר קיים
                    firstNameEditText.setError("User already exists");
                    lastNameEditText.setError("User already exists");
                } else {
                    User newUser = new User();
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setAvatar(avatarUrl);

                    userViewModel.insert(newUser);
                    clearInputFields();
                }
            } else {
                if (firstName.isEmpty()) {
                    firstNameEditText.setError("First Name is required");
                }
                if (lastName.isEmpty()) {
                    lastNameEditText.setError("Last Name is required");
                }
                if (avatarUrl.isEmpty()) {
                    avatarUrlEditText.setError("Avatar URL is required");
                }
            }
        });

        // Update user button functionality
        updateUserButton.setOnClickListener(v -> {
            if (selectedUser != null) {
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String avatarUrl = avatarUrlEditText.getText().toString().trim();

                if (!firstName.isEmpty() && !lastName.isEmpty() && !avatarUrl.isEmpty()) {
                    selectedUser.setFirstName(firstName);
                    selectedUser.setLastName(lastName);
                    selectedUser.setAvatar(avatarUrl);

                    userViewModel.update(selectedUser);
                    clearInputFields();
                } else {
                    if (firstName.isEmpty()) {
                        firstNameEditText.setError("First Name is required");
                    }
                    if (lastName.isEmpty()) {
                        lastNameEditText.setError("Last Name is required");
                    }
                    if (avatarUrl.isEmpty()) {
                        avatarUrlEditText.setError("Avatar URL is required");
                    }
                }
            }
        });

        // Delete user functionality
        adapter.setOnDeleteClickListener(user -> {
            userViewModel.delete(user);
            clearInputFields();
        });
    }

    private void clearInputFields() {
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        avatarUrlEditText.setText("");
        selectedUser = null;
    }
}
