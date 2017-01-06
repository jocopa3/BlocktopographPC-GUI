/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui.nbt;

import com.jocopa3.blocktopographpc.gui.panels.EditableJLabel;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

/**
 *
 * @author Matt
 */
public class NBTCelln extends JPanel {
    
    private boolean isValueVisible = true;

    /**
     * Creates new form NBTCell
     */
    public NBTCelln() {
        initComponents();
        
        setOpaque(false);
        nameLabel.setOpaque(false);
        valueLabel.setOpaque(false);
        separatorLabel.setOpaque(false);
    }
    
    private void initComponents() {
        nameLabel = new EditableJLabel("Name");
        separatorLabel = new JLabel();
        valueLabel = new EditableJLabel("Value");
        iconLabel = new JLabel();

        nameLabel.getLabel().setFont(nameLabel.getLabel().getFont().deriveFont(nameLabel.getLabel().getFont().getStyle() | Font.BOLD));

        separatorLabel.setText(":");

        iconLabel.setText(" ");
        iconLabel.setIconTextGap(0);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(iconLabel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(nameLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separatorLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valueLabel)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(nameLabel)
                .addComponent(separatorLabel)
                .addComponent(valueLabel)
                .addComponent(iconLabel))
        );
    }
    
    public void setIcon(ImageIcon icon) {
        if (icon != null) {
            iconLabel.setIcon(icon);
        }
    }

    public void setTagName(String tag) {
        nameLabel.setText(tag);
    }

    public void setTagValue(String tag) {
        valueLabel.setText(tag);
    }

    public void shouldShowValue(boolean show) {
        valueLabel.setVisible(show);
        separatorLabel.setVisible(show);

        isValueVisible = show;
    }

    public boolean isShowingValue() {
        return isValueVisible;
    }

    @Override
    public Font getFont() {
        if (valueLabel == null || valueLabel.getLabel() == null) {
            return null;
        }

        return valueLabel.getLabel().getFont();
    }
                
    private JLabel iconLabel;
    private EditableJLabel valueLabel;
    private JLabel separatorLabel;
    private EditableJLabel nameLabel;
}
