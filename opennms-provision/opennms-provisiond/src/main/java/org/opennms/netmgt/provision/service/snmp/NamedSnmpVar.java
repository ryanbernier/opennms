/*******************************************************************************
 * This file is part of the OpenNMS(R) Application.
 *
 * Copyright (C) 2002-2009 The OpenNMS Group, Inc.  All rights reserved.
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


package org.opennms.netmgt.provision.service.snmp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.opennms.netmgt.snmp.AggregateTracker;
import org.opennms.netmgt.snmp.Collectable;
import org.opennms.netmgt.snmp.CollectionTracker;
import org.opennms.netmgt.snmp.ColumnTracker;
import org.opennms.netmgt.snmp.SingleInstanceTracker;
import org.opennms.netmgt.snmp.SnmpInstId;
import org.opennms.netmgt.snmp.SnmpObjId;

/**
 * The NamedSnmpVar class is used to associate a name for a particular snmp
 * instance with its object identifier. Common names often include ifIndex,
 * sysObjectId, etc al. These names are the names of particular variables as
 * defined by the SMI.
 * 
 * Should the instance also be part of a table, then the column number of the
 * instance is also stored in the object.
 * 
 * @author <A HREF="mailto:weave@oculan.com">Brian Weaver </A>
 * @author <A HREF="mailto:mike@opennms.org">Mike Davidson </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 * 
 * 
 */
public final class NamedSnmpVar {
    /**
     * String which contains the Class name of the expected SNMP data type for
     * the object.
     */
    private String m_type;

    /**
     * The class object for the class name stored in the m_type string.
     */
    private Class<?> m_typeClass;

    /**
     * The alias name for the object identifier.
     */
    private String m_name;

    /**
     * The actual object identifer string for the object.
     */
    private String m_oid;

    /**
     * If set then the object identifier is an entry some SNMP table.
     */
    private boolean m_isTabular;

    /**
     * If the instance is part of a table then this is the column number for the
     * element.
     */
    private int m_column;

    //
    // Class strings for valid SNMP data types
    // 
    public static final String SNMPINT32 = "org.opennms.protocols.snmp.SnmpInt32";

    public static final String SNMPUINT32 = "org.opennms.protocols.snmp.SnmpUInt32";

    public static final String SNMPCOUNTER32 = "org.opennms.protocols.snmp.SnmpCounter32";

    public static final String SNMPCOUNTER64 = "org.opennms.protocols.snmp.SnmpCounter64";

    public static final String SNMPGAUGE32 = "org.opennms.protocols.snmp.SnmpGauge32";

    public static final String SNMPTIMETICKS = "org.opennms.protocols.snmp.SnmpTimeTicks";

    public static final String SNMPOCTETSTRING = "org.opennms.protocols.snmp.SnmpOctetString";

    public static final String SNMPOPAQUE = "org.opennms.protocols.snmp.SnmpOpaque";

    public static final String SNMPIPADDRESS = "org.opennms.protocols.snmp.SnmpIPAddress";

    public static final String SNMPOBJECTID = "org.opennms.protocols.snmp.SnmpObjectId";

    public static final String SNMPV2PARTYCLOCK = "org.opennms.protocols.snmp.SnmpV2PartyClock";

    public static final String SNMPNOSUCHINSTANCE = "org.opennms.protocols.snmp.SnmpNoSuchInstance";

    public static final String SNMPNOSUCHOBJECT = "org.opennms.protocols.snmp.SnmpNoSuchObject";

    public static final String SNMPENDOFMIBVIEW = "org.opennms.protocols.snmp.SnmpEndOfMibView";

    public static final String SNMPNULL = "org.opennms.protocols.snmp.SnmpNull";

    /**
     * This constructor creates a new instance of the class with the type, alias
     * and object identifier. The instance is not considered to be part of a
     * table.
     * 
     * @param type
     *            The expected SNMP data type of this object.
     * @param alias
     *            The alias for the object identifier.
     * @param oid
     *            The object identifier for the instance.
     */
    public NamedSnmpVar(String type, String alias, String oid) {
        m_type = type;
        m_typeClass = null;
        m_name = alias;
        m_oid = oid;
        m_isTabular = false;
        m_column = 0;
    }

    /**
     * This constructor creates a new instance of the class with the type,
     * alias, object identifier, and table column set. The instance is
     * considered to be part of a table and the column is the "instance" number
     * for the table.
     * 
     * @param type
     *            The expected SNMP data type of this object.
     * @param alias
     *            The alias for the object identifier.
     * @param oid
     *            The object identifier for the instance.
     * @param column
     *            The column entry for its table.
     * 
     */
    public NamedSnmpVar(String type, String alias, String oid, int column) {
        m_type = type;
        m_typeClass = null;
        m_name = alias;
        m_oid = oid;
        m_isTabular = true;
        m_column = column;
    }

    /**
     * Returns the class name stored in m_type which represents the expected
     * SNMP data type of the object.
     */
    public String getType() {
        return m_type;
    }

    /**
     * Returns the class object associated with the class name stored in m_type.
     * 
     * @exception java.lang.ClassNotFoundException
     *                Thrown from this method if forName() fails.
     */
    public Class<?> getTypeClass() throws ClassNotFoundException {
        if (m_typeClass == null) {
            m_typeClass = Class.forName(m_type);
        }
        return m_typeClass;
    }

    /**
     * Returns the alias for the object identifier.
     */
    public String getAlias() {
        return m_name;
    }

    /**
     * Returns the object identifer for this instance.
     */
    public String getOid() {
        return m_oid;
    }
    
    public SnmpObjId getSnmpObjId() {
        return SnmpObjId.get(m_oid);
    }

    /**
     * Returns true if this instance is part of a table.
     */
    public boolean isTableEntry() {
        return m_isTabular;
    }
    
    public CollectionTracker getCollectionTracker(Set<SnmpInstId> instances) {
        if ( instances == null ) {
            return m_isTabular ? new ColumnTracker(getSnmpObjId()) : 
                             new SingleInstanceTracker(getSnmpObjId(), SnmpInstId.INST_ZERO);
        } else {
            Collection<Collectable> trackers = new ArrayList<Collectable>();
            for(SnmpInstId inst : instances) {
                trackers.add(new SingleInstanceTracker(getSnmpObjId(), inst));
            }
            return new AggregateTracker(trackers);
        }
    }

    /**
     * Returns the column of the table this instance is in. If the instance is
     * not part of a table then the return code is not defined.
     */
    public int getColumn() {
        return m_column;
    }

    public static CollectionTracker[] getTrackersFor(NamedSnmpVar[] columns, Set<SnmpInstId> instances) {
        CollectionTracker[] trackers = new CollectionTracker[columns.length];
        for(int i = 0; i < columns.length; i++)
            trackers[i] = columns[i].getCollectionTracker(instances);
        
         return trackers;
    }

    public static CollectionTracker[] getTrackersFor(NamedSnmpVar[] ms_elemList) {
        return getTrackersFor(ms_elemList, null);
    }

}
