/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser.tags;

import com.jocopa3.blocktopographpc.util.nbtparser.NBTSignature;
import com.protolambda.blocktopograph.map.Block;
import com.protolambda.blocktopograph.nbt.tags.ShortTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;

/**
 *
 * @author Matt
 */
public class ItemIdTag extends ParseableTag {

    public ItemIdTag(String format) {
        super(format);
    }

    @Override
    public String parseTag(Tag tag) {
        if (tag instanceof ShortTag) {
            short id = ((ShortTag) tag).getValue();

            Block block = Block.getBlock(id, 0);
            if (block == null) {
                return String.format(format, "Unknown");
            }

            return String.format(format, block.name);
        }
        return "";
    }

}
