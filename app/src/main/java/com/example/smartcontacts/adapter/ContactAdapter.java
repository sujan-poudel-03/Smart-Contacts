package com.example.smartcontacts.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcontacts.R;
import com.example.smartcontacts.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements Filterable {

    private List<Contact> contacts;
    private List<Contact> contactsFull;
    private OnContactClickListener listener;

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
        void onEditClick(Contact contact);
        void onDeleteClick(Contact contact);
    }

    public ContactAdapter(List<Contact> contacts, OnContactClickListener listener) {
        this.contacts = contacts;
        this.contactsFull = new ArrayList<>(contacts);
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    private Filter contactFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(contactsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Contact item : contactsFull) {
                    if (item.getFullName().toLowerCase().contains(filterPattern) || item.getCompany().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contacts.clear();
            contacts.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatar, tvFullName, tvCompany;
        ImageView ivOptions;

        public ViewHolder(View view) {
            super(view);
            tvAvatar = view.findViewById(R.id.tvAvatar);
            tvFullName = view.findViewById(R.id.tvFullName);
            tvCompany = view.findViewById(R.id.tvCompany);
            ivOptions = view.findViewById(R.id.ivOptions);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contact c = contacts.get(position);
        holder.tvFullName.setText(c.getFullName());
        holder.tvCompany.setText(c.getCompany());

        if (c.getFirstName() != null && !c.getFirstName().isEmpty()) {
            holder.tvAvatar.setText(String.valueOf(c.getFirstName().charAt(0)));
        } else {
            holder.tvAvatar.setText(""); // Clear avatar if no first name
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onContactClick(c);
            }
        });

        holder.ivOptions.setOnClickListener(v -> {
            if (listener != null) {
                showPopupMenu(holder.ivOptions, c);
            }
        });
    }

    private void showPopupMenu(View view, Contact contact) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.menu_contact_options, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_edit) {
                listener.onEditClick(contact);
                return true;
            } else if (itemId == R.id.action_delete) {
                listener.onDeleteClick(contact);
                return true;
            }
            return false;
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void updateList(List<Contact> newList) {
        contacts.clear();
        contacts.addAll(newList);
        contactsFull.clear();
        contactsFull.addAll(newList);
        notifyDataSetChanged();
    }
}
