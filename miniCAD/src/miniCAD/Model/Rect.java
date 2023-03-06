package miniCAD.Model;

import java.awt.*;
import java.io.Serial;

public class Rect extends myShape {
    @Serial
    private static final long serialVersionUID = 1L;

    public Rect(Point p1, Point p2) {
        super(p1, p2);
    }

    @Override
    public void Draw(Graphics2D g) {
        g.setStroke(new BasicStroke(thickness));    // set thickness
        g.setColor(color);                          // set color
        Point[] points = adjustPoints();
        g.drawRect(points[0].x, points[0].y, points[1].x - points[0].x, points[1].y - points[0].y);
    }

    /**
     * If the point selected by the cursor lies within the rectangle, then the rectangle is selected
     * @param p point clicked by the cursor
     */
    @Override
    public boolean isSelected(Point p) {
        Point[] points = adjustPoints();
        return p.x > points[0].x && p.y > points[0].y && p.x < points[1].x && p.y < points[1].y;
    }
}
