/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui;

import com.protolambda.blocktopograph.util.io.ImageUtil;
import javax.swing.ImageIcon;

/**
 *
 * @author Matt
 */
public enum MaterialIcon {

    ARROW_RIGHT("material_icons/ic_action_arrow_right.png", null),
    COMPASS(null, "material_icons/ic_action_compass.png"),
    ADD(null, "material_icons/ic_add_b.png"),
    DELETE(null, "material_icons/ic_delete_b.png"),
    CUT(null, "material_icons/ic_content_cut_b.png"),
    COPY(null, "material_icons/ic_content_copy_b.png"),
    PASTE(null, "material_icons/ic_content_paste_b.png"),
    SAVE("material_icons/ic_action_save.png", "material_icons/ic_action_save_b.png"),
    WARNING(null, "material_icons/ic_warning_b.png"),
    ERROR(null, "material_icons/ic_error_b.png");

    public final ImageIcon whiteIcon;
    public final ImageIcon blackIcon;

    // Cache the last scaled icon; normally the program will constantly request the same sized icon
    private ImageIcon lastScaledWhiteIcon = null;
    private int whiteScaleWidth = 0;
    private int whiteScaleHeight = 0;

    // Cache the last scaled icon; normally the program will constantly request the same sized icon
    private ImageIcon lastScaledBlackIcon = null;
    private int blackScaleWidth = 0;
    private int blackScaleHeight = 0;

    MaterialIcon(String white, String black) {
        ImageIcon temp;

        try {
            if (white != null) {
                temp = new ImageIcon(ImageUtil.readImage(white));

            } else {
                temp = null;
            }
        } catch (Exception e) {
            temp = null;
        }
        whiteIcon = temp;

        try {
            if (black != null) {
                temp = new ImageIcon(ImageUtil.readImage(black));

            } else {
                temp = null;
            }
        } catch (Exception e) {
            temp = null;
        }
        blackIcon = temp;
    }

    public ImageIcon getIcon() {
        if (blackIcon == null) {
            if (whiteIcon == null) {
                return null;
            }

            return whiteIcon;
        }

        return blackIcon;
    }

    public ImageIcon getWhiteIcon() {
        if (whiteIcon == null) {
            return null;
        }

        return whiteIcon;
    }

    public ImageIcon getBlackIcon() {
        if (blackIcon == null) {
            return null;
        }

        return blackIcon;
    }

    public ImageIcon getIcon(int width, int height) {
        ImageIcon icon = getBlackIcon(width, height);
        if (icon == null) {
            icon = getWhiteIcon(width, height);
        }

        return icon;
    }

    public ImageIcon getWhiteIcon(int width, int height) {
        if (lastScaledWhiteIcon == null || width != whiteScaleWidth || height != whiteScaleHeight) {
            lastScaledWhiteIcon = new ImageIcon(ImageUtil.scaleImage(whiteIcon.getImage(), width, height));
            whiteScaleWidth = width;
            whiteScaleHeight = height;
        }

        return lastScaledWhiteIcon;
    }

    public ImageIcon getBlackIcon(int width, int height) {
        if (lastScaledBlackIcon == null || width != blackScaleWidth || height != blackScaleHeight) {
            lastScaledBlackIcon = new ImageIcon(ImageUtil.scaleImage(blackIcon.getImage(), width, height));
            blackScaleWidth = width;
            blackScaleHeight = height;
        }

        return lastScaledBlackIcon;
    }
}
