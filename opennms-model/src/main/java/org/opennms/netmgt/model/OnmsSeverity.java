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


package org.opennms.netmgt.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Embeddable;

/**
 * OpenNMS severity enumeration.
 * 
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 */
@Embeddable
public enum OnmsSeverity implements Serializable {
    // Keep this ordered by ID so we can use the internal enum compareTo
    INDETERMINATE(1, "Indeterminate", "lightblue"),
    CLEARED(2, "Cleared", "white"),
    NORMAL(3, "Normal", "green"),
    WARNING(4, "Warning", "cyan"),
    MINOR(5, "Minor", "yellow"),
    MAJOR(6, "Major", "orange"),
    CRITICAL(7, "Critical", "red");
    
    private static final Map<Integer, OnmsSeverity> m_idMap; 
    private static final List<Integer> m_ids;
    
    private int m_id;
    private String m_label;
    private String m_color;

    static {
        m_ids = new ArrayList<Integer>(values().length);
        m_idMap = new HashMap<Integer, OnmsSeverity>(values().length);
        for (OnmsSeverity severity : values()) {
            m_ids.add(severity.getId());
            m_idMap.put(severity.getId(), severity);
        }
    }

    private OnmsSeverity(int id, String label, String color) {
        m_id = id;
        m_label = label;
        m_color = color;
    }
    
    public int getId() {
        return m_id;
    }
    
    public String getLabel() {
        return m_label;
    }

    public String getColor() {
        return m_color;
    }

    public boolean isLessThan(OnmsSeverity other) {
        return compareTo(other) < 0;
    }

    public boolean isLessThanOrEqual(OnmsSeverity other) {
        return compareTo(other) <= 0;
    }

    public boolean isGreaterThan(OnmsSeverity other) {
        return compareTo(other) > 0;
    }
    
    public boolean isGreaterThanOrEqual(OnmsSeverity other) {
        return compareTo(other) >= 0;
    }
    
    public static OnmsSeverity get(int id) {
        if (m_idMap.containsKey(id)) {
            return m_idMap.get(id);
        } else {
            throw new IllegalArgumentException("Cannot create OnmsSeverity from unknown ID " + id);
        }
    }
    
    public static OnmsSeverity escalate(OnmsSeverity sev) {
        if (sev.isLessThan(OnmsSeverity.CRITICAL)) {
            return OnmsSeverity.get(sev.getId()+1);
        } else {
            return OnmsSeverity.get(sev.getId());
        }
    }
}
