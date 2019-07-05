package com.example.hangout;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> _contacts;
    private AppCompatActivity _appCompatActivity;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ConstraintLayout constraintLayout;
        public ContactViewHolder(ConstraintLayout v) {
            super(v);
            constraintLayout = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactAdapter(List<Contact> contacts, AppCompatActivity parent) {
        _contacts = contacts;
        _appCompatActivity = parent;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // create a new view
        final ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_view, parent, false);
        final RecyclerView recyclerView = (RecyclerView) parent;
        constraintLayout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                Contact item = _contacts.get(itemPosition);
                Intent intent = new Intent(_appCompatActivity, ContactProfileActivity.class);
                intent.putExtra("id", item.getID());
                _appCompatActivity.startActivity(intent);
            }
        });;
        ContactViewHolder vh = new ContactViewHolder(constraintLayout);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {
        TextView name = (TextView) holder.constraintLayout.getViewById(R.id.contact_list_name);
        TextView description = (TextView) holder.constraintLayout.getViewById(R.id.contact_list_description);
        ImageView image = (ImageView) holder.constraintLayout.getViewById(R.id.contact_list_image);
        name.setText(_contacts.get(position).getName());
        description.setText(_contacts.get(position).getDescription());
        image.setImageBitmap(_contacts.get(position).getImage());
//        holder.view.setText(_contacts.get(position).getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _contacts.size();
    }
}
