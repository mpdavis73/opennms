/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
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

package org.opennms.systemreport.sanitizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationSanitizer {

    private final Map<String, ConfigFileSanitizer> sanitizers = new HashMap<>();

    @Autowired
    public ConfigurationSanitizer(Collection<ConfigFileSanitizer> configFileSanitizerList) {
        for (ConfigFileSanitizer sanitizer : configFileSanitizerList) {
            sanitizers.put(sanitizer.getFileName(), sanitizer);
        }
    }

    public Resource getSanitizedResource(final File file) {
        ConfigFileSanitizer fileSanitizer = getSanitizer(file.getName());

        if (fileSanitizer != null) {
            try {
                return fileSanitizer.getSanitizedResource(file);
            } catch (FileSanitizationException e) {
                e.getCause().printStackTrace();

                return new ByteArrayResource(e.getMessage().getBytes());
            }
        }

        return new FileSystemResource(file);
    }

    private ConfigFileSanitizer getSanitizer(String fileName) {
        ConfigFileSanitizer fileSanitizer = null;

        if (sanitizers.containsKey(fileName)) {
            fileSanitizer = sanitizers.get(fileName);
        } else if (fileName.contains(".")) {
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            if (sanitizers.containsKey("*" + fileExtension)) {
                fileSanitizer = sanitizers.get("*" + fileExtension);
            }
        }

        return fileSanitizer;
    }
}
