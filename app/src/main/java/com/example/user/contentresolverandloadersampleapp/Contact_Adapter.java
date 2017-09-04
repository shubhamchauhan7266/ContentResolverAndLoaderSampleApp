package com.example.user.contentresolverandloadersampleapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shubham Chauhan on 1/9/17.
 */

class Contact_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Contact_Model> arrayList;

    Contact_Adapter(Context context, ArrayList<Contact_Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Contact_Model getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Contact_Model model = arrayList.get(position);
        ViewHodler holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_view, parent, false);
            holder = new ViewHodler();
            holder.contactImage = convertView.findViewById(R.id.contactImage);
            holder.contactName = convertView.findViewById(R.id.contactName);
            holder.contactEmail = convertView.findViewById(R.id.contactEmail);
            holder.contactNumber = convertView.findViewById(R.id.contactNumber);
            holder.contactOtherDetails = convertView.findViewById(R.id.contactOtherDetails);
            convertView.setTag(holder);
        } else {
            holder = (ViewHodler) convertView.getTag();
        }

        // Set items to all view
        if (!model.getContactName().equals("") && model.getContactName() != null) {
            holder.contactName.setText(model.getContactName());
        } else {
            holder.contactName.setText(R.string.no_name);
        }

        if (!model.getContactEmail().equals("") && model.getContactEmail() != null) {
            holder.contactEmail.setText(String.valueOf("Email id - " + model.getContactEmail()));
        } else {
            holder.contactEmail.setText(R.string.no_email_id);
        }

        if (!model.getContactNumber().equals("") && model.getContactNumber() != null) {
            holder.contactNumber.setText(String.valueOf("CONTACT NUMBER - " + model.getContactNumber()));
        } else {
            holder.contactNumber.setText(String.valueOf("CONTACT NUMBER - " + "No Contact Number"));
        }

        if (!model.getContactOtherDetails().equals("") && model.getContactOtherDetails() != null) {
            holder.contactOtherDetails.setText(String.valueOf("OTHER DETAILS - " + model.getContactOtherDetails()));
        } else {
            holder.contactOtherDetails.setText(String.valueOf("OTHER DETAILS - " + "Other details are empty"));
        }

        // Bitmap for imageview
        Bitmap image;
        if (!model.getContactPhoto().equals("") && model.getContactPhoto() != null) {
            image = BitmapFactory.decodeFile(model.getContactPhoto());// decode

            if (image != null)
                holder.contactImage.setImageBitmap(image);
            else {
                image = BitmapFactory.decodeResource(context.getResources(), R.drawable.androhub_logo);
                holder.contactImage.setImageBitmap(image);
            }
        } else {
            image = BitmapFactory.decodeResource(context.getResources(), R.drawable.androhub_logo);
            holder.contactImage.setImageBitmap(image);
        }
        return convertView;
    }

    // View holder to hold views
    private class ViewHodler {
        ImageView contactImage;
        TextView contactName, contactNumber, contactEmail, contactOtherDetails;
    }
}
