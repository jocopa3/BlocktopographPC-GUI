/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser;

import com.jocopa3.blocktopographpc.util.WorldListUtil;
import com.protolambda.blocktopograph.map.Block;
import com.protolambda.blocktopograph.map.Difficulty;
import com.protolambda.blocktopograph.map.Dimension;
import com.protolambda.blocktopograph.map.GameMode;
import com.protolambda.blocktopograph.map.Generator;
import com.protolambda.blocktopograph.nbt.tags.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Matt
 */
public enum NBTSignature {

    /*
    // Armor item that player is wearing
    COMPOUND_ARMOR_ITEM(0x52E3D64C0750C698L, "$id ($Damage)",
            new NBTParameter[]{
                new NBTParameter("Count", Byte.class),
                new NBTParameter("Damage", Short.class),
                new NBTParameter("id", Short.class)
            }); 
     */
    
    // TODO: split this up into individual classes for each parser
    COMPOUND_ARMOR_ITEM(0x52E3D64C0750C698L, "%s (%d)"), // Armor item that player is wearing
    COMPOUND_ATTRIBUTE_ITEM(0x913229C76E8C2F02L, "%s: %.2f / %.2f"), // Player attribute item
    COMPOUND_INVENTORY_ITEM(0x61830298F8FEC600L, "Slot %d: %s (%d)"), // Item in player inventory
    COMPOUND_INVENTORY_ITEM_DATA(0x6824D62C917ED96DL, "Slot %d: %s (%d)"), // Item in player inventory

    INT_DIFFICULTY(0xC7C6C6CEDE7E60D7L, "(%s)"), // World difficulty
    INT_DIMENSION_ID(0x3CB7F9367AC27DA4L, "(%s)"), // Player's current dimension
    INT_GAMETYPE(0x79B8F6C886675FEEL, "(%s)"), // World game type
    INT_GENERATOR(0x66C09C13DC5B83A5L, "(%s)"), // World generator type

    LIST_LAST_OPENED_WITH_VERSION(0x440EFC59B57028DDL, "%d.%d.%d.%d"), // Last played game version 
    LIST_POS_TAG(0xDFF9F43C33C551DDL, "X: %.2f | Y: %.2f | Z: %.2f"), // Position x, y, z tag

    SHORT_ITEM_ID(0xD498B451A043F9B0L, "(%s)"); // Item id tag

    /*
    public static String parseTagInfo(Tag tag, NBTSignature signature) {
        if (signature == null) {
            return "";
        }

        String parsedInfo = signature.format;

        switch (signature) {
            case COMPOUND_ARMOR_ITEM:
                if (tag instanceof CompoundTag) {
                    int count = 0, damage = 0, id = 0;
                    for (Tag child : ((CompoundTag) tag).getValue()) {
                        Class type = signature.parameterList.getType(child.getName());
                        
                        if(type.isInstance(child.getValue())) {
                            
                        }
                    }
                }
                break;
        }

        return parsedInfo;
    }
     */
    public static String parseTagInfo(Tag tag, NBTSignature signature) {
        if (signature == null) {
            return "";
        }
        try {
            switch (signature) {
                case COMPOUND_ARMOR_ITEM:

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
                                return String.format(signature.format, "unknown", damage);
                            }

                            return String.format(signature.format, block.name, damage);
                        }

                        return String.format(signature.format, block.subName + " " + block.name, count);
                    }
                    break;
                case COMPOUND_ATTRIBUTE_ITEM:
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

