package miniCAD.Model;

import java.awt.*;
import java.io.Serial;

public class Line extends myShape {
    @Serial
    private static final long serialVersionUID = 1L;

    public Line(Point p1, Point p2) {
        super(p1, p2);
    }

    @Override
    public void Draw(Graphics2D g) {
        g.setStroke(new BasicStroke(thickness));    // set thickness
        g.setColor(color);                          // set color
        g.drawLine(points.get(0).x, points.get(0).y, points.get(1).x, points.get(1).y); // draw a line connecting (x1, y1) and (x2, y2)
    }

    /**
     * If the point selected by the cursor lies within the line segment, then the line segment is selected
     *
     * @param p point clicked by the cursor
     */
    @Override
    public boolean isSelected(Point p) {
        Point[] points = adjustPoints();
        return p.x > points[0].x && p.y > points[0].y && p.x < points[1].x && p.y < points[1].y && getPointDist(p) <= delta;
    }

    double getPointDist(Point p) {
        Point p1 = points.get(0), p2 = points.get(1);
        // vertical
        if (p1.x == p2.x)
            return Math.abs(p.x - p1.x);
        // horizontal
        if (p1.y == p2.y)
            return Math.abs(p.y - p1.y);

        double k = (p2.y - p1.y) * 1.0 / (p2.x - p1.x);
        double b = p2.y - k * p2.x;
        return Math.abs((k * p.x - p.y + b) / (Math.sqrt(k * k + 1)));
    }
}
