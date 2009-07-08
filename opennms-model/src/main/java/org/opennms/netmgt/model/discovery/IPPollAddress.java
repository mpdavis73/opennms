/*******************************************************************************
 * This file is part of the OpenNMS(R) Application.
 *
 * Copyright (C) 2002-2004, 2006-2008 The OpenNMS Group, Inc.  All rights reserved.
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


package org.opennms.netmgt.model.discovery;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>
 * This class is used to represent the polling information needed by the
 * discovery process. Each instance encapsulates an internet address, timeout in
 * milliseconds, and a retry count.
 * </p>
 * 
 * @author <A HREF="mailto:sowmya@opennms.org">Sowmya </A>
 * @author <A HREF="mailto:weave@oculan.com">Brian Weaver </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 * 
 */
public class IPPollAddress {
    /**
     * The dotted decimal IPv4 address for the poll.
     */
    private InetAddress m_address; // dotted IP m_address

    /**
     * The timeout for the poller in 1/1000th of a second.
     */
    private long m_timeout;

    /**
     * The number of times to attempt to contact the remote.
     */
    private int m_retries;

    /**
     * <P>
     * Constructs an IPPollAddress object with the specified parameters.
     * </P>
     * 
     * @param ipAddress
     *            The Dotted Decimal IPv4 Address.
     * @param timeout
     *            The timeout between retries in 1/1000th of a second.
     * @param retries
     *            The number of times to attempt to contact the address.
     * 
     * @exception java.net.UnknownHostException
     *                Thrown by the InetAddress class if the hostname cannot be
     *                resolved.
     */
    public IPPollAddress(String ipAddress, long timeout, int retries) throws UnknownHostException {
        // check if this is a valid IP address
        InetAddress.getByName(ipAddress);

        m_address = InetAddress.getByName(ipAddress);
        m_timeout = timeout;
        m_retries = retries;
    }

    /**
     * <P>
     * Constructs an IPPollAddress object with the specified parameters.
     * </P>
     * 
     * @param ipAddress
     *            The Dotted Decimal IPv4 Address.
     * @param timeout
     *            The timeout between retries in 1/1000th of a second.
     * @param retries
     *            The number of times to attempt to contact the address.
     * 
     */
    IPPollAddress(InetAddress ipAddress, long timeout, int retries) {
        m_address = ipAddress;
        m_timeout = timeout;
        m_retries = retries;
    }

    /**
     * <P>
     * Returns the timeout in 1/1000th of a second increments.
     * </P>
     * 
     * @return The timeout associated with the host in 1/1000th of a second.
     */
    public long getTimeout() {
        return m_timeout;
    }

    /**
     * <P>
     * Returns the current number of retries set for this address.
     * </P>
     * 
     * @return The retry count for the instance.
     */
    public int getRetries() {
        return m_retries;
    }

    /**
     * Returns the internet address encapsulated in the object.
     * 
     * @return The encapsulated internet address.
     */
    public InetAddress getAddress() {
        return m_address;
    }

    /**
     * <P>
     * Returns true if the passed object is equal to self. The objects must be
     * equal in address, timeout, and the number of retries.
     * </P>
     * 
     * @return True if the objects are logically equal. False is returned
     *         otherwise.
     */
    public boolean equals(IPPollAddress pollAddr) {
        boolean bRet = false;

        if (pollAddr != null) {
            if (pollAddr == this) {
                bRet = true;
            } else if (pollAddr.getAddress().equals(m_address) && pollAddr.getRetries() == m_retries && pollAddr.getTimeout() == m_timeout) {
                bRet = true;
            }
        }

        return bRet;
    }
}
