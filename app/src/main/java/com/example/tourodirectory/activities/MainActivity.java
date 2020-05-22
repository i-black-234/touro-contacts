package com.example.tourodirectory.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.tourodirectory.classes.CSVFile;
import com.example.tourodirectory.classes.Contact;
import com.example.tourodirectory.classes.ContactAdapter;
import com.example.tourodirectory.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    // Our Adapter
    private ContactAdapter mContactAdapter;
    // running total of contact clicks
    public int mCounter;

    // Keys
    private static final String COUNTER = "COUNTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup
        setupToolbar();
        setupFAB();

        // This will update the counter in the case where the screen was rotated.
        mCounter = savedInstanceState == null ? 0 : savedInstanceState.getInt( COUNTER);

        // When the user clicks on a contact that is part of the RV, then the OnClick() from the RV will "broadcast" to us to update the counter
        // We can then get that value and update our counter.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("update_counter"));

        // create/setup RecyclerView
        setupDirectory();
        
    }



    // Setup the directory
    private void setupDirectory() {

        // Get the recyclerview element from our content_main.xml file.
        RecyclerView objRecyclerView = findViewById(R.id.recycler_view);

        // Our contact list does not change once it is created in the adapter/backend
        objRecyclerView.setHasFixedSize(true);

        // This sets up our vertical directory
        RecyclerView.LayoutManager objLayoutManager = new LinearLayoutManager(this);


        List<Contact> allContacts = getContacts();

        // Instantiate our ContactAdapter (and optionally pass in our size )
        mContactAdapter = new ContactAdapter((ArrayList<Contact>) allContacts, mCounter);

        // set the RecyclerView to use the layoutmanager
        objRecyclerView.setLayoutManager(objLayoutManager);

        // set the recyclerview to use the adapter
        objRecyclerView.setAdapter(mContactAdapter);
    }


    // This will generate ALL the Contact objects, from the CSV file, to be used in out adapter.
    private List<Contact> getContacts() {
        InputStream inputStream = getResources().openRawResource(R.raw.touro_contact_results);
        CSVFile csvFile = new CSVFile(inputStream);
        List<String[]> contacts = csvFile.read();

        List<Contact> allContacts = new ArrayList<>();

        // Skip first 'row' because that is the header
        boolean firstRow = true;
        for (String[] contact : contacts) {
            if (firstRow) {
                firstRow = false;
                continue;
            }
            try {
                allContacts.add( new Contact(contact[0], contact[1], contact[2], contact[3], contact[4]) );
            }
            catch (Exception e) {
            }
        }
        return allContacts;
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
                // in order for the intent object to talk to the resultsactiviyu class we us the getApplicationContext() to be the bridge between the two
                Intent intent = new Intent(getApplicationContext(), ContactDetailActivity.class);
                startActivity(intent);
/*                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    // When the contact is clicked then we increment the amount of clicks the  user clicked.
    @Override protected void onSaveInstanceState (@NonNull Bundle outState)
    {
        super.onSaveInstanceState (outState);
        outState.putInt(COUNTER, mCounter);
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
             mCounter = intent.getIntExtra(COUNTER, mCounter);
            // Toast.makeText(MainActivity.this, mCounter + "", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content).getRootView(),  "You clicked on " + mCounter + " contacts.", Snackbar.LENGTH_LONG).show(); //.setAction("Action", recentContacts()).show();
        }
    };


    // Settings page stuff

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
