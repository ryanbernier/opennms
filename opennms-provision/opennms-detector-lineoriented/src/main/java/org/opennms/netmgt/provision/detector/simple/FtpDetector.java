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

package org.opennms.netmgt.provision.detector.simple;

import java.nio.charset.Charset;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.opennms.netmgt.provision.support.codec.MultilineOrientedCodecFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class FtpDetector extends AsyncMultilineDetector {
    
    private static final String DEFAULT_SERVICE_NAME = "FTP";
    private static final int DEFAULT_PORT = 21;
    private String m_multilineIndicator = "-";
    
    /**
     * Default constructor
     */
    public FtpDetector() {
        super(DEFAULT_SERVICE_NAME, DEFAULT_PORT);
    }
    
    /**
     * Constructor for creating a non-default service based on this protocol
     * 
     * @param serviceName
     * @param port
     */
    public FtpDetector(String serviceName, int port) {
        super(serviceName, port);
    }

    public void onInit() {
        //setup the correct codec for this Detector
        setProtocolCodecFilter(new ProtocolCodecFilter( new MultilineOrientedCodecFactory( Charset.forName( "UTF-8"), getMultilineIndicator())));
        
        expectBanner(expectCodeRange(100, 600));
        send(request("quit"), expectCodeRange(100,600));
    }

    public void setMultilineIndicator(String multilineIndicator) {
        m_multilineIndicator = multilineIndicator;
    }

    public String getMultilineIndicator() {
        return m_multilineIndicator;
    }
}
