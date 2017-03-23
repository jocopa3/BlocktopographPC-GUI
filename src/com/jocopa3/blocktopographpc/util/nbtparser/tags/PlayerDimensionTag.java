/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser.tags;

import com.jocopa3.blocktopographpc.util.nbtparser.NBTSignature;
import com.protolambda.blocktopograph.map.Dimension;
import com.protolambda.blocktopograph.nbt.tags.IntTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;

/**
 *
 * @author Matt
 */
public class PlayerDimensionTag extends ParseableTag {

    public PlayerDimensionTag(String format) {
        super(format);
    }

    @Override
    public String parseTag(Tag tag) {
        if (tag instanceof IntTag) {
            String dimensionName = Dimension.getDimension((int) tag.getValue()).name;

            if (!dimensionName.equals("Null")) {
                return String.format(format, dimensionName);
            }
        }
        
        return "";
    }

}
