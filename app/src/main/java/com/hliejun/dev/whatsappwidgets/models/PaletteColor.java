package com.hliejun.dev.whatsappwidgets.models;

import java.io.Serializable;

public class PaletteColor implements Serializable {

    public enum ColorType {
        LIGHT, DARK
    }

    private String name;
    private String hex;
    private ColorType type = ColorType.DARK;

    /*** Constructors ***/

    public PaletteColor(String name, String hex) {
        super();
        this.name = name;
        this.hex = hex;
    }

    public PaletteColor(String name, String hex, ColorType type) {
        this(name, hex);
        this.type = type;
    }

    /*** Setters ***/

    public void setName(String name) {
        this.name = name;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public void setType(ColorType type) {
        this.type = type;
    }

    /*** Getters ***/

    public String getName() {
        return name;
    }

    public String getHex() {
        return hex;
    }

    public ColorType getType() {
        return type;
    }

    public boolean isLightColor() {
        return type == ColorType.LIGHT;
    }

    /*** Auxiliary ***/

    public String toString() {
        return "Color [name=" + name
                  + ", hex=" + hex
                  + ", type=" + type.toString()
                  + "]";
    }

}
