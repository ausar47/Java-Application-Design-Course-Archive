package miniCAD.Model;

import java.awt.*;
import java.io.Serial;

public class Circle extends myShape {
    @Serial
    private static final long serialVersionUID = 1L;

    public Circle(Point p1, Point p2) {
        super(p1, p2);
    }

    @Override
    public void Draw(Graphics2D g) {
        g.setStroke(new BasicStroke(thickness));    // set thickness
        g.setColor(color);                          // set color
        Point[] points = adjustPoints();
        g.drawOval(points[0].x, points[0].y, points[1].x - points[0].x, points[1].x - points[0].x);
    }

    /**
     * If the point selected by the cursor lies within the circle, then the circle is selected
     * @param p point clicked by the cursor
     */
    @Override
    public boolean isSelected(Point p) {
        Point[] points = adjustPoints();
        double centralX = (points[0].getX() + points[1].getX()) / 2;
        double centralY = (points[0].getY() + points[1].getY()) / 2;
        return Math.sqrt(Math.pow(p.x - centralX, 2) + Math.pow(p.y - centralY, 2))
                - Math.sqrt(Math.pow(points[0].x - centralX, 2) + Math.pow(points[0].y - centralY, 2)) <= delta;
    }
}
