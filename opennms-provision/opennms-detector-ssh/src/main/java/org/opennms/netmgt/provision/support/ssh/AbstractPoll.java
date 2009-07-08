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

package org.opennms.netmgt.provision.support.ssh;

import java.util.Collections;

import org.opennms.netmgt.model.PollStatus;
import org.opennms.core.utils.TimeoutTracker;

public abstract class AbstractPoll implements Poll {
    // default timeout of 3 seconds
    protected int m_timeout = 3000;
    
    /**
     * Set the timeout in milliseconds. 
     * @param milliseconds the timeout
     */
    public void setTimeout(int milliseconds) {
        m_timeout = milliseconds;
    }

    /**
     * Get the timeout in milliseconds.
     * @return the timeout
     */
    public int getTimeout() {
        return m_timeout;
    }

    public abstract PollStatus poll(TimeoutTracker tracker) throws InsufficientParametersException;
    
    public PollStatus poll() throws InsufficientParametersException {
        TimeoutTracker tracker = new TimeoutTracker(Collections.emptyMap(), 1, getTimeout());
        return poll(tracker);
    }

}
