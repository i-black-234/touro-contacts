package com.example.tourodirectory.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.tourodirectory.R;
import com.example.tourodirectory.classes.Contact;
import com.example.tourodirectory.classes.RecentContactAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RecentContactsActivity extends AppCompatActivity {
    List<Contact> mContactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_contacts);

        //Get all the RecentContacts from intent
        Bundle extras = getIntent().getExtras();

        for (String contact : extras.getStringArrayList("RECENT_CONTACTS")) {
            mContactsList.add(getObjectFromJSONString(contact));
        }

        setupRecentContacts();

    }

    private void setupRecentContacts() {

        // Get the recyclerview element from our content_main.xml file.
        RecyclerView objRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Our contact list does not change once it is created in the adapter/backend
        objRecyclerView.setHasFixedSize(true);

        // This sets up our vertical directory
        RecyclerView.LayoutManager objLayoutManager = new LinearLayoutManager( this );
        // Get a list of contacts from the hashset and store in list

        // Instantiate our ContactAdapter (and optionally pass in our size )
        RecentContactAdapter mRecentContactAdapter = new RecentContactAdapter((ArrayList<Contact>) mContactsList);

        // set the RecyclerView to use the layoutmanager
        objRecyclerView.setLayoutManager(objLayoutManager);

        // set the recyclerview to use the adapter
        objRecyclerView.setAdapter(mRecentContactAdapter);
    }


    // Convert object tostring to be passed to ContactDetailActivity
    public static Contact getObjectFromJSONString (String json)
    {
        Gson gson = new Gson ();
        return gson.fromJson (json, Contact.class);
    }
}
