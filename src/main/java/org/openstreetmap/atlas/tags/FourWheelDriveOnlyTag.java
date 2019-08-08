package org.openstreetmap.atlas.tags;

import org.openstreetmap.atlas.tags.annotations.Tag;
import org.openstreetmap.atlas.tags.annotations.TagKey;

/**
 * This tag denotes roads that should only be driven on by vehicles wit 4-wheel drive
 *
 * @author ameliaewatts
 */
@Tag(taginfo = "https://taginfo.openstreetmap.org/keys/?key=4wd_only", osm = "https://wiki.openstreetmap.org/wiki/Key:4wd_only")
public enum FourWheelDriveOnlyTag /**valid tags*/
{
    YES,
    NO;

    @TagKey
    public static final String KEY = "4wd_only";
}
