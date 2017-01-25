/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.options;

import com.jocopa3.blocktopographpc.util.WorldListUtil;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Matt
 */
public enum OptionEnum {
    BACKUP_FOLDER("backup_folder", WorldListUtil.getDefaultBackupFolder(), String.class),
    WORLD_FOLDER("world_folder", WorldListUtil.getMinecraftFolderLocation(), String.class);
    
    private final String name, defaultValue;
    private final Class typeClass;
    
    OptionEnum(String optionName, String defaultValue, Class optionTypeClass) {
        name = optionName;
        this.defaultValue = defaultValue;
        typeClass = optionTypeClass;
    }
    
    public String getKeyName() {
        return name;
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }
    
    public Class getTypeClass() {
        return typeClass;
    }
}
