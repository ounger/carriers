public class Carriers2DSimulation {

    private static final int SIZE = 20;

    public static void main(String[] args){
        World world = World.getInstance();
        world.setSize(SIZE, SIZE);
        world.generateTerrainAuto(0.25f);

        GUI gui = new GUI(world);
        gui.showUp();
    }
}
