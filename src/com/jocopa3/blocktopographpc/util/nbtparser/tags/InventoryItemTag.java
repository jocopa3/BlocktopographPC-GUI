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
public class InventoryItemTag extends ParseableTag {

    private boolean showSlot;

    public InventoryItemTag(String format, boolean showSlot) {
        super(format);
        this.showSlot = showSlot;
    }

    @Override
    public String parseTag(Tag tag) {
        if (tag instanceof CompoundTag) {
            int slot = 0, id = 0, count = 0, damage = 0;

            for (Tag child : ((CompoundTag) tag).getValue()) {
                switch (child.getName()) {
                    case "Count":
                        count = (byte) child.getValue();
                        break;
                    case "Damage":
                        damage = (short) child.getValue();
                        break;
                    case "Slot":
                        slot = (byte) child.getValue();
                        break;
                    case "id":
                        id = (short) child.getValue();
                        break;
                }
            }

            String blockString = "";

            if (showSlot && count <= 0) {
                blockString = "Empty";
            } else {
                Block block = Block.getBlock(id, damage);

                if (block == null || block.subName == null) {
                    block = Block.getBlock(id, 0);

                    if (block == null) {
                        blockString = "unknown";
                    } else {

                        blockString = block.name;
                    }
                } else {
                    blockString = block.subName + " " + block.name;
                }
            }
            
            if (showSlot) {
                return String.format(format, slot, blockString, count);
            } else {
                return String.format(format, blockString, count);
            }
        }

        return "";
    }

}
