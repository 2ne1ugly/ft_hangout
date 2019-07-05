package com.example.hangout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ContactAddingActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_adding);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_activity_contact_adding);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int val = pref.getInt("barColorIndex", 0);
        Resources res = getResources();
        String string = res.getStringArray(R.array.color_values)[val];
        toolbar.setBackgroundColor(Color.parseColor(string));

        final ImageView image = findViewById(R.id.profileImage);
        image.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        Button btnClick = findViewById(R.id.submitButton) ;
        btnClick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameInput = findViewById(R.id.nameInput);
                EditText numberInput = findViewById(R.id.numberInput);
                EditText descriptionInput = findViewById(R.id.descriptionInput);
                ContactManager.instance().newContact(
                        nameInput.getText().toString(),
                        numberInput.getText().toString(),
                        descriptionInput.getText().toString(),
                        ((BitmapDrawable)image.getDrawable()).getBitmap());
                finish();
            }
        });
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                ImageView image = findViewById(R.id.profileImage);
                image.setImageURI(data.getData());
                image.setImageBitmap(getResizedBitmap(((BitmapDrawable) image.getDrawable()).getBitmap(), 100));
            }
        }
    }
}
