/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Application's settings.
 */
public final class Configuration {

    /**
     * The one and only one instance.
     */
    private static Configuration instance = null;

    /**
     * The last local path from where a DSD file was loaded. This may be <code>null</code>.
     */
    private File lastDSDPath = null;

    /**
     * The configuration regarding SDMX Registry instances.
     */
    private List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();

    /**
     * Constructs a new <code>Configuration</code> instance.
     */
    private Configuration() {
    }

    /**
     * Getter for the one and only one instance.
     *
     * @return the instance
     */
    public static synchronized Configuration getInstance() {
        if (Configuration.instance == null) {
            Configuration.instance = new Configuration();
            Configuration.instance.load();
        }

        return Configuration.instance;
    }

    /**
     * Getter for the {@link #lastDSDPath} property.
     *
     * @return the value
     */
    public File getLastDSDPath() {
        return this.lastDSDPath;
    }

    /**
     * Setter for the {@link #lastDSDPath} property.
     *
     * @param value the value
     */
    public void setLastDSDPath(File value) {
        this.lastDSDPath = value;
        this.save();
    }

    /**
     * Getter for {@link #registryConfigs} property.
     *
     * @return the value
     */
    public List<RegistryConfig> getRegistryConfigs() {
        return this.registryConfigs;
    }

    /**
     * Finds a SDMX Registry configuration.
     *
     * @param name the name of the configuration to find
     *
     * @return the configuration, <code>null</code> if there's none for specified name
     */
    public RegistryConfig findRegistryConfig(String name) {
        for (RegistryConfig cfg : this.registryConfigs) {
            if (cfg.getName().equals(name)) {
                return cfg;
            }
        }

        return null;
    }

    /**
     * Adds a new SDMX Registry configuration.
     *
     * @param cfg the config
     */
    public void saveRegistryConfig(RegistryConfig cfg) {
        if (StringUtils.trimToNull(cfg.getName()) == null) {
            throw new IllegalArgumentException("Cannot save a SDMX Registry configuration without a valid name!");
        }
        RegistryConfig oldCfg = this.findRegistryConfig(cfg.getName());

        if (oldCfg != null) {
            int index = this.registryConfigs.indexOf(oldCfg);
            this.registryConfigs.set(index, cfg);
        } else {
            this.registryConfigs.add(cfg);
        }

        this.save();
    }

    /**
     * Deletes an existing SDMX Registry configuration.
     *
     * @param name the name
     */
    public void deleteRegistryConfig(String name) {
        RegistryConfig cfg = this.findRegistryConfig(name);

        if (cfg != null) {
            this.registryConfigs.remove(cfg);
            this.save();
        }
    }

    /**
     * Loads application's settings from configuration file.
     */
    private void load() {
        Properties properties = new Properties();

        FileReader reader = null;
        try {
            reader = new FileReader(Constants.CONFIGURATION_FILE);
            properties.load(reader);
        } catch (FileNotFoundException e) {
            // nothing
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            IOUtils.closeQuietly(reader);
        }

        this.lastDSDPath = this.getPath(properties, "last.dsd.path");

        for (int i = 1; ; i++) {
            RegistryConfig cfg = this.getRegistryConfig(properties, i);

            if (cfg != null) {
                this.registryConfigs.add(cfg);
            } else {
                break;
            }
        }
    }

    /**
     * Retrieves the data for a SDMX Registry configuration.
     *
     * @param properties the properties
     * @param index      the index of the configuration to read (starts from 1)
     *
     * @return the configuration bean, <code>null</code> if fails
     */
    private RegistryConfig getRegistryConfig(Properties properties, int index) {
        RegistryConfig cfg = new RegistryConfig();
        String propertiesPrefix = "registry.config." + index;

        cfg.setName(StringUtils.trimToNull(properties.getProperty(propertiesPrefix)));
        cfg.setDomain(StringUtils.trimToNull(properties.getProperty(propertiesPrefix + ".domain")));
        cfg.setUsername(StringUtils.trimToNull(properties.getProperty(propertiesPrefix + ".username")));
        cfg.setPassword(StringUtils.trimToNull(properties.getProperty(propertiesPrefix + ".password")));
        cfg.setURL(this.getURL(properties, propertiesPrefix + ".url"));
        cfg.setProxyHost(StringUtils.trimToNull(properties.getProperty(propertiesPrefix + ".proxy.host")));
        try {
            cfg.setProxyPort(Integer.parseInt(properties.getProperty(propertiesPrefix + ".proxy.port")));
        } catch (NumberFormatException e) {
            return null;
        }
        cfg.setProxyUsername(StringUtils.trimToNull(
                properties.getProperty(propertiesPrefix + ".proxy.username")));
        cfg.setProxyPassword(StringUtils.trimToNull(
                properties.getProperty(propertiesPrefix + ".proxy.password")));

        return cfg.isValid() ? cfg : null;
    }

    /**
     * Saves the current settings to configuration file.
     */
    private void save() {
        Properties properties = new Properties();

        if (this.lastDSDPath != null) {
            properties.put("last.dsd.path", this.lastDSDPath.getPath());
        }

        for (int i = 0; i < this.registryConfigs.size(); i++) {
            this.putRegistryConfig(properties, this.registryConfigs.get(i), i + 1);
        }

        Writer writer = null;

        try {
            writer = new FileWriter(Constants.CONFIGURATION_FILE);
            properties.store(writer, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * Puts the data about a SDMX Registry configuration into specified <code>Properties</code> instance.
     *
     * @param properties the properties where to save configuration's data
     * @param cfg        the configuration bean
     * @param index      the index of the configuration
     */
    private void putRegistryConfig(Properties properties, RegistryConfig cfg, int index) {
        String propertiesPrefix = "registry.config." + index;

        properties.put(propertiesPrefix, cfg.getName());
        properties.put(propertiesPrefix + ".domain", cfg.getDomain());
        properties.put(propertiesPrefix + ".username", cfg.getUsername());
        properties.put(propertiesPrefix + ".password", cfg.getPassword());
        properties.put(propertiesPrefix + ".url", "" + cfg.getURL());

        properties.put(propertiesPrefix + ".proxy.host", StringUtils.trimToEmpty(cfg.getProxyHost()));
        properties.put(propertiesPrefix + ".proxy.port", "" + cfg.getProxyPort());
        properties.put(propertiesPrefix + ".proxy.username", StringUtils.trimToEmpty(cfg.getProxyUsername()));
        properties.put(propertiesPrefix + ".proxy.password",
                StringUtils.trimToEmpty(cfg.getProxyPassword()));
    }

    /**
     * Getter for a file property.
     *
     * @param properties the properties
     * @param key        the name of the property
     *
     * @return the file, <code>null</code> if property is missing or is invalid
     */
    private File getPath(Properties properties, String key) {
        String value = properties.getProperty(key);

        if (value != null) {
            File file = new File(value);
            return file.isDirectory() ? file : null;
        }

        return null;
    }

    /**
     * Getter for a URL property.
     *
     * @param properties the properties
     * @param key        the name of the property
     *
     * @return the URL, <code>null</code> if the property is missing or is invalid
     */
    private URL getURL(Properties properties, String key) {
        String value = properties.getProperty(key);

        if (value != null) {
            try {
                return new URL(value);
            } catch (Exception e) {
                //
            }
        }

        return null;
    }
}
