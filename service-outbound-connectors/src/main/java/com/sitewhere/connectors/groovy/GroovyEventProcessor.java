/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.sitewhere.connectors.groovy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sitewhere.connectors.FilteredOutboundConnector;
import com.sitewhere.rest.model.device.event.request.scripting.DeviceEventRequestBuilder;
import com.sitewhere.rest.model.device.event.scripting.DeviceEventSupport;
import com.sitewhere.rest.model.device.request.scripting.DeviceManagementRequestBuilder;
import com.sitewhere.spi.SiteWhereException;
import com.sitewhere.spi.device.IDevice;
import com.sitewhere.spi.device.IDeviceAssignment;
import com.sitewhere.spi.device.event.IDeviceAlert;
import com.sitewhere.spi.device.event.IDeviceCommandInvocation;
import com.sitewhere.spi.device.event.IDeviceCommandResponse;
import com.sitewhere.spi.device.event.IDeviceEvent;
import com.sitewhere.spi.device.event.IDeviceEventContext;
import com.sitewhere.spi.device.event.IDeviceLocation;
import com.sitewhere.spi.device.event.IDeviceMeasurements;
import com.sitewhere.spi.device.event.IDeviceStateChange;
import com.sitewhere.spi.server.lifecycle.ILifecycleProgressMonitor;

import groovy.lang.Binding;

/**
 * Outbound event processor that uses a Groovy script to process events.
 * 
 * @author Derek
 */
public class GroovyEventProcessor extends FilteredOutboundConnector {

    /** Static logger instance */
    private static Log LOGGER = LogFactory.getLog(GroovyEventProcessor.class);

    /** Supports building device management entities */
    private DeviceManagementRequestBuilder deviceBuilder;

    /** Supports building various types of device events */
    private DeviceEventRequestBuilder eventsBuilder;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sitewhere.device.event.processor.FilteredOutboundEventProcessor#start
     * (com.sitewhere.spi.server.lifecycle.ILifecycleProgressMonitor)
     */
    @Override
    public void start(ILifecycleProgressMonitor monitor) throws SiteWhereException {
	// Required for filters.
	super.start(monitor);

	this.deviceBuilder = new DeviceManagementRequestBuilder(getDeviceManagement());
	this.eventsBuilder = new DeviceEventRequestBuilder(getDeviceManagement(), getDeviceEventManagement());
    }

    /*
     * @see com.sitewhere.outbound.FilteredOutboundEventProcessor#
     * onMeasurementsNotFiltered(com.sitewhere.spi.device.event.IDeviceEventContext,
     * com.sitewhere.spi.device.event.IDeviceMeasurements)
     */
    @Override
    public void onMeasurementsNotFiltered(IDeviceEventContext context, IDeviceMeasurements measurements)
	    throws SiteWhereException {
	processEvent(measurements);
    }

    /*
     * @see
     * com.sitewhere.outbound.FilteredOutboundEventProcessor#onLocationNotFiltered(
     * com.sitewhere.spi.device.event.IDeviceEventContext,
     * com.sitewhere.spi.device.event.IDeviceLocation)
     */
    @Override
    public void onLocationNotFiltered(IDeviceEventContext context, IDeviceLocation location) throws SiteWhereException {
	processEvent(location);
    }

    /*
     * @see
     * com.sitewhere.outbound.FilteredOutboundEventProcessor#onAlertNotFiltered(com.
     * sitewhere.spi.device.event.IDeviceEventContext,
     * com.sitewhere.spi.device.event.IDeviceAlert)
     */
    @Override
    public void onAlertNotFiltered(IDeviceEventContext context, IDeviceAlert alert) throws SiteWhereException {
	processEvent(alert);
    }

    /*
     * @see com.sitewhere.outbound.FilteredOutboundEventProcessor#
     * onStateChangeNotFiltered(com.sitewhere.spi.device.event.IDeviceEventContext,
     * com.sitewhere.spi.device.event.IDeviceStateChange)
     */
    @Override
    public void onStateChangeNotFiltered(IDeviceEventContext context, IDeviceStateChange state)
	    throws SiteWhereException {
	processEvent(state);
    }

    /*
     * @see com.sitewhere.outbound.FilteredOutboundEventProcessor#
     * onCommandInvocationNotFiltered(com.sitewhere.spi.device.event.
     * IDeviceEventContext, com.sitewhere.spi.device.event.IDeviceCommandInvocation)
     */
    @Override
    public void onCommandInvocationNotFiltered(IDeviceEventContext context, IDeviceCommandInvocation invocation)
	    throws SiteWhereException {
	processEvent(invocation);
    }

    /*
     * @see com.sitewhere.outbound.FilteredOutboundEventProcessor#
     * onCommandResponseNotFiltered(com.sitewhere.spi.device.event.
     * IDeviceEventContext, com.sitewhere.spi.device.event.IDeviceCommandResponse)
     */
    @Override
    public void onCommandResponseNotFiltered(IDeviceEventContext context, IDeviceCommandResponse response)
	    throws SiteWhereException {
	processEvent(response);
    }

    /**
     * Perform custom event processing in a Groovy script.
     * 
     * @param event
     * @throws SiteWhereException
     */
    protected void processEvent(IDeviceEvent event) throws SiteWhereException {
	// These should be cached, so no performance hit.
	IDeviceAssignment assignment = getDeviceManagement().getDeviceAssignment(event.getDeviceAssignmentId());
	IDevice device = getDeviceManagement().getDevice(assignment.getDeviceId());

	// Create Groovy binding with handles to everything.
	Binding binding = new Binding();
	binding.setVariable("logger", getLogger());
	binding.setVariable("event", new DeviceEventSupport(assignment, event));
	binding.setVariable("assignment", assignment);
	binding.setVariable("device", device);
	binding.setVariable("deviceBuilder", deviceBuilder);
	binding.setVariable("eventBuilder", eventsBuilder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sitewhere.spi.server.lifecycle.ILifecycleComponent#getLogger()
     */
    @Override
    public Log getLogger() {
	return LOGGER;
    }
}