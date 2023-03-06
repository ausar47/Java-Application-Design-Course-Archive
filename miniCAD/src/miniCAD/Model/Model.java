package miniCAD.Model;

import miniCAD.View.View;
import java.util.ArrayList;

public class Model {
    public static View view;
    private static ArrayList<myShape> shapes;

    public Model() {}

    public Model(ArrayList<myShape> s, View v) {
        view = v;
        shapes = s;
    }

    public ArrayList<myShape> getAllShapes() {
        return shapes;
    }

    public myShape getCurrentShape() {
        return shapes.get(shapes.size() - 1);
    }

    public void addShape(myShape s) {
        shapes.add(s);
    }

    public void setAllShapes(ArrayList<myShape> s) {
        shapes = s;
    }

    public void removeAllShapes() {
        shapes.clear();
    }

    public static void updateView() {
        view.paintAllShapes(shapes);
    }
}