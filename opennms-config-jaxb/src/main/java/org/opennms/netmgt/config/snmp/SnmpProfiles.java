/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.config.snmp;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.opennms.core.xml.ValidateUsing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="profiles")
@XmlAccessorType(XmlAccessType.NONE)
@ValidateUsing("snmp-config.xsd")
/**
 * Keep the XML annotation is due to existing UI still using xml output
 */
public class SnmpProfiles {

    @XmlElement(name="profile")
    @JsonProperty("profile")
    private List<SnmpProfile> profiles = new ArrayList<>();

    public List<SnmpProfile> getSnmpProfiles() {
        return profiles;
    }

    public void setSnmpProfiles(List<SnmpProfile> snmpProfiles) {
        this.profiles = snmpProfiles;
    }

    public List<SnmpProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<SnmpProfile> profiles) {
        this.profiles = profiles;
    }

    public void addSnmpProfile(SnmpProfile snmpProfile) {
        this.profiles.add(snmpProfile);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SnmpProfiles that = (SnmpProfiles) o;
        return Objects.equals(profiles, that.profiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profiles);
    }
}
