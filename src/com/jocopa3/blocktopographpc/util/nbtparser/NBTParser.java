/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbt;

import com.protolambda.blocktopograph.nbt.tags.*;
import java.util.LinkedList;

/**
 *
 * @author Matt
 */
public class NBTParser {
    
    public NBTParser() {
        
    }
    
    // Generate order-independent hash/signature
    // rootTag is the tag currently being hashed
    // parentTag is an optional parent to the rootTag
    public long getTagSignature(Tag rootTag, Tag parentTag) {
        LinkedList<Tag> stack = new LinkedList<>();
        
        if (parentTag != null) {
            stack.add(parentTag);
        }
        stack.add(rootTag);

        // Two signatures using two order-independent hash methods
        long sigA = 0; // stores hashes combined using addition
        long sigB = 0; // stores hashes combined using xor

        // Iterate over all tags and child tags
        while (!stack.isEmpty()) {
            Tag current = stack.remove();
            switch (current.getType()) {
                case COMPOUND:
                    for (Tag child : ((CompoundTag) current).getValue()) {
                        stack.add(child);
                    }
                    break;
                case LIST:
                    for (Tag child : ((ListTag) current).getValue()) {
                        stack.add(child);
                    }
                    break;
            }
            
            // Hash the name of the tag
            long hash = current.getName().hashCode();

            sigA += hash; // ADD
            sigB ^= hash | (hash << 32); // XOR 
        }

        // Generate full signature given sigA and sigB
        long signature = sigB + 0x9e3779b9 + (sigA << 6) + (sigA >> 2);

        return signature;
    }
}
