package com.example.hangout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContactMessageActivity extends AppCompatActivity {
    Contact _contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_message);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int val = pref.getInt("barColorIndex", 0);
        Resources res = getResources();
        String string = res.getStringArray(R.array.color_values)[val];
        toolbar.setBackgroundColor(Color.parseColor(string));
        toolbar.setTitle(R.string.title_activity_contact_message);
        Intent intent = getIntent();
        _contact = ContactManager.instance().getContact(intent.getIntExtra("id", -1));

        TextView textView = findViewById(R.id.contactName);
        textView.setText(_contact.getName());

        Button btnClick = findViewById(R.id.messageSubmit) ;
        btnClick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.messageBox);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(_contact.getNumber(), null, editText.getText().toString(), null, null);
                finish();
            }
        });
    }

}
