package com.example.tourodirectory.activities;

import android.os.Bundle;

import com.example.tourodirectory.classes.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.example.tourodirectory.R;
import com.google.gson.Gson;

public class ContactDetailActivity extends AppCompatActivity {

    Contact contact;
    TextView full_name_tv, school_tv, department_tv, email, number_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail2);

        setupToolbar();
        setupFAB();


        getIncomingContactData();


    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    // Get the incoming Contact object.
    private void getIncomingContactData() {
        // create bundle object that refers to the bundle inside the intent
        Bundle extras = getIntent().getExtras();
        contact = getObjectFromJSONString( extras.getString("CONTACT")  );
        displayContactDetails();
    }


    // set the text views text to the Contact's details
    private void displayContactDetails() {
        bindTextViews();
        populateTextViews();
    }


    // bind text fields
    private void bindTextViews() {
        full_name_tv = findViewById(R.id.full_name);
        school_tv = findViewById(R.id.school);
        department_tv = findViewById(R.id.departments);
        email = findViewById(R.id.email);
        number_tv = findViewById(R.id.number);
    }

    // Populates the text fields
    private void populateTextViews() {
        full_name_tv.setText(contact.getFullName());
        school_tv.setText(contact.getSchool());
        department_tv.setText(getDepartmentString());
        email.setText(contact.getEmail());
        number_tv.setText(contact.getNumber());
    }

    // This will split the department string and make it nice, new line etc.
    private String getDepartmentString() {
        String[] departments = contact.getDepartment().split("\\$");
        String result = "";
        for (String  dept : departments)
            result += dept.replace('~', ',') + "\n";
        return result;
    }

    public static Contact getObjectFromJSONString (String json)
    {
        Gson gson = new Gson ();
        return gson.fromJson (json, Contact.class);
    }

}
