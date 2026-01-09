package com.example.smartcontacts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartcontacts.AppExecutor;
import com.example.smartcontacts.R;
import com.example.smartcontacts.database.DBHelper;
import com.example.smartcontacts.model.Contact;

public class ContactDetailActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private Contact contact;
    private int contactId;

    private TextView tvAvatar, tvPhone, tvEmail, tvCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DBHelper(this);
        tvAvatar = findViewById(R.id.tvAvatar);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvCompany = findViewById(R.id.tvCompany);

        contactId = getIntent().getIntExtra("contact_id", -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (contactId != -1) {
            loadContact();
        }
    }

    private void loadContact() {
        AppExecutor.getInstance().diskIO().execute(() -> {
            contact = dbHelper.getContact(contactId);
            AppExecutor.getInstance().mainThread().execute(() -> {
                if (contact != null) {
                    getSupportActionBar().setTitle(contact.getFullName());
                    if (contact.getFirstName() != null && !contact.getFirstName().isEmpty()) {
                        tvAvatar.setText(String.valueOf(contact.getFirstName().charAt(0)));
                    } else {
                        tvAvatar.setText("");
                    }
                    tvPhone.setText(contact.getPhone());
                    tvEmail.setText(contact.getEmail());
                    tvCompany.setText(contact.getCompany());
                }
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(ContactDetailActivity.this, AddEditContactActivity.class);
            intent.putExtra("contact_id", contactId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
