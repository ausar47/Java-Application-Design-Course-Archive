package miniCAD.View;

import miniCAD.Control.Control;
import miniCAD.Model.myShape;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

// canvas, will be placed in the Center of JFrame
public class View extends JPanel {
    private ArrayList<myShape> shapes = new ArrayList<>();

    public View() {
        setBackground(Color.WHITE);
        addMouseListener(Control.pl);
        addMouseMotionListener(Control.pl);
        addKeyListener(Control.pl);
    }

    public void paintAllShapes(ArrayList<myShape> s) {
        shapes = s;
        repaint();
    }

    // repaint calls paint
    public void paint(Graphics g) {
        super.paint(g);
        if (!shapes.isEmpty()) {
            for (myShape shape : shapes) {
                shape.Draw((Graphics2D) g);
            }
        }
    }
}
