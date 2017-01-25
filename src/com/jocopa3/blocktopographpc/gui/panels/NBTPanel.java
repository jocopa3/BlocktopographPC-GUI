/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui.panels;

import com.jocopa3.blocktopographpc.gui.CleanableComponent;
import com.jocopa3.blocktopographpc.gui.MaterialIcon;
import com.jocopa3.blocktopographpc.gui.nbt.NBTCell;
import com.jocopa3.blocktopographpc.gui.nbt.NBTIcon;
import com.jocopa3.blocktopographpc.gui.nbt.NBTNode;
import com.jocopa3.blocktopographpc.gui.windows.WorldWindow;
import com.jocopa3.blocktopographpc.util.MessageImportance;
import com.jocopa3.blocktopographpc.util.PlatformUtils;
import com.jocopa3.blocktopographpc.util.WordUtils;
import com.jocopa3.blocktopographpc.util.nbtparser.NBTSignature;
import com.protolambda.blocktopograph.nbt.EditableNBT;
import com.protolambda.blocktopograph.nbt.convert.NBTConstants;
import com.protolambda.blocktopograph.nbt.convert.NBTConstants.NBTType;
import com.protolambda.blocktopograph.nbt.tags.CompoundTag;
import com.protolambda.blocktopograph.nbt.tags.ListTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.LinkedList;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Matt
 */
public class NBTPanel extends javax.swing.JPanel implements CleanableComponent {

    private final WorldWindow parentWindow;

    private EditableNBT nbtData;

    private static ArrayList<NBTNode> clipboard = new ArrayList<>(); // Static to allow nodes to be copied from one window to another

    private void iterateTags(NBTNode rootNode) {
        LinkedList<NBTNode> stack = new LinkedList<>();
        stack.add(rootNode);

        while (!stack.isEmpty()) {
            NBTNode current = stack.remove();
            switch (current.getTag().getType()) {
                case COMPOUND:
                    for (Tag child : ((CompoundTag) current.getTag()).getValue()) {
                        NBTNode childNode = new NBTNode(child, current.getTag());
                        stack.add(childNode);
                        current.add(childNode);
                    }
                    break;
                case LIST:
                    for (Tag child : ((ListTag) current.getTag()).getValue()) {
                        NBTNode childNode = new NBTNode(child, current.getTag());
                        stack.add(childNode);
                        current.add(childNode);
                    }
                    break;
            }
        }
    }

    private void setTreeModel() {
        NBTNode rootNode = new NBTNode(new CompoundTag("Root", new ArrayList<>()), null);

        for (Tag tag : nbtData.getTags()) {
            NBTNode child = new NBTNode(tag, rootNode.getTag());
            switch (tag.getType()) {
                case LIST:
                case COMPOUND:
                    iterateTags(child);
                default:
                    rootNode.add(child);
            }
        }

        DefaultTreeModel nbtTreeModel = new DefaultTreeModel(rootNode);
        nbtTreeModel.addTreeModelListener(new NBTTreeModelListener(nbtTreeModel));

        nbtTree.setModel(nbtTreeModel);
        nbtTree.setCellRenderer(new NBTTreeCellRenderer());
        nbtTree.addKeyListener(new NBTTreeKeyListener());
        nbtTree.addMouseListener(new PopupTriggerListener());
        //jTree1.setCellEditor(new DefaultTreeCellEditor(jTree1, new NBTTreeCellRenderer(), new DefaultCellEditor(new NBTCell())));
        nbtTree.setEditable(true);
    }

    /**
     * Creates new form NBTPanel
     */
    public NBTPanel(WorldWindow parentWindow, EditableNBT nbtData) {
        initComponents();

        this.parentWindow = parentWindow;
        this.nbtData = nbtData;
        setName(nbtData.getRootTitle());
        setTreeModel();
    }

    private boolean buttonsInited = false;

    @Override
    public void paint(Graphics g) {
        if (!buttonsInited) {
            initButtonIcons(g);
        }

        super.paint(g);
    }

