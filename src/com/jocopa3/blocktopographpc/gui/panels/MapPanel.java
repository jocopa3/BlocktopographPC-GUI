/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui.panels;

import com.jocopa3.blocktopographpc.gui.CleanableComponent;
import com.jocopa3.blocktopographpc.gui.dialogs.ChunkNBTDialog;
import com.jocopa3.blocktopographpc.gui.map.MCTileProvider;
import com.jocopa3.blocktopographpc.gui.map.MCTileProviderInfo;
import com.jocopa3.blocktopographpc.gui.windows.WorldWindow;
import com.jocopa3.blocktopographpc.util.WordUtils;
import com.protolambda.blocktopograph.map.Map;
import com.protolambda.blocktopograph.map.renderer.MapType;
import com.protolambda.blocktopograph.util.io.ImageUtil;
import com.protolambda.blocktopograph.util.math.DimensionVector3;
import com.protolambda.blocktopograph.world.World;
import com.protolambda.blocktopograph.world.WorldProvider;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle;
import javax.swing.Timer;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.TileFactory;

/**
 *
 * @author Matt
 */
public class MapPanel extends javax.swing.JPanel implements CleanableComponent {

    private WorldWindow parent;
    private World world;
    private WorldProvider provider;
    private int comboLastIndex = 0;
    private TileFactory factory;
    PanMouseInputListener mia;

    public void setWorld(File worldFile) {
        try {
            world = new World(worldFile);
            world.getWorldData().load();
        } catch (Exception e) {
            System.exit(-1);
        }

        provider = new WorldProvider(world);
    }

    public void setup() {
        //provider = new WorldProvider(world);

        //SeedLabel.setText("Seed: " + world.getWorldSeed());
        //world.logDBKeys();
        world.logDBTables();
        factory = new MCTileProvider(new MCTileProviderInfo(provider));

        // Setup JXMapViewer
        mapViewer.setTileFactory(factory);

        // Set the focus
        mapViewer.setZoom(3);
        DimensionVector3<Integer> spawn;
        try {
            spawn = Map.getSpawnPos(provider);
            //mapViewer.setCenter(new Point2D.Float(spawn.x, spawn.z));
            System.out.println("SpawnX: " + spawn.x + " SpawnZ: " + spawn.z);
        } catch (Exception e) {

        }

        DimensionVector3<Float> playerPos;
        try {
            playerPos = Map.getPlayerPos(provider);
            mapViewer.setCenter(new Point2D.Float(playerPos.x, playerPos.z));
            System.out.println("SpawnX: " + playerPos.x + " SpawnZ: " + playerPos.z);
        } catch (Exception e) {

        }

        mapViewer.setLoadingImage(ImageUtil.readImage("program_icons/icon128.png"));
        mapViewer.setRestrictOutsidePanning(false);

        // Add interactions
        mia = new PanMouseInputListener(mapViewer);
        
        mapViewer.addMouseListener(new PopupTriggerListener());
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.setDrawTileBorders(true);

        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        // Initialize combo box using map renderer enum names
        
        MapType[] values = MapType.values();
        ArrayList<String> tfLabels = new ArrayList<>(values.length);
        for (int i = 3; i < values.length; i++) {
            tfLabels.add(WordUtils.capitalizeString(values[i].name()));
        }

        combo.setModel(new DefaultComboBoxModel<String>(tfLabels.toArray(new String[0])));

        comboLastIndex = tfLabels.indexOf(WordUtils.capitalizeString(provider.getMapType().name()));
        combo.setSelectedIndex(comboLastIndex);

        combo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() != ItemEvent.SELECTED)
                    return;
                
                provider.changeMapType(values[combo.getSelectedIndex()+3]);
                ((MCTileProvider) factory).clearCache(true);
                ((MCTileProvider) factory).clearQueue();
                ((MCTileProvider) factory).stopAllThreads();

                // Dimension is being changed; clear chunk cache from previous dimension
                if (!values[comboLastIndex].dimension.equals(values[combo.getSelectedIndex()+3].dimension)) {
                    provider.getChunkManager(values[comboLastIndex].dimension).disposeAll();
                }

                comboLastIndex = combo.getSelectedIndex();
                
                ((MapPanel)((JComboBox)e.getSource()).getParent()).setName(tfLabels.get(combo.getSelectedIndex()));
            }
        });
    }

    /**
     * Creates new form MapPanel
     */
    public MapPanel(WorldWindow parent) {
        initComponents();
        this.parent = parent;
        this.world = parent.getWorld();
        this.provider = parent.getWorldProvider();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setup();
            }
        });
        
        setName("Map");

        //TileFactoryInfo osmInfo = new OSMTileFactoryInfo();
        //TileFactoryInfo veInfo = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
//		factories.add(new EmptyTileFactory());
        //factories.add(new DefaultTileFactory(osmInfo));
        //factories.add(new DefaultTileFactory(veInfo));
        Timer t = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Set<Thread> threads = Thread.getAllStackTraces().keySet();
                labelThreadCount.setText("Threads: " + threads.size());
                Point2D pixelCoordinates = mia.getMouseCoords();
                if(pixelCoordinates == null)
                    return;
                
                Point2D temp = new Point2D.Double(pixelCoordinates.getX(), pixelCoordinates.getY());
                
                //GeoPosition g = mapViewer.getTileFactory().pixelToGeo(pixelCoordinates, mapViewer.getZoom());
                //GeoPosition gg = new GeoPosition(g.getLatitude() + mapViewer.getCenterPosition().getLatitude(), g.getLongitude() + mapViewer.getCenterPosition().getLongitude());
                //Point2D xy = mapViewer.getTileFactory().geoToPixel(gg, mapViewer.getZoom());
                Point2D center = mapViewer.getCenter();
                int zoom = mapViewer.getZoom();
                int scale = (zoom >> 1) + 1;
                temp.setLocation(temp.getX() + center.getX()*scale, temp.getY() + center.getY() * scale);
                
                xLabel.setText(""+(temp.getX()));
                yLabel.setText(""+(temp.getY()));
            }
        });

        t.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mapPopupMenu = new JPopupMenu();
        openEntityNbtMenuItem = new JMenuItem();
        openTileEntityNbtMenuItem = new JMenuItem();
        mapViewer = new JXMapViewer();
        jLabel1 = new JLabel();
        jSeparator1 = new JSeparator();
        combo = new JComboBox<>();
        labelThreadCount = new JLabel();
        xLabel = new JLabel();
        yLabel = new JLabel();

        openEntityNbtMenuItem.setText("Edit Entities");
        openEntityNbtMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                openEntityNbtMenuItemActionPerformed(evt);
            }
        });
        mapPopupMenu.add(openEntityNbtMenuItem);

        openTileEntityNbtMenuItem.setText("Edit Tile Entities");
        openTileEntityNbtMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                openTileEntityNbtMenuItemActionPerformed(evt);
            }
        });
        mapPopupMenu.add(openTileEntityNbtMenuItem);

        GroupLayout mapViewerLayout = new GroupLayout(mapViewer);
        mapViewer.setLayout(mapViewerLayout);
        mapViewerLayout.setHorizontalGroup(mapViewerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        mapViewerLayout.setVerticalGroup(mapViewerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 516, Short.MAX_VALUE)
        );

        jLabel1.setText("Map Type:");

        labelThreadCount.setText("Threads:");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelThreadCount)
                .addGap(18, 18, 18)
                .addComponent(xLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combo, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jSeparator1)
            .addComponent(mapViewer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(combo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelThreadCount)
                    .addComponent(xLabel)
                    .addComponent(yLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mapViewer, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void openEntityNbtMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_openEntityNbtMenuItemActionPerformed
        ChunkNBTDialog dialog = new ChunkNBTDialog(parent, true, true);
        dialog.setVisible(true);
    }//GEN-LAST:event_openEntityNbtMenuItemActionPerformed

    private void openTileEntityNbtMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_openTileEntityNbtMenuItemActionPerformed
        ChunkNBTDialog dialog = new ChunkNBTDialog(parent, false, true);
        dialog.setVisible(true);
    }//GEN-LAST:event_openTileEntityNbtMenuItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JComboBox<String> combo;
    private JLabel jLabel1;
    private JSeparator jSeparator1;
    private JLabel labelThreadCount;
    private JPopupMenu mapPopupMenu;
    private JXMapViewer mapViewer;
    private JMenuItem openEntityNbtMenuItem;
    private JMenuItem openTileEntityNbtMenuItem;
    private JLabel xLabel;
    private JLabel yLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean cleanUp() {
        ((MCTileProvider)factory).dispose();
        ((MCTileProvider)factory).stopAllThreads(); // This is critical
        this.provider.clean();
        return true;
    }
    
    class PopupTriggerListener extends MouseAdapter {

        public void mousePressed(MouseEvent ev) {
            if (ev.isPopupTrigger()) {
                mapPopupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
            }
        }

        public void mouseReleased(MouseEvent ev) {
            if (ev.isPopupTrigger()) {
                System.out.println(mapViewer.getCenterPosition());
                mapPopupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
            }
        }

        public void mouseClicked(MouseEvent ev) {
        }
    }
}
