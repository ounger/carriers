public enum Mode {

    PFA(0),
    SIM(1);

    public int asInt() {
        return asInt;
    }

    private final int asInt;

    Mode(int asInt) {
        this.asInt = asInt;
    }
}