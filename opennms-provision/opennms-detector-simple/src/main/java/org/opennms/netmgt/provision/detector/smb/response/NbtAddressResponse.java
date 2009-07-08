/*******************************************************************************
 * This file is part of the OpenNMS(R) Application.
 *
 * Copyright (C) 2008 The OpenNMS Group, Inc.  All rights reserved.
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

package org.opennms.netmgt.provision.detector.smb.response;

import jcifs.netbios.NbtAddress;

/**
 * @author thedesloge
 *
 */
public class NbtAddressResponse {
    
    private String m_address;
    private NbtAddress m_nbtAddress;
    
    public void receive(String address, NbtAddress nbtAddress) {
        m_address = address;
        m_nbtAddress = nbtAddress;
    }
    
    public boolean validateAddressIsNotSame() {
        if(m_nbtAddress.getHostName().equals(m_address)) {
           return false; 
        }else {
            return true;
        }
    }
}
