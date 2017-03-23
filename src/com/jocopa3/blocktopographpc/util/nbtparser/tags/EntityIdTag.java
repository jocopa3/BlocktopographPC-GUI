/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser.tags;

import com.jocopa3.blocktopographpc.util.nbtparser.NBTSignature;
import com.protolambda.blocktopograph.map.Entity;
import com.protolambda.blocktopograph.nbt.tags.IntTag;
import com.protolambda.blocktopograph.nbt.tags.Tag;

/**
 *
 * @author Matt
 */
public class EntityIdTag extends ParseableTag {

    public EntityIdTag(String format) {
        super(format);
    }

    @Override
    public String parseTag(Tag tag) {
        if (tag instanceof IntTag) {
            int id = ((IntTag) tag).getValue();

            Entity entity = Entity.getEntity(id & 0xFF);
            if (entity == null) {
                return String.format(format, Entity.UNKNOWN.displayName);
            }

            return String.format(format, entity.displayName);
        }
        return "";
    }
    
}
