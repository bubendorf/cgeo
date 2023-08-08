package cgeo.geocaching.maps.mapsforge;

import org.mapsforge.core.model.Tile;
import org.mapsforge.map.layer.download.tilesource.AbstractTileSource;

import java.net.MalformedURLException;
import java.net.URL;

public class TileSourceSwissImage extends AbstractTileSource {
    /**
     * https://wmts.geo.admin.ch/1.0.0/ch.swisstopo.swissimage/default/current/3857/{z}/{x}/{y}.jpeg
     */
    public static final TileSourceSwissImage INSTANCE = new TileSourceSwissImage(new String[]{
            "wmts.geo.admin.ch" }, 443);
    private static final int PARALLEL_REQUESTS_LIMIT = 8;
    private static final String PROTOCOL = "https";
    private static final int ZOOM_LEVEL_MAX = 20;
    private static final int ZOOM_LEVEL_MIN = 8;

    public TileSourceSwissImage(final String[] hostNames, final int port) {
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
        return new URL(PROTOCOL, getHostName(), port, "/1.0.0/ch.swisstopo.swissimage/default/current/3857/" + tile.zoomLevel + '/' + tile.tileX + '/' + tile.tileY + ".jpeg");
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

