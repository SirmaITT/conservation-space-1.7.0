package com.sirma.itt.cmf.services.observers;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.sirma.itt.cmf.constants.TaskProperties;
import com.sirma.itt.cmf.event.task.worklog.AddedWorkLogEntryEvent;
import com.sirma.itt.cmf.event.task.worklog.DeleteWorkLogEntryEvent;
import com.sirma.itt.cmf.event.task.worklog.UpdatedWorkLogEntryEvent;
import com.sirma.itt.seip.configuration.Options;
import com.sirma.itt.seip.domain.instance.DefaultProperties;
import com.sirma.itt.seip.domain.instance.Instance;
import com.sirma.itt.seip.domain.security.ActionTypeConstants;
import com.sirma.itt.seip.event.EventService;
import com.sirma.itt.seip.instance.dao.ServiceRegistry;
import com.sirma.itt.seip.instance.event.InstanceEventProvider;
import com.sirma.itt.seip.instance.properties.PropertiesService;
import com.sirma.itt.seip.instance.state.Operation;

/**
 * An asynchronous update interface for receiving notifications about TaskLogWork information as the TaskLogWork is
 * constructed.
 */
@ApplicationScoped
public class TaskLogWorkObserver {

	/** The Constant ADD. */
	private static final Operation ADD = new Operation(ActionTypeConstants.LOG_WORK);
	/** The Constant UPDATE. */
	private static final Operation UPDATE = new Operation(ActionTypeConstants.UPDATE_LOGGED_WORK);
	/** The Constant DELETE. */
	private static final Operation DELETE = new Operation(ActionTypeConstants.DELETE_LOGGED_WORK);

	/** The Constant CHANGED_PROPERTIES. */
	private static final Set<String> CHANGED_PROPERTIES = new HashSet<>(DefaultProperties.DEFAULT_HEADERS);

	static {
		CHANGED_PROPERTIES.add(DefaultProperties.ACTUAL_EFFORT);
	}

	/** The properties service. */
	@Inject
	private PropertiesService propertiesService;
	/** The service register. */
	@Inject
	private ServiceRegistry serviceRegistry;
	/** The event service. */
	@Inject
	private EventService eventService;

	/**
	 * This method is called when a {@link AddedWorkLogEntryEvent} is observed. It accumulates task's actualEffort based
	 * on the logged work.
	 *
	 * @param event
	 *            the event
	 */
	public void onAddedWorkLog(@Observes AddedWorkLogEntryEvent event) {
		Map<String, Serializable> workLogData = event.getProperties();
		if (workLogData != null) {
			Serializable timeSpent = workLogData.get(TaskProperties.TIME_SPENT);
			if (timeSpent instanceof Number) {
				Instance instance = event.getInstanceRef().toInstance();
				accumulateWorkLogOnTask(instance, ((Number) timeSpent).longValue(), ADD);
			}
		}
	}

	/**
	 * This method is called when a {@link UpdatedWorkLogEntryEvent} is observed. It calculates task's actualEffort
	 * based on the logged work.
	 *
	 * @param event
	 *            the event
	 */
	public void onUpdatedWorkLog(@Observes UpdatedWorkLogEntryEvent event) {
		Map<String, Serializable> newLoggedData = event.getNewLoggedData();
		Map<String, Serializable> oldLoggedData = event.getOldLoggedData();
		if (newLoggedData != null && oldLoggedData != null) {
			Serializable newTimeSpent = newLoggedData.get(TaskProperties.TIME_SPENT);
			Serializable oldTimeSpent = oldLoggedData.get(TaskProperties.TIME_SPENT);
			if (newTimeSpent instanceof Number && oldTimeSpent instanceof Number) {
				long timeSpent = ((Number) newTimeSpent).longValue() - ((Number) oldTimeSpent).longValue();
				Instance instance = event.getInstanceRef().toInstance();
				accumulateWorkLogOnTask(instance, timeSpent, UPDATE);
			}
		}
	}

	/**
	 * This method is called when a {@link DeleteWorkLogEntryEvent} is observed. It calculates task's actualEffort based
	 * on the logged work.
	 *
	 * @param event
	 *            the event
	 */
	public void onDeletedWorkLog(@Observes DeleteWorkLogEntryEvent event) {
		Map<String, Serializable> workLogData = event.getWorkLogData();
		if (workLogData != null) {
			Serializable timeSpent = workLogData.get(TaskProperties.TIME_SPENT);
			if (timeSpent instanceof Number) {
				Instance instance = event.getInstanceRef().toInstance();
				accumulateWorkLogOnTask(instance, -((Number) timeSpent).longValue(), DELETE);
			}
		}
	}

	/**
	 * Accumulates time in hours to task's current actualEffort.
	 *
	 * @param instance
	 *            the instance ref
	 * @param timeToAdd
	 *            the time to add. Can be negative on log work update or delete.
	 * @param operation
	 *            the operation that leaded to the change.
	 */
	private void accumulateWorkLogOnTask(Instance instance, long timeToAdd, Operation operation) {
		Map<String, Serializable> taskProperties = instance.getProperties();
		if (taskProperties != null) {
			Serializable actualEffort = taskProperties.get(DefaultProperties.ACTUAL_EFFORT);
			long updatedActualEffort = 0L;
			if (actualEffort instanceof Long) {
				updatedActualEffort = (Long) actualEffort;
			}
			updatedActualEffort += timeToAdd;
			// Actual effort couldn't be negative value
			if (updatedActualEffort >= 0L) {
				// update the property and save the updated instance - this will update the semantic
				// and relational db and trigger logging the operation in the audit log
				taskProperties.put(DefaultProperties.ACTUAL_EFFORT, updatedActualEffort);
				refreshInstanceHeaders(instance);
				// leave only the changed properties (headers and effort)
				taskProperties.keySet().retainAll(CHANGED_PROPERTIES);
				// set the current operation for audit log
				Options.CURRENT_OPERATION.set(operation);
				try {
					propertiesService.saveProperties(instance, true);
				} finally {
					Options.CURRENT_OPERATION.clear();
				}
			}
		}
	}

	/**
	 * Fires event for instance change to trigger header generation on the instance.
	 *
	 * @param instance
	 *            the instance
	 */
	private void refreshInstanceHeaders(Instance instance) {
		InstanceEventProvider<Instance> eventProvider = serviceRegistry.getEventProvider(instance);
		if (eventProvider != null) {
			eventService.fire(eventProvider.createChangeEvent(instance));
		}
	}

}