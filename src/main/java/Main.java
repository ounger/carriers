public class Main{

    private static final int SIZE = 20;

    public static void main(String[] args){
        World world = World.getInstance();
        world.setSize(SIZE, SIZE);
        world.generateEmptyTerrain();

        GUI gui = new GUI(world);
        gui.showUp();
    }
}
