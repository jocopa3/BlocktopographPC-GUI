/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser.tags;

import com.jocopa3.blocktopographpc.util.nbtparser.NBTSignature;
import com.protolambda.blocktopograph.nbt.tags.IntTag;
import com.protolambda.blocktopograph.nbt.tags.ListTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;
import java.util.ArrayList;

/**
 *
 * @author Matt
 */
public class LastOpenedVersionTag extends ParseableTag {

    public LastOpenedVersionTag(String format) {
        super(format);
    }

    @Override
    public String parseTag(Tag tag) {
        if (tag instanceof ListTag) {
            ArrayList<Tag> values = ((ListTag) tag).getValue();
            int major = ((IntTag) (values.get(0))).getValue(),
                    minor = ((IntTag) (values.get(1))).getValue(),
                    patch = ((IntTag) (values.get(2))).getValue(),
                    beta = ((IntTag) (values.get(3))).getValue();

            return String.format(format, major, minor, patch, beta);
        }
        return "";
    }

}
