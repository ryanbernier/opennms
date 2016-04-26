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

package org.opennms.features.topology.plugins.topo.atlas.vertices;

import java.util.HashMap;

import org.opennms.features.topology.api.topo.LevelAware;
import org.opennms.features.topology.api.topo.SimpleLeafVertex;

public class DefaultAtlasVertex extends SimpleLeafVertex implements LevelAware {
    private String glue;
    private final String subGraphId;
    private HashMap<String, Object> properties = new HashMap<>();

    public DefaultAtlasVertex(final String namespace,
                              final String id,
                              final String label,
                              final String subGraphId,
                              final String glue) {
        super(namespace, id, null, null);
        this.setLabel(label);
        this.subGraphId = subGraphId;
        this.glue = glue;
    }

    public String getGlue() {
        return this.glue;
    }

    public String getSubGraphId() {
        return this.subGraphId;
    }

    @Override
    public int getLevel() {
        return glue != null ? 1 : 0;
    }

    public void setProperties(HashMap<String, Object> properties) {
        this.properties = properties;
    }

    public void setGlue(String subGraphId) {
        this.glue = subGraphId;
    }
}