/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui.panels;

import com.jocopa3.blocktopographpc.gui.CleanableComponent;
import com.jocopa3.blocktopographpc.gui.Main;
import com.jocopa3.blocktopographpc.util.WorldListUtil;
import com.jocopa3.blocktopographpc.gui.windows.WorldWindow;
import com.jocopa3.blocktopographpc.options.OptionEnum;
import com.jocopa3.blocktopographpc.util.io.FileUtils;
import static com.jocopa3.blocktopographpc.util.io.FileUtils.zipDirectory;
import com.protolambda.blocktopograph.Log;
import com.protolambda.blocktopograph.util.io.IOUtil;
import com.protolambda.blocktopograph.util.io.ImageUtil;
import com.protolambda.blocktopograph.world.World;
import com.protolambda.blocktopograph.world.WorldData;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Matt
 */
public class WorldListPanel extends javax.swing.JPanel implements CleanableComponent {

    private List<World> mValues;
    private List<String> worldNames;

    private File savesFolder;

    //returns true if it has loaded a new list of worlds, false otherwise
    public boolean reloadWorldList() {
        String path = Main.Options.get(OptionEnum.WORLD_FOLDER.getKeyName());
        
        //String path = WorldListUtil.getMinecraftFolderLocation();
        Log.d("minecraftWorlds path: " + path);

        savesFolder = new File(path);
        
        if (mValues != null && mValues.size() > 0) {
            for (World w : mValues) {
                try {
                    w.closeDown();
                } catch (Exception e) {

                }
            }
            mValues.clear();
        }

        worldNames.clear();

        File[] saves = savesFolder.exists() ? savesFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return new File(pathname + "/level.dat").exists();
            }
        }) : null;

        if (saves != null) {
            Log.d("Number of minecraft worlds: " + saves.length);

            for (File save : saves) {
                //debug if we see all worlds
                Log.d("FileName: " + save.getName());

                try {
                    mValues.add(new World(save));
                } catch (World.WorldLoadException e) {
                    e.printStackTrace();
                    Log.d("Skipping world while reloading world list: "
                            + save.getName() + ", loading failed somehow, check stack trace");
                }
            }
        }

        Collections.sort(mValues, new Comparator<World>() {
            @Override
            public int compare(World a, World b) {
                try {
                    long tA = WorldListUtil.getLastPlayedTimestamp(a);
                    long tB = WorldListUtil.getLastPlayedTimestamp(b);
                    return tA > tB ? -1 : (tA == tB ? 0 : 1);
                } catch (Exception e) {
                    return 0;
                }
            }
        });

        if (mValues != null) {
            for (World w : mValues) {
                worldNames.add(w.getWorldDisplayName());
            }

            worldList.setModel(new javax.swing.AbstractListModel<World>() {
                @Override
                public int getSize() {
                    if (mValues == null) {
                        return 0;
                    }

                    return mValues.size();
                }

                @Override
                public World getElementAt(int i) {
                    if (mValues == null) {
                        return null;
                    }

                    return mValues.get(i);
                }
            });

            worldList.setCellRenderer(new WorldListCellRenderer());
        }
        //load data into view
        //this.notifyDataSetChanged();

        return mValues.size() > 0;
    }

    /**
     * Creates new form WorldListPanel
     */
    public WorldListPanel() {
        initComponents();
        mValues = new ArrayList<>();
        worldNames = new ArrayList<>();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        worldListScrollPane = new javax.swing.JScrollPane();
        worldList = new javax.swing.JList<>();
        worldLabel = new javax.swing.JLabel();
        buttonGridPanel = new javax.swing.JPanel();
        worldOpenButton = new javax.swing.JButton();
        refreshListButton = new javax.swing.JButton();
        backupWorldButton = new javax.swing.JButton();
        deleteWorldButton = new javax.swing.JButton();

        worldList.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        worldListScrollPane.setViewportView(worldList);

        worldLabel.setFont(worldLabel.getFont().deriveFont(worldLabel.getFont().getStyle() | java.awt.Font.BOLD, worldLabel.getFont().getSize()+2));
        worldLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        worldLabel.setText("Worlds");

        buttonGridPanel.setLayout(new java.awt.GridLayout(2, 2));

        worldOpenButton.setBackground(new java.awt.Color(51, 153, 255));
        worldOpenButton.setText("Open World");
        worldOpenButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(51, 204, 255), new java.awt.Color(0, 153, 255), new java.awt.Color(0, 153, 204), new java.awt.Color(0, 153, 255)));
        worldOpenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                worldOpenButtonActionPerformed(evt);
            }
        });
        buttonGridPanel.add(worldOpenButton);

        refreshListButton.setBackground(new java.awt.Color(255, 255, 102));
        refreshListButton.setText("Refresh List");
        refreshListButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 255, 153), new java.awt.Color(255, 255, 102), new java.awt.Color(204, 204, 0), new java.awt.Color(255, 255, 102)));
        refreshListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshListButtonActionPerformed(evt);
            }
        });
        buttonGridPanel.add(refreshListButton);

        backupWorldButton.setBackground(new java.awt.Color(102, 255, 102));
        backupWorldButton.setText("Backup");
        backupWorldButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(153, 255, 153), new java.awt.Color(102, 255, 102), new java.awt.Color(0, 204, 0), new java.awt.Color(102, 255, 102)));
        backupWorldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backupWorldButtonActionPerformed(evt);
            }
        });
        buttonGridPanel.add(backupWorldButton);

        deleteWorldButton.setBackground(new java.awt.Color(255, 51, 51));
        deleteWorldButton.setText("Delete");
        deleteWorldButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(255, 102, 102), new java.awt.Color(255, 51, 51), new java.awt.Color(204, 0, 0), new java.awt.Color(255, 51, 51)));
        deleteWorldButton.setEnabled(false);
        deleteWorldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteWorldButtonActionPerformed(evt);
            }
        });
        buttonGridPanel.add(deleteWorldButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(worldListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(worldLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(buttonGridPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(worldLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(worldListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonGridPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void worldOpenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_worldOpenButtonActionPerformed
        if (worldList.getSelectedIndex() < 0) {
            return;
        }
        
        WorldWindow mw = new WorldWindow(mValues.get(worldList.getSelectedIndex()));

        mw.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mw.setTitle("World: " + mw.getWorld().getWorldDisplayName());

        if (!mw.loadWorld()) {
            return;
        }
        
        System.out.println(mw.getWorld() +" "+mw.getWorldProvider());

        mw.addTab(
                new MapPanel(mw.getWorld(), mw.getWorldProvider()), 
                new ImageIcon(ImageUtil.readImage("world_icon.png")));

        mw.pack();
        mw.setLocationRelativeTo(null);

        mw.setVisible(true);

        Main.OpenWindows.put(mw.keyName, mw);
        //WorldSelectWindow.mainWindow.setVisible(false);
    }//GEN-LAST:event_worldOpenButtonActionPerformed

    private void refreshListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshListButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refreshListButtonActionPerformed

    private void backupWorldButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backupWorldButtonActionPerformed
        World world = worldList.getSelectedValue();
        
        try {
            File backupFolder = new File(Main.Options.get(OptionEnum.BACKUP_FOLDER.getKeyName()));
            if(!backupFolder.exists()) {
                backupFolder.mkdir();
            }
            
            FileUtils.zipDirectory(world.worldFolder.toString(), backupFolder.toPath().resolve(world.worldFolder.getName() + ".zip").toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_backupWorldButtonActionPerformed

    private void deleteWorldButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteWorldButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteWorldButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backupWorldButton;
    private javax.swing.JPanel buttonGridPanel;
    private javax.swing.JButton deleteWorldButton;
    private javax.swing.JButton refreshListButton;
    private javax.swing.JLabel worldLabel;
    private javax.swing.JList<World> worldList;
    private javax.swing.JScrollPane worldListScrollPane;
    private javax.swing.JButton worldOpenButton;
    // End of variables declaration//GEN-END:variables

    private static class WorldListCellRenderer extends JPanel implements ListCellRenderer<World> {

        private JLabel worldName = new JLabel(" ");
        private JLabel worldSize = new JLabel(" ");
        private JLabel gamemode = new JLabel(" ");
        private JLabel lastPlayed = new JLabel(" ");
        private int fontSize = worldName.getFont().getSize() + 1;

        private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
        private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
        protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

        //protected Border noFocusBorder = new EmptyBorder(15, 1, 1, 1);
        protected TitledBorder focusBorder = new TitledBorder(LineBorder.createGrayLineBorder(), "title");

        protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        protected Border getNoFocusBorder() {
            Border border = UIManager.getBorder("List.cellNoFocusBorder");
            if (System.getSecurityManager() != null) {
                if (border != null) {
                    return border;
                }
                return SAFE_NO_FOCUS_BORDER;
            } else {
                if (border != null
                        && (noFocusBorder == null
                        || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
                    return border;
                }
                return noFocusBorder;
            }
        }

        @Override
        public Component getListCellRendererComponent(JList list, World world, int index, boolean isSelected, boolean cellHasFocus) {
            //setBackground(new Color(255, 255, 255));
            Color bg = null;
            Color fg = null;

            JList.DropLocation dropLocation = list.getDropLocation();
            if (dropLocation != null
                    && !dropLocation.isInsert()
                    && dropLocation.getIndex() == index) {

                bg = UIManager.getColor("List.dropCellBackground");
                fg = UIManager.getColor("List.dropCellForeground");

                isSelected = true;
            }

            if (isSelected) {
                setBackground(bg == null ? new Color(244, 244, 244) : bg);
                //setForeground(fg == null ? list.getSelectionForeground() : fg);
            } else {
                setBackground(new Color(255, 255, 255));
                setForeground(list.getForeground());
            }

            worldName.setFont(list.getFont().deriveFont(worldName.getFont().getStyle() | Font.BOLD, fontSize));
            worldName.setText(world.getWorldDisplayName());

            worldSize.setForeground(new Color(51, 51, 51));
            worldSize.setText(IOUtil.getFileSizeInText(world.worldFolder));

            gamemode.setForeground(new Color(51, 51, 51));
            gamemode.setText(WorldListUtil.getWorldGamemodeText(world));

            lastPlayed.setForeground(new Color(51, 51, 51));
            lastPlayed.setText(WorldListUtil.getLastPlayedText(world));

            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(worldName)
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(worldSize)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(gamemode)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lastPlayed))
            );
            layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(worldName)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(worldSize)
                                    .addComponent(gamemode)
                                    .addComponent(lastPlayed))
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            Border border = null;
            if (cellHasFocus) {
                if (isSelected) {
                    border = BorderFactory.createEtchedBorder(EtchedBorder.RAISED, new Color(204, 204, 204), new Color(153, 153, 153));
                    //border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
                }
                if (border == null) {
                    border = BorderFactory.createEmptyBorder(2, 2, 2, 2);
                    //border = UIManager.getBorder("List.focusCellHighlightBorder");
                }
            } else {
                border = BorderFactory.createEmptyBorder(2, 2, 2, 2);
                //border = getNoFocusBorder();
            }
            setBorder(border);

            return this;
        }
    }

    @Override
    public boolean cleanUp() {
        for (World w : mValues) {
            try {
                w.closeDown();
            } catch (WorldData.WorldDBException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        return true;
    }
}
