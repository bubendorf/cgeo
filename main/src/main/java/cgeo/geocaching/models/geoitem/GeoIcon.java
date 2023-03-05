package cgeo.geocaching.models.geoitem;

import cgeo.geocaching.location.Geopoint;
import cgeo.geocaching.location.Viewport;
import cgeo.geocaching.utils.ImageUtils;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Immutable value class for GeoItem Style info. Includes some helpers to deal with these objects.
 */
public class GeoIcon implements Parcelable {

    @Nullable private final BitmapProvider bitmapProvider;
    private final float xAnchor;
    private final float yAnchor;
    private final float angle;

    /**
     * complex image caches etc may implement this interface on their own
     * Implementors shall also implement Parcelable as well as equals() and hashCode()
     */
    public interface BitmapProvider extends Parcelable {

        Bitmap getBitmap();

        default Bitmap getRotatedBitmap(float angleInDegree) {
            return ImageUtils.rotateBitmap(getBitmap(), angleInDegree);
        }
    }

    public static class SimpleBitmapProvider implements BitmapProvider {

        private final Bitmap bitmap;

        public SimpleBitmapProvider(final Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public Bitmap getBitmap() {
            return bitmap;
        }

        // Parcelable stuff

        public SimpleBitmapProvider(final Parcel in) {
            this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(final Parcel dest, final int flags) {
            dest.writeParcelable(bitmap, flags);
        }

        public static final Creator<SimpleBitmapProvider> CREATOR = new Creator<SimpleBitmapProvider>() {
            @Override
            public SimpleBitmapProvider createFromParcel(final Parcel in) {
                return new SimpleBitmapProvider(in);
            }

            @Override
            public SimpleBitmapProvider[] newArray(final int size) {
                return new SimpleBitmapProvider[size];
            }
        };

        // equals/hashCode stuff
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof SimpleBitmapProvider)) {
                return false;
            }
            return Objects.equals(bitmap, ((SimpleBitmapProvider) o).bitmap);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(bitmap);
        }
    }

    /**
     * Hotspots are described from perspective of the icon image! E.g. LOWER_RIGHT_CORNER
     * means that the lower right corner of the icon image is placed upon the Geoitems lat/lon position
     */
    public enum Hotspot {
        CENTER(0.5f, 0.5f),
        BOTTOM_CENTER(0.5f, 1f),
        TOP_CENTER(0.5f, 0f),
        RIGHT_CENTER(1f, 0.5f),
        LEFT_CENTER(0f, 0.5f),
        UPPER_RIGHT_CORNER(1f, 0f),
        LOWER_RIGHT_CORNER(1f, 1f),
        UPPER_LEFT_CORNER(0f, 0f),
        LOWER_LEFT_CORNER(0f, 1f);

        public final float xAnchor;
        public final float yAnchor;

        Hotspot(final float xAnchor, final float yAnchor) {
            this.xAnchor = xAnchor;
            this.yAnchor = yAnchor;
        }

    }

    private GeoIcon(@Nullable final BitmapProvider bitmapProvider, final float xAnchor, final float yAnchor, final float angle) {
        this.bitmapProvider = bitmapProvider;
        this.xAnchor = xAnchor;
        this.yAnchor = yAnchor;
        this.angle = angle;
    }

    @Nullable
    public Bitmap getBitmap() {
        return bitmapProvider == null ? null : bitmapProvider.getBitmap();
    }

    @Nullable
    public Bitmap getRotatedBitmap() {
        return bitmapProvider == null ? null : bitmapProvider.getRotatedBitmap(getAngle());
    }


    public float getXAnchor() {
        return xAnchor;
    }

    public float getYAnchor() {
        return yAnchor;
    }

    public float getAngle() {
        return angle;
    }

    public Viewport getViewport(final Geopoint base, final float yPerLat, final float xPerLon) {
        final Bitmap bm = getBitmap();
        if (bm == null) {
            return null;
        }
        final int h = bm.getHeight();
        final int w = bm.getWidth();
        final int minLat = base.getLatitudeE6() + (int) (h * (yAnchor - 0.5f) / yPerLat);
        final int maxLat = base.getLatitudeE6() + (int) (h * (yAnchor + 0.5f) / yPerLat);
        final int minLon = base.getLongitudeE6() + (int) (w * (xAnchor - 0.5f) / xPerLon);
        final int maxLon = base.getLongitudeE6() + (int) (w * (xAnchor + 0.5f) / xPerLon);

        return new Viewport(new Geopoint(minLat, minLon), new Geopoint(maxLat, maxLon));
    }

    public static Builder builder() {
        return new Builder();
    }


    public Builder buildUpon() {
        return builder().setBitmapProvider(bitmapProvider).setXAnchor(xAnchor).setYAnchor(yAnchor).setAngle(angle);
    }

    //equals/hashCode

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof GeoIcon)) {
            return false;
        }
        final GeoIcon other = (GeoIcon) o;
        return
            Objects.equals(bitmapProvider, other.bitmapProvider) &&
            Objects.equals(xAnchor, other.xAnchor) &&
            Objects.equals(yAnchor, other.yAnchor) &&
            Objects.equals(angle, other.angle);
    }

    @Override
    public int hashCode() {
        return (bitmapProvider == null ? 7 : bitmapProvider.hashCode()) ^ (int) angle;
    }


    //Builder stuff

    public static class Builder {

        @Nullable private BitmapProvider bitmapProvider;
        private float xAnchor;
        private float yAnchor;
        private float angle;

        private Builder() {
            setHotspot(Hotspot.CENTER);
        }

        public Builder setBitmap(@Nullable final Bitmap bitmap) {
            return setBitmapProvider(new SimpleBitmapProvider(bitmap));
        }

        public Builder setBitmapProvider(@Nullable final BitmapProvider bitmapProvider) {
            this.bitmapProvider = bitmapProvider;
            return this;
        }

        public Builder setXAnchor(final float xAnchor) {
            this.xAnchor = xAnchor;
            return this;
        }

        public Builder setHotspot(final Hotspot hotspot) {
            this.xAnchor = hotspot.xAnchor;
            this.yAnchor = hotspot.yAnchor;
            return this;
        }

        public Builder setYAnchor(final float yAnchor) {
            this.yAnchor = yAnchor;
            return this;
        }

        public Builder setAngle(final float angle) {
            this.angle = angle;
            return this;
        }

        public GeoIcon build() {
            return new GeoIcon(bitmapProvider, xAnchor, yAnchor, angle);
        }
    }

    // parcelable stuff

    protected GeoIcon(final Parcel in) {
        bitmapProvider = in.readParcelable(BitmapProvider.class.getClassLoader());
        xAnchor = in.readFloat();
        yAnchor = in.readFloat();
        angle = in.readFloat();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeParcelable(bitmapProvider, flags);
        dest.writeFloat(xAnchor);
        dest.writeFloat(yAnchor);
        dest.writeFloat(angle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GeoIcon> CREATOR = new Creator<GeoIcon>() {
        @Override
        public GeoIcon createFromParcel(final Parcel in) {
            return new GeoIcon(in);
        }

        @Override
        public GeoIcon[] newArray(final int size) {
            return new GeoIcon[size];
        }
    };
}
