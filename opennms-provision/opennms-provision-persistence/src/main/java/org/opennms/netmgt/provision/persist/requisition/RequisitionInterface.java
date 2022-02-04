/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.01.29 at 01:15:48 PM EST 
//

package org.opennms.netmgt.provision.persist.requisition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.net.InetAddress;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.opennms.core.network.IPValidationException;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.opennms.core.network.IPAddress;
import org.opennms.core.utils.InetAddressUtils;
import org.opennms.netmgt.model.PrimaryType;
import org.opennms.netmgt.model.PrimaryTypeAdapter;
import org.opennms.netmgt.model.events.EventUtils;
import org.opennms.netmgt.events.api.EventForwarder;
import org.opennms.netmgt.xml.event.Event;

/**
 * <p>RequisitionInterface class.</p>
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name="", propOrder = { "m_monitoredServices", "m_categories", "m_metaData" })
@XmlRootElement(name = "interface")
public class RequisitionInterface implements Comparable<RequisitionInterface> {

    //TODO Change these to be sets so that we don't have to verify duplicates in the lists
    @XmlElement(name="monitored-service")
    protected List<RequisitionMonitoredService> m_monitoredServices = new ArrayList<>();

    @XmlElement(name="category")
    protected List<RequisitionCategory> m_categories = new ArrayList<>();

    @XmlElement(name="meta-data")
    protected List<RequisitionMetaData> m_metaData = new ArrayList<>();

    @XmlAttribute(name="descr")
    protected String m_description;

    @XmlAttribute(name="ip-addr", required=true)
    protected String m_ipAddressStr;
    
    @XmlAttribute(name="managed")
    protected Boolean m_isManaged;
    
    // annotated on the class, for some compatibility/initialization
    protected PrimaryType m_snmpPrimary;
    
    @XmlAttribute(name="status")
    protected Integer m_status;

    @Autowired
    private EventForwarder m_eventForwarder;

    protected InetAddress m_ipAddress;

    /**
     * <p>getMonitoredServiceCount</p>
     *
     * @return a int.
     */
    @XmlTransient
    public int getMonitoredServiceCount() {
        return (m_monitoredServices == null) ? 0 : m_monitoredServices.size();
    }

    /* backwards-compat with ModelImport */
    /**
     * <p>getMonitoredService</p>
     *
     * @return an array of {@link org.opennms.netmgt.provision.persist.requisition.RequisitionMonitoredService} objects.
     */
    @XmlTransient
    public RequisitionMonitoredService[] getMonitoredService() {
        return getMonitoredServices().toArray(new RequisitionMonitoredService[] {});
    }

    /**
     * <p>getMonitoredServices</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<RequisitionMonitoredService> getMonitoredServices() {
        if (m_monitoredServices == null) {
            m_monitoredServices = new ArrayList<>();
        }
        return m_monitoredServices;
    }

    /**
     * <p>setMonitoredServices</p>
     *
     * @param services a {@link java.util.List} object.
     */
    public void setMonitoredServices(List<RequisitionMonitoredService> services) {
        m_monitoredServices = services;
    }

    /**
     * <p>getMonitoredService</p>
     *
     * @param service a {@link java.lang.String} object.
     * @return a {@link org.opennms.netmgt.provision.persist.requisition.RequisitionMonitoredService} object.
     */
    public RequisitionMonitoredService getMonitoredService(String service) {
        if (m_monitoredServices != null) {
            for (RequisitionMonitoredService svc : m_monitoredServices) {
                if (svc.getServiceName().equals(service)) {
                    return svc;
                }
            }
        }
        return null;
    }

    /**
     * <p>deleteMonitoredService</p>
     *
     * @param service a {@link org.opennms.netmgt.provision.persist.requisition.RequisitionMonitoredService} object.
     */
    public void deleteMonitoredService(RequisitionMonitoredService service) {
        m_monitoredServices.remove(service);
    }

    /**
     * <p>deleteMonitoredService</p>
     *
     * @param service a {@link java.lang.String} object.
     */
    public void deleteMonitoredService(String service) {
        if (m_monitoredServices != null) {
            Iterator<RequisitionMonitoredService> i = m_monitoredServices.iterator();
            while (i.hasNext()) {
                RequisitionMonitoredService svc = i.next();
                if (svc.getServiceName().equals(service)) {
                    i.remove();
                    break;
                }
            }
        }
    }

    /**
     * <p>insertMonitoredService</p>
     *
     * @param service a {@link org.opennms.netmgt.provision.persist.requisition.RequisitionMonitoredService} object.
     */
    public void insertMonitoredService(RequisitionMonitoredService service) {
        Iterator<RequisitionMonitoredService> iterator = m_monitoredServices.iterator();
        while (iterator.hasNext()) {
            RequisitionMonitoredService existingService = iterator.next();
            if (existingService.getServiceName().equals(service.getServiceName())) {
                iterator.remove();
            }
        }
        m_monitoredServices.add(0, service);
    }

    /**
     * <p>putMonitoredService</p>
     *
     * @param service a {@link org.opennms.netmgt.provision.persist.requisition.RequisitionMonitoredService} object.
     */
    public void putMonitoredService(RequisitionMonitoredService service) {
        Iterator<RequisitionMonitoredService> iterator = m_monitoredServices.iterator();
        while (iterator.hasNext()) {
            RequisitionMonitoredService existingService = iterator.next();
            if (existingService.getServiceName().equals(service.getServiceName())) {
                iterator.remove();
            }
        }
        m_monitoredServices.add(service);
    }

    /**
     * <p>getCategories</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<RequisitionCategory> getCategories() {
        if (m_categories == null) {
            m_categories = new ArrayList<>();
        }
        return m_categories;
    }
    
    /**
     * <p>setCategories</p>
     *
     * @param categories a {@link java.util.List} object.
     */
    public void setCategories(List<RequisitionCategory> categories) {
        m_categories = categories;
    }

    /**
     * <p>getCategory</p>
     *
     * @param category a {@link java.lang.String} object.
     * @return a {@link org.opennms.netmgt.provision.persist.requisition.RequisitionCategory} object.
     */
    public RequisitionCategory getCategory(String category) {
        if (m_categories != null) {
            for (RequisitionCategory cat : m_categories) {
                if (cat.getName().equals(category)) {
                    return cat;
                }
            }
            
        }
        return null;
    }

    /**
     * <p>deleteCategory</p>
     *
     * @param category a {@link org.opennms.netmgt.provision.persist.requisition.RequisitionCategory} object.
     */
    public void deleteCategory(RequisitionCategory category) {
        m_categories.remove(category);
    }

    /**
     * <p>deleteCategory</p>
     *
     * @param category a {@link java.lang.String} object.
     */
    public void deleteCategory(String category) {
        if (m_categories != null) {
            Iterator<RequisitionCategory> i = m_categories.iterator();
            while (i.hasNext()) {
                RequisitionCategory cat = i.next();
                if (cat.getName().equals(category)) {
                    i.remove();
                    break;
                }
            }
        }
    }

    public List<RequisitionMetaData> getMetaData() {
        return m_metaData;
    }

    public void setMetaData(List<RequisitionMetaData> metaData) {
        m_metaData = metaData;
    }

    /**
     * <p>getDescr</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescr() {
        return m_description;
    }

    /**
     * <p>setDescr</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setDescr(String value) {
        m_description = value;
    }

    /**
     * <p>getIpAddr</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public InetAddress getIpAddr() {
        return m_ipAddress;
    }

    /**
     * <p>setIpAddr</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setIpAddr(String value) {
        try {
            m_ipAddress = InetAddressUtils.getInetAddress(value);
            m_ipAddressStr = value;
        } catch (Throwable e) {
            throw new IllegalArgumentException(String.format("Invalid IP address specified: {}", value), e);
        }
    }

    /**
     * <p>isManaged</p>
     *
     * @return a boolean.
     */
    public boolean isManaged() {
        if (m_isManaged == null) {
            return true;
        } else {
            return m_isManaged;
        }
    }

    /**
     * <p>setManaged</p>
     *
     * @param value a {@link java.lang.Boolean} object.
     */
    public void setManaged(Boolean value) {
        m_isManaged = value;
    }

    /**
     * <p>getSnmpPrimary</p>
     *
     * @deprecated It's not a good idea to have side-effects on a getter, like returning
     * a value that does not exactly reflect the internal state of the object.
     *
     * @return a {@link java.lang.String} object.
     */
    @XmlAttribute(name="snmp-primary")
    @XmlJavaTypeAdapter(PrimaryTypeAdapter.class)
    public PrimaryType getSnmpPrimary() {
        if (m_snmpPrimary == null) {
            return PrimaryType.NOT_ELIGIBLE;
        } else {
            return m_snmpPrimary;
        }
    }

    /**
     * <p>setSnmpPrimary</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setSnmpPrimary(final PrimaryType value) {
        m_snmpPrimary = value;
    }

    /**
     * <p>getStatus</p>
     *
     * @return a int.
     */
    public Integer getStatus() {
        if (m_status == null) {
            return  1;
        } else {
            return m_status;
        }
    }

    /**
     * <p>setStatus</p>
     *
     * @param value a {@link java.lang.Integer} object.
     */
    public void setStatus(Integer value) {
        m_status = value;
    }

    public void validate(RequisitionNode node) throws ValidationException {
        if (m_ipAddress == null) {
            if (m_ipAddressStr != null) {
                validateHost();
            }
            else {
                throw new ValidationException("Requisition interface 'ip-addr' is a required attribute!");
            }
        }

        if (m_monitoredServices != null) {
            Set<String> serviceNameSet = new HashSet<>();
            for (final RequisitionMonitoredService svc : m_monitoredServices) {
                svc.validate();
                if (!serviceNameSet.add(svc.getServiceName())) {
                    throw new ValidationException("Duplicate service name: " + svc.getServiceName());
                }
            }
        }

        // there can be only one primary interface per node
        if (m_snmpPrimary == PrimaryType.PRIMARY) {
            long otherPrimaryInterfaces = node.getInterfaces().stream()
                    .filter(iface -> PrimaryType.PRIMARY == iface.getSnmpPrimary())
                    .filter(iface -> !iface.getIpAddr().equals(this.getIpAddr()))
                    .count();
            if (otherPrimaryInterfaces > 0) {
                throw new ValidationException("Node foreign ID (" + node.getForeignId() + ") contains multiple primary interfaces. Maximum one is allowed.");
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 67;
        int result = 1;
        result = prime * result + ((m_categories == null) ? 0 : m_categories.hashCode());
        result = prime * result + ((m_metaData == null) ? 0 : m_metaData.hashCode());
        result = prime * result + ((m_description == null) ? 0 : m_description.hashCode());
        result = prime * result + ((m_ipAddress == null) ? 0 : m_ipAddress.hashCode());
        result = prime * result + ((m_isManaged == null) ? 0 : m_isManaged.hashCode());
        result = prime * result + ((m_monitoredServices == null) ? 0 : m_monitoredServices.hashCode());
        result = prime * result + ((m_snmpPrimary == null) ? 0 : m_snmpPrimary.hashCode());
        result = prime * result + ((m_status == null) ? 0 : m_status.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof RequisitionInterface)) return false;
        final RequisitionInterface other = (RequisitionInterface) obj;
        if (m_categories == null) {
            if (other.m_categories != null) return false;
        } else if (!m_categories.equals(other.m_categories)) {
            return false;
        }
        if (m_metaData == null) {
            if (other.m_metaData != null) return false;
        } else if (!m_metaData.equals(other.m_metaData)) {
            return false;
        }
        if (m_description == null) {
            if (other.m_description != null) return false;
        } else if (!m_description.equals(other.m_description)) {
            return false;
        }
        if (m_ipAddress == null) {
            if (other.m_ipAddress != null) return false;
        } else if (!m_ipAddress.equals(other.m_ipAddress)) {
            return false;
        }
        if (m_isManaged == null) {
            if (other.m_isManaged != null) return false;
        } else if (!m_isManaged.equals(other.m_isManaged)) {
            return false;
        }
        if (m_monitoredServices == null) {
            if (other.m_monitoredServices != null) return false;
        } else if (!m_monitoredServices.equals(other.m_monitoredServices)) {
            return false;
        }
        if (m_snmpPrimary == null) {
            if (other.m_snmpPrimary != null) return false;
        } else if (!getSnmpPrimary().equals(other.getSnmpPrimary())) {
            return false;
        }
        if (m_status == null) {
            if (other.m_status != null) return false;
        } else if (!m_status.equals(other.m_status)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RequisitionInterface [monitoredServices="
                + m_monitoredServices + ", categories=" + m_categories
                + ", metaData=" + m_metaData
                + ", description=" + m_description + ", ipAddress="
                + m_ipAddress + ", isManaged=" + m_isManaged
                + ", snmpPrimary=" + m_snmpPrimary + ", status="
                + m_status + "]";
    }

    @Override
    public int compareTo(final RequisitionInterface other) {
        return new CompareToBuilder()
            .append(m_ipAddress, other.m_ipAddress)
            .append(m_status, other.m_status)
            .append(m_isManaged, other.m_isManaged)
            .append(getSnmpPrimary(), other.getSnmpPrimary())
            .append(m_monitoredServices, other.m_monitoredServices)
            .append(m_categories, other.m_categories)
            .append(m_metaData, other.m_metaData)
            .append(m_description, other.m_description)
            .toComparison();
    }

    @SuppressWarnings("unused")
    public void beforeMarshal(Marshaller u) {
        if (m_ipAddressStr == null && m_ipAddress != null) {
            m_ipAddressStr = InetAddressUtils.str(m_ipAddress);
        }
    }

    @SuppressWarnings("unused")
    public void afterUnmarshal(Unmarshaller u, Object parent) throws IPValidationException {
        try {
            validateHost();
        }
        catch (ValidationException ve) {
            final Event e = EventUtils.createInterfaceRejectedEvent("Provisiond", "filler", m_ipAddressStr);
            // TODO how to wire this
            // m_eventForwarder.sendNow(e);
            throw new IPValidationException(ve.getMessage(), ve);
        }
    }

    /**
     *  Post-unmarshall, this is called to construct the InetAddress
     *  from the parsed ip-addr String.
     *
     * @throws ValidationException  If the parsed ip-addr attribute
     *                              does not resolve to a valid address
     */
    protected void validateHost() throws ValidationException {
        try {
            m_ipAddress = new IPAddress(m_ipAddressStr).toInetAddress();
        }
        catch (IllegalArgumentException iae) {
            throw new ValidationException(m_ipAddressStr);
        }
    }
}
