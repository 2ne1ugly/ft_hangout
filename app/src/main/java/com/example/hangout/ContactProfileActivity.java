package com.example.hangout;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactProfileActivity extends AppCompatActivity {
    Contact _contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        _contact = ContactManager.instance().getContact(intent.getIntExtra("id", -1));
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_contact_profile);

        setContentView(R.layout.activity_contact_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView name = findViewById(R.id.profileName);
        TextView description = findViewById(R.id.profileDescription);
        TextView number = findViewById(R.id.profileNumber);
        ImageView image = findViewById(R.id.imageView);
        name.setText(_contact.getName());
        description.setText(_contact.getDescription());
        number.setText(_contact.getNumber());
        image.setImageBitmap(_contact.getImage());
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int val = pref.getInt("barColorIndex", 0);
        Resources res = getResources();
        String string = res.getStringArray(R.array.color_values)[val];
        toolbar.setBackgroundColor(Color.parseColor(string));


        FloatingActionButton callFab = findViewById(R.id.callFab);
        callFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(ContactProfileActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + _contact.getNumber()));
                startActivity(callIntent);
            }
        });

        FloatingActionButton SMSFab = findViewById(R.id.SMSFab);
        SMSFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    return;
                Intent intent = new Intent(ContactProfileActivity.this, ContactMessageActivity.class);
                intent.putExtra("id", _contact.getID());
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_profile) {
            Intent intent = new Intent(this, ContactEditingActivity.class);
            intent.putExtra("id", _contact.getID());
            startActivityForResult(intent, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                String action = intent.getStringExtra("action");
                if (action.equals("delete")) {
                    finish();
                } else if (action.equals("edit")) {
                    _contact = ContactManager.instance().getContact(intent.getIntExtra("id", -1));
                    TextView name = findViewById(R.id.profileName);
                    TextView description = findViewById(R.id.profileDescription);
                    TextView number = findViewById(R.id.profileNumber);
                    ImageView imageView = findViewById(R.id.imageView);
                    name.setText(_contact.getName());
                    description.setText(_contact.getDescription());
                    number.setText(_contact.getNumber());
                    imageView.setImageBitmap(_contact.getImage());
                }
            }
        }
    }
}
