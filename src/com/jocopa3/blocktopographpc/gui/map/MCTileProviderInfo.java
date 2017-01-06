/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jocopa3.blocktopographpc.gui.map;

import com.protolambda.blocktopograph.world.WorldProvider;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 *
 * @author Matt
 */
public class MCTileProviderInfo extends TileFactoryInfo {

    private final static int TOP_ZOOM_LEVEL = 5;

    private final static int MAX_ZOOM_LEVEL = 5;

    private final static int MIN_ZOOM_LEVEL = 1;

    private final static int TILE_SIZE = 256;
    private final static int[] TILE_SIZES = new int[]{16};

    WorldProvider worldProvider;

    public MCTileProviderInfo(WorldProvider worldProvider) {
        super("Minecraft Map", MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL, TOP_ZOOM_LEVEL, TILE_SIZE, false, false, "", "", "", "");
        this.worldProvider = worldProvider;
    }

    @Override
    public String getTileUrl(final int x, final int y, final int zoom) {
        return "";
    }

    /**
     * Get the tile size.
     *
     * @param zoom the zoom level
     * @return the tile size
     */
    @Override
    public int getTileSize(int zoom) {
        return TILE_SIZE;
        /*
        if(zoom > TILE_SIZES.length)
            return TILE_SIZES[TILE_SIZES.length-1];
        else if (zoom <= 0)
            return 256;
        
        return TILE_SIZES[zoom-1];
        */
    }
}
