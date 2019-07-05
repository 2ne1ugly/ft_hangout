package com.example.hangout;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URI;

public class Contact {
    private int     _ID;
    private String  _name;
    private String  _number;
    private String  _description;
    private Bitmap _image;

    public Contact(String name, String number, String description, int ID, Bitmap uri) {
        _name = name;
        _number = number;
        _description = description;
        _ID = ID;
        _image = uri;
    }

    public int getID() {
        return _ID;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getNumber() {
        return _number;
    }

    public void setNumber(String _number) {
        this._number = _number;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String _description) {
        this._description = _description;
    }

    public Bitmap getImage() {
        return _image;
    }

    public void setImage(Bitmap _image) {
        this._image = _image;
    }
}
