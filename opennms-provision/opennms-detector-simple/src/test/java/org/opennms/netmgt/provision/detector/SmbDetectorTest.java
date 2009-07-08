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

package org.opennms.netmgt.provision.detector;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Test;

/**
 * @author Donald Desloge
 *
 */
public class SmbDetectorTest {
    
//    private SmbDetector m_detector;
    
//    @Before
//    public void setUp() {
//        m_detector = new SmbDetector();
//        
//    }
    
    @After
    public void tearDown() {
        
    }
    
    //Tested against a Windows XP machine on local network. 
    @Test
    public void testMyDetector() throws UnknownHostException {
        //m_detector.init();
        //FIXME: This needs to be fixed
        //assertTrue(m_detector.isServiceDetected(InetAddress.getByName("192.168.1.103"), new NullDetectorMonitor()));
    }
}
