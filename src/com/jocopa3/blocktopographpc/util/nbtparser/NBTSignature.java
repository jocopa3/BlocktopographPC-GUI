/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser;

import com.jocopa3.blocktopographpc.util.nbtparser.tags.*;
import com.jocopa3.blocktopographpc.util.nbtparser.tags.ParseableTag;
import com.protolambda.blocktopograph.nbt.tags.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
/**
 *
 * @author Matt
 */
public enum NBTSignature {

    COMPOUND_ARMOR_ITEM(0x52E3D64C0750C698L, new ArmorTag("%s (%d)")), // Armor item that player is wearing
    COMPOUND_ATTRIBUTE_ITEM(0x913229C76E8C2F02L, new AttributeTag("%s: %.2f / %.2f")), // Player attribute item
    COMPOUND_INVENTORY_ITEM(0x61830298F8FEC600L, new InventoryItemTag("Slot %d: %s (%d)", true)), // Item in player inventory
    COMPOUND_INVENTORY_ITEM_DATA(0x6824D62C917ED96DL, new InventoryItemTag("Slot %d: %s (%d)", true)), // Item in player inventory which has additional data
    COMPOUND_INVENTORY_ITEM_BREWINGSTAND(0x51D68E7FF7653435L, new InventoryItemTag("Slot %d: %s (%d)", true)), // Item in brewing stand
    COMPOUND_ENTITY_ITEM(0x529848FA483EF34FL, new InventoryItemTag("%s (%d)", false)),

    INT_DIFFICULTY(0xC7C6C6CEDE7E60D7L, new WorldDifficultyTag("(%s)")), // World difficulty
    INT_DIMENSION_ID(0x3CB7F9367AC27DA4L, new PlayerDimensionTag("(%s)")), // Player's current dimension
    INT_GAMETYPE(0x79B8F6C886675FEEL, new WorldGameTypeTag("(%s)")), // World game type
    INT_GENERATOR(0x66C09C13DC5B83A5L, new WorldGeneratorTag("(%s)")), // World generator type
    INT_ENTITY_ID(0xD5A8B451A183F9B0L, new EntityIdTag("(%s)")),

    LIST_LAST_OPENED_WITH_VERSION(0x440EFC59B57028DDL, new LastOpenedVersionTag("%d.%d.%d.%d")), // Last played game version 
    LIST_POS_TAG(0xDFF9F43C33C551DDL, new Vec3Tag("X: %.2f | Y: %.2f | Z: %.2f")), // Position x, y, z tag
    LIST_MOTION_TAG(0x744BCCF90DE87EA4L, new Vec3Tag("X: %.2f | Y: %.2f | Z: %.2f")), // Position x, y, z tag
    LIST_ROTATION_TAG(0xDE33513CCD5398CEL, new Vec2Tag("Yaw: %.2f | Pitch: %.2f")),

    SHORT_ITEM_ID(0xD498B451A043F9B0L, new ItemIdTag("(%s)")), // Item id tag
    SHORT_ITEM_ENTITY_ID(0xD49AE344D06FA4C2L, new ItemIdTag("(%s)"));  // Entity item id tag
    //STRING_MAP_UUID_TAG(0xE6509328851E5, new ActionTag)
    private long signature;
    private ParseableTag tagParser;
    private static final HashMap<Long, NBTSignature> signatures;

    static {
        signatures = new HashMap<>();
        for (NBTSignature sig : NBTSignature.values()) {
            signatures.put(sig.signature, sig);
        }
    }

    NBTSignature(long signature, ParseableTag tagParser) {
        this.signature = signature;
        this.tagParser = tagParser;
    }

    public static NBTSignature get(long signature) {
        return signatures.get(signature);
    }
    
    public static long calculateTagSignature(Tag rootTag) {
        return calculateTagSignature(rootTag, null);
    }

