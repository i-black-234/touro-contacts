package com.example.tourodirectory.classes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourodirectory.R;
import com.example.tourodirectory.activities.ContactDetailActivity;
import com.google.gson.Gson;

import java.util.ArrayList;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourodirectory.R;
import com.example.tourodirectory.activities.ContactDetailActivity;
import com.google.gson.Gson;

import java.net.Inet4Address;
import java.util.ArrayList;
public class RecentContactAdapter extends RecyclerView.Adapter<RecentContactAdapter.ViewHolder> {

    // 1) Store it in a text file and pull it in
    // 2) from g sheet
    // 3) Store it in an array
    // private String[] mContacts;
    private ArrayList<Contact> mContacts;

    // Constructor

    public RecentContactAdapter(ArrayList<Contact> contacts) {
        mContacts = contacts;
    }


    // STEP 1
    // this will be called automatically to essentially construct each individual contact, which is a ViewHolder
    // inflates and then returns a copy of our rv_item.xml to the RecyclerView!
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // INFLATE our contact.xml
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    // STEP 3
    // – this will also be called automatically after onCreateViewHolder() to bind this square to its data source
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //holder.mButton.setText(mContacts[position]);
        holder.mButton.setText(mContacts.get(position).getFullName());
    }

    // Our RecyclerView calls this getItemCount() to know how many items there are to inflate
    @Override
    public int getItemCount() {

        // return mContacts.length;
        return mContacts.size();
    }


    // STEP 2
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Contact button
        private Button mButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // sets the button to be used in the onBindViewHolder()
            mButton = itemView.findViewById(R.id.contact_button);

            // Attached onclick to the button
            mButton.setOnClickListener(this);

        }


        // When a user clicks on a contact.
        @Override
        public void onClick(View v) {
            // This will launch the contact detail popup.
            Intent contactIntent = new Intent(v.getContext(), ContactDetailActivity.class);
            contactIntent.putExtra("CONTACT", getJSONStringFromObject(mContacts.get(getAbsoluteAdapterPosition())));
            v.getContext().startActivity(contactIntent);
        }


        // Convert object tostring to be passed to ContactDetailActivity
        public String getJSONStringFromObject(Contact obj) {
            Gson gson = new Gson();
            return gson.toJson(obj);
        }
    }
}

/*
 *
 * STEP 1:
 * create an interface
 * 2:
 * implement the interface in main activytu
 * implement methods
 * mContacts.getPositon
 * Intent intent = new Intent(this, COntac6tdetail.class)
 * start();
 * */