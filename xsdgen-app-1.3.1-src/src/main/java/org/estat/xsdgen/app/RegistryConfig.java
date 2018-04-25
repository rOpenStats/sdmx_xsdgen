/*
 *
 * Copyright (c) 2011 by the European Commission, represented by Eurostat. All rights reserved.
 *
 * Licensed under the European Union Public License (EUPL) version 1.1. If you do not accept this license, you are
 * not allowed to make any use of this file.
 *
 */

package org.estat.xsdgen.app;

import org.estat.xsdgen.sdmx20.XsdGeneratorException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Configuration for a SDMX Registry instance.
 */
public class RegistryConfig implements java.io.Serializable {

    /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
     * The name.
     */
    private String name = null;

    /**
     * The domain.
     */
    private String domain = null;

    /**
     * The username.
     */
    private String username = null;

    /**
     * The password.
     */
    private String password = null;

    /**
     * The URL.
     */
    private URL url = null;

    /**
     * The proxy host. This may be <code>null</code> if no proxy is set.
     */
    private String proxyHost = null;

    /**
     * The proxy port.
     */
    private int proxyPort = 80;

    /**
     * The username to be used for proxy authentication. May be <code>null</code>.
     */
    private String proxyUsername = null;

    /**
     * The password to be used for proxy authentication.
     */
    private String proxyPassword = null;

    /**
     * Constructs a new <code>RegistryConfig</code> instance.
     */
    public RegistryConfig() {
    }

    /**
     * @return <code>true</code> if the configuration seems to be valid
     */
    public boolean isValid() {
        return (this.name != null) && (this.domain != null) && (this.username != null)
                && (this.password != null) && (this.url != null);
    }

    /**
     * Getter for {@link #name} property.
     *
     * @return the value
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for {@link #name} property.
     *
     * @param value the value
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Getter for {@link #domain} property.
     *
     * @return the value
     */
    public String getDomain() {
        return this.domain;
    }

    /**
     * Setter for {@link #domain} property.
     *
     * @param value the value
     */
    public void setDomain(String value) {
        this.domain = value;
    }

    /**
     * Getter for {@link #username} property.
     *
     * @return the value
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Setter for {@link #username} property.
     *
     * @param value the value
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Getter for {@link #password} property.
     *
     * @return the value
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Setter for {@link #password} property.
     *
     * @param value the value
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Getter for {@link #url} property.
     *
     * @return the value
     */
    public URL getURL() {
        return this.url;
    }

    /**
     * Setter for {@link #url} property.
     *
     * @param value the value
     */
    public void setURL(URL value) {
        this.url = value;
    }

    /**
     * Setter for {@link #url} property, as a <code>string</code> object.
     *
     * @param value the value
     */
    public void setUrlAsString(String value) {
        try {
            this.url = new URL(value);
        } catch (MalformedURLException e) {
            throw new XsdGeneratorException("Invalid URL: " + value, e);
        }
    }

    /**
     * Getter for {@link #proxyHost} property.
     *
     * @return the value
     */
    public String getProxyHost() {
        return this.proxyHost;
    }

    /**
     * Setter for {@link #setProxyHost} property.
     *
     * @param value the value
     */
    public void setProxyHost(String value) {
        this.proxyHost = value;
    }

    /**
     * Getter for {@link #proxyPort} property.
     *
     * @return the value
     */
    public int getProxyPort() {
        return this.proxyPort;
    }

    /**
     * Setter for {@link #proxyPort} property.
     *
     * @param value the value
     */
    public void setProxyPort(int value) {
        this.proxyPort = value;
    }

    /**
     * Getter for {@link #proxyUsername} property.
     *
     * @return the value
     */
    public String getProxyUsername() {
        return this.proxyUsername;
    }

    /**
     * Setter for {@link #proxyUsername} property.
     *
     * @param value the value
     */
    public void setProxyUsername(String value) {
        this.proxyUsername = value;
    }

    /**
     * Getter for {@link #proxyPassword} property.
     *
     * @return the value
     */
    public String getProxyPassword() {
        return this.proxyPassword;
    }

    /**
     * Setter for {@link #proxyPassword} property.
     *
     * @param value the value
     */
    public void setProxyPassword(String value) {
        this.proxyPassword = value;
    }
}
