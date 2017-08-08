package org.openstreetmap.atlas.geography.atlas.pbf;

import java.util.HashMap;

import org.openstreetmap.atlas.geography.atlas.builder.store.AtlasPrimitiveObjectStore;
import org.openstreetmap.atlas.geography.atlas.pbf.converters.AtlasPrimitiveAreaToOsmosisWayConverter;
import org.openstreetmap.atlas.geography.atlas.pbf.converters.AtlasPrimitiveLineItemToOsmosisWayConverter;
import org.openstreetmap.atlas.geography.atlas.pbf.converters.AtlasPrimitiveLocationItemToOsmosisNodeConverter;
import org.openstreetmap.atlas.geography.atlas.pbf.converters.AtlasPrimitiveRelationToOsmosisRelationConverter;
import org.openstreetmap.atlas.geography.atlas.pbf.converters.LocationToOsmosisNodeConverter;
import org.openstreetmap.atlas.streaming.StringInputStream;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import crosby.binary.osmosis.OsmosisReader;

/**
 * Mock an Osmosis reader to be able to test without any PBF resource.
 *
 * @author matthieun
 */
public class OsmosisReaderMock extends OsmosisReader
{
    private final AtlasPrimitiveObjectStore source;
    private Sink sink;

    public OsmosisReaderMock(final AtlasPrimitiveObjectStore source)
    {
        super(new StringInputStream(""));
        this.source = source;
    }

    @Override
    public void run()
    {
        this.sink.initialize(new HashMap<>());
        // Call the nodes
        this.source.getNodes().forEach((identifier, node) -> this.sink.process(new NodeContainer(
                new AtlasPrimitiveLocationItemToOsmosisNodeConverter().convert(node))));
        this.source.getPoints().forEach((identifier, point) -> this.sink.process(new NodeContainer(
                new AtlasPrimitiveLocationItemToOsmosisNodeConverter().convert(point))));
        this.source.getEdges().forEach((identifier, edge) ->
        {
            edge.getPolyLine().innerLocations().forEach(location -> this.sink.process(
                    new NodeContainer(new LocationToOsmosisNodeConverter().convert(location))));
        });
        this.source.getLines().forEach((identifier, edge) ->
        {
            edge.getPolyLine().forEach(location -> this.sink.process(
                    new NodeContainer(new LocationToOsmosisNodeConverter().convert(location))));
        });
        this.source.getAreas().forEach((identifier, area) ->
        {
            area.getPolygon().forEach(location -> this.sink.process(
                    new NodeContainer(new LocationToOsmosisNodeConverter().convert(location))));
        });
        // Call the ways
        this.source.getEdges().forEach((identifier, edge) -> this.sink.process(
                new WayContainer(new AtlasPrimitiveLineItemToOsmosisWayConverter().convert(edge))));
        this.source.getLines().forEach((identifier, line) -> this.sink.process(
                new WayContainer(new AtlasPrimitiveLineItemToOsmosisWayConverter().convert(line))));
        this.source.getAreas().forEach((identifier, area) -> this.sink.process(
                new WayContainer(new AtlasPrimitiveAreaToOsmosisWayConverter().convert(area))));
        // Call the relations
        this.source.getRelations()
                .forEach((identifier, relation) -> this.sink.process(new RelationContainer(
                        new AtlasPrimitiveRelationToOsmosisRelationConverter().convert(relation))));
        // Close
        this.sink.release();
        this.sink.complete();
    }

    @Override
    public void setSink(final Sink sink)
    {
        this.sink = sink;
    }
}
