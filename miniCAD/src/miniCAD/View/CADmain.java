package miniCAD.View;

import javax.swing.*;
import java.awt.*;
import miniCAD.Control.Control;
import miniCAD.Model.*;
import java.net.URL;
import java.util.ArrayList;

public class CADmain extends JFrame {
    private static final String[] shape = { "Select", "Line", "Rectangle", "Circle", "Text" };
    private static final String[] colorName = { "black", "darkGray", "gray", "blue", "cyan", "magenta", "green", "pink",
            "white", "red", "orange", "yellow" };
    private static final Color[] colors = { Color.black, Color.darkGray, Color.gray, Color.blue, Color.cyan,
            Color.magenta,
            Color.green, Color.pink, Color.white, Color.red, Color.orange, Color.yellow };

    public CADmain() {
        setTitle("MiniCAD");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        // Menu
        JMenuBar menuBar = new JMenuBar();

        // File
        JMenu menu1 = new JMenu("File");
        JMenuItem menu1Item1 = new JMenuItem("New...");
        JMenuItem menu1Item2 = new JMenuItem("Open...");
        JMenuItem menu1Item3 = new JMenuItem("Save...");
        JMenuItem menu1Item4 = new JMenuItem("Close");
        menu1Item1.addActionListener(Control.pl);
        menu1Item2.addActionListener(Control.pl);
        menu1Item3.addActionListener(Control.pl);
        menu1Item4.addActionListener(Control.pl);
        menu1.add(menu1Item1);
        menu1.add(menu1Item2);
        menu1.add(menu1Item3);
        menu1.add(menu1Item4);
        menuBar.add(menu1);

        // Help
        JMenu menu2 = new JMenu("Help");
        JMenuItem menu2Item = new JMenuItem("Manual");
        menu2Item.addActionListener(Control.pl);
        menu2.add(menu2Item);
        menuBar.add(menu2);

        // Paint Choice
        JPanel choicePanel = new JPanel();
        choicePanel.setBackground(Color.black);
        choicePanel.setLayout(new GridLayout(6, 1));
        for (int i = 0; i < 5; i++) {
            JButton button = new JButton();
            URL url = this.getClass().getResource("/Images/" + shape[i] + ".png");
            assert url != null;
            ImageIcon icon = new ImageIcon(url);
            icon.setImage(icon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
            button.setIcon(icon);
            button.setActionCommand(shape[i]);
            button.setBackground(Color.white);
            button.setPreferredSize(new Dimension(20, 80));
            button.setBorder(BorderFactory.createRaisedBevelBorder());
            button.addActionListener(Control.pl);
            button.addKeyListener(Control.pl);
            choicePanel.add(button);
        }

        // Paint Colors
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(Color.black);
        colorPanel.setLayout(new GridLayout(4, 3));
        for (int i = 0; i < 12; i++) {
            JButton button = new JButton();
            button.setActionCommand(colorName[i]);
            button.addActionListener(Control.pl);
            button.addKeyListener(Control.pl);
            button.setBackground(colors[i]);
            colorPanel.add(button);
        }
        choicePanel.add(colorPanel);

        setJMenuBar(menuBar); // top bar
        add(choicePanel, BorderLayout.EAST); // choice menu
        this.setFocusable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        CADmain window = new CADmain();
        ArrayList<myShape> shapes = new ArrayList<>();
        View view = new View();
        Model model = new Model(shapes, view);
        window.add(view, BorderLayout.CENTER);
        window.setVisible(true);
        @SuppressWarnings("unused")
        Control ctrl = new Control(model);
    }
}
