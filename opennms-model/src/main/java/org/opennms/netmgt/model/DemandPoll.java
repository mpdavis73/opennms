/*******************************************************************************
 * This file is part of the OpenNMS(R) Application.
 *
 * Copyright (C) 2006-2008 The OpenNMS Group, Inc.  All rights reserved.
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

package org.opennms.netmgt.model;

import java.util.Date;
import java.util.Set;

public class DemandPoll {
	
	private Integer m_id;
	private Date m_requestTime;
	private String m_userName;
	private String m_description;
	private Set<PollResult> m_pollResults;
	
	public DemandPoll() {
		
	}
	
	public String getDescription() {
		return m_description;
	}
	public void setDescription(String description) {
		m_description = description;
	}
	public Integer getId() {
		return m_id;
	}
	public void setId(int id) {
		m_id = id;
	}
	public void setId(Integer id) {
		m_id = id;
	}
	public Set<PollResult> getPollResults() {
		return m_pollResults;
	}
	public void setPollResults(Set<PollResult> pollResults) {
		m_pollResults = pollResults;
	}
	public Date getRequestTime() {
		return m_requestTime;
	}
	public void setRequestTime(Date requestTime) {
		m_requestTime = requestTime;
	}
	public String getUserName() {
		return m_userName;
	}
	public void setUserName(String user) {
		m_userName = user;
	}
	
}
