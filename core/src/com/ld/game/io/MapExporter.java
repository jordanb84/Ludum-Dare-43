package com.ld.game.io;

import com.badlogic.gdx.files.FileHandle;
import com.ld.game.map.Map;
import com.ld.game.map.MapLayer;
import com.ld.game.tile.TileType;

public class MapExporter {

    public static void exportMap(Map map, FileHandle destination) {
        StringBuffer buffer = new StringBuffer();

        for(MapLayer mapLayer : map.getLayers()) {
            for(TileType tileType : mapLayer.getTiles()) {
                buffer.append(tileType.ordinal() + ",");
            }
            buffer.append("\n");
        }

        destination.writeString(buffer.toString(), false);
    }

}
