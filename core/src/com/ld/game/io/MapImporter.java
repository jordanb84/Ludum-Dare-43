package com.ld.game.io;

import com.badlogic.gdx.files.FileHandle;
import com.ld.game.map.Map;
import com.ld.game.map.MapDefinition;
import com.ld.game.map.MapLayer;
import com.ld.game.tile.TileType;

import java.util.ArrayList;
import java.util.List;

public class MapImporter {

    public static Map importMap(FileHandle location, MapDefinition mapDefinition, boolean generateJourneys) {
        String mapFileContents = location.readString();

        String[] layersRaw = mapFileContents.split("\n");

        List<MapLayer> mapLayers = new ArrayList<MapLayer>();

        for(int layer = 0; layer < layersRaw.length; layer++) {
            List<TileType> layerTiles = new ArrayList<TileType>();
            //char[] layerContents = layersRaw[layer].toCharArray();
            String[] layerContents = layersRaw[layer].split(",");

            for(String tile : layerContents) {
                TileType type = TileType.values()[Integer.parseInt("" + tile)];
                layerTiles.add(type);
            }

            mapLayers.add(new MapLayer(layerTiles, mapDefinition));
        }

        return new Map(mapLayers, mapDefinition, generateJourneys);
    }

}
