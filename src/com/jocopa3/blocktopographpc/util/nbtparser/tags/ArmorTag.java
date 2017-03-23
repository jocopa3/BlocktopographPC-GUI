/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser.tags;

import com.jocopa3.blocktopographpc.util.nbtparser.NBTSignature;
import com.protolambda.blocktopograph.map.Block;
import com.protolambda.blocktopograph.nbt.tags.CompoundTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;

/**
 *
 * @author Matt
 */
public class ArmorTag extends ParseableTag {

    public ArmorTag(String format) {
        super(format);
    }

    @Override
    public String parseTag(Tag tag) {
        if (tag instanceof CompoundTag) {
            int damage = 0, id = 0, count = 0;

            for (Tag child : ((CompoundTag) tag).getValue()) {
                switch (child.getName()) {
                    case "Count":
                        count = (byte) child.getValue();
                        break;
                    case "Damage":
                        damage = (short) child.getValue();
                        break;
                    case "id":
                        id = (short) child.getValue();
                        break;
                }
            }

            if (id == 0) {
                return "Empty";
            }

            Block block = Block.getBlock(id, damage);
            if (block == null || block.subName == null) {
                block = Block.getBlock(id, 0);

                if (block == null) {
                    return String.format(format, "unknown", damage);
                }

                return String.format(format, block.name, damage);
            }

            return String.format(format, block.subName + " " + block.name, count);
        }
        
        return "";
    }
}
