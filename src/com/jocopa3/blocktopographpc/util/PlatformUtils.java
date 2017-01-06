/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util;

import java.awt.event.InputEvent;

/**
 *
 * @author Matt
 */
public class PlatformUtils {

    public static final OSType OS;

    static {
        String OSName = System.getProperty("os.name").toLowerCase();

        if (OSName.contains("win")) {
            OS = OSType.WIN;
        } else if (OSName.contains("mac")) {
            OS = OSType.MAC;
        } else if (OSName.contains("nix") || OSName.contains("nux") || OSName.contains("aix")) {
            OS = OSType.NIX;
        } else if (OSName.contains("sun")) {
            OS = OSType.SUN;
        } else {
            OS = OSType.UNK;
        }
    }

    public static boolean isMultiSelectKeyDown(InputEvent event) {
        switch (OS) {
            case WIN:
            case NIX:
            case SUN:
                return event.isControlDown();
            case MAC:
                return event.isMetaDown();
        }

        return false;
    }

    public static boolean isAltKeyDown(InputEvent event) {
        switch (OS) {
            case WIN:
            case NIX:
            case SUN:
            case MAC:
                return event.isAltDown();
        }
        
        return false;
    }
    
    public static boolean isCtrlKeyDown(InputEvent event) {
        switch (OS) {
            case WIN:
            case NIX:
            case SUN:
                return event.isControlDown();
            case MAC:
                return event.isMetaDown();
        }
        
        return false;
    }
}
