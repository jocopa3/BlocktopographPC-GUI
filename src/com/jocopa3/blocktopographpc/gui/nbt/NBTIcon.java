/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui.nbt;

import com.protolambda.blocktopograph.nbt.convert.NBTConstants.NBTType;
import com.protolambda.blocktopograph.util.io.ImageUtil;
import javax.swing.ImageIcon;

/**
 *
 * @author Matt
 */
public enum NBTIcon {

    END(NBTType.END, "nbt_icons/ic_tag_default.png"),
    BYTE(NBTType.BYTE, "nbt_icons/ic_tag_byte.png"),
    SHORT(NBTType.SHORT, "nbt_icons/ic_tag_short.png"),
    INT(NBTType.INT, "nbt_icons/ic_tag_int.png"),
    LONG(NBTType.LONG, "nbt_icons/ic_tag_long.png"),
    FLOAT(NBTType.FLOAT, "nbt_icons/ic_tag_float.png"),
    DOUBLE(NBTType.DOUBLE, "nbt_icons/ic_tag_double.png"),
    BYTE_ARRAY(NBTType.BYTE_ARRAY, "nbt_icons/ic_tag_byte_array.png"),
    STRING(NBTType.STRING, "nbt_icons/ic_tag_string.png"),
    LIST(NBTType.LIST, "nbt_icons/ic_tag_list.png"),
    COMPOUND(NBTType.COMPOUND, "nbt_icons/ic_tag_compound.png"),
    INT_ARRAY(NBTType.INT_ARRAY, "nbt_icons/ic_tag_int_array.png"),
    SHORT_ARRAY(NBTType.SHORT_ARRAY, "nbt_icons/ic_tag_int_array.png");

    public final NBTType type;
    public final ImageIcon icon;

    // Cache the last scaled icon; normally the program will constantly request the same sized icon
    private ImageIcon lastScaledIcon = null;
    private int scaleWidth = 0;
    private int scaleHeight = 0;

    NBTIcon(NBTType type, String iconPath) {
        this.type = type;
        this.icon = new ImageIcon(ImageUtil.readImage(iconPath));
    }

    public static NBTIcon getIconByType(NBTType type) {
        switch (type) {
            case END:
                return END;
            case BYTE:
                return BYTE;
            case SHORT:
                return SHORT;
            case INT:
                return INT;
            case LONG:
                return LONG;
            case FLOAT:
                return FLOAT;
            case DOUBLE:
                return DOUBLE;
            case BYTE_ARRAY:
                return BYTE_ARRAY;
            case STRING:
                return STRING;
            case LIST:
                return LIST;
            case COMPOUND:
                return COMPOUND;
            case INT_ARRAY:
                return INT_ARRAY;
            case SHORT_ARRAY:
                return SHORT_ARRAY;
            default:
                return END; // END tag uses default icon
        }
    }

    public String getDisplayName() {
        return type.displayName;
    }

    public ImageIcon getIcon(int width, int height) {
        if (lastScaledIcon == null || width != scaleWidth || height != scaleHeight) {
            lastScaledIcon = new ImageIcon(ImageUtil.scaleImage(icon.getImage(), width, height));
            scaleWidth = width;
            scaleHeight = height;
        }

        return lastScaledIcon;
    }
    
    public ImageIcon getLastScaledIcon() {
        return lastScaledIcon;
    }
}
