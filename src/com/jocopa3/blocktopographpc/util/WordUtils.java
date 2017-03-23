/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util;

/**
 *
 * @author Matt
 */
public class WordUtils {

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        char underscore = '_';
        char space = ' ';
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == underscore) {
                chars[i] = space;
            }

            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i])) {
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static String whitespaceBeforeUppercase(String string) {
        char[] chars = string.toCharArray();
        StringBuilder charBuilder = new StringBuilder();
        charBuilder.append(chars[0]);

        for (int i = 1; i < chars.length; i++) {
            if (Character.isUpperCase(chars[i])) {
                charBuilder.append(" ");
            }
            charBuilder.append(chars[i]);
        }

        return charBuilder.toString();
    }
}
