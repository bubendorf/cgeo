package cgeo.geocaching.unifiedmap.tileproviders;

import android.net.Uri;

import androidx.core.util.Pair;

class SwissImageSource extends AbstractMapsforgeOnlineTileProvider {
    SwissImageSource() {
        super("Swiss Image",
                Uri.parse("https://wmts.geo.admin.ch"),
                "/1.0.0/ch.swisstopo.swissimage/default/current/3857/{Z}/{X}/{Y}.jpeg",
                8, 20,
                new Pair<>("", true));
    }
}
