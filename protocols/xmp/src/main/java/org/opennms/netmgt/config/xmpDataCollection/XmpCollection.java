/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.config.xmpDataCollection;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

/**
 * a grouping of XMP related RRD parms, MIB object groups
 *  and sysoid based system definitions.
 * 
 * @version $Revision$ $Date$
 */

@SuppressWarnings("all") public class XmpCollection implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * collectoion name
     */
    private java.lang.String _name;

    /**
     * indicates if collected XMP data is to be stored for
     *  "all" interfaces or only for the "primary" interface.
     *  Currently ignored. 
     */
    private java.lang.String _xmpStorageFlag;

    /**
     * RRD parms
     */
    private org.opennms.netmgt.config.xmpDataCollection.Rrd _rrd;

    /**
     * MIB object groups
     */
    private org.opennms.netmgt.config.xmpDataCollection.Groups _groups;


      //----------------/
     //- Constructors -/
    //----------------/

    public XmpCollection() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Overrides the java.lang.Object.equals method.
     * 
     * @param obj
     * @return true if the objects are equal.
     */
    @Override()
    public boolean equals(
            final java.lang.Object obj) {
        if ( this == obj )
            return true;
        
        if (obj instanceof XmpCollection) {
        
            XmpCollection temp = (XmpCollection)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._xmpStorageFlag != null) {
                if (temp._xmpStorageFlag == null) return false;
                else if (!(this._xmpStorageFlag.equals(temp._xmpStorageFlag))) 
                    return false;
            }
            else if (temp._xmpStorageFlag != null)
                return false;
            if (this._rrd != null) {
                if (temp._rrd == null) return false;
                else if (!(this._rrd.equals(temp._rrd))) 
                    return false;
            }
            else if (temp._rrd != null)
                return false;
            if (this._groups != null) {
                if (temp._groups == null) return false;
                else if (!(this._groups.equals(temp._groups))) 
                    return false;
            }
            else if (temp._groups != null)
                return false;
            return true;
        }
        return false;
    }

    /**
     * Returns the value of field 'groups'. The field 'groups' has
     * the following description: MIB object groups
     * 
     * @return the value of field 'Groups'.
     */
    public org.opennms.netmgt.config.xmpDataCollection.Groups getGroups(
    ) {
        return this._groups;
    }

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: collectoion name
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName(
    ) {
        return this._name;
    }

    /**
     * Returns the value of field 'rrd'. The field 'rrd' has the
     * following description: RRD parms
     * 
     * @return the value of field 'Rrd'.
     */
    public org.opennms.netmgt.config.xmpDataCollection.Rrd getRrd(
    ) {
        return this._rrd;
    }

    /**
     * Returns the value of field 'xmpStorageFlag'. The field
     * 'xmpStorageFlag' has the following description: indicates if
     * collected XMP data is to be stored for
     *  "all" interfaces or only for the "primary" interface.
     *  Currently ignored. 
     * 
     * @return the value of field 'XmpStorageFlag'.
     */
    public java.lang.String getXmpStorageFlag(
    ) {
        return this._xmpStorageFlag;
    }

    /**
     * Overrides the java.lang.Object.hashCode method.
     * <p>
     * The following steps came from <b>Effective Java Programming
     * Language Guide</b> by Joshua Bloch, Chapter 3
     * 
     * @return a hash code value for the object.
     */
    @Override
    public int hashCode(
    ) {
        int result = 17;
        
        long tmp;
        if (_name != null) {
           result = 37 * result + _name.hashCode();
        }
        if (_xmpStorageFlag != null) {
           result = 37 * result + _xmpStorageFlag.hashCode();
        }
        if (_rrd != null) {
           result = 37 * result + _rrd.hashCode();
        }
        if (_groups != null) {
           result = 37 * result + _groups.hashCode();
        }
        
        return result;
    }

    /**
     * Sets the value of field 'groups'. The field 'groups' has the
     * following description: MIB object groups
     * 
     * @param groups the value of field 'groups'.
     */
    public void setGroups(
            final org.opennms.netmgt.config.xmpDataCollection.Groups groups) {
        this._groups = groups;
    }

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: collectoion name
     * 
     * @param name the value of field 'name'.
     */
    public void setName(
            final java.lang.String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'rrd'. The field 'rrd' has the
     * following description: RRD parms
     * 
     * @param rrd the value of field 'rrd'.
     */
    public void setRrd(
            final org.opennms.netmgt.config.xmpDataCollection.Rrd rrd) {
        this._rrd = rrd;
    }

    /**
     * Sets the value of field 'xmpStorageFlag'. The field
     * 'xmpStorageFlag' has the following description: indicates if
     * collected XMP data is to be stored for
     *  "all" interfaces or only for the "primary" interface.
     *  Currently ignored. 
     * 
     * @param xmpStorageFlag the value of field 'xmpStorageFlag'.
     */
    public void setXmpStorageFlag(
            final java.lang.String xmpStorageFlag) {
        this._xmpStorageFlag = xmpStorageFlag;
    }

}