package com.hliejun.dev.whatsappwidgets.interfaces;

import com.hliejun.dev.whatsappwidgets.models.Contact;

public interface ContactInterface {

    public void writeContact(Contact contact);

    public Contact readContact();

}