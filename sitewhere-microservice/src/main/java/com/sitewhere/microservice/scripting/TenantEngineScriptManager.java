/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.microservice.scripting;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sitewhere.server.lifecycle.TenantEngineLifecycleComponent;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.microservice.scripting.IScriptManager;
import com.sitewhere.spi.microservice.scripting.IScriptMetadata;

/**
 * Manages runtime scripting support for a tenant engine.
 * 
 * @author Derek
 */
public class TenantEngineScriptManager extends TenantEngineLifecycleComponent implements IScriptManager {

    /** Static logger instance */
    private static Log LOGGER = LogFactory.getLog(TenantEngineScriptManager.class);

    /*
     * @see
     * com.sitewhere.spi.microservice.scripting.IScriptManager#resolve(java.lang.
     * String)
     */
    @Override
    public IScriptMetadata resolve(String scriptId) throws SiteWhereException {
	return null;
    }

    /*
     * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
     */
    @Override
    public Log getLogger() {
	return LOGGER;
    }
}