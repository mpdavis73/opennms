/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2002-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.opennms.core.db.DataSourceFactory;
import org.opennms.core.utils.ConfigFileConstants;
import org.opennms.netmgt.config.notificationCommands.NotificationCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * <p>NotificationCommandFactory class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class NotificationCommandFactory extends NotificationCommandManager {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationCommandFactory.class);

    public static final String CONFIG_NAME = "notificationCommands";

    public static final String DEFAULT_CONFIG_ID = "default";

    /**
     */
    private static NotificationCommandFactory instance;

    /**
     * Boolean indicating if the init() method has been called
     */
    private static boolean initialized = false;

    /**
     * 
     */
    public NotificationCommandFactory() {
        super();
    }

    public NotificationCommandFactory(NotificationCommands config) {
        super(config);
    }

    @PostConstruct
    public void postConstruct() throws IOException {
    }
    /**
     * <p>init</p>
     *
     * @throws java.io.IOException if any.
     */
    public static synchronized void init() throws IOException {
        if (!initialized) {
            getInstance().update();
            initialized = true;
        }
    }

    /**
     * <p>Getter for the field <code>instance</code>.</p>
     *
     * @return a {@link org.opennms.netmgt.config.NotificationCommandFactory} object.
     */
    public static synchronized NotificationCommandFactory getInstance() {

        if (instance == null || !initialized) {
            instance = new NotificationCommandFactory();
        }

        return instance;
    }
    
    /**
     * <p>update</p>
     *
     * @throws java.io.FileNotFoundException if any.
     * @throws java.io.IOException if any.
     */
    @Override
    public void update() throws FileNotFoundException, IOException {
        InputStream configIn = new FileInputStream(ConfigFileConstants.getFile(ConfigFileConstants.NOTIF_COMMANDS_CONF_FILE_NAME));
        parseXML(configIn);
    }

    @Override
    public String getConfigName() {
        return CONFIG_NAME;
    }

    @Override
    protected String getDefaultConfigId() {
        return DEFAULT_CONFIG_ID;
    }
}
