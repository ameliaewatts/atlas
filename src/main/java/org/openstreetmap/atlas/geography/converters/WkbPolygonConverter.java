package org.openstreetmap.atlas.geography.converters;

import org.openstreetmap.atlas.exception.CoreException;
import org.openstreetmap.atlas.geography.Polygon;
import org.openstreetmap.atlas.geography.converters.jts.JtsPolygonConverter;
import org.openstreetmap.atlas.utilities.conversion.TwoWayConverter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;

/**
 * Convert Polygons to a Well Known Binary (WKB) byte array. Polygons retain only one of first
 * versus last point (which are the same for a closed loop).
 *
 * @author Sid
 * @author cstaylor
 */
public class WkbPolygonConverter implements TwoWayConverter<Polygon, byte[]>
{
    @Override
    public Polygon backwardConvert(final byte[] wkb)
    {
        com.vividsolutions.jts.geom.Polygon geometry = null;
        final WKBReader myReader = new WKBReader();
        try
        {
            geometry = (com.vividsolutions.jts.geom.Polygon) myReader.read(wkb);
        }
        catch (final ParseException | ClassCastException e)
        {
            throw new CoreException("Cannot parse wkb : {}", WKBWriter.toHex(wkb));
        }
        return new JtsPolygonConverter().backwardConvert(geometry);
    }

    @Override
    public byte[] convert(final Polygon polygon)
    {
        final Geometry geometry = new JtsPolygonConverter().convert(polygon);
        final byte[] wkb = new WKBWriter().write(geometry);
        return wkb;
    }
}
