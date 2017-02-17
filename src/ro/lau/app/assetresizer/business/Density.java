package ro.lau.app.assetresizer.business;

public enum Density {
    MDPI("mdpi", 1f),
    HDPI("hdpi", 1.5f),
    XHDPI("xhdpi", 2f),
    XXHDPI("xxhdpi", 3f),
    XXXHDPI("xxxhdpi", 4f);

    private String name;
    private float multiplier;

    Density(String name, float multiplier) {
        this.name = name;
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public float getMultiplier() {
        return multiplier;
    }
}