package miniCAD.Control;

import miniCAD.Model.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Control {
    public static Model model;
    public static paintListener pl = new paintListener();
    public static String state = "null";

    public Control(Model m) {
        model = m;
    }

    public static void updateView() {
        Model.updateView(); // decoupling
    }
}

class paintListener implements KeyListener, MouseListener, MouseMotionListener, ActionListener {
    private myShape selectedShape;
    private Point p1;
    private Point p2;
    private String text;
    private ArrayList<Point> initialPoints = new ArrayList<>(); // record points of the shape before moving it
    private static final String[] colorName = { "black", "darkGray", "gray", "blue", "cyan", "magenta", "green", "pink", "white", "red", "orange", "yellow" };
    private static final Color[] colors = { Color.black, Color.darkGray, Color.gray, Color.blue, Color.cyan, Color.magenta,
            Color.green, Color.pink, Color.white, Color.red, Color.orange, Color.yellow };

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (selectedShape != null) {
            if (e.getKeyChar() == '+' || e.getKeyChar() == '=') {
                selectedShape.zoom(true);                                               // zoom in
            } else if (e.getKeyChar() == '_' || e.getKeyChar() == '-') {
                selectedShape.zoom(false);                                              // zoom out
            } else if (e.getKeyChar() == ']') {
                selectedShape.setThickness(selectedShape.getThickness() + 1);               // thicken
            } else if (e.getKeyChar() == '[') {
                if (selectedShape.getThickness() > 1) {
                    selectedShape.setThickness(selectedShape.getThickness() - 1);           // thin
                }
            } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == KeyEvent.VK_DELETE) {
                Control.model.getAllShapes().remove(selectedShape);                         // delete
            } else if (e.getKeyChar() == KeyEvent.VK_CLEAR) {
                Control.model.getAllShapes().clear();                                       // clear
            } else if (e.getKeyChar() == KeyEvent.VK_INSERT || e.getKeyChar() == 'i' || e.getKeyChar() == 'a') {
                if (selectedShape instanceof Text tmp) {
                    tmp.setText(JOptionPane.showInputDialog("Input new text content: "));   // change the content of text
                }
            }
        }
        Control.updateView();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    private myShape findSelectedShape(Point p) {
        for (myShape shape : Control.model.getAllShapes()) {
            if (shape.isSelected(p)) {
                return shape;
            }
        }
        return null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        p1 = new Point(e.getX(), e.getY());
        p2 = new Point(e.getX(), e.getY());
        switch (Control.state) {
            case "line" -> {
                Control.model.addShape(new Line(p1, p2));
                selectedShape = null;
            }
            case "rectangle" -> {
                Control.model.addShape(new Rect(p1, p2));
                selectedShape = null;
            }
            case "circle" -> {
                Control.model.addShape(new Circle(p1, p2));
                selectedShape = null;
            }
            case "text" -> {
                Control.model.addShape(new Text(p1, p2, text));
                selectedShape = null;
            }
            case "select" -> {
                selectedShape = findSelectedShape(p1);  // find the selected shape
                if (selectedShape != null) {
                    initialPoints.clear();
                    for (int i = 0; i < selectedShape.getPoints().size(); i++) {
                        initialPoints.add(new Point(selectedShape.getPoints().get(i).x, selectedShape.getPoints().get(i).y));
                    }
                }
            }
            default -> selectedShape = null;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        if (Control.state.equals("null"))
            return;

        if (Control.state.equals("select")) {
            // select a shape to drag it
            if (selectedShape != null) {
                ArrayList<Point> tmp = new ArrayList<>();
                for (int i = 0; i < selectedShape.getPoints().size(); i++) {
                    tmp.add(new Point(initialPoints.get(i).x + e.getX() - p1.x, initialPoints.get(i).y + e.getY() - p1.y));
                }
                selectedShape.setPoints(tmp);
                Control.updateView();
            }
        } else {
            // alter p2 of the shape, we can only create a shape after a mousePressed and a mouseDragged
            myShape s = Control.model.getCurrentShape();
            s.getPoints().get(1).x = e.getX();
            s.getPoints().get(1).y = e.getY();
            Control.updateView();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    private void saveFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(null);
        chooser.setDialogTitle("Save File");
        File file = chooser.getSelectedFile();

        if (file != null) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                out.writeObject(Control.model.getAllShapes());
                JOptionPane.showMessageDialog(null, "Saved Successfully!");
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Save Failed!");
            }
        }
    }

    private void newFile() {
        int value = JOptionPane.showConfirmDialog(null, "Save current file first?", null, 0);
        Control.state = "null";
        if (value == 0) {
            saveFile();
        }
        Control.model.removeAllShapes();
        Control.updateView();
    }

    @SuppressWarnings("unchecked")
    private void openFile() {
        int value = JOptionPane.showConfirmDialog(null, "Save current file first?", null, 0);
        if (value == 0) {
            saveFile();
        }
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Open File");
            chooser.showOpenDialog(null);
            File file = chooser.getSelectedFile();
            if (file != null) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                Control.model.setAllShapes((ArrayList<myShape>) in.readObject());
                Control.updateView();
                in.close();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, "Open Failed!");
        }
    }

    private void showManual() {
        String manual = "Draw graphics:\n  You can draw line, rectangle, circle and text.\n  Hold the left mouse button and drag it to draw a shape.\n\nDelete a graphic:\n  Select a graphic, press DELETE or BACKSPACE to delete it.\n\nChange a graphic's thickness:\n  Select a graphic and press ']' to thicken it, press '[' to thin it.\n\nZoom a graphic:\n  Select a graphic and press '+' to zoom in, press '-' to zoom out.\n\nChange a graphic's color:\n  Select a graphic and choose a color button on the toolbar on your right.";
        JOptionPane.showMessageDialog(null, manual, "Manual", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String btnName = e.getActionCommand();
        switch (btnName) {
            case "Select" -> Control.state = "select";
            case "Line" -> Control.state = "line";
            case "Rectangle" -> Control.state = "rectangle";
            case "Circle" -> Control.state = "circle";
            case "Text" -> {
                Control.state = "text";
                text = JOptionPane.showInputDialog("Input text content: ");
            }
            case "black", "darkGray", "gray", "blue", "cyan", "magenta", "green", "pink", "white", "red", "orange", "yellow" -> { // colors
                if (selectedShape != null) {
                    int i = 0;
                    for (i = 0; i < colorName.length; i++) {
                        if (btnName.equals(colorName[i]))
                            break;
                    }
                    selectedShape.setColor(colors[i]);
                }
                Control.updateView();
            }
            case "New..." -> newFile();
            case "Open..." -> openFile();
            case "Save..." -> saveFile();
            case "Close" -> System.exit(0);
            case "Manual" -> showManual();
            default -> Control.state = "null";
        }
    }
}