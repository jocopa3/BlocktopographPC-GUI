/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui.nbt;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 *
 * @author Matt
 */
public class AddNBTAction implements RevertableAction {

    //public AddNBTAction(JTree tree, TreePath[] paths)
    
    @Override
    public boolean undoAction() {
        try {
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }

    @Override
    public boolean redoAction() {
        try {
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
}
