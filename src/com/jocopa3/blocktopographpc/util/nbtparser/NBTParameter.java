/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser;

/**
 *
 * @author Matt
 */
public class NBTParameter {

    private String name;
    private Class type;

    protected NBTParameter(String name, Class type) {
        this.name = name;
        this.type = type;
    }
    
    protected String getName() {
        return name;
    }
    
    protected Class getType() {
        return type;
    }
}
