/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser.tags;

import com.jocopa3.blocktopographpc.util.nbtparser.NBTSignature;
import com.protolambda.blocktopograph.map.Generator;
import com.protolambda.blocktopograph.nbt.tags.IntTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;

/**
 *
 * @author Matt
 */
public class WorldGeneratorTag extends ParseableTag {

    public WorldGeneratorTag(String format) {
        super(format);
    }

    @Override
    public String parseTag(Tag tag) {
        if (tag instanceof IntTag) {
            int generator = (int) tag.getValue();

            if (generator >= 0 && generator < Generator.values().length) {
                String gameType = Generator.values()[generator].name;
                return String.format(format, gameType);
            }
        }
        return "";
    }

}
