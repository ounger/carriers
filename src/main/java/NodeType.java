import java.awt.*;

public enum NodeType {
    //World
	GROUND('⋅', Color.GREEN),
	SOLID('#', Color.BLUE),

    //Others
    PATH('+', Color.LIGHT_GRAY),
	START('S', Color.RED),
	END('E', Color.BLACK);

    public char asChar() {
        return asChar;
    }

    public Color getColor(){ return color; }

    private final char asChar;
    private final Color color;

    NodeType(char asChar, Color color) {
        this.asChar = asChar;
        this.color = color;
    }
}