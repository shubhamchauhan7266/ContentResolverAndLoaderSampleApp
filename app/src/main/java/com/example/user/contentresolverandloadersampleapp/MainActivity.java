package com.example.user.contentresolverandloadersampleapp;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static ListView contact_listview;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contact_listview = (ListView) findViewById(R.id.contact_listview);

        createProgressDialog();
        getLoaderManager().initLoader(1, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i == 1) {
            return new CursorLoader(this, ContactsContract.Contacts.CONTENT_URI, null, null,
                    null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        ArrayList<Contact_Model> arrayList = readContacts(cursor);
        if (arrayList != null && arrayList.size() > 0) {

            getSupportActionBar().setSubtitle(
                    arrayList.size() + " Contacts");
            Contact_Adapter adapter;
            adapter = new Contact_Adapter(MainActivity.this, arrayList);
            contact_listview.setAdapter(adapter);// set adapter
            adapter.notifyDataSetChanged();
        } else {
            // If adapter is null then show toast
            Toast.makeText(MainActivity.this, "There are no contacts.",
                    Toast.LENGTH_LONG).show();
        }
        progressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    // Method that return all contact details in array format
    private ArrayList<Contact_Model> readContacts(Cursor contactsCursor) {
        ArrayList<Contact_Model> contactList = new ArrayList<>();

        assert contactsCursor != null;
        if (contactsCursor.moveToFirst()) {
            do {
                long contctId = contactsCursor.getLong(contactsCursor
                        .getColumnIndex("_ID"));

                Uri dataUri = ContactsContract.Data.CONTENT_URI;
                Cursor dataCursor = getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contctId,
                        null, null);

                // Strings to get all details
                String displayName;
                String nickName;
                String homePhone;
                String mobilePhone;
                String workPhone;
                String photoPath = "" + R.drawable.ic_launcher;
                byte[] photoByte;
                // in BLOB
                String homeEmail;
                String workEmail;
                String companyName;
                String title;

                // This strings stores all contact numbers, email and other
                // details like nick name, company etc.
                String contactNumbers = "";
                String contactEmailAddresses = "";
                String contactOtherDetails = "";

                // Now start the cusrsor
                assert dataCursor != null;
                if (dataCursor.moveToFirst()) {
                    displayName = dataCursor
                            .getString(dataCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    do {
                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)) {
                            nickName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                            contactOtherDetails += "NickName : " + nickName + "\n";
                        }

                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                            // In this get All contact numbers like home,
                            // mobile, work, etc and add them to numbers string
                            switch (dataCursor.getInt(dataCursor.getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    homePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    contactNumbers += "Home Phone : " + homePhone + "\n";
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    workPhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    contactNumbers += "Work Phone : " + workPhone + "\n";
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    mobilePhone = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    contactNumbers += "Mobile Phone : " + mobilePhone + "\n";
                                    break;

                            }
                        }
                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {

//                             In this get all Emails like home, work etc and
//                             add them to email string
                            switch (dataCursor.getInt(dataCursor.getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                    homeEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    contactEmailAddresses += "Home Email : " + homeEmail + "\n";
                                    break;
                                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                                    workEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    contactEmailAddresses += "Work Email : " + workEmail + "\n";
                                    break;

                            }
                        }

                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)) {
                            companyName = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                            contactOtherDetails += "Coompany Name : " + companyName + "\n";
                            title = dataCursor.getString(dataCursor.getColumnIndex("data4"));
                            contactOtherDetails += "Title : " + title + "\n";

                        }

                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)) {
                            photoByte = dataCursor.getBlob(dataCursor.getColumnIndex("data15"));
                            if (photoByte != null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);
                                File cacheDirectory = getBaseContext().getCacheDir();
                                File tmp = new File(cacheDirectory.getPath() + "/_androhub" + contctId + ".png");
                                try {
                                    FileOutputStream fileOutputStream = new FileOutputStream(tmp);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                                photoPath = tmp.getPath();
                            }

                        }

                    } while (dataCursor.moveToNext());

                    contactList.add(new Contact_Model(Long.toString(contctId),
                            displayName, contactNumbers, contactEmailAddresses,
                            photoPath, contactOtherDetails));

                }

            } while (contactsCursor.moveToNext());
        }
        return contactList;
    }

    public void createProgressDialog() {
        progressDialog = new ProgressDialog(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        progressDialog.setMessage("Loading Data Please Wait!!!");
        progressDialog.show();
    }
}
