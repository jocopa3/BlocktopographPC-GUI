/*
 * WaypointRenderer.java
 *
 * Created on March 30, 2006, 5:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui.map;

import com.protolambda.blocktopograph.util.io.ImageUtil;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

/**
 * A fancy waypoint painter
 *
 * @author Martin Steiger
 */
public class MCWaypointRenderer implements WaypointRenderer<MyWaypoint> {

    private final Map<Color, BufferedImage> map = new HashMap<Color, BufferedImage>();

//	private final Font font = new Font("Lucida Sans", Font.BOLD, 10);
    private BufferedImage origImage;

    /**
     * Uses a default waypoint image
     */
    public MCWaypointRenderer() {
        try {
            //origImage = AbstractMarker.
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer viewer, MyWaypoint w) {
        g = (Graphics2D) g.create();

        if (origImage == null) {
            return;
        }

        BufferedImage myImg = map.get(w.getColor());

        if (myImg == null) {

        }

        Point2D point = viewer.getTileFactory().geoToPixel(w.getPosition(), viewer.getZoom());

        int x = (int) point.getX();
        int y = (int) point.getY();

        g.drawImage(myImg, x - myImg.getWidth() / 2, y - myImg.getHeight(), null);

        String label = w.getLabel();

//		g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int tw = metrics.stringWidth(label);
        int th = 1 + metrics.getAscent();

//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawString(label, x - tw / 2, y + th - myImg.getHeight());

        g.dispose();
    }
}
