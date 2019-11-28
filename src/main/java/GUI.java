import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;

public class GUI extends JFrame{

    private static final int PLANNED_SIZE = 640;
    private static final int MARGIN = 10;

    private World world;

    private int tileSize;
    private int actualSize;

    private Node startNode;
    private Node endNode;
    private ArrayList<ArrayList<Node>> paths;

    private HashSet<Node> resources;
    private HashSet<Node> vassals;
    private HashSet<Node> storages;

    private MapPane mapPane;

    private JButton pfaButton;
    private JButton simButton;
    private JButton resetButton;
    private JButton startExecuteButton;

    private JToggleButton solidToggle;
    private JToggleButton startNodeToggle;
    private JToggleButton endNodeToggle;
    private JToggleButton groundToggle;
    private JToggleButton storageToggle;
    private JToggleButton vasToggle;
    private JToggleButton resourceToggle;

    private Checkbox diagonalMovingCheckBox;

    private Mode mode = Mode.SIM;
    private DrawMode drawMode = DrawMode.NONE;

    public GUI(World world){
        if(world.getWorldWidth() != world.getWorldHeight()){
            throw new IllegalArgumentException("Width != Height");
        }
        this.world = world;
        resize();
        this.paths = new ArrayList<>();
        this.vassals = new HashSet<>();
        this.storages = new HashSet<>();
        this.resources = new HashSet<>();
    }

    private void resize(){
        this.tileSize = (int) Math.floor((double) PLANNED_SIZE / (double) world.getWorldWidth());
        this.actualSize = tileSize * world.getWorldWidth();

        if(mapPane != null){
            mapPane.repaint();
        }
    }

    private void reset(boolean resetTerrain){
        this.startNode = null;
        this.endNode = null;
        this.paths = new ArrayList<>();
        this.vassals = new HashSet<>();
        this.storages = new HashSet<>();
        this.resources = new HashSet<>();

        if(resetTerrain){
            world.generateEmptyTerrain();
        }

        if(mapPane != null){
            mapPane.repaint();
        }
    }

    public void showUp(){
        init();
    }

