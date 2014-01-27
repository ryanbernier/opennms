/*
 * This class was converted to JAXB from Castor.
 */

package org.opennms.netmgt.config.poller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.opennms.core.xml.ValidateUsing;
import org.opennms.netmgt.config.pagesequence.PageSequence;

/**
 * Top-level element for the poller-configuration.xml
 *  configuration file.
 */

@XmlRootElement(name="poller-configuration")
@ValidateUsing("poller-configuration.xsd")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({PageSequence.class})
public class PollerConfiguration implements Serializable {
    private static final long serialVersionUID = 3402898044699865749L;

    /**
     * The maximum number of threads used for polling.
     */
    @XmlAttribute(name="threads")
    private Integer m_threads = 30;

    /**
     * SQL query for getting the next outage ID.
     */
    @XmlAttribute(name="nextOutageId")
    private String m_nextOutageId = "SELECT nextval('outageNxtId')";

    /**
     * Enable/disable serviceUnresponsive behavior
     */
    @XmlAttribute(name="serviceUnresponsiveEnabled")
    private String m_serviceUnresponsiveEnabled = "false";

    /**
     * Flag which indicates if an external XMLRPC server has to be notified
     * with any event process errors
     */
    @XmlAttribute(name="xmlrpc")
    private String m_xmlrpc = "false";

    /**
     * Flag which indicates if the optional path outage feature is enabled
     */
    @XmlAttribute(name="pathOutageEnabled")
    private String m_pathOutageEnabled = "false";

    /**
     * Configuration of node-outage functionality
     */
    @XmlElement(name="node-outage")
    private NodeOutage m_nodeOutage;

    /**
     * Package encapsulating addresses, services to be polled for these
     * addresses, etc..
     */
    @XmlElement(name="package")
    private List<Package> m_packages = new ArrayList<Package>();

    /**
     * Service monitors
     */
    @XmlElement(name="monitor")
    private List<Monitor> m_monitors = new ArrayList<Monitor>();

    /**
     * The maximum number of threads used for polling.
     */
    public Integer getThreads() {
        return m_threads == null? 0 : m_threads;
    }

    public void setThreads(final Integer threads) {
        m_threads = threads;
    }

    /**
     * SQL query for getting the next outage ID.
     */
    public String getNextOutageId() {
        return m_nextOutageId == null? "SELECT nextval('outageNxtId')" : m_nextOutageId;
    }

    public void setNextOutageId(final String nextOutageId) {
        m_nextOutageId = nextOutageId;
    }

    /**
     * Enable/disable serviceUnresponsive behavior
     */
    public String getServiceUnresponsiveEnabled() {
        return m_serviceUnresponsiveEnabled;
    }

    public void setServiceUnresponsiveEnabled(final String serviceUnresponsiveEnabled) {
        m_serviceUnresponsiveEnabled = serviceUnresponsiveEnabled;
    }

    /**
     * Flag which indicates if an external XMLRPC server has to be notified
     * with any event process errors
     */
    public String getXmlrpc() {
        return m_xmlrpc == null? "false" : m_xmlrpc;
    }
    
    public void setXmlrpc(final String xmlrpc) {
        m_xmlrpc = xmlrpc;
    }

    /**
     * Flag which indicates if the optional path outage feature is enabled
     */
    public String getPathOutageEnabled() {
        return m_pathOutageEnabled == null? "false" : m_pathOutageEnabled;
    }

    public void setPathOutageEnabled(final String pathOutageEnabled) {
        m_pathOutageEnabled = pathOutageEnabled;
    }

    /**
     * Configuration of node-outage functionality
     */
    public NodeOutage getNodeOutage() {
        return m_nodeOutage;
    }

    public void setNodeOutage(final NodeOutage nodeOutage) {
        m_nodeOutage = nodeOutage;
    }

    public List<Package> getPackages() {
        return Collections.unmodifiableList(m_packages);
    }

    public void setPackages(final List<Package> packages) {
        m_packages = new ArrayList<Package>(packages);
    }

    public void addPackage(final Package pack) throws IndexOutOfBoundsException {
        m_packages.add(pack);
    }

    public boolean removePackage(final Package pack) {
        return m_packages.remove(pack);
    }

    public Package getPackage(final String packageName) {
        for (final Package pkg : m_packages) {
            if (pkg.getName().equals(packageName)) {
                return pkg;
            }
        }
        return null;
    }

    public List<Monitor> getMonitors() {
        return Collections.unmodifiableList(m_monitors);
    }

    public void setMonitors(final List<Monitor> monitors) {
        m_monitors = new ArrayList<Monitor>(monitors);
    }

