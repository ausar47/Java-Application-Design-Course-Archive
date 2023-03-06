package miniCAD.Model;

import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.io.Serializable;

public abstract class myShape implements Serializable {
    protected Color color;                                  // shape's color
    protected float thickness;                              // shape's thickness
    protected ArrayList<Point> points = new ArrayList<>();  // vertices to form the shape

    protected static final int delta = 15;                  // used for selected test
    @Serial
    private static final long serialVersionUID = 1L;        // used for Serializable

    public myShape() {}

    public myShape(Point p1, Point p2) {
        points.add(p1);
        points.add(p2);
        color = Color.black;    // default color
        thickness = 2.0f;       // default thickness
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public abstract void Draw(Graphics2D g);

    /**
     * If the point selected by the cursor lies within the shape, then the shape is selected
     * @param p point clicked by the cursor
     */
    public abstract boolean isSelected(Point p);

    protected Point getCentralPoint() {
        int sumX = 0, sumY = 0;
        for (Point p : points) {
            sumX += p.x;
            sumY += p.y;
        }
        return new Point(sumX / points.size(), sumY / points.size());
    }

    /**
     * adjust the two points so that the first one has both smaller x and y than the second one
     */
    protected Point[] adjustPoints() {
        Point[] res = new Point[2];
        Point p1 = points.get(0);
        Point p2 = points.get(1);
        if (p1.x > p2.x && p1.y > p2.y) {
            res[0] = p2;
            res[1] = p1;
        } else if (p1.x < p2.x && p1.y < p2.y) {
            res[0] = p1;
            res[1] = p2;
        } else if (p1.x > p2.x && p1.y < p2.y) {
            res[0] = new Point(p2.x, p1.y);
            res[1] = new Point(p1.x, p2.y);
        } else {
            res[0] = new Point(p1.x, p2.y);
            res[1] = new Point(p2.x, p1.y);
        }
        return res;
    }

    /**
     * zoom in/out the shape
     * @param flag true-zoom in, false-zoom out
    */
    public void zoom(boolean flag) {
        Point centralPoint = getCentralPoint();
        for (Point p : points) {
            double dx = p.getX() - centralPoint.getX();
            double dy = p.getY() - centralPoint.getY();
            if (flag) {
                dx *= 1.1;
                dy *= 1.1;
            } else {
                dx *= 0.9;
                dy *= 0.9;
            }
            p.setLocation(centralPoint.getX() + dx, centralPoint.getY() + dy);
        }
    }
}

