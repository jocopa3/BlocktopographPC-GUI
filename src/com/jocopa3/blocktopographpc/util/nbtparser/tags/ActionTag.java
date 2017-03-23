/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser.tags;

/**
 *
 * @author Matt
 */
public abstract class ActionTag extends ParseableTag {
    
    public ActionTag(String format) {
        super(format);
    }
    
    public abstract void doAction();
}
