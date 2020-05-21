package com.example.tourodirectory.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourodirectory.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    // 1) Store it in a text file and pull it in
    // 2) from g sheet
    // 3) Store it in an array
    private String[] mContacts;


    // Constructor
    public ContactAdapter() {
        mContacts = new String[]{"Professor A", "Professor B", "Professor C"};
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mButton.setText(mContacts[position]);
    }


    // Our RecyclerView calls this getItemCount() to know how many items there are to inflate
    @Override
    public int getItemCount() {
        return mContacts.length;
    }


    // STEP 2
    public class ViewHolder extends RecyclerView.ViewHolder {

        // Contact button
        private Button mButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // sets the button to be used in the onBindViewHolder()
            mButton = itemView.findViewById(R.id.contact_button);
        }
    }
}
