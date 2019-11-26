public enum NodeType{
	GROUND('⋅'),
	SOLID('#'), 
	PATH('+'),
	START('S'),
	END('E');

    public char asChar() {
        return asChar;
    }

    private final char asChar;

    NodeType(char asChar) {
        this.asChar = asChar;
    }
}