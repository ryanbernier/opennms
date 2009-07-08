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
import org.opennms.netmgt.provision.detector.simple.request.LineOrientedRequest;
import org.opennms.netmgt.provision.detector.simple.response.LineOrientedResponse;
import org.opennms.netmgt.provision.support.AsyncBasicDetector;
import org.opennms.netmgt.provision.support.AsyncClientConversation.ResponseValidator;
import org.opennms.netmgt.provision.support.codec.LineOrientedCodecFactory;

/**
 * @author Donald Desloge
 *
 */
public abstract class AsyncLineOrientedDetector extends AsyncBasicDetector<LineOrientedRequest, LineOrientedResponse> {

    public AsyncLineOrientedDetector(String serviceName, int port) {
        super(serviceName, port);
        setProtocolCodecFilter( new ProtocolCodecFilter ( new LineOrientedCodecFactory ( Charset.forName("UTF-8" ))));
    }

    /**
     * @param port
     * @param timeout
     * @param retries
     */
    public AsyncLineOrientedDetector(String serviceName, int port, int timeout, int retries) {
        super(serviceName, port, timeout, retries);
        setProtocolCodecFilter( new ProtocolCodecFilter (new LineOrientedCodecFactory ( Charset.forName("UTF-8" ))));
    }

    @Override
    abstract protected void onInit();
    
    protected ResponseValidator<LineOrientedResponse> startsWith(final String prefix) {
        return new ResponseValidator<LineOrientedResponse>() {

            public boolean validate(LineOrientedResponse response) {
                return response.startsWith(prefix);
            }
            
        };
    }
    
    public ResponseValidator<LineOrientedResponse> find(final String regex){
        return new ResponseValidator<LineOrientedResponse>() {

            public boolean validate(LineOrientedResponse response) {
                return response.find(regex);
            }
          
            
        };
    }
    
    public LineOrientedRequest request(String command) {
        return new LineOrientedRequest(command);
    }

}
