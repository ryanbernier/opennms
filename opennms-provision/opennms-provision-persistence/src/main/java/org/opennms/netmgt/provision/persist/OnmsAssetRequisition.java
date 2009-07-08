/*******************************************************************************
 * This file is part of the OpenNMS(R) Application.
 *
 * Copyright (C) 2009 The OpenNMS Group, Inc.  All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc.:
 *
 *      51 Franklin Street
 *      5th Floor
 *      Boston, MA 02110-1301
 *      USA
 *
 * For more information contact:
 *
 *      OpenNMS Licensing <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 *
 *******************************************************************************/

package org.opennms.netmgt.provision.persist;

import org.opennms.netmgt.provision.persist.requisition.RequisitionAsset;

/**
 * OnmsAssetRequisition
 *
 * @author brozow
 */
public class OnmsAssetRequisition {

    private RequisitionAsset m_asset;
    
    public OnmsAssetRequisition(RequisitionAsset asset) {
        m_asset = asset;
    }
    
    RequisitionAsset getAsset() {
        return m_asset;
    }

    public void visit(RequisitionVisitor visitor) {
        visitor.visitAsset(this);
        visitor.completeAsset(this);
    }

    public String getName() {
        return m_asset.getName();
    }

    public String getValue() {
        return m_asset.getValue();
    }
    
    

}
