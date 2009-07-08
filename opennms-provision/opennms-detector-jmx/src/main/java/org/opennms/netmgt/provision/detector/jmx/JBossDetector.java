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

package org.opennms.netmgt.provision.detector.jmx;

import org.opennms.netmgt.provision.detector.jmx.client.JBossClient;
import org.opennms.netmgt.provision.detector.jmx.client.JMXClient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class JBossDetector extends JMXDetector {
    
    private static String DEFAULT_SERVICE_NAME = "JBoss";
    private static int DEFAULT_JBOSS_PORT = 1099;
    
    public JBossDetector() {
        super(DEFAULT_SERVICE_NAME, DEFAULT_JBOSS_PORT);
    }

    @Override
    protected JMXClient getClient() {
        JBossClient client = new JBossClient();
        return client;
    }

    @Override
    public void onInit() {
        expectBeanCount(greatThan(0));
    }

}
