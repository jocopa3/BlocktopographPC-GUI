/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui.nbt;

import com.protolambda.blocktopograph.nbt.tags.CompoundTag;
import com.protolambda.blocktopograph.nbt.tags.ListTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Matt
 */
public class NBTNode extends DefaultMutableTreeNode {

    private Tag selfTag, parentTag;
    private NBTIcon icon;

    public NBTNode(Tag self, Tag parent) {
        super();

        this.selfTag = self;
        this.parentTag = parent;

        updateUserObject();

        icon = NBTIcon.valueOf(self.getType().name());
    }
    
    public void updateUserObject() {
        switch (selfTag.getType()) {
            case COMPOUND:
            case LIST:
                allowsChildren = true;
                userObject = selfTag.getName();
                break;
            case BYTE_ARRAY:
                allowsChildren = false;
                if (selfTag.getValue() == null) {
                    selfTag.setValue(new byte[]{0});
                }
                userObject = selfTag.getName() + " : " + Arrays.toString((byte[]) selfTag.getValue());
                break;
            case SHORT_ARRAY:
                allowsChildren = false;
                if (selfTag.getValue() == null) {
                    selfTag.setValue(new short[]{0});
                }
                userObject = selfTag.getName() + " : " + Arrays.toString((short[]) selfTag.getValue());
                break;
            case INT_ARRAY:
                allowsChildren = false;
                if (selfTag.getValue() == null) {
                    selfTag.setValue(new int[]{0});
                }
                userObject = selfTag.getName() + " : " + Arrays.toString((int[]) selfTag.getValue());
                break;
            default:
                allowsChildren = false;
                userObject = selfTag.getName() + " : " + selfTag.getValue().toString();
                break;
        }
    }

    public Tag getTag() {
        return selfTag;
    }

    public Tag getParentTag() {
        return parentTag;
    }

    public ImageIcon getIcon() {
        return icon.icon;
    }

    public int getDefaultIconWidth() {
        return icon.icon.getIconWidth();
    }

    public int getDefaultIconHeight() {
        return icon.icon.getIconHeight();
    }

    public ImageIcon getIcon(int width, int height) {
        return icon.getIcon(width, height);
    }
    
    public ImageIcon getLastScaledIcon() {
        return icon.getLastScaledIcon();
    }

    public void addTag(NBTNode newChild) {
        super.add(newChild);

        Tag tag = ((NBTNode) newChild).getTag();
        if (tag == null) {
            return;
        }

        switch (selfTag.getType()) {
            case COMPOUND:
                ((CompoundTag) selfTag).getValue().add(tag);
                break;
            case LIST:
                ((ListTag) selfTag).getValue().add(tag);
                break;
        }
    }

    @Override
    public void remove(MutableTreeNode removedNode) {
        if (removedNode instanceof NBTNode) {
            Tag removedTag = ((NBTNode) removedNode).getTag();

            switch (selfTag.getType()) {
                case COMPOUND:
                    ((CompoundTag) selfTag).getValue().remove(removedTag);
                    break;
                case LIST:
                    ((ListTag) selfTag).getValue().remove(removedTag);
                    break;
            }
        }

        super.remove(removedNode);
    }

    @Override
    public void remove(int index) {
        TreeNode removedNode = getChildAt(index);

        if (removedNode instanceof NBTNode) {
            Tag removedTag = ((NBTNode) removedNode).getTag();

            switch (selfTag.getType()) {
                case COMPOUND:
                    ((CompoundTag) selfTag).getValue().remove(removedTag);
                    break;
                case LIST:
                    ((ListTag) selfTag).getValue().remove(removedTag);
                    break;
            }
        }

        super.remove(index);
    }

    @Override
    public void removeFromParent() {
        switch (parentTag.getType()) {
            case COMPOUND:
                ((CompoundTag) parentTag).getValue().remove(selfTag);
                break;
            case LIST:
                ((ListTag) parentTag).getValue().remove(selfTag);
                break;
        }

        super.removeFromParent();
    }
}
