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

package org.opennms.netmgt.model.events;

import org.opennms.netmgt.model.AbstractEntityVisitor;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsSnmpInterface;
import org.opennms.netmgt.xml.event.Event;

public class UpdateEventVisitor extends AbstractEntityVisitor {
    
    private static final String m_eventSource = "Provisiond";
    private EventForwarder m_eventForwarder;

    public UpdateEventVisitor(EventForwarder eventForwarder) {
        m_eventForwarder = eventForwarder;
    }
    
    @Override
    public void visitNode(OnmsNode node) {
        System.out.printf("Sending nodeAdded Event for %s\n", node);
        m_eventForwarder.sendNow(createNodeUpdatedEvent(node));
    }

    @Override
    public void visitIpInterface(OnmsIpInterface iface) {
        //TODO decide what to do here and when to do it
    }

    @Override
    public void visitMonitoredService(OnmsMonitoredService monSvc) {
        //TODO decide what to do here and when to do it
    }
    
    @Override
    public void visitSnmpInterface(org.opennms.netmgt.model.OnmsEntity snmpIface) {
        //TODO decide what to do here and when to do it
    }

    private Event createNodeUpdatedEvent(OnmsNode node) {
        return EventUtils.createNodeUpdatedEvent(m_eventSource, node.getId(), node.getLabel(), node.getLabelSource());
    }

    @SuppressWarnings("unused")
    private Event createIpInterfaceUpdatedEvent(OnmsIpInterface iface) {
        return null;
    }
    
    @SuppressWarnings("unused")
    private Event createSnmpInterfaceUpdatedEvent(OnmsSnmpInterface iface) {
        return null;
    }

    @SuppressWarnings("unused")
    private Event createMonitoredServiceUpdatedEvent(OnmsMonitoredService monSvc) {
        return null;
    }

}
