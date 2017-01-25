/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.util.nbtparser;

import java.util.HashMap;

/**
 *
 * @author Matt
 */
public class NBTParameterList {
    
    private HashMap<String, Class> parameters;
    
    protected NBTParameterList(NBTParameter[] parameterList) {
        parameters = new HashMap<>();
        
        for(NBTParameter param : parameterList) {
            parameters.putIfAbsent(param.getName(), param.getType());
        }
    }
    
    protected Class getType(String parameterName) {
        return parameters.get(parameterName);
    }
}