    private String getTagShortcut(NBTType type) {
        switch (type) {
                case END:
                    return "";
                case BYTE:
                    return " (Alt-B)";
                case SHORT:
                    return " (Alt-S)";
                case INT:
                    return " (Alt-I)";
                case LONG:
                    return " (Alt-L)";
                case FLOAT:
                    return " (Alt-F)";
                case DOUBLE:
                    return " (Alt-D)";
                case BYTE_ARRAY:
                    return " (Alt-Shift-B)";
                case STRING:
                    return " (Alt-Shift-S)";
                case LIST:
                    return " (Alt-Shift-L)";
                case COMPOUND:
                    return " (Alt-C)";
                case INT_ARRAY:
                    return " (Alt-Shift-I)";
                case SHORT_ARRAY:
                    return "";
                default:
                    return "";
            }
    }
    
    private void initButtonIcons(Graphics g) {
        for (NBTType type : NBTType.editorOptions_asType) {
            NBTIcon icon = NBTIcon.getIconByType(type);

            JButton addTagButton = new JButton();

            int iconWidth, iconHeight;

            if (addTagButton.getFont() != null && g != null) {
                // Find out how much to scale the icon by
                FontMetrics metrics = g.getFontMetrics(addTagButton.getFont());
                int height = metrics.getHeight() - 1;
                float scaleRatio = ((float) height) / ((float) NBTIcon.END.icon.getIconWidth());

                iconWidth = Math.round(((float) NBTIcon.END.icon.getIconWidth()) * scaleRatio);
                iconHeight = height;
            } else {
                iconWidth = addTagButton.getFont().getSize();
                iconHeight = addTagButton.getFont().getSize();
            }

            addTagButton.setIcon(icon.getIcon(iconWidth, iconHeight));
            addTagButton.setFocusable(false);

            addTagButton.setToolTipText("Add " + WordUtils.whitespaceBeforeUppercase(icon.getDisplayName()) + getTagShortcut(type));

            addTagButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    addTagButtonActionPerformed(evt, icon.type);
                }
            });

            buttonToolBar.add(addTagButton);
        }

        setButtonMaterialIcon(saveToolbarButton, MaterialIcon.SAVE, g);
        setButtonMaterialIcon(cutToolbarButton, MaterialIcon.CUT, g);
        setButtonMaterialIcon(copyToolbarButton, MaterialIcon.COPY, g);
        setButtonMaterialIcon(pasteToolbarButton, MaterialIcon.PASTE, g);
        setButtonMaterialIcon(deleteToolbarButton, MaterialIcon.DELETE, g);

        buttonsInited = true;
    }

    private void setButtonMaterialIcon(JButton button, MaterialIcon icon, Graphics g) {
        int iconWidth, iconHeight;

        if (button.getFont() != null && g != null) {
            // Find out how much to scale the icon by
            FontMetrics metrics = g.getFontMetrics(button.getFont());
            int height = metrics.getHeight() - 1;
            float scaleRatio = ((float) height) / ((float) icon.getBlackIcon().getIconHeight());

            iconWidth = Math.round(((float) icon.getBlackIcon().getIconWidth()) * scaleRatio);
            iconHeight = height;
        } else {
            iconWidth = button.getFont().getSize();
            iconHeight = button.getFont().getSize();
        }

        button.setIcon(icon.getBlackIcon(iconWidth, iconHeight));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        treePopupMenu = new JPopupMenu();
        editMenuItem = new JMenuItem();
        expandMenuItem = new JMenuItem();
        jSeparator2 = new JPopupMenu.Separator();
        cutMenuItem = new JMenuItem();
        copyMenuItem = new JMenuItem();
        pasteMenuItem = new JMenuItem();
        deleteMenuItem = new JMenuItem();
        nbtTreeScrollPane = new JScrollPane();
        nbtTree = new JTree();
        buttonToolBar = new JToolBar();
        saveToolbarButton = new JButton();
        cutToolbarButton = new JButton();
        copyToolbarButton = new JButton();
        pasteToolbarButton = new JButton();
        deleteToolbarButton = new JButton();
        toolToTagSeparator = new JToolBar.Separator();

        editMenuItem.setText("Edit");
        editMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                editMenuItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(editMenuItem);

        expandMenuItem.setText("Toggle Expand");
        expandMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                expandMenuItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(expandMenuItem);
        treePopupMenu.add(jSeparator2);

        cutMenuItem.setText("Cut");
        cutMenuItem.setToolTipText("");
        cutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cutMenuItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(cutMenuItem);

        copyMenuItem.setText("Copy");
        copyMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                copyMenuItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(copyMenuItem);

        pasteMenuItem.setText("Paste");
        pasteMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                pasteMenuItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(pasteMenuItem);

        deleteMenuItem.setText("Delete");
        deleteMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                deleteMenuItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(deleteMenuItem);

        nbtTreeScrollPane.setViewportView(nbtTree);

        buttonToolBar.setFloatable(false);
        buttonToolBar.setRollover(true);

        saveToolbarButton.setToolTipText("Save NBT Data (Ctrl-S)");
        saveToolbarButton.setFocusable(false);
        saveToolbarButton.setHorizontalTextPosition(SwingConstants.CENTER);
        saveToolbarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        saveToolbarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                saveToolbarButtonActionPerformed(evt);
            }
        });
        buttonToolBar.add(saveToolbarButton);

        cutToolbarButton.setToolTipText("Cut Selected Nodes (Ctrl-X)");
        cutToolbarButton.setFocusable(false);
        cutToolbarButton.setHorizontalTextPosition(SwingConstants.CENTER);
        cutToolbarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        cutToolbarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cutToolbarButtonActionPerformed(evt);
            }
        });
        buttonToolBar.add(cutToolbarButton);

        copyToolbarButton.setToolTipText("Copy Selected Nodes (Ctrl-C)");
        copyToolbarButton.setFocusable(false);
        copyToolbarButton.setHorizontalTextPosition(SwingConstants.CENTER);
        copyToolbarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        copyToolbarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                copyToolbarButtonActionPerformed(evt);
            }
        });
        buttonToolBar.add(copyToolbarButton);

        pasteToolbarButton.setToolTipText("Paste Into Selected Nodes (Ctrl-V)");
        pasteToolbarButton.setFocusable(false);
        pasteToolbarButton.setHorizontalTextPosition(SwingConstants.CENTER);
        pasteToolbarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        pasteToolbarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                pasteToolbarButtonActionPerformed(evt);
            }
        });
        buttonToolBar.add(pasteToolbarButton);

        deleteToolbarButton.setToolTipText("Delete Selected Nodes (Del)");
        deleteToolbarButton.setFocusable(false);
        deleteToolbarButton.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteToolbarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        deleteToolbarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                deleteToolbarButtonActionPerformed(evt);
            }
        });
        buttonToolBar.add(deleteToolbarButton);
        buttonToolBar.add(toolToTagSeparator);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(nbtTreeScrollPane, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
            .addComponent(buttonToolBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(buttonToolBar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(nbtTreeScrollPane, GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void expandMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_expandMenuItemActionPerformed
        toggleExpandSelectedNodes();
    }//GEN-LAST:event_expandMenuItemActionPerformed

    private void saveToolbarButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_saveToolbarButtonActionPerformed
        saveNBTData();
    }//GEN-LAST:event_saveToolbarButtonActionPerformed

    private void copyToolbarButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_copyToolbarButtonActionPerformed
        copySelectedNodesToClipboard();
    }//GEN-LAST:event_copyToolbarButtonActionPerformed

    private void pasteToolbarButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_pasteToolbarButtonActionPerformed
        pasteClipboardIntoSelectedNodes();
    }//GEN-LAST:event_pasteToolbarButtonActionPerformed

    private void deleteToolbarButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_deleteToolbarButtonActionPerformed
        deleteSelectedNodes();
    }//GEN-LAST:event_deleteToolbarButtonActionPerformed

    private void copyMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_copyMenuItemActionPerformed
        copySelectedNodesToClipboard();
    }//GEN-LAST:event_copyMenuItemActionPerformed

    private void pasteMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_pasteMenuItemActionPerformed
        pasteClipboardIntoSelectedNodes();
    }//GEN-LAST:event_pasteMenuItemActionPerformed

    private void cutMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cutMenuItemActionPerformed
        cutSelectedNodesToClipboard();
    }//GEN-LAST:event_cutMenuItemActionPerformed

    private void deleteMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_deleteMenuItemActionPerformed
        deleteSelectedNodes();
    }//GEN-LAST:event_deleteMenuItemActionPerformed

    private void cutToolbarButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cutToolbarButtonActionPerformed
        cutSelectedNodesToClipboard();
    }//GEN-LAST:event_cutToolbarButtonActionPerformed

    private void editMenuItemActionPerformed(ActionEvent evt) {//GEN-FIRST:event_editMenuItemActionPerformed
        editLastSelectedNode();
    }//GEN-LAST:event_editMenuItemActionPerformed

    private void addTagButtonActionPerformed(ActionEvent evt, NBTType type) {
        addNewNodeToSelectedNodes(type);
    }

    private void saveNBTData() {
        if (!nbtData.save()) {
            JOptionPane.showMessageDialog(this, "Failed to save NBT Data!", "Couldn't Save...", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Successfully saved " + nbtData.getRootTitle() + "!", "Saved!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addNewNodeToSelectedNodes(NBTType type) {
        DefaultTreeModel model = (DefaultTreeModel) nbtTree.getModel();

        TreePath[] paths = nbtTree.getSelectionPaths();
        if (paths == null) {
            return;
        }

        for (TreePath path : paths) {
            if (path.getLastPathComponent() instanceof NBTNode) {
                NBTNode selectedNode = (NBTNode) path.getLastPathComponent();

                switch (selectedNode.getTag().getType()) {
                    case COMPOUND:
                    case LIST:
                        selectedNode.addTag(new NBTNode(NBTConstants.NBTType.newInstance("New"+type.displayName, type), selectedNode.getTag()));
                        model.reload(selectedNode);
                        break;
                    default:
                        ((NBTNode) selectedNode.getParent()).addTag(new NBTNode(NBTConstants.NBTType.newInstance("New"+type.displayName, type), selectedNode.getTag()));
                        model.reload(selectedNode.getParent());
                        break;
                }
            }
        }
    }

    private void copySelectedNodesToClipboard() {
        if (clipboard == null) {
            return;
        }
        clipboard.clear();

        TreePath[] paths = nbtTree.getSelectionPaths();
        if (paths == null) {
            return;
        }

        for (TreePath path : paths) {
            if (path.getLastPathComponent() instanceof NBTNode) {
                NBTNode selectedNode = (NBTNode) path.getLastPathComponent();

                clipboard.add(selectedNode);
            }
        }
    }

    private void cutSelectedNodesToClipboard() {
        copySelectedNodesToClipboard();
        deleteSelectedNodes();
    }

    private void pasteClipboardIntoSelectedNodes() {
        if (clipboard == null || clipboard.isEmpty()) {
            return;
        }

        DefaultTreeModel model = (DefaultTreeModel) nbtTree.getModel();

        TreePath[] paths = nbtTree.getSelectionPaths();
        if (paths == null) {
            return;
        }

        for (TreePath path : paths) {
            if (path.getLastPathComponent() instanceof NBTNode) {
                NBTNode selectedNode = (NBTNode) path.getLastPathComponent();

                switch (selectedNode.getTag().getType()) {
                    case COMPOUND:
                        for (NBTNode clipboardNode : clipboard) {
                            NBTNode clone = new NBTNode(clipboardNode.getTag().getDeepCopy(), selectedNode.getTag());

                            iterateTags(clone);
                            selectedNode.addTag(clone);
                            model.reload(clone);
                        }

                        nbtTree.expandPath(path);
                        model.reload(selectedNode);
                        break;
                    case LIST:
                        NBTType listType = ((ListTag) selectedNode.getTag()).getListType();
                        boolean listEmpty = ((ListTag) selectedNode.getTag()).getValue().isEmpty();

                        for (NBTNode clipboardNode : clipboard) {
                            if (!listEmpty || clipboardNode.getTag().getType() != listType) {
                                continue;
                            }

                            listEmpty = false;
                            listType = clipboardNode.getTag().getType();

                            NBTNode clone = new NBTNode(clipboardNode.getTag().getDeepCopy(), selectedNode.getTag());

                            iterateTags(clone);
                            selectedNode.addTag(clone);
                            model.reload(clone);
                        }

                        nbtTree.expandPath(path);
                        model.reload(selectedNode);
                        break;
                    default:
                        for (NBTNode clipboardNode : clipboard) {
                            NBTNode clone = new NBTNode(clipboardNode.getTag().getDeepCopy(), selectedNode.getTag());
                            iterateTags(clone);

                            ((NBTNode) selectedNode.getParent()).addTag(clone);
                            model.reload(clone);
                        }

                        model.reload(selectedNode.getParent());
                        break;
                }
            }
        }
    }

    private void deleteSelectedNodes() {
        DefaultTreeModel model = (DefaultTreeModel) nbtTree.getModel();

        TreePath[] paths = nbtTree.getSelectionPaths();
        if (paths == null) {
            return;
        }

        for (TreePath path : paths) {
            if (path.getLastPathComponent() instanceof NBTNode) {
                NBTNode selectedNode = (NBTNode) path.getLastPathComponent();

                model.removeNodeFromParent(selectedNode);
            }
        }
    }

    private void toggleExpandSelectedNodes() {
        TreePath[] paths = nbtTree.getSelectionPaths();
        if (paths == null) {
            return;
        }

        for (TreePath path : paths) {
            if (path.getLastPathComponent() instanceof NBTNode) {
                NBTNode selectedNode = (NBTNode) path.getLastPathComponent();

                switch (selectedNode.getTag().getType()) {
                    case COMPOUND:
                    case LIST:
                        if (nbtTree.isExpanded(path)) {
                            nbtTree.collapsePath(path);
                        } else {
                            nbtTree.expandPath(path);
                        }
                        break;
                }
            }
        }
    }

    private void editLastSelectedNode() {
        TreePath lastSelectedNode = nbtTree.getLeadSelectionPath();
        if (lastSelectedNode == null) {
            return;
        }

        if (lastSelectedNode.getLastPathComponent() instanceof NBTNode) {
            nbtTree.startEditingAtPath(lastSelectedNode);
        }
    }
    
    private void printTagSignature() {
        TreePath[] paths = nbtTree.getSelectionPaths();
        if (paths == null) {
            return;
        }

        for (TreePath path : paths) {
            if (path.getLastPathComponent() instanceof NBTNode) {
                NBTNode selectedNode = (NBTNode) path.getLastPathComponent();
                Tag tag = selectedNode.getTag();
                Tag parentTag = selectedNode.getParentTag();
                
                System.out.format("Signature for %s (%s): %X%n", tag.getName(), tag.getType().displayName, NBTSignature.calculateTagSignature(tag, parentTag));
            }
        }
    }

    private boolean parseNodeValue(NBTNode node) {
        String nodeText = (String) node.getUserObject();
        String[] nodeData = nodeText.split(":", 2);
        NBTType nodeType = node.getTag().getType();

        int indexOfColon = nodeText.indexOf(":");

        boolean returnValue = true;

        switch (nodeData.length) {
            case 0: // Node is completely empty
                node.getTag().setName("");
                if (nodeType != NBTType.COMPOUND && nodeType != NBTType.LIST) {
                    node.getTag().setValue(NBTType.getDefaultValue(nodeType));
                }
                break;
            case 1: // Either the node name or value exists, but not both
                if (indexOfColon < 0 || (nodeText.indexOf(nodeData[0]) < indexOfColon)) {
                    node.getTag().setName(nodeData[0].trim());
                    if (nodeType != NBTType.COMPOUND && nodeType != NBTType.LIST) {
                        node.getTag().setValue(NBTType.getDefaultValue(nodeType));
                    }
                } else {
                    node.getTag().setName("");
                    if (nodeType != NBTType.COMPOUND && nodeType != NBTType.LIST) {
                        try {
                            node.getTag().setValue(NBTType.parseValue(nodeData[0], nodeType));
                        } catch (Exception e) {
                            parentWindow.footerBar.logMessage("Couldn't parse NBT value", MessageImportance.ERROR);
                            returnValue = false;
                        }
                    }
                }
                break;
            case 2:
                node.getTag().setName(nodeData[0].trim());
                if (nodeType != NBTType.COMPOUND && nodeType != NBTType.LIST) {
                    try {
                        node.getTag().setValue(NBTType.parseValue(nodeData[1], nodeType));
                    } catch (Exception e) {
                        parentWindow.footerBar.logMessage("Couldn't parse NBT value", MessageImportance.ERROR);
                        returnValue = false;
                        //e.printStackTrace();
                    }
                }
                break;
        }

        node.updateUserObject();

        String value;

        switch (nodeType) {
            case BYTE_ARRAY:
                value = Arrays.toString((byte[]) node.getTag().getValue());
                break;
            case INT_ARRAY:
                value = Arrays.toString((int[]) node.getTag().getValue());
                break;
            case SHORT_ARRAY:
                value = Arrays.toString((short[]) node.getTag().getValue());
                break;
            default:
                value = node.getTag().getValue().toString();
                break;
        }

        System.out.println("New Tag Values: " + node.getTag().getName() + ", " + value);
        //System.out.println("New Tag Object: " + node.getUserObject());
        return returnValue;
    }

    private JButton[] addTagButtons = new JButton[NBTIcon.values().length];

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JToolBar buttonToolBar;
    private JMenuItem copyMenuItem;
    private JButton copyToolbarButton;
    private JMenuItem cutMenuItem;
    private JButton cutToolbarButton;
    private JMenuItem deleteMenuItem;
    private JButton deleteToolbarButton;
    private JMenuItem editMenuItem;
    private JMenuItem expandMenuItem;
    private JPopupMenu.Separator jSeparator2;
    private JTree nbtTree;
    private JScrollPane nbtTreeScrollPane;
    private JMenuItem pasteMenuItem;
    private JButton pasteToolbarButton;
    private JButton saveToolbarButton;
    private JToolBar.Separator toolToTagSeparator;
    private JPopupMenu treePopupMenu;
    // End of variables declaration//GEN-END:variables

    @Override
    public boolean cleanUp() {
        return true;
    }

    class NBTTreeCellRenderer extends DefaultTreeCellRenderer {

        //private final NBTCell cell;

        NBTTreeCellRenderer() {
            super();
            //cell = new NBTCell();
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            NBTCell cell = new NBTCell();
            int iconHeight = cell.getFont().getSize();

            if (value instanceof NBTNode) {
                Tag tag = ((NBTNode) value).getTag();
                Tag parentTag = ((NBTNode) value).getParentTag();
                ImageIcon nbtIcon = ((NBTNode) value).getIcon(iconHeight, iconHeight);

                /*
                if (cell.getFont() != null && cell.getGraphics() != null) {
                    // Find out how much to scale the icon by
                    FontMetrics metrics = cell.getGraphics().getFontMetrics(cell.getFont());
                    int height = metrics.getHeight() - 1;
                    float scaleRatio = ((float) height) / ((float) ((NBTNode) value).getDefaultIconHeight());

                    nbtIcon = ((NBTNode) value).getIcon(Math.round(((NBTNode) value).getDefaultIconWidth() * scaleRatio), height);
                } else {
                    // Guess the size
                    //System.out.println("Is null: " + (cell.getFont() == null) + ", " + (cell.getGraphics() == null));
                    nbtIcon = ((NBTNode) value).getLastScaledIcon();
                }
                 */
                if (nbtIcon != null) {
                    cell.setIcon(nbtIcon);
                }

                cell.setTagName(tag.getName());

                switch (tag.getType()) {
                    case COMPOUND:
                    case LIST:
                        cell.shouldShowValue(true);
                        cell.setTagValue("");
                        break;
                    case BYTE_ARRAY:
                        cell.setTagValue(Arrays.toString((byte[]) tag.getValue()));
                        cell.shouldShowValue(true);
                        break;
                    case SHORT_ARRAY:
                        cell.setTagValue(Arrays.toString((short[]) tag.getValue()));
                        cell.shouldShowValue(true);
                        break;
                    case INT_ARRAY:
                        cell.setTagValue(Arrays.toString((int[]) tag.getValue()));
                        cell.shouldShowValue(true);
                        break;
                    default:
                        cell.setTagValue(tag.getValue().toString());
                        cell.shouldShowValue(true);
                }
                
                cell.setAdditionalInfo(NBTSignature.parseTagInfo(tag, parentTag));
            } else {
                //System.out.println("This is odd... " + value.getClass().getName());
                //cell.setName("This is odd... " + value.getClass().getName()); // Change text for the public release
                cell.setIcon(NBTIcon.END.getIcon(iconHeight, iconHeight)); // The END tag uses the default icon
            }

            return cell;
        }
    }

    private static class NBTTreeCellEditor extends DefaultTreeCellEditor {

        public NBTTreeCellEditor(JTree tree, NBTTreeCellRenderer renderer) {
            super(tree, renderer);
        }

        @Override
        public Component getTreeCellEditorComponent(JTree tree, Object value,
                boolean isSelected, boolean expanded, boolean leaf, int row) {

            if (value instanceof NBTNode) {
                value = ((NBTNode) value).getUserObject();
            }

            return super.getTreeCellEditorComponent(tree, value, isSelected, expanded,
                    leaf, row);
        }
    }

    class NBTTreeModelListener implements TreeModelListener {

        private DefaultTreeModel model;

        public NBTTreeModelListener(DefaultTreeModel model) {
            this.model = model;
        }

        @Override
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());

            try {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode) (node.getChildAt(index));
            } catch (NullPointerException ex) {
                //ex.printStackTrace();
            }

            if (node instanceof NBTNode) {
                parseNodeValue((NBTNode) node);
                model.reload(node);
            }
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }

    class NBTTreeKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (PlatformUtils.isCtrlKeyDown(e)) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_C:
                        copySelectedNodesToClipboard();
                        break;
                    case KeyEvent.VK_V:
                        pasteClipboardIntoSelectedNodes();
                        break;
                    case KeyEvent.VK_X:
                        cutSelectedNodesToClipboard();
                        break;
                    case KeyEvent.VK_S:
                        saveNBTData();
                        break;
                    case KeyEvent.VK_P:
                        printTagSignature();
                        break;
                    default:
                    //System.out.println("Ctrl+" + KeyEvent.getKeyText(e.getKeyCode()));
                }
            } else if (PlatformUtils.isAltKeyDown(e)) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_B:
                        if (e.isShiftDown()) {
                            addNewNodeToSelectedNodes(NBTType.BYTE_ARRAY);
                        } else {
                            addNewNodeToSelectedNodes(NBTType.BYTE);
                        }
                        break;
                    case KeyEvent.VK_S:
                        if (e.isShiftDown()) {
                            addNewNodeToSelectedNodes(NBTType.STRING);
                        } else {
                            addNewNodeToSelectedNodes(NBTType.SHORT);
                        }
                        break;
                    case KeyEvent.VK_I:
                        if (e.isShiftDown()) {
                            addNewNodeToSelectedNodes(NBTType.INT_ARRAY);
                        } else {
                            addNewNodeToSelectedNodes(NBTType.INT);
                        }
                        break;
                    case KeyEvent.VK_L:
                        if (e.isShiftDown()) {
                            addNewNodeToSelectedNodes(NBTType.LIST);
                        } else {
                            addNewNodeToSelectedNodes(NBTType.LONG);
                        }
                        break;
                    case KeyEvent.VK_F:
                        addNewNodeToSelectedNodes(NBTType.FLOAT);
                        break;
                    case KeyEvent.VK_D:
                        addNewNodeToSelectedNodes(NBTType.DOUBLE);
                        break;
                    case KeyEvent.VK_C:
                        addNewNodeToSelectedNodes(NBTType.COMPOUND);
                    default:
                    //System.out.println("Alt+" + KeyEvent.getKeyText(e.getKeyCode()));
                }
            } else {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DELETE:
                        deleteSelectedNodes();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    class PopupTriggerListener extends MouseAdapter {

        public void mousePressed(MouseEvent ev) {
            if (SwingUtilities.isRightMouseButton(ev)) {
                int selRow = nbtTree.getRowForLocation(ev.getX(), ev.getY());
                TreePath selPath = nbtTree.getPathForLocation(ev.getX(), ev.getY());

                if (PlatformUtils.isMultiSelectKeyDown(ev)) {
                    nbtTree.addSelectionPath(selPath);
                    if (selRow > -1) {
                        nbtTree.addSelectionRow(selRow);
                    }
                } else if (nbtTree.isSelectionEmpty()) {
                    nbtTree.setSelectionPath(selPath);
                    if (selRow > -1) {
                        nbtTree.setSelectionRow(selRow);
                    }
                }
            }

            if (ev.isPopupTrigger()) {
                treePopupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
            }
        }

        public void mouseReleased(MouseEvent ev) {
            if (ev.isPopupTrigger()) {
                treePopupMenu.show(ev.getComponent(), ev.getX(), ev.getY());
            }
        }

        public void mouseClicked(MouseEvent ev) {
        }
    }
}
