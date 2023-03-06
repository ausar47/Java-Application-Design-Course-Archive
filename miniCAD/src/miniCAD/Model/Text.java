package miniCAD.Model;

import java.awt.*;
import java.io.Serial;

public class Text extends myShape {
    @Serial
    private static final long serialVersionUID = 1L;

    private String str;

    public Text(Point p1, Point p2, String s) {
        super(p1, p2);
        this.str = s;
    }

    @Override
    public void Draw(Graphics2D g) {
        g.setColor(color);
        Point[] points = adjustPoints();
        g.setFont(new Font("宋体", Font.ITALIC, (points[1].y - points[0].y) / 2));
        g.drawString(str, points[0].x, points[1].y);
    }

    /**
     * If the point selected by the cursor lies within the text, then the text is selected
     * @param p point clicked by the cursor
     */
    @Override
    public boolean isSelected(Point p) {
        Point[] points = adjustPoints();
        return p.x > points[0].x && p.y > points[0].y && p.x < points[1].x && p.y < points[1].y;
    }

    public void setText(String s) {
        this.str = s;
    }
}