    public void addMonitor(final Monitor monitor) throws IndexOutOfBoundsException {
        m_monitors.add(monitor);
    }

    public void addMonitor(final String service, final String className) {
        addMonitor(new Monitor(service, className));
    }

    public boolean removeMonitor(final Monitor monitor) {
        return m_monitors.remove(monitor);
    }

    public PollerConfiguration getPollerConfigurationForPackage(final String pollingPackageName) {
        if (pollingPackageName == null) return null;
        final Package pkg = getPackage(pollingPackageName);
        if (pkg == null) return null;

        final Set<String> seenMonitors = new HashSet<String>();
        final PollerConfiguration newConfig = new PollerConfiguration();
        newConfig.setThreads(getThreads());
        newConfig.setNextOutageId(getNextOutageId());
        newConfig.setServiceUnresponsiveEnabled(getServiceUnresponsiveEnabled());
        newConfig.setXmlrpc(getXmlrpc());
        newConfig.setPathOutageEnabled(getPathOutageEnabled());
        newConfig.setNodeOutage(getNodeOutage());
        
        newConfig.addPackage(pkg);
        
        for (final Service service : pkg.getServices()) {
            seenMonitors.add(service.getName());
        }
        
        for (final Monitor monitor : getMonitors()) {
            if (seenMonitors.contains(monitor.getService())) {
                newConfig.addMonitor(monitor);
            }
        }
        return newConfig;
    }

    @Override
    public String toString() {
        return "PollerConfiguration[" +
                "threads=" + m_threads +
                ",nextOutageId=" + m_nextOutageId +
                ",serviceUnresponsiveEnabled=" + m_serviceUnresponsiveEnabled +
                ",xmlrpc=" + m_xmlrpc +
                ",pathOutageEnabled=" + m_pathOutageEnabled +
                ",nodeOutage=" + m_nodeOutage +
                ",packages=" + m_packages +
                ",monitors=" + m_monitors +
                "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_monitors == null) ? 0 : m_monitors.hashCode());
        result = prime * result + ((m_nextOutageId == null) ? 0 : m_nextOutageId.hashCode());
        result = prime * result + ((m_nodeOutage == null) ? 0 : m_nodeOutage.hashCode());
        result = prime * result + ((m_packages == null) ? 0 : m_packages.hashCode());
        result = prime * result + ((m_pathOutageEnabled == null) ? 0 : m_pathOutageEnabled.hashCode());
        result = prime * result + ((m_serviceUnresponsiveEnabled == null) ? 0 : m_serviceUnresponsiveEnabled.hashCode());
        result = prime * result + ((m_threads == null) ? 0 : m_threads.hashCode());
        result = prime * result + ((m_xmlrpc == null) ? 0 : m_xmlrpc.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PollerConfiguration)) {
            return false;
        }
        final PollerConfiguration other = (PollerConfiguration) obj;
        if (m_monitors == null) {
            if (other.m_monitors != null) {
                return false;
            }
        } else if (!m_monitors.equals(other.m_monitors)) {
            return false;
        }
        if (m_nextOutageId == null) {
            if (other.m_nextOutageId != null) {
                return false;
            }
        } else if (!m_nextOutageId.equals(other.m_nextOutageId)) {
            return false;
        }
        if (m_nodeOutage == null) {
            if (other.m_nodeOutage != null) {
                return false;
            }
        } else if (!m_nodeOutage.equals(other.m_nodeOutage)) {
            return false;
        }
        if (m_packages == null) {
            if (other.m_packages != null) {
                return false;
            }
        } else if (!m_packages.equals(other.m_packages)) {
            return false;
        }
        if (m_pathOutageEnabled == null) {
            if (other.m_pathOutageEnabled != null) {
                return false;
            }
        } else if (!m_pathOutageEnabled.equals(other.m_pathOutageEnabled)) {
            return false;
        }
        if (m_serviceUnresponsiveEnabled == null) {
            if (other.m_serviceUnresponsiveEnabled != null) {
                return false;
            }
        } else if (!m_serviceUnresponsiveEnabled.equals(other.m_serviceUnresponsiveEnabled)) {
            return false;
        }
        if (m_threads == null) {
            if (other.m_threads != null) {
                return false;
            }
        } else if (!m_threads.equals(other.m_threads)) {
            return false;
        }
        if (m_xmlrpc == null) {
            if (other.m_xmlrpc != null) {
                return false;
            }
        } else if (!m_xmlrpc.equals(other.m_xmlrpc)) {
            return false;
        }
        return true;
    }

}
