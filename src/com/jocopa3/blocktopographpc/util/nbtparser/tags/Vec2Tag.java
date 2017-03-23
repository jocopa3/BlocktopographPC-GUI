/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser.tags;

import com.jocopa3.blocktopographpc.util.nbtparser.NBTSignature;
import com.protolambda.blocktopograph.nbt.tags.FloatTag;
import com.protolambda.blocktopograph.nbt.tags.ListTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;
import java.util.ArrayList;

/**
 *
 * @author Matt
 */
public class Vec2Tag extends ParseableTag {

    public Vec2Tag(String format) {
        super(format);
    }

    @Override
    public String parseTag(Tag tag) {
        if (tag instanceof ListTag) {
            ArrayList<Tag> values = ((ListTag) tag).getValue();
            float x = ((FloatTag) (values.get(0))).getValue(),
                    y = ((FloatTag) (values.get(1))).getValue();

            return String.format(format, x, y);
        }
        return "";
    }

}
