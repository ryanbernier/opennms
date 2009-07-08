/*******************************************************************************
 * This file is part of the OpenNMS(R) Application.
 *
 * Copyright (C) 2008-2009 The OpenNMS Group, Inc.  All rights reserved.
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

package org.opennms.netmgt.provision;

/**
 * This class provides an API for implementing provider "extensions" to the OpenNMS
 * Provisioning daemon.
 * 
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 *
 */
public interface ProvisioningAdapter {

    /**
     * This method is called by the Provisioner when a new node is provisioned. 
     * @throws ProvisioningAdapterException
     */
    void addNode(int nodeId) throws ProvisioningAdapterException;
    
    /**
     * This method is called by the Provisioner when a node is updated through provisioning. 
     * @throws ProvisioningAdapterException
     */
    void updateNode(int nodeId) throws ProvisioningAdapterException;
    
    /**
     * This method is called by the Provisioner when a node is deleted through provisioning. 
     * @throws ProvisioningAdapterException
     */
    void deleteNode(int nodeId) throws ProvisioningAdapterException;

    String getName();

    /**
     * This method is called when a configuration change event has occurred from any source.  Typically,
     * Traps sent from a device are converted to an event and that event is then identified for translation
     * and translated into a generic configuration changed event.
     * 
     * @param nodeid
     * @throws ProvisioningAdapterException
     */
    void nodeConfigChanged(int nodeid) throws ProvisioningAdapterException;
    
}
