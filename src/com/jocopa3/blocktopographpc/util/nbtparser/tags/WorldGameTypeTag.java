/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser.tags;

import com.jocopa3.blocktopographpc.util.nbtparser.NBTSignature;
import com.protolambda.blocktopograph.map.GameMode;
import com.protolambda.blocktopograph.nbt.tags.IntTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;

/**
 *
 * @author Matt
 */
public class WorldGameTypeTag extends ParseableTag {

    public WorldGameTypeTag(String format) {
        super(format);
    }

    @Override
    public String parseTag(Tag tag) {
        if (tag instanceof IntTag) {
            int gamemode = (int) tag.getValue();

            if (gamemode >= 0 && gamemode < GameMode.values().length) {
                String gameType = GameMode.values()[gamemode].name;
                return String.format(format, gameType);
            }
        }
        return "";
    }

}
