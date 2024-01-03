package org.car.routing.gui.waypoint;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ButtonWaypoint extends JButton {

    public ButtonWaypoint() {
        setContentAreaFilled(false);
        URL icon = Thread.currentThread().getContextClassLoader().getResource("icon/pin.png");
        setIcon(new ImageIcon(icon));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setSize(new Dimension(24, 24));
    }
}
