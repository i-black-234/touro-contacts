package com.example.tourodirectory.classes;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourodirectory.R;
import com.example.tourodirectory.activities.ContactDetailActivity;

import java.net.Inet4Address;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    // 1) Store it in a text file and pull it in
    // 2) from g sheet
    // 3) Store it in an array
    // private String[] mContacts;
    private ArrayList<Contact> mContacts;



    // Constructor
    public ContactAdapter(ArrayList<Contact> contacts) {
        // mContacts = new String[]{"Professor A", "Professor B", "Professor C"};
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
            mButton.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ContactDetailActivity.class);
            v.getContext().startActivity(intent);
            Toast.makeText(v.getContext(), "CLICKED: " + getAbsoluteAdapterPosition(), Toast.LENGTH_SHORT).show();

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