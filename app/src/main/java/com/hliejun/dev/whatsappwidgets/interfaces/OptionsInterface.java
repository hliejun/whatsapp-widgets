package com.hliejun.dev.whatsappwidgets.interfaces;

public interface OptionsInterface {

    public void writeLabel(String label);

    public String readLabel();

    public void writeDescription(String description);

    public String readDescription();

    public void writeNumberOption(boolean shouldUseNumber);

    public boolean readNumberOption();

    public void writeAvatarOption(boolean shouldUseAvatar);

    public boolean readAvatarOption();

    public void writeLargeTextOption(boolean shouldUseLargeText);

    public boolean readLargeTextOption();

}