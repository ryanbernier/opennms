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

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.opennms.netmgt.provision.detector.simple.request.LineOrientedRequest;
import org.opennms.netmgt.provision.detector.simple.response.HttpStatusResponse;
import org.opennms.netmgt.provision.support.AsyncBasicDetector;
import org.opennms.netmgt.provision.support.AsyncClientConversation.ResponseValidator;
import org.opennms.netmgt.provision.support.codec.HttpProtocolCodecFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class HttpDetector extends AsyncBasicDetector<LineOrientedRequest, HttpStatusResponse> {
    
    private static final String DEFAULT_SERVICE_NAME = "HTTP";
    private static final int DEFAULT_PORT = 80;
    private static String DEFAULT_URL="/";
    private static int DEFAULT_MAX_RET_CODE = 399;
    
    private String m_url;
    private int m_maxRetCode;
    private boolean m_checkRetCode = false;

    /**
     * Default constructor
     */
    public HttpDetector() {
        super(DEFAULT_SERVICE_NAME, DEFAULT_PORT);
        contructDefaults();
    }

    /**
     * Constructor for creating a non-default service based on this protocol
     * 
     * @param serviceName
     * @param port
     */
    public HttpDetector(String serviceName, int port) {
        super(serviceName, port);
        contructDefaults();
    }
    
    private void contructDefaults() {
        setProtocolCodecFilter(new ProtocolCodecFilter(new HttpProtocolCodecFactory()));
        setUrl(DEFAULT_URL);
        setMaxRetCode(DEFAULT_MAX_RET_CODE);
    }
    
    protected void onInit() {
        send(request(httpCommand("GET")), contains(DEFAULT_SERVICE_NAME, getUrl(), isCheckRetCode(), getMaxRetCode()));
    }
    
    /**
     * @param string
     * @return
     */
    protected String httpCommand(String command) {
        
        return String.format("%s %s  HTTP/1.0\r\n\r\n", command, getUrl());
    }
    
    protected LineOrientedRequest request(String command) {
        return new LineOrientedRequest(command);
    }
    
    protected ResponseValidator<HttpStatusResponse> contains(final String pattern, final String url, final boolean isCheckCode, final int maxRetCode){
        return new ResponseValidator<HttpStatusResponse>(){

            public boolean validate(HttpStatusResponse message) {
                
                try {
                    return message.validateResponse(pattern, url, isCheckCode, maxRetCode);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return false;
                }
            }
            
        };
    }
    
    
    //Public setters and getters
    
    public void setUrl(String url) {
        m_url = url;
    }

    public String getUrl() {
        return m_url;
    }

    public void setMaxRetCode(int maxRetCode) {
        m_maxRetCode = maxRetCode;
    }

    public int getMaxRetCode() {
        return m_maxRetCode;
    }

    public void setCheckRetCode(boolean checkRetCode) {
        m_checkRetCode = checkRetCode;
    }

    public boolean isCheckRetCode() {
        return m_checkRetCode;
    }
}
