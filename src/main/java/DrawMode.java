public enum DrawMode {

    NONE(0),
    SOLID(1),
    START(2),
    END(3),
    RESOURCE(4),
    VASALL(5),
    STORAGE(6),
    GROUND(7);

    public int asInt() {
        return asInt;
    }

    private final int asInt;

    DrawMode(int asInt) {
        this.asInt = asInt;
    }
}