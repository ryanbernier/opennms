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

package org.opennms.features.topology.app.internal.menu;

import java.util.List;

import org.opennms.features.topology.api.CheckedOperation;
import org.opennms.features.topology.api.OperationContext;
import org.opennms.features.topology.api.topo.VertexRef;

import com.google.common.collect.Lists;

/**

 */
public class OperationMenuItem implements MenuItem {

    private final OperationService operationService;

    private int order = -1;

    public OperationMenuItem(OperationService operationService) {
        this.operationService = operationService;
    }

    @Override
    public String getLabel() {
        return operationService.getCaption();
    }

    @Override
    public boolean isVisible(List<VertexRef> targets, OperationContext operationContext) {
        return operationService.getOperation().display(targets, operationContext);
    }

    @Override
    public boolean isEnabled(List<VertexRef> targets, OperationContext operationContext) {
        return operationService.getOperation().enabled(targets, operationContext);
    }

    @Override
    public List<MenuItem> getChildren() {
        return Lists.newArrayList();
    }

    @Override
    public void addChildren(MenuItem menuItem) {
		throw new UnsupportedOperationException();
    }

    @Override
    public MenuCommand getCommand() {
        return (targets, operationContext) -> operationService.getOperation().execute(targets, operationContext);
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public boolean isCheckable() {
        return operationService.getOperation() instanceof CheckedOperation;
    }

    @Override
    public boolean isChecked(List<VertexRef> targets, OperationContext operationContext) {
        return isCheckable() && ((CheckedOperation) operationService.getOperation()).isChecked(targets, operationContext);
    }
}
