/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui.map;

import com.protolambda.blocktopograph.chunk.ChunkManager;
import com.protolambda.blocktopograph.map.renderer.MapType;
import java.util.Objects;
import org.jxmapviewer.viewer.Tile;

/**
 *
 * @author Matt
 */
public class MCTile extends Tile {
    
    private ChunkManager cm;
    private MapType map;
    
    public MCTile(int x, int y, int zoom, ChunkManager cm, MapType map) {
        super(x, y, zoom);
        
        this.cm = cm;
        this.map = map;
    }
    
    public ChunkManager getChunkManager() {
        return cm;
    }
    
    public MapType getMapType() {
        return map;
    }
    
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 67 * hash + Objects.hashCode(this.cm);
        hash = 67 * hash + Objects.hashCode(this.map);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MCTile other = (MCTile) obj;
        if (!Objects.equals(this.cm, other.cm)) {
            return false;
        }
        if (this.map != other.map) {
            return false;
        }
        return true;
    }
}