    private void init(){
        setTitle("Carriers Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new EmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
        leftPanel.setPreferredSize(new Dimension(200, PLANNED_SIZE));
        initLeftPanel(leftPanel);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        mapPane = new MapPane();
        mapPane.setPreferredSize(new Dimension(PLANNED_SIZE, PLANNED_SIZE));
        mainPanel.add(mapPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(new EmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
        rightPanel.setPreferredSize(new Dimension(200, PLANNED_SIZE));
        initRightPanel(rightPanel);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        switchMode();
    }

    private void initLeftPanel(JPanel leftPanel){
        GridLayout gridLayout = new GridLayout(0,2);
        leftPanel.setLayout(gridLayout);
        gridLayout.setVgap(250);
        gridLayout.setHgap(MARGIN);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel();
        spinnerModel.setMinimum(World.MIN_SIZE + 1);
        spinnerModel.setMaximum(World.MAX_SIZE);
        spinnerModel.setValue(world.getWorldWidth());

        JLabel worldSizeLabel = new JLabel("Größe:");
        leftPanel.add(worldSizeLabel);

        JSpinner worldSizeSpinner = new JSpinner(spinnerModel);
        worldSizeSpinner.addChangeListener(e -> {
            world.setSize((int) worldSizeSpinner.getValue(), (int) worldSizeSpinner.getValue());
            reset(true);
            resize();
            untoggleRightSideButtons();
        });
        leftPanel.add(worldSizeSpinner);

        pfaButton = new JButton("PFA");
        pfaButton.setEnabled(false);
        pfaButton.setFocusable(false);
        pfaButton.addActionListener(e -> {
            switchMode();
            untoggleRightSideButtons();
        });
        leftPanel.add(pfaButton);

        simButton = new JButton("SIM");
        simButton.setFocusable(false);
        simButton.addActionListener(e -> {
            switchMode();
            untoggleRightSideButtons();
        });
        leftPanel.add(simButton);

        startExecuteButton = new JButton("Start");
        startExecuteButton.setFocusable(false);
        startExecuteButton.addActionListener(e -> {
            if(startNode != null && endNode != null){
                paths.clear();
                untoggleRightSideButtons();
                drawMode = DrawMode.NONE;
                AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(world, diagonalMovingCheckBox.getState());
                ArrayList path = (ArrayList) aStarAlgorithm.execute(startNode, endNode);
                if(path != null){
                    addPath(path);
                    JOptionPane.showMessageDialog(this,
                            "Path was found.");
                }
                else{
                    JOptionPane.showMessageDialog(this,
                            "No path was found.");
                }
            }
            else{
                if(mode == Mode.PFA)
                    JOptionPane.showMessageDialog(this,
                            "Min. ein Start- und Endknoten benötigt.");
            }
            untoggleRightSideButtons();
        });
        leftPanel.add(startExecuteButton);

        diagonalMovingCheckBox = new Checkbox("Diagonal");
        diagonalMovingCheckBox.setFocusable(false);
        leftPanel.add(diagonalMovingCheckBox);

    }

    private void initRightPanel(JPanel rightPanel){
        GridLayout gridLayout = new GridLayout(0,1);
        rightPanel.setLayout(gridLayout);
        gridLayout.setVgap(40);
        gridLayout.setHgap(MARGIN);

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel();
        spinnerModel.setMinimum(World.MIN_SIZE + 1);
        spinnerModel.setMaximum(World.MAX_SIZE);
        spinnerModel.setValue(world.getWorldWidth());

        groundToggle = new JToggleButton("Boden");
        groundToggle.setFocusable(false);
        groundToggle.addActionListener(e -> {
            untoggleRightSideButtons();
            groundToggle.setSelected(true);
            if(drawMode != DrawMode.GROUND){
                drawMode = DrawMode.GROUND;
            }
            else{
                drawMode = DrawMode.NONE;
            }
            paths.clear();
            mapPane.repaint();
        });
        rightPanel.add(groundToggle);

        solidToggle = new JToggleButton("Hindernis");
        solidToggle.setFocusable(false);
        solidToggle.addActionListener(e -> {
            untoggleRightSideButtons();
            solidToggle.setSelected(true);
            if(drawMode != DrawMode.SOLID){
                drawMode = DrawMode.SOLID;
            }
            else{
                drawMode = DrawMode.NONE;
            }
            paths.clear();
            mapPane.repaint();
        });
        rightPanel.add(solidToggle);

        startNodeToggle = new JToggleButton("Startknoten");
        startNodeToggle.setFocusable(false);
        startNodeToggle.addActionListener(e -> {
            untoggleRightSideButtons();
            startNodeToggle.setSelected(true);
            if(drawMode != DrawMode.START){
                drawMode = DrawMode.START;
            }
            else{
                drawMode = DrawMode.NONE;
            }
            paths.clear();
            mapPane.repaint();
        });
        rightPanel.add(startNodeToggle);

        endNodeToggle = new JToggleButton("Endknoten");
        endNodeToggle.setFocusable(false);
        endNodeToggle.addActionListener(e -> {
            untoggleRightSideButtons();
            endNodeToggle.setSelected(true);
            if(drawMode != DrawMode.END){
                drawMode = DrawMode.END;
            }
            else{
                drawMode = DrawMode.NONE;
            }
            paths.clear();
            mapPane.repaint();
        });
        rightPanel.add(endNodeToggle);

        resourceToggle = new JToggleButton("Ressource");
        resourceToggle.setFocusable(false);
        resourceToggle.addActionListener(e -> {
            untoggleRightSideButtons();
            resourceToggle.setSelected(true);
            if(drawMode != DrawMode.RESOURCE){
                drawMode = DrawMode.RESOURCE;
            }
            else{
                drawMode = DrawMode.NONE;
            }
            paths.clear();
            mapPane.repaint();
        });
        rightPanel.add(resourceToggle);

        vasToggle = new JToggleButton("Vasall");
        vasToggle.setFocusable(false);
        vasToggle.addActionListener(e -> {
            untoggleRightSideButtons();
            vasToggle.setSelected(true);
            if(drawMode != DrawMode.VASALL){
                drawMode = DrawMode.VASALL;
            }
            else{
                drawMode = DrawMode.NONE;
            }
            paths.clear();
            mapPane.repaint();
        });
        rightPanel.add(vasToggle);

        storageToggle = new JToggleButton("Lager");
        storageToggle.setFocusable(false);
        storageToggle.addActionListener(e -> {
            untoggleRightSideButtons();
            storageToggle.setSelected(true);
            if(drawMode != DrawMode.STORAGE){
                drawMode = DrawMode.STORAGE;
            }
            else{
                drawMode = DrawMode.NONE;
            }
            paths.clear();
            mapPane.repaint();
        });
        rightPanel.add(storageToggle);

        resetButton = new JButton("Reset");
        resetButton.setFocusable(false);
        resetButton.addActionListener(e -> {
            world.generateEmptyTerrain();
            mapPane.repaint();
            untoggleRightSideButtons();
        });
        rightPanel.add(resetButton);
    }

    public void addStartNode(Node startNode){
        this.startNode = startNode;
        mapPane.repaint();
    }

    public void addEndNode(Node endNode){
        this.endNode = endNode;
        mapPane.repaint();
    }

    public void removeStartNode(){
        this.startNode = null;
        mapPane.repaint();
    }

    public void removeEndNode(){
        this.endNode = null;
        mapPane.repaint();
    }

    public void addPath(ArrayList path){
        paths.add(path);
        mapPane.repaint();
    }

    public void removePath(ArrayList path){
        paths.remove(path);
        mapPane.repaint();
    }

    private void switchMode(){
        if(mode == Mode.PFA){
            mode = Mode.SIM;

            pfaButton.setEnabled(true);
            simButton.setEnabled(false);

            startNodeToggle.setEnabled(false);
            endNodeToggle.setEnabled(false);

            resourceToggle.setEnabled(true);
            vasToggle.setEnabled(true);
            storageToggle.setEnabled(true);
        }
        else{
            mode = Mode.PFA;

            pfaButton.setEnabled(false);
            simButton.setEnabled(true);

            startNodeToggle.setEnabled(true);
            endNodeToggle.setEnabled(true);

            resourceToggle.setEnabled(false);
            vasToggle.setEnabled(false);
            storageToggle.setEnabled(false);
        }
        reset(false);
    }

    private void untoggleRightSideButtons(){
        groundToggle.setSelected(false);
        solidToggle.setSelected(false);
        startNodeToggle.setSelected(false);
        endNodeToggle.setSelected(false);
        resourceToggle.setSelected(false);
        vasToggle.setSelected(false);
        storageToggle.setSelected(false);

        drawMode = DrawMode.NONE;
    }

    private class MapPane extends JPanel implements MouseListener, MouseMotionListener {

        private int offset;
        private int mouseX;
        private int mouseY;
        private boolean mouseInside;

        MapPane(){
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            offset = (PLANNED_SIZE - actualSize) / 2;
            paintWorld(g);
            paintPaths(g);
            paintStartEndResVasAndStorageNodes(g);
            paintGrid(g);
            if(isMouseInMap())
                paintSelectorRect(g);
        }

        private void paintWorld(Graphics g){
            for(int row = 0; row < world.getWorldWidth(); row++){
                for(int col = 0; col < world.getWorldWidth(); col++){
                    paintRectangle(g, world.getNodeTypeAt(row, col).getColor(), true, row, col);
                }
            }
        }

        private void paintPaths(Graphics g){
            for(ArrayList<Node> path : paths){
                for(Node node : path){
                    paintRectangle(g, NodeType.PATH.getColor(), true, node.getRow(), node.getCol());
                }
            }
        }

        private void paintStartEndResVasAndStorageNodes(Graphics g){
            if(startNode != null){
                paintRectangle(g, NodeType.START.getColor(), true, startNode.getRow(), startNode.getCol());
            }

            if(endNode != null){
                paintRectangle(g, NodeType.END.getColor(), true, endNode.getRow(), endNode.getCol());
            }

            for(Node node : resources){
                paintCircle(g, Color.YELLOW, node.getRow(), node.getCol());
            }

            for(Node node : storages){
                paintRectangle(g, Color.ORANGE, true, node.getRow(), node.getCol());
            }

            for(Node node : vassals){
                paintCircle(g, Color.PINK, node.getRow(), node.getCol());
            }
        }

        private void paintGrid(Graphics g){
            g.setColor(Color.BLACK);
            for(int i = 0; i < world.getWorldWidth() - 1; i++){
                g.drawLine((i+1) * tileSize + offset, offset, (i+1) * tileSize + offset, actualSize + offset);
                g.drawLine(offset, (i+1) * tileSize + offset, actualSize + offset, (i+1) * tileSize + offset);
            }
        }

        private void paintSelectorRect(Graphics g){
            int row = (mouseX - offset) / tileSize;
            int col = (mouseY - offset) / tileSize;
            if(drawMode == DrawMode.SOLID)
                paintRectangle(g, Color.BLUE, true, row, col);
            else if(drawMode == DrawMode.START)
                paintRectangle(g, Color.RED, true, row, col);
            else if(drawMode == DrawMode.END)
                paintRectangle(g, Color.BLACK, true, row, col);
            else if(drawMode == DrawMode.GROUND)
                paintRectangle(g, Color.GREEN, true, row, col);
            else if(drawMode == DrawMode.VASALL)
                paintCircle(g, Color.PINK, row, col);
            else if(drawMode == DrawMode.RESOURCE)
                paintCircle(g, Color.YELLOW, row, col);
            else if(drawMode == DrawMode.STORAGE)
                paintRectangle(g, Color.ORANGE, true, row, col);
            paintRectangle(g, Color.magenta, false, row, col);
        }

        private void paintRectangle(Graphics g, Color color, boolean filled, int row, int col){
            g.setColor(color);
            if(filled)
                g.fillRect(row * tileSize + offset, col * tileSize + offset, tileSize, tileSize);
            else
                g.drawRect(row * tileSize + offset, col * tileSize + offset, tileSize, tileSize);
        }

        private void paintCircle(Graphics g, Color color, int row, int col){
            g.setColor(color);
            g.fillOval(row * tileSize + offset, col * tileSize + offset, tileSize, tileSize);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(isMouseInMap()) draw();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            mouseInside = true;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            mouseInside = false;
            repaint();
        }

        private boolean isMouseInMap(){
            return (mouseInside && mouseX > offset && mouseY > offset && mouseX < PLANNED_SIZE - (offset + 1) && mouseY < PLANNED_SIZE - (offset + 1));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            if(isMouseInMap()) draw();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }

        public void draw(){
            int row = (mouseX - offset) / tileSize;
            int col = (mouseY - offset) / tileSize;

            if(drawMode == DrawMode.SOLID){
                if(startNode != null && row == startNode.getRow() && col == startNode.getCol()){
                    startNode = null;
                }
                else if(endNode != null && row == endNode.getRow() && col == endNode.getCol()){
                    endNode = null;
                }
                vassals.remove(new Node(row, col));
                resources.remove(new Node(row, col));
                storages.remove(new Node(row, col));
                world.setNodeTypeAt(NodeType.SOLID, row, col);
            }
            else if(drawMode == DrawMode.START){
                if(endNode != null && row == endNode.getRow() && col == endNode.getCol()){
                    endNode = null;
                }
                startNode = new Node(row, col);
                world.setNodeTypeAt(NodeType.GROUND, row, col);
            }
            else if(drawMode == DrawMode.END){
                if(startNode != null && row == startNode.getRow() && col == startNode.getCol()){
                    startNode = null;
                }
                endNode = new Node(row, col);
                world.setNodeTypeAt(NodeType.GROUND, row, col);
            }
            else if(drawMode == DrawMode.GROUND){
                if(startNode != null && row == startNode.getRow() && col == startNode.getCol()){
                    startNode = null;
                }
                else if(endNode != null && row == endNode.getRow() && col == endNode.getCol()){
                    endNode = null;
                }
                vassals.remove(new Node(row, col));
                resources.remove(new Node(row, col));
                storages.remove(new Node(row, col));
                world.setNodeTypeAt(NodeType.GROUND, row, col);
            }
            else if(drawMode == DrawMode.RESOURCE){
                vassals.remove(new Node(row, col));
                storages.remove(new Node(row, col));
                resources.add(new Node(row, col));
                world.setNodeTypeAt(NodeType.GROUND, row, col);
            }
            else if(drawMode == DrawMode.VASALL){
                resources.remove(new Node(row, col));
                vassals.add(new Node(row, col));
                world.setNodeTypeAt(NodeType.GROUND, row, col);
            }
            else if(drawMode == DrawMode.STORAGE){
                resources.remove(new Node(row, col));
                storages.add(new Node(row, col));
                world.setNodeTypeAt(NodeType.GROUND, row, col);
            }

            repaint();
        }
    }
}

