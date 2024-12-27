package cgeo.geocaching.unifiedmap.tileproviders;

import android.net.Uri;

import androidx.core.util.Pair;

import cgeo.geocaching.CgeoApplication;
import cgeo.geocaching.R;

class SwissTopoSource extends AbstractMapsforgeOnlineTileProvider {
    SwissTopoSource() {
        super("Swiss Topo",
                Uri.parse("https://mp.bubendorf.net"),
                "/swissproxy/tiles/tp/{Z}/{X}/{Y}.jpeg",
                4, 19,
                new Pair<>("", true));
    }
}
