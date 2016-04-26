/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
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

package org.opennms.features.topology.plugins.topo.atlas.operations;

import java.util.List;
import java.util.Set;

import org.opennms.features.topology.api.GraphContainer;
import org.opennms.features.topology.api.Operation;
import org.opennms.features.topology.api.OperationContext;
import org.opennms.features.topology.api.topo.Criteria;
import org.opennms.features.topology.api.topo.VertexRef;
import org.opennms.features.topology.plugins.topo.atlas.AtlasTopologyProvider;
import org.opennms.features.topology.plugins.topo.atlas.vertices.DefaultAtlasVertex;

import com.google.common.base.Strings;

public class NavigateOperation implements Operation {

    private final AtlasTopologyProvider topologyProvider;

    public NavigateOperation(final AtlasTopologyProvider topologyProvider) {
        this.topologyProvider = topologyProvider;
    }

    @Override
    public boolean display(List<VertexRef> targets, OperationContext operationContext) {
        return true;
    }

    @Override
    public boolean enabled(List<VertexRef> targets, OperationContext operationContext) {
    	if (targets.size() == 1 && targets.get(0) instanceof DefaultAtlasVertex) {
            GraphContainer graphContainer = operationContext.getGraphContainer();
            return !Strings.isNullOrEmpty(((DefaultAtlasVertex) graphContainer.getBaseTopology().getVertex(targets.get(0))).getGlue());
        }

        return false;
    }

    @Override
    public String getId() {
        return "Navigate";
    }

    @Override
    public void execute(List<VertexRef> targets, OperationContext operationContext) {
        final DefaultAtlasVertex vertex = (DefaultAtlasVertex) operationContext.getGraphContainer().getBaseTopology().getVertex(targets.get(0));
        if (!Strings.isNullOrEmpty(vertex.getGlue())) {
            navigateTo(operationContext.getGraphContainer(), vertex.getGlue());
        }
    }
    public void navigateTo(GraphContainer container, String glue) {
        container.clearCriteria();
        Set<Criteria> defaultCriteriaSet = AtlasTopologyProvider.createDefaultCriteriaSet(topologyProvider, glue);
        for (Criteria eachCriteria : defaultCriteriaSet) {
            container.addCriteria(eachCriteria);
        }
        container.redoLayout();
    }
}