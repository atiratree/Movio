package cz.muni.fi.pv256.movio2.fk410022.util.image;

enum PosterSize implements Size {
    W92(92),
    W154(154),
    W185(185),
    W342(342),
    W500(500),
    W780(780),
    ORIGINAL(Integer.MAX_VALUE);

    private int size;

    PosterSize(int size) {
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
