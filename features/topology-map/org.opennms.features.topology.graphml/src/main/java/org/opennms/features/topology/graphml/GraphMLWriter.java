/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2016 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2016 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.features.topology.graphml;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXB;

import org.graphdrawing.graphml.xmlns.DataType;
import org.graphdrawing.graphml.xmlns.EdgeType;
import org.graphdrawing.graphml.xmlns.GraphType;
import org.graphdrawing.graphml.xmlns.GraphmlType;
import org.graphdrawing.graphml.xmlns.KeyForType;
import org.graphdrawing.graphml.xmlns.KeyType;
import org.graphdrawing.graphml.xmlns.KeyTypeType;
import org.graphdrawing.graphml.xmlns.NodeType;

public class GraphMLWriter {

    public static interface ProcessHook {
        void process(GraphML input, GraphmlType result);
    }

    private static interface DataTypeAddCallback {
        void addData(DataType dataType);
    }

    public static void write(GraphML graphML, File file, ProcessHook... hooks) throws InvalidGraphException {
        GraphmlType graphmlType = convert(graphML);
        if (hooks != null) {
            for (ProcessHook eachHook : hooks) {
                eachHook.process(graphML, graphmlType);
            }
        }
        JAXB.marshal(graphmlType, file);
    }

    private static GraphmlType convert(GraphML graphML) throws InvalidGraphException {
        GraphmlType graphmlType = new GraphmlType();
        addProperties(graphmlType, KeyForType.GRAPHML, graphML, dataType -> graphmlType.getGraphOrData().add(dataType));

        for (GraphMLGraph eachGraph : graphML.getGraphs()) {
            GraphType graphType = new GraphType();
            graphType.setId(eachGraph.getId());
            addProperties(graphmlType, KeyForType.GRAPH, eachGraph, dataType -> graphType.getDataOrNodeOrEdge().add(dataType));

            for (GraphMLNode eachNode : eachGraph.getNodes()) {
                NodeType nodeType = new NodeType();
                nodeType.setId(eachNode.getId());
                graphType.getDataOrNodeOrEdge().add(nodeType);
                addProperties(graphmlType, KeyForType.NODE, eachNode, dataType -> nodeType.getDataOrPort().add(dataType));
            }

            for (GraphMLEdge eachEdge : eachGraph.getEdges()) {
                EdgeType edgeType = new EdgeType();
                edgeType.setId(eachEdge.getId());
                graphType.getDataOrNodeOrEdge().add(edgeType);
                addProperties(graphmlType, KeyForType.EDGE, eachEdge, dataType -> edgeType.getData().add(dataType));
            }

            graphmlType.getGraphOrData().add(graphType);
        }
        return graphmlType;
    }

    private static void addProperties(GraphmlType graphmlType, KeyForType keyForType, GraphMLElement element, DataTypeAddCallback callback) throws InvalidGraphException {
        for (Map.Entry<String, Object> eachEntry : element.getProperties().entrySet()) {
            if (eachEntry.getKey().equals("id")) { // skip IDs
                continue;
            }
            List<KeyType> definedKeys = graphmlType.getKey().stream()
                    .filter(eachKey -> eachKey.getFor() == keyForType)
                    .filter(eachKey -> eachKey.getId().equals(eachEntry.getKey()))
                    .collect(Collectors.toList());
            if (definedKeys.isEmpty()) {
                KeyType keyType = new KeyType();
                keyType.setFor(keyForType);
                keyType.setId(eachEntry.getKey());
                keyType.setAttrName(eachEntry.getKey());
                keyType.setAttrType(parseType(eachEntry.getValue()));
                graphmlType.getKey().add(keyType);
            }
            if (definedKeys.size() > 1) {
                throw new InvalidGraphException("Duplicate key found for id " + eachEntry.getKey());
            }

            DataType dataType = new DataType();
            dataType.setKey(eachEntry.getKey());
            dataType.setContent(String.valueOf(eachEntry.getValue()));

            callback.addData(dataType);
        }
    }

    private static KeyTypeType parseType(Object input) throws InvalidGraphException {
        if (input instanceof Boolean) {
            return KeyTypeType.BOOLEAN;
        }
        if (input instanceof Double) {
            return KeyTypeType.STRING;
        }
        if (input instanceof Float) {
            return KeyTypeType.FLOAT;
        }
        if (input instanceof Integer) {
            return KeyTypeType.INT;
        }
        if (input instanceof Long) {
            return KeyTypeType.LONG;
        }
        if (input instanceof String) {
            return KeyTypeType.STRING;
        }
        throw new InvalidGraphException("Input '" + input + "'not parseable");
    }




}