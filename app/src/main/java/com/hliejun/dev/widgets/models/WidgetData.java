package com.hliejun.dev.widgets.models;

import android.net.Uri;

import java.io.Serializable;

public class WidgetData implements Serializable {

    private Contact contact;

    private String label;
    private String description;
    private boolean shouldUseNumber = false;
    private boolean shouldUseAvatar = false;
    private boolean shouldUseLargeText = false;

    private PaletteColor color;

    /*** Constructors ***/

    public WidgetData() {
        super();
    }

    /*** Setters ***/

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void setContact(String whatsAppId, String name, String number) {
        this.contact = new Contact(whatsAppId, name, number);
    }

    public void setContact(String whatsAppId, String name, String number, Uri photo) {
        this.contact = new Contact(whatsAppId, name, number, photo);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShouldUseNumber(boolean shouldUseNumber) {
        this.shouldUseNumber = shouldUseNumber;
    }

    public void setShouldUseAvatar(boolean shouldUseAvatar) {
        this.shouldUseAvatar = shouldUseAvatar;
    }

    public void setShouldUseLargeText(boolean shouldUseLargeText) {
        this.shouldUseLargeText = shouldUseLargeText;
    }

    public void setOptions(String label, String description, boolean shouldUseNumber, boolean shouldUseAvatar, boolean shouldUseLargeText) {
        this.label = label;
        this.description = description;
        this.shouldUseNumber = shouldUseNumber;
        this.shouldUseAvatar = shouldUseAvatar;
        this.shouldUseLargeText = shouldUseLargeText;
    }

    public void setColor(PaletteColor color) {
        this.color = color;
    }

    public void setColor(String name, String hex) {
        this.color = new PaletteColor(name, hex);
    }

    public void setColor(String name, String hex, PaletteColor.ColorType type) {
        this.color = new PaletteColor(name, hex, type);
    }

    /*** Getters ***/

    public Contact getContact() {
        return contact;
    }

    public String getWhatsAppId() {
        return contact != null ? contact.getWhatsAppId() : null;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public boolean isShouldUseNumber() {
        return shouldUseNumber;
    }

    public boolean isShouldUseAvatar() {
        return shouldUseAvatar;
    }

    public boolean isShouldUseLargeText() {
        return shouldUseLargeText;
    }

    public PaletteColor getColor() {
        return color;
    }

    public String getColorHex() {
        return color != null ? color.getHex() : null;
    }

    /*** Auxiliary ***/

    public String toString() {
        return "Color [contact=" + contact
                  + ", label=" + label
                  + ", description=" + description
                  + ", shouldUseNumber=" + shouldUseNumber
                  + ", shouldUseAvatar=" + shouldUseAvatar
                  + ", shouldUseLargeText=" + shouldUseLargeText
                  + ", color=" + color
                  + "]";
    }

}
