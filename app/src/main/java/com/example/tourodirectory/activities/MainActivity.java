package com.example.tourodirectory.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.tourodirectory.classes.CSVFile;
import com.example.tourodirectory.classes.Contact;
import com.example.tourodirectory.classes.ContactAdapter;
import com.example.tourodirectory.R;
import com.example.tourodirectory.classes.RecentContactAdapter;
import com.example.tourodirectory.classes.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity  {

    // settings preferences
    private boolean mPrefSaveRecentContacts;

    // prefs key for SP file
    private final String mPrefKey = "PREFS";
    // Name of key for setting that saves contacts
    private String mKeyPrefSaveRecentContacts = "SAVE_RECENT_CONTACTS";
    // name of key that is used to input data to  SP
    private String mKeyRecentContacts = "RECENT_CONTACTS";


    // Our Adapter
    private ContactAdapter mContactAdapter;

    // running total of contact clicks
    public int mCounter = 0;

    HashSet<Contact> mRecentContacts = new HashSet<>();
    // Keys
    private static final String COUNTER = "COUNTER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup
        setupToolbar();
        setupFAB();

        // Settings the defaults SP values
        PreferenceManager.setDefaultValues (getApplicationContext (), R.xml.root_preferences, true);


        // When the user clicks on a contact that is part of the RV, then the OnClick() from the RV will "broadcast" to us to update the counter
        // We can then get that value and update our counter.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("update_counter"));

        // reset fields if savedIntanceState
        if (!(savedInstanceState == null))
            getDataFromSavedInstanceState(savedInstanceState);

        // create/setup RecyclerView
        setupDirectory();
        restoreAppSettingsFromPrefs();
        getRecentContactsFromPrefs();


    }



    private void getDataFromSavedInstanceState(Bundle savedInstanceState) {

        // Update the counter to the counter stored in the savedInstanceState
        // Update the counter to the counter stored in the savedInstanceState
        mCounter =  savedInstanceState.getInt( COUNTER);

        // Fill the mRecentContacts HashSet with the recent contacts stored in the savedInstanceState
        for (String contact : savedInstanceState.getStringArrayList("RECENT_CONTACTS")) {
            mRecentContacts.add(getObjectFromJSONString(contact));
        }

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
/*                Intent intent = new Intent(getApplicationContext(), ContactDetailActivity.class);
                startActivity(intent);*/
                Snackbar.make(view, "" + mRecentContacts.size(), Snackbar.LENGTH_LONG)
                        .setAction("Recent Contacts", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showRecentContacts();
                                    }
                                }
                        ).show();
            }
        });
    }

    private void showRecentContacts() {
        Intent intent = new Intent(getApplicationContext(), RecentContactsActivity.class);
        // loop through hashset, create a string of each Contact and add it to a string array
        ArrayList<String> recentContactsStringArray = new ArrayList<>();
        for (Contact contact : mRecentContacts)
            recentContactsStringArray.add( getJSONStringFromObject(contact) );
        intent.putStringArrayListExtra("RECENT_CONTACTS",recentContactsStringArray);
        startActivity(intent);
    }

    // When the contact is clicked then we increment the amount of clicks the  user clicked.
    @Override protected void onSaveInstanceState (@NonNull Bundle outState)
    {
        super.onSaveInstanceState (outState);
        outState.putInt(COUNTER, mCounter);

        // Send the mRecentContacts HashSet to the saved InstanceState bundle
        ArrayList<String> recentContactsStringArray = new ArrayList<>();
        for (Contact contact : mRecentContacts)
            recentContactsStringArray.add( getJSONStringFromObject(contact) );
        outState.putStringArrayList("RECENT_CONTACTS", recentContactsStringArray);
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Add the contact to the recently clicked contact hashset.
            // I had to override the hashcode so that it does not duplicate the objects.
            String contactString = intent.getStringExtra("CLICKED_CONTACT");
            Contact contact = getObjectFromJSONString(contactString);
            mRecentContacts.add(contact);

             mCounter = intent.getIntExtra(COUNTER, mCounter);
            // Toast.makeText(MainActivity.this, mCounter + "", Toast.LENGTH_SHORT).show();
            //Snackbar.make(findViewById(android.R.id.content).getRootView(),  "You clicked on " + mCounter + " contacts.", Snackbar.LENGTH_LONG).show(); //.setAction("Action", recentContacts()).show();
        }
    };


    // PREFS

    // when the apps stops then we'll call saveToSharedPref() to save the info
    @Override
    protected void onStop() {
        saveToSharedPref();
        super.onStop();
    }

    private void saveToSharedPref ()
    {
        // create file or read from already created file. name of file should match  key.
        SharedPreferences preferences = getSharedPreferences (mPrefKey, MODE_PRIVATE);

        // Editor object
        SharedPreferences.Editor editor = preferences.edit ();

        // clear current file from last save
        editor.clear ();

        // save the settings (keep recent contacts)
        saveSettingsToSharedPrefs (editor);

        // if autoSave is on then save the actual data/recentcontacts
        saveRecentContactsToSharedPrefsIfAutoSaveIsOn (editor);

        // apply the changes to the XML file in the device's storage
        editor.apply ();
    }

    private void saveRecentContactsToSharedPrefsIfAutoSaveIsOn(SharedPreferences.Editor editor) {
        if (mPrefSaveRecentContacts){
            HashSet<String> recentContactsStringArray = new HashSet<>();
            for (Contact contact : mRecentContacts)
                recentContactsStringArray.add( getJSONStringFromObject(contact) );
            editor.putStringSet(mKeyRecentContacts ,recentContactsStringArray);

        }
    }

    private void saveSettingsToSharedPrefs(SharedPreferences.Editor editor) {
        // save/nosave users recent contacts
        editor.putBoolean(mKeyPrefSaveRecentContacts, mPrefSaveRecentContacts);
    }

    // Courtesy feature to keep the same setting checked from last session.
    private void restoreAppSettingsFromPrefs ()
    {
        // Since this is for reading only, no editor is needed unlike in saveRestoreState
        SharedPreferences preferences = getSharedPreferences (mPrefKey, MODE_PRIVATE);

        // restore AutoSave preference value
        mPrefSaveRecentContacts = preferences.getBoolean (mKeyPrefSaveRecentContacts, true);
    }

    private void getRecentContactsFromPrefs() {
        // Restore if user wants us to.
        if (mPrefSaveRecentContacts && isExistingContactsInPrefs())
            restoreRecentContacts();
    }

    // Gets the contacts from prefs and stores it in our Recentcontact hashset
    private void restoreRecentContacts() {
        SharedPreferences preferences = getSharedPreferences(mPrefKey, MODE_PRIVATE);
        Set<String> recentContactStrings = preferences.getStringSet(mKeyRecentContacts, new HashSet<String>());
        for (String contact : recentContactStrings)
            mRecentContacts.add( getObjectFromJSONString(contact) );
    }

    // Make sure there is at least one contact stored in  prefs
    private boolean isExistingContactsInPrefs() {
        SharedPreferences preferences = getSharedPreferences(mPrefKey, MODE_PRIVATE);
        Set<String> contactsExist = preferences.getStringSet(mKeyRecentContacts, new HashSet<String>());
        return (contactsExist.size() > 0);
    }
    // END - PREFS


    // Settings page stuff

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_toggle_save_rc).setChecked(mPrefSaveRecentContacts);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_toggle_save_rc) {
            toggleContactMenuItem(item);
            return true;
        }
        if (id == R.id.action_about)
            Utils.showInfoDialog (this, R.string.app_name, R.string.about_message);
        return super.onOptionsItemSelected(item);

    }

    // Toggle the menu button
    private void toggleContactMenuItem(MenuItem item) {
        item.setChecked(!item.isChecked());
        mPrefSaveRecentContacts = item.isChecked();
    }


    // GSON helpers
    public static Contact getObjectFromJSONString (String json)
    {
        Gson gson = new Gson ();
        return gson.fromJson (json, Contact.class);
    }

    // Convert object tostring to be passed to ContactDetailActivity
    public String getJSONStringFromObject (Contact obj)
    {
        Gson gson = new Gson ();
        return gson.toJson (obj);
    }

}
