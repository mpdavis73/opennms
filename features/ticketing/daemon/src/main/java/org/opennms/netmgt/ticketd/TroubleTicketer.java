/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2022 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.ticketd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opennms.core.utils.InsufficientInformationException;
import org.opennms.netmgt.daemon.SpringServiceDaemon;
import org.opennms.netmgt.events.api.EventConstants;
import org.opennms.netmgt.events.api.EventIpcManager;
import org.opennms.netmgt.events.api.EventListener;
import org.opennms.netmgt.events.api.model.IEvent;
import org.opennms.netmgt.events.api.model.IParm;
import org.opennms.netmgt.model.events.EventUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Manages Events trouble ticket related events and passes them to the service layer
 * implementation.
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 */
public class TroubleTicketer implements SpringServiceDaemon, EventListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(TroubleTicketer.class);

	private static final String LOG4J_CATEGORY = "trouble-ticketer";
	
    private volatile boolean m_initialized = false;
    
    /**
     * Typically wired in by Spring (applicationContext-troubleTicketer.xml)
     * @param eventIpcManager
     */
	private EventIpcManager m_eventIpcManager;
    private TicketerServiceLayer m_ticketerServiceLayer;

	/**
	 * <p>setEventIpcManager</p>
	 *
	 * @param eventIpcManager a {@link org.opennms.netmgt.events.api.EventIpcManager} object.
	 */
	public void setEventIpcManager(EventIpcManager eventIpcManager) {
		m_eventIpcManager = eventIpcManager;
	}
    
	
    /**
     * <p>setTicketerServiceLayer</p>
     *
     * @param ticketerServiceLayer a {@link org.opennms.netmgt.ticketd.TicketerServiceLayer} object.
     */
    public void setTicketerServiceLayer(TicketerServiceLayer ticketerServiceLayer) {
        m_ticketerServiceLayer = ticketerServiceLayer;
    }

    /**
     * SpringFramework method from implementation of the Spring Interface
     * <code>org.springframework.beans.factory.InitializingBean</code>
     *
     * @throws java.lang.Exception An exception is thrown when detecting an invalid state such
     *         as data not properly initialized or this method called more then once.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(!m_initialized, "shouldn't be calling afterProperties set more than once");
        Assert.state(m_eventIpcManager != null, "property eventIpcManager must be set to a non-null value");
        Assert.state(m_ticketerServiceLayer != null, "property ticketerServiceLayer must be set to a non-null value");

        String[] ueis = {
    			EventConstants.TROUBLETICKET_CANCEL_UEI,
    			EventConstants.TROUBLETICKET_CLOSE_UEI,
    			EventConstants.TROUBLETICKET_CREATE_UEI,
    			EventConstants.TROUBLETICKET_UPDATE_UEI,
                EventConstants.RELOAD_DAEMON_CONFIG_UEI
    	};
    	m_eventIpcManager.addEventListener(this, Arrays.asList(ueis));
        
        m_initialized = true;
    }

    @Override
    public void start() {
    	// nothing to do to "start", we only do state transitions here
    }

    @Override
    public void destroy() throws Exception {
    	// like start, nothing to destroy
    }

	/**
	 * EventListener Interface required implementation
	 *
	 * @return <code>java.lang.String</code> representing the name of this service daemon
	 */
    @Override
	public String getName() {
		return "OpenNMS.TroubleTicketer";
	}

	/**
	 * {@inheritDoc}
	 *
	 * Event listener Interface required implementation
	 */
    @Override
	public void onEvent(IEvent e) {
        try {
		if (EventConstants.TROUBLETICKET_CANCEL_UEI.equals(e.getUei())) {
			handleCancelTicket(e);
		} else if (EventConstants.TROUBLETICKET_CLOSE_UEI.equals(e.getUei())) {
			handleCloseTicket(e);
		} else if (EventConstants.TROUBLETICKET_CREATE_UEI.equals(e.getUei())) {
			handleCreateTicket(e);
		} else if (EventConstants.TROUBLETICKET_UPDATE_UEI.equals(e.getUei())) {
			handleUpdateTicket(e);
		} else if (isReloadConfigEvent(e)) {
            m_ticketerServiceLayer.reloadTicketer();
 		}
        } catch (InsufficientInformationException ex) {
            LOG.warn("Unable to create trouble ticket due to lack of information: {}", ex.getMessage());
        } catch (final Exception t) {
            LOG.error("Error occurred during trouble ticket processing!", t);
        }
	}

	/**
	 * Makes call to API to close a trouble ticket associated with an OnmsAlarm.
	 * @param e An OpenNMS event.
	 * @throws InsufficientInformationException
	 */
    private void handleCloseTicket(IEvent e) throws InsufficientInformationException {
        EventUtils.requireParm(e, EventConstants.PARM_ALARM_ID);
        EventUtils.requireParm(e, EventConstants.PARM_ALARM_UEI);
        EventUtils.requireParm(e, EventConstants.PARM_USER);
        EventUtils.requireParm(e, EventConstants.PARM_TROUBLE_TICKET);
        
        int alarmId = EventUtils.getIntParm(e, EventConstants.PARM_ALARM_ID, 0);
        String ticketId = EventUtils.getParm(e, EventConstants.PARM_TROUBLE_TICKET);
        
        m_ticketerServiceLayer.closeTicketForAlarm(alarmId, ticketId);
	}

    /**
     * Make call to API to Update a trouble ticket with new data from an OnmsAlarm.
     * @param e An OpenNMS Event
     * @throws InsufficientInformationException
     */
	private void handleUpdateTicket(IEvent e) throws InsufficientInformationException {
        EventUtils.requireParm(e, EventConstants.PARM_ALARM_ID);
        EventUtils.requireParm(e, EventConstants.PARM_ALARM_UEI);
        EventUtils.requireParm(e, EventConstants.PARM_USER);
        EventUtils.requireParm(e, EventConstants.PARM_TROUBLE_TICKET);

        int alarmId = EventUtils.getIntParm(e, EventConstants.PARM_ALARM_ID, 0);
        String ticketId = EventUtils.getParm(e, EventConstants.PARM_TROUBLE_TICKET);
        
        m_ticketerServiceLayer.updateTicketForAlarm(alarmId, ticketId);
    }

	/**
	 * Make call to API to Create a new Trouble Ticket to be associated with an OnmsAlarm.
	 * @param e An OpenNMS Event
	 * @throws InsufficientInformationException
	 */
	private void handleCreateTicket(IEvent e) throws InsufficientInformationException {
        EventUtils.requireParm(e, EventConstants.PARM_ALARM_ID);
        EventUtils.requireParm(e, EventConstants.PARM_ALARM_UEI);
        EventUtils.requireParm(e, EventConstants.PARM_USER);

        int alarmId = EventUtils.getIntParm(e, EventConstants.PARM_ALARM_ID, 0);
        Map<String,String> attributes = new HashMap<>();
        for (final IParm parm: e.getParmCollection()) {
        	attributes.put(parm.getParmName(), parm.getValue().getContent());
        }
        
        m_ticketerServiceLayer.createTicketForAlarm(alarmId,attributes);
	}

	/**
	 * Makes call to API to Cancel a Trouble Ticket associated with an OnmsAlarm.
	 * @param e
	 * @throws InsufficientInformationException
	 */
	private void handleCancelTicket(IEvent e) throws InsufficientInformationException {
        EventUtils.requireParm(e, EventConstants.PARM_ALARM_ID);
        EventUtils.requireParm(e, EventConstants.PARM_ALARM_UEI);
        EventUtils.requireParm(e, EventConstants.PARM_USER);
        EventUtils.requireParm(e, EventConstants.PARM_TROUBLE_TICKET);

        int alarmId = EventUtils.getIntParm(e, EventConstants.PARM_ALARM_ID, 0);
        String ticketId = EventUtils.getParm(e, EventConstants.PARM_TROUBLE_TICKET);
        
        m_ticketerServiceLayer.cancelTicketForAlarm(alarmId, ticketId);
	}

    private boolean isReloadConfigEvent(IEvent event) {
        boolean isTarget = false;
        if (EventConstants.RELOAD_DAEMON_CONFIG_UEI.equals(event.getUei())) {
            List<IParm> parmCollection = event.getParmCollection();
            for (IParm parm : parmCollection) {
                if (EventConstants.PARM_DAEMON_NAME.equals(parm.getParmName()) && "Ticketd".equalsIgnoreCase(parm.getValue().getContent())) {
                    isTarget = true;
                    break;
                }
            }
        }
        return isTarget;
    }
    
    public static String getLoggingCategory() {
        return LOG4J_CATEGORY;
    }
}
