package com.example.smartcontacts.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartcontacts.AppExecutor;
import com.example.smartcontacts.R;
import com.example.smartcontacts.database.DBHelper;
import com.example.smartcontacts.model.Contact;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditContactActivity extends AppCompatActivity {

    private TextInputLayout tilFirstName, tilLastName, tilCompany, tilPhone, tilEmail;
    private TextInputEditText etFirstName, etLastName, etCompany, etPhone, etEmail;
    private Button btnSave;
    private DBHelper dbHelper;
    private int contactId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        tilFirstName = findViewById(R.id.tilFirstName);
        tilLastName = findViewById(R.id.tilLastName);
        tilCompany = findViewById(R.id.tilCompany);
        tilPhone = findViewById(R.id.tilPhone);
        tilEmail = findViewById(R.id.tilEmail);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etCompany = findViewById(R.id.etCompany);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSave);
        dbHelper = new DBHelper(this);

        contactId = getIntent().getIntExtra("contact_id", -1);

        if (contactId != -1) {
            getSupportActionBar().setTitle("Edit Contact");
            loadContact();
        } else {
            getSupportActionBar().setTitle("Add Contact");
        }

        btnSave.setOnClickListener(v -> saveContact());
    }

    private void loadContact() {
        AppExecutor.getInstance().diskIO().execute(() -> {
            Contact contact = dbHelper.getContact(contactId);
            AppExecutor.getInstance().mainThread().execute(() -> {
                if (contact != null) {
                    etFirstName.setText(contact.getFirstName());
                    etLastName.setText(contact.getLastName());
                    etCompany.setText(contact.getCompany());
                    etPhone.setText(contact.getPhone());
                    etEmail.setText(contact.getEmail());
                }
            });
        });
    }

    private void saveContact() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String company = etCompany.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        tilFirstName.setError(null);
        tilPhone.setError(null);

        if (TextUtils.isEmpty(firstName)) {
            tilFirstName.setError("First name cannot be empty");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            tilPhone.setError("Phone cannot be empty");
            return;
        }

        Contact contact = new Contact(firstName, lastName, company, phone, email);

        AppExecutor.getInstance().diskIO().execute(() -> {
            if (contactId == -1) {
                dbHelper.insertContact(contact);
            } else {
                contact.setId(contactId);
                dbHelper.updateContact(contact);
            }
            AppExecutor.getInstance().mainThread().execute(() -> {
                Toast.makeText(this, (contactId == -1) ? "Contact saved" : "Contact updated", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