    // Generate order-independent hash/signature
    // rootTag is the tag currently being hashed
    // parentTag is an optional parent to the rootTag
    public static long calculateTagSignature(Tag tag, Tag parentTag) {
        LinkedList<Tag> stack = new LinkedList<>();

        stack.add(tag);

        // Add immediate child tags to stack
        // Doesn't add tags deeper than 1 level down
        switch (tag.getType()) {
            case COMPOUND:
                for (Tag child : ((CompoundTag) tag).getValue()) {
                    stack.add(child);
                }
                break;
            case LIST:
                for (Tag child : ((ListTag) tag).getValue()) {
                    stack.add(child);
                }
                break;
        }

        // Two signatures using two order-independent hash methods
        long sigA = 0; // stores hashes combined using addition
        long sigB = 0; // stores hashes combined using xor
        long hash; // temporary variable which stores computed hashes

        // If parent tag is non-null, include it in the hash
        if (parentTag != null) {
            // Hash the name of the tag and convert to unsigned int
            // Parent tag should be hashed different from child tags
            hash = 0xd0ef + ((long) parentTag.getName().hashCode()) & 0xffffffffL;
            hash += (long) (parentTag.getType().id << 24); // Parent tag type in upper bits

            sigA += hash; // ADD
            sigB ^= hash; // XOR
        }

        // Iterate over all child tags
        while (!stack.isEmpty()) {
            Tag current = stack.remove();

            // Hash the name of the tag and convert to unsigned int
            hash = ((long) current.getName().hashCode()) & 0xffffffffL;
            hash += (long) (current.getType().id << 24); // Include tag type info in the upper bits

            sigA += hash; // ADD
            sigB ^= hash | (hash << 32); // XOR 
        }

        //System.out.format("%X, %X%n", sigA, sigB);
        // Generate full signature given sigA and sigB
        long signature = sigB + 0xd1d89955f542e43aL + (sigA << 28) + (sigA >>> 2);

        return signature;
    }
    
    public static long calculateTagSignatureB(Tag tag, Tag parentTag) {
        LinkedList<Tag> stack = new LinkedList<>();

        stack.add(tag);

        // Add immediate child tags to stack
        // Doesn't add tags deeper than 1 level down
        switch (tag.getType()) {
            case COMPOUND:
                for (Tag child : (ArrayList<Tag>)tag.getValue()) {
                    stack.add(child);
                }
                break;
        }

        // Two signatures using two order-independent hash methods
        long sigA = 0; // stores hashes combined using addition
        long sigB = 0; // stores hashes combined using xor

        // If parent tag is non-null, include it in the hash
        if (parentTag != null) {
            // Hash the name of the tag and convert to unsigned int
            // Parent tag should be hashed different from child tags
            long hash = 0xd0ef + ((long) parentTag.getName().hashCode()) & 0xffffffffL;
            hash += (long) (parentTag.getType().id << 24); // Parent tag type in lower bits

            sigA += hash; // ADD
            sigB ^= hash; // XOR
        }

        // Iterate over all child tags
        while (!stack.isEmpty()) {
            Tag current = stack.remove();

            // Hash the name of the tag and convert to unsigned int
            long hash = ((long) current.getName().hashCode()) & 0xffffffffL;
            hash += (long) (current.getType().id << 24); // Include tag type info in the upper bits

            sigA += hash; // ADD
            sigB ^= hash | (hash << 32); // XOR 
        }

        //System.out.format("%X, %X%n", sigA, sigB);
        // Generate full signature given sigA and sigB
        long signature = sigB + 0xd1d89955f542e43aL + (sigA << 28) + (sigA >>> 2);

        return signature;
    }

    public static String parseTagInfo(Tag tag, Tag parentTag) {
        return parseTagInfo(tag, get(calculateTagSignature(tag, parentTag)));
    }

    public static String parseTagInfo(Tag tag, long signature) {
        return parseTagInfo(tag, get(signature));
    }
    
    public static String parseTagInfo(Tag tag, NBTSignature signature) {
        if (signature == null) {
            return "";
        }
        
        try {
            return signature.tagParser.parseTag(tag);
        } catch (Exception e) {
            e.printStackTrace();
            
            return "";
        }
    }
    
    public static void main(String[] args) {
        
    }

}
