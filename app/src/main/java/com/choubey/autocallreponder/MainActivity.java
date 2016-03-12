package com.choubey.autocallreponder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.choubey.autocallreponder.db.InmemoryCacheFreshness;
import com.choubey.autocallreponder.db.TemplatesDbDao;
import com.choubey.autocallreponder.db.UserTemplatesData;
import com.choubey.autocallreponder.fetchcontacts.FetchContactsMainActivity;

public class MainActivity extends ActionBarActivity {
    private static final int GET_CONTACT_REQUEST_CODE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 6;
    private String numberEntered = null;
    private String contactName = null;
    private static final String OPENING_BRACES = "[";
    private static final String CLOSING_BRACES = "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private UserTemplatesData constructDataFromInput(UserTemplatesData.ActiveStatus active, Context context)
    {
        TextView messageTextView = (TextView)findViewById(R.id.edit_text);
        String messageEntered = messageTextView.getText().toString();
        if(numberEntered == null || numberEntered.equals("") ||
                messageEntered == null || messageEntered.equals(""))
        {
            Toast.makeText(context, "Please enter a number and a message", Toast.LENGTH_LONG);
            return null;
        }

        UserTemplatesData userTemplatesData = new UserTemplatesData();
        userTemplatesData.setContactNumber(numberEntered);
        userTemplatesData.setContactName(contactName);
        userTemplatesData.setMessage(messageEntered);
        userTemplatesData.setStatus(active);
        return userTemplatesData;
    }

    public void activate(View view) {
        UserTemplatesData userTemplatesData = constructDataFromInput(UserTemplatesData.ActiveStatus.Y, view.getContext());
        if(userTemplatesData != null) {
            TemplatesDbDao.createTemplate(this, userTemplatesData);
            setContentView(R.layout.activity_main);
            InmemoryCacheFreshness.setCacheFresh(false);
            Toast.makeText(view.getContext(), "Template activated", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(view.getContext(), "Template could not be activated", Toast.LENGTH_SHORT).show();
        }
    }

    public void save(View view) {
        UserTemplatesData userTemplatesData = constructDataFromInput(UserTemplatesData.ActiveStatus.N, view.getContext());
        if(userTemplatesData != null) {
            TemplatesDbDao.createTemplate(this, userTemplatesData);
            setContentView(R.layout.activity_main);
            Toast.makeText(view.getContext(), "Template saved successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(view.getContext(), "Template could not be saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void manageExistingTemplates(View view) {
        Intent intent = new Intent(this, ManageTemplatesActivity.class);
        startActivity(intent);
    }

    public void showContacts(View view) {
        //Toast.makeText(view.getContext(), "Fetching contacts", Toast.LENGTH_SHORT).show();
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                } else {
                // No explanation needed, we can request the permission.
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

                }

                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
        else
        {
            Intent intent = new Intent(this, FetchContactsMainActivity.class);
            startActivityForResult(intent, GET_CONTACT_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(this, FetchContactsMainActivity.class);
                    startActivityForResult(intent, GET_CONTACT_REQUEST_CODE);

                } else {
                    Toast.makeText(this.getBaseContext(), "You need to provide permission to read the contacts", Toast.LENGTH_LONG);
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == GET_CONTACT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                EditText editTextForNumber = (EditText) findViewById(R.id.edit_number);
                String name = data.getStringExtra("contactName");
                String number = data.getStringExtra("number");
                this.contactName = name;
                this.numberEntered = number;
                Toast.makeText(getBaseContext(), "Contact name = " + name, Toast.LENGTH_SHORT);
                Log.i(this.getClass().getName(), "Name = " + name + ", number = " + number);
                editTextForNumber.setText(formatContactNameNumber(name, number, /*editTextForNumber.getMaxWidth()*/ 23));

            } else {
                Toast.makeText(getBaseContext(), "Error while fetching contact", Toast.LENGTH_SHORT);
            }
        }
        else
        {
            Toast.makeText(getBaseContext(), "Unknown request code = " + requestCode, Toast.LENGTH_SHORT);
        }
    }

    private String formatContactNameNumber(String name, String number, int maxTextBoxSize)
    {
        Log.i(this.getClass().getName(), "Max text box size = " + maxTextBoxSize);
        StringBuilder formattedContact = new StringBuilder();
        int charsRemainingForName = maxTextBoxSize - number.length() - 2;
        formattedContact.append(name.substring(0, charsRemainingForName < name.length() ? charsRemainingForName : name.length()));
        formattedContact.append(OPENING_BRACES);
        formattedContact.append(number);
        formattedContact.append(CLOSING_BRACES);
        return formattedContact.toString();
    }
}
