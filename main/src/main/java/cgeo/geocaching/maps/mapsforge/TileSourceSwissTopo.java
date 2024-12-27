package cgeo.geocaching.maps.mapsforge;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.download.tilesource.AbstractTileSource;

import java.net.MalformedURLException;
import java.net.URL;

public class TileSourceSwissTopo extends AbstractTileSource {
    /**
     */
    public static final TileSourceSwissTopo INSTANCE = new TileSourceSwissTopo(new String[]{
            "mp.bubendorf.net" }, 443);
    private static final int PARALLEL_REQUESTS_LIMIT = 8;
    private static final String PROTOCOL = "https";
    private static final int ZOOM_LEVEL_MAX = 19;
    private static final int ZOOM_LEVEL_MIN = 4;

    public TileSourceSwissTopo(final String[] hostNames, final int port) {
        super(hostNames, port);
        /* Default TTL: 7 days */
        defaultTimeToLive = 604800000;
    }

    @Override
    public int getParallelRequestsLimit() {
        return PARALLEL_REQUESTS_LIMIT;
    }

    @Override
    public URL getTileUrl(final Tile tile) throws MalformedURLException {
        return new URL(PROTOCOL, getHostName(), port, "/swissproxy/tiles/tp/" + tile.zoomLevel + '/' + tile.tileX + '/' + tile.tileY + ".jpeg");
    }

    @Override
    public byte getZoomLevelMax() {
        return ZOOM_LEVEL_MAX;
    }

    @Override
    public byte getZoomLevelMin() {
        return ZOOM_LEVEL_MIN;
    }

    @Override
    public boolean hasAlpha() {
        return false;
    }

}

