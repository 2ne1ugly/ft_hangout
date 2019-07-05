package com.example.hangout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ContactEditingActivity extends AppCompatActivity {
    Contact _contact;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        _contact = ContactManager.instance().getContact(intent.getIntExtra("id", -1));
        setContentView(R.layout.activity_contact_editing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int val = pref.getInt("barColorIndex", 0);
        Resources res = getResources();
        String string = res.getStringArray(R.array.color_values)[val];
        toolbar.setBackgroundColor(Color.parseColor(string));
        setTitle(R.string.title_activity_contact_editing);
        setSupportActionBar(toolbar);
        final EditText name = findViewById(R.id.editNameInput);
        final EditText number = findViewById(R.id.editNumberInput);
        final EditText description = findViewById(R.id.editDescriptionInput);
        final ImageView image = findViewById(R.id.profileImage);
        name.setText(_contact.getName());
        number.setText(_contact.getNumber());
        description.setText(_contact.getDescription());
        image.setImageBitmap(_contact.getImage());
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

        Button buttonEdit = (Button) findViewById(R.id.submitButton) ;
        buttonEdit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                _contact.setName(name.getText().toString());
                _contact.setDescription(description.getText().toString());
                _contact.setNumber(number.getText().toString());
                _contact.setImage(((BitmapDrawable)image.getDrawable()).getBitmap());
                ContactManager.editContact(_contact);
                Intent intent = new Intent();
                intent.putExtra("action", "edit");
                intent.putExtra("id", _contact.getID());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        Button buttonDelete = (Button) findViewById(R.id.deleteButton) ;
        buttonDelete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactManager.deleteContact(_contact);
                Intent intent = new Intent();
                intent.putExtra("action", "delete");
                setResult(RESULT_OK, intent);
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
