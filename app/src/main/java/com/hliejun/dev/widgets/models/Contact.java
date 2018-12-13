package com.hliejun.dev.widgets.models;

import android.net.Uri;

import java.io.Serializable;

public class Contact implements Serializable {

    private String whatsAppId;
    private String name;
    private String number;
    private String photo;

    /*** Constructors ***/

    public Contact(String whatsAppId, String name, String number) {
        super();
        this.whatsAppId = whatsAppId;
        this.name = name;
        this.number = number;
    }

    public Contact(String whatsAppId, String name, String number, Uri photo) {
        this(whatsAppId, name, number);
        this.photo = photo == null ? null : photo.toString();
    }

    /*** Setters ***/

    public void setWhatsAppId(String whatsAppId) {
        this.whatsAppId = whatsAppId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo == null ? null : photo.toString();
    }

    /*** Getters ***/

    public String getWhatsAppId() {
        return whatsAppId;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Uri getPhoto() {
        return photo == null ? null : Uri.parse(photo);
    }

    /*** Auxiliary ***/

    public String getInitials() {
        String[] splitString = name.split(" ");

        if (splitString.length == 1) {
            String firstWord = splitString[0];
            return firstWord.substring(0, Math.min(firstWord.length(), 3));
        }

        StringBuilder initials = new StringBuilder();

        int count = 0;
        while (initials.length() < 3 && count < splitString.length) {
            initials.append(splitString[count].substring(0, 1));
            count += 1;
        }

        return initials.toString().toUpperCase();
    }

    public String toString() {
        return "Contact [id=" + whatsAppId
                    + ", name=" + name
                    + ", number=" + number
                    + ", photo=" + photo
                    + "]";
    }

}
