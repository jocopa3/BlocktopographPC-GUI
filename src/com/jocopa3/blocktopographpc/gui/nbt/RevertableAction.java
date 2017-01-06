/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui.nbt;

/**
 *
 * @author Matt
 */
public interface RevertableAction {
    public boolean undoAction();
    public boolean redoAction();
}