                        return String.format(signature.format, Name, current, max);
                    }
                    break;
                case COMPOUND_INVENTORY_ITEM_DATA:
                case COMPOUND_INVENTORY_ITEM:
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

                        if (count == -1) {
                            return "Slot " + slot + ": Empty";
                        }

                        Block block = Block.getBlock(id, damage);
                        if (block == null || block.subName == null) {
                            block = Block.getBlock(id, 0);

                            if (block == null) {
                                return String.format(signature.format, slot, "unknown", count);
                            }

                            return String.format(signature.format, slot, block.name, count);
                        }

                        return String.format(signature.format, slot, block.subName + " " + block.name, count);
                    }
                    break;
                case INT_DIFFICULTY:
                    if (tag instanceof IntTag) {
                        int difficulty = (int) tag.getValue();

                        if (difficulty >= 0 && difficulty < Difficulty.values().length) {
                            String difficultyType = Difficulty.values()[difficulty].name;
                            return String.format(signature.format, difficultyType);
                        }
                    }
                    break;
                case INT_DIMENSION_ID:
                    if (tag instanceof IntTag) {
                        String dimensionName = Dimension.getDimension((int) tag.getValue()).name;

                        if (!dimensionName.equals("Null")) {
                            return String.format(signature.format, dimensionName);
                        }
                    }
                    break;
                case INT_GAMETYPE:
                    if (tag instanceof IntTag) {
                        int gamemode = (int) tag.getValue();

                        if (gamemode >= 0 && gamemode < GameMode.values().length) {
                            String gameType = GameMode.values()[gamemode].name;
                            return String.format(signature.format, gameType);
                        }
                    }
                    break;
                case INT_GENERATOR:
                    if (tag instanceof IntTag) {
                        int generator = (int) tag.getValue();

                        if (generator >= 0 && generator < Generator.values().length) {
                            String gameType = Generator.values()[generator].name;
                            return String.format(signature.format, gameType);
                        }
                    }
                    break;
                case LIST_LAST_OPENED_WITH_VERSION:
                    if (tag instanceof ListTag) {
                        ArrayList<Tag> values = ((ListTag) tag).getValue();
                        int major = ((IntTag) (values.get(0))).getValue(),
                                minor = ((IntTag) (values.get(1))).getValue(),
                                patch = ((IntTag) (values.get(2))).getValue(),
                                beta = ((IntTag) (values.get(3))).getValue();

                        return String.format(signature.format, major, minor, patch, beta);
                    }
                    break;
                case LIST_POS_TAG:
                    if (tag instanceof ListTag) {
                        ArrayList<Tag> values = ((ListTag) tag).getValue();
                        float x = ((FloatTag) (values.get(0))).getValue(),
                                y = ((FloatTag) (values.get(1))).getValue(),
                                z = ((FloatTag) (values.get(2))).getValue();

                        return String.format(signature.format, x, y, z);
                    }
                    break;
                case SHORT_ITEM_ID:
                    if (tag instanceof ShortTag) {
                        short id = ((ShortTag) tag).getValue();

                        Block block = Block.getBlock(id, 0);
                        if (block == null) {
                            return String.format(signature.format, "unknown");
                        }

                        return String.format(signature.format, block.name);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String format, emptyFormat;
    private long signature;
    private NBTParameterList parameterList;
    private static final HashMap<Long, NBTSignature> signatures;

    static {
        signatures = new HashMap<>();
        for (NBTSignature sig : NBTSignature.values()) {
            signatures.put(sig.signature, sig);
        }
    }

    /*
    NBTSignature(long signature, String stringFormat, NBTParameter[] parameters) {
        this(signature, stringFormat, "", new NBTParameterList(parameters));
    }

    NBTSignature(long signature, String stringFormat, String emptyFormat, NBTParameter[] parameters) {
        this(signature, stringFormat, emptyFormat, new NBTParameterList(parameters));
    }

    NBTSignature(long signature, String stringFormat, NBTParameterList parameters) {
        this(signature, stringFormat, "", parameters);
    }

    NBTSignature(long signature, String stringFormat, String emptyFormat, NBTParameterList parameters) {
        this.signature = signature;
        this.parameterList = parameters;
        this.format = stringFormat;
        this.emptyFormat = emptyFormat;
    }
     */
    NBTSignature(long signature, String stringFormat) {
        this.signature = signature;
        this.format = stringFormat;
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
            hash += (long) (parentTag.getType().id << 24); // Parent tag type in lower bits

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

    public static String parseTagInfo(Tag tag, Tag parentTag) {
        return parseTagInfo(tag, get(calculateTagSignature(tag, parentTag)));
    }

    public static String parseTagInfo(Tag tag, long signature) {
        return parseTagInfo(tag, get(signature));
    }
}
