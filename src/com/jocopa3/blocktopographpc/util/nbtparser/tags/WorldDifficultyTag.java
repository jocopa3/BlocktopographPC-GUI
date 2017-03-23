/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser.tags;

import com.jocopa3.blocktopographpc.util.nbtparser.NBTSignature;
import com.protolambda.blocktopograph.map.Difficulty;
import com.protolambda.blocktopograph.nbt.tags.IntTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;

/**
 *
 * @author Matt
 */
public class WorldDifficultyTag extends ParseableTag {

    public WorldDifficultyTag(String format) {
        super(format);
    }

    @Override
    public String parseTag(Tag tag) {
        if (tag instanceof IntTag) {
            int difficulty = (int) tag.getValue();

            if (difficulty >= 0 && difficulty < Difficulty.values().length) {
                String difficultyType = Difficulty.values()[difficulty].name;
                return String.format(format, difficultyType);
            }
        }
        return "";
    }

}
