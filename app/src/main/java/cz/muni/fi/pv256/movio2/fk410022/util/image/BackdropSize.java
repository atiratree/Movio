package cz.muni.fi.pv256.movio2.fk410022.util.image;

enum BackdropSize implements Size {
    W300(300),
    W780(780),
    W1280(1280),
    ORIGINAL(Integer.MAX_VALUE);

    private final int size;

    BackdropSize(int size) {
        this.size = size;
    }

    @Override
    public String getUrlPart() {
        return name().toLowerCase();
    }

    @Override
    public int getValue() {
        return size;
    }
}
