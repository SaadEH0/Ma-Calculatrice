package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;


import java.util.ArrayList;

public class RemoveUserActivity extends AppCompatActivity {

    ListView userList;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        userList = findViewById(R.id.userList);
        DB = new DBHelper(this);

        // Fetch user list from the database
        ArrayList<String> users = DB.getAllUsers();

        // Create an ArrayAdapter to display the user list in a ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        userList.setAdapter(adapter);

        // Set item click listener to handle user selection for deletion
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedUser = users.get(position);

                // Show the confirmation dialog before deletion
                showConfirmationDialog(selectedUser);
            }
        });
    }

    private void showConfirmationDialog(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete user: " + username + "?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Add logic to delete the selected user from the database
                boolean isUserDeleted = DB.removeUser(username);

                if (isUserDeleted) {
                    // Refresh the user list after deletion
                    updateListView();
                    Toast.makeText(RemoveUserActivity.this, "User deleted: " + username, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RemoveUserActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, just close the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateListView() {
        // Refresh the user list after deletion
        ArrayList<String> updatedUsers = DB.getAllUsers();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, updatedUsers);
        userList.setAdapter(adapter);
    }
}
