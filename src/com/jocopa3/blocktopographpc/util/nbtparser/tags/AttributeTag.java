/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser.tags;

import com.jocopa3.blocktopographpc.util.nbtparser.NBTSignature;
import com.protolambda.blocktopograph.nbt.tags.CompoundTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;

/**
 *
 * @author Matt
 */
public class AttributeTag extends ParseableTag {

    public AttributeTag(String format) {
        super(format);
    }

    @Override
    public String parseTag(Tag tag) {
        if (tag instanceof CompoundTag) {
            float base = 0, max = 0, current = 0;
            String Name = "";

            for (Tag child : ((CompoundTag) tag).getValue()) {
                switch (child.getName()) {
                    case "Base":
                        base = (float) child.getValue();
                        break;
                    case "Max":
                        max = (float) child.getValue();
                        break;
                    case "Current":
                        current = (float) child.getValue();
                        break;
                    case "Name":
                        Name = (String) child.getValue();
                        break;
                }
            }

            if (max > 1000000.0f || max < -1000000.0f) {
                max = Float.POSITIVE_INFINITY;
            }

            return String.format(format, Name, current, max);
        }
        
        return "";
    }
}
