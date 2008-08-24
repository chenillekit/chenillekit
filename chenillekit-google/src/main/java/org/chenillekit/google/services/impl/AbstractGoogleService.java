package org.chenillekit.google.services.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ProxySelector;
import java.net.URLEncoder;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.Defense;

import org.slf4j.Logger;
import sun.net.spi.DefaultProxySelector;

/**
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id$
 */
abstract public class AbstractGoogleService
{
    public static String GOOGLE_KEY_FILE = "google.keyfile";
    public static String GOOGLE_TIMEOUT = "google.timeout";
    public static String GOOGLE_REFERER = "google.referer";
    public static String GOOGLE_PROXY = "google.proxy";

    private final Logger logger;

    /**
     * configuration for the service.
     */
    private Configuration serviceConfiguration;

    /**
     * proxy selector.
     */
    private ProxySelector proxySelector;

    /**
     * your personal google map key.
     */
    private File googleKeyResource;

    /**
     * the configured referer.
     */
    private String referer;

    /**
     * timeout for service request.
     */
    private int timeout;

    /**
     * standard constructor.
     *
     * @param logger         system logger
     * @param configResource file that holds your google map setup
     */
    protected AbstractGoogleService(Logger logger, Resource configResource)
    {
        Defense.notNull(configResource, "configResource");

        this.logger = logger;

        if (!configResource.exists())
            throw new RuntimeException(String.format("config resource '%s' not found!", configResource.toString()));

        initService(configResource);
    }

    /**
     * read and check all service parameters.
     */
    private void initService(Resource configResource)
    {
        try
        {
            serviceConfiguration = new PropertiesConfiguration(configResource.toURL());

            String googleKeyFileName = serviceConfiguration.getString(AbstractGoogleService.GOOGLE_KEY_FILE);
            if (googleKeyFileName.length() == 0)
                throw new RuntimeException(String.format("the property '%s' is not set!", AbstractGoogleService.GOOGLE_KEY_FILE));

            googleKeyResource = new File(googleKeyFileName);
            timeout = serviceConfiguration.getInt(AbstractGoogleService.GOOGLE_TIMEOUT, 30000);
            referer = serviceConfiguration.getString(AbstractGoogleService.GOOGLE_REFERER, "");

            proxySelector = new DefaultProxySelector();
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * get the access key for your map service.
     * <p/>
     * reads a simple ASCII file until finds a line that not starts with the '#' sign
     * and line is not empty.
     * <p/>
     * sample
     * <pre>
     * #
     * # this is the google map key
     * #
     * <p/>
     * ABQIAA ... your key ... KwBzBXU3z7Bk5UOzahnw
     * <p/>
     * </pre>
     *
     * @return the access key
     */
    public String getAccessKey()
    {
        BufferedReader br = null;

        if (!googleKeyResource.exists())
            throw new RuntimeException("the named google key file does not exists: '" + googleKeyResource.toString() + "'!");

        try
        {
            br = new BufferedReader(new InputStreamReader(googleKeyResource.toURL().openStream()));
            String lastReadedLine;
            while ((lastReadedLine = br.readLine()) != null)
            {
                if (!lastReadedLine.trim().startsWith("#") && lastReadedLine.trim().length() > 0)
                    break;
            }
            return lastReadedLine;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                if (br != null)
                    br.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * encode string if not null.
     */
    protected String getEncodedString(String source) throws UnsupportedEncodingException
    {
        if (source == null)
            return "";

        return URLEncoder.encode(source.replace(' ', '+'), "UTF-8");
    }

    /**
     * configuration for the service.
     */
    public Configuration getServiceConfiguration()
    {
        return serviceConfiguration;
    }

    /**
     * proxy selector.
     */
    public ProxySelector getProxySelector()
    {
        return proxySelector;
    }

    /**
     * the configured referer.
     */
    public String getReferer()
    {
        return referer;
    }

    /**
     * timeout for service request.
     */
    public int getTimeout()
    {
        return timeout;
    }
}
