package org.com.wso2.ushan.window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.com.wso2.ushan.algorithm.Algoritham;
import org.com.wso2.ushan.bean.User;
import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.event.StreamEvent;
import org.wso2.siddhi.core.event.in.InEvent;
import org.wso2.siddhi.core.event.in.InListEvent;
import org.wso2.siddhi.core.query.QueryPostProcessingElement;
import org.wso2.siddhi.core.query.processor.window.WindowProcessor;
import org.wso2.siddhi.query.api.definition.AbstractDefinition;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.expression.Variable;
import org.wso2.siddhi.query.api.extension.annotation.SiddhiExtension;

@SiddhiExtension(namespace = "custom", function = "getCordinate")
public class CEP_Window extends WindowProcessor {

	String variableUID = "";
	String variableSID = "";
	String variableDISTANCE = "";

	int variableUIDPosition = 0;
	int variableSIDPosition = 0;
	int variableDISPosition = 0;
	int noValue = 0;
	private Map<String, ArrayList<User>> userDetails = null;

	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processEvent(InEvent event) {
		try {
			doProcess(event);
        } finally
        {
            releaseLock();
        }
	}

	@Override
	protected void processEvent(InListEvent listEvent) {
		for (int i = 0; i < listEvent.getActiveEvents(); i++) {
			InEvent inEvent = (InEvent) listEvent.getEvent(i);
			processEvent(inEvent);
		}

	}

	@Override
	public Iterator<StreamEvent> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<StreamEvent> iterator(String predicate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object[] currentState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void restoreState(Object[] data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init(Expression[] parameters,
			QueryPostProcessingElement nextProcessor,
			AbstractDefinition streamDefinition, String elementId,
			boolean async, SiddhiContext siddhiContext) {
		variableUID = ((Variable) parameters[1]).getAttributeName();
		variableSID = ((Variable) parameters[0]).getAttributeName();
		variableDISTANCE = ((Variable) parameters[2]).getAttributeName();
		//noValue = ((IntConstant) parameters[1]).getValue();
		userDetails = new HashMap<String, ArrayList<User>>();
		variableUIDPosition = streamDefinition
				.getAttributePosition(variableUID);
		variableSIDPosition = streamDefinition
				.getAttributePosition(variableSID);
		variableDISPosition = streamDefinition
				.getAttributePosition(variableDISTANCE);

	}

	private void doProcess(InEvent event) {
		event.getData(variableUIDPosition);
		log.info(event);
		event.getData(variableSIDPosition);
		log.info(event);
		event.getData(variableDISPosition);
		log.info(event);
		String uid = (String) event.getData(variableUIDPosition);
		String sid = (String) event.getData(variableSIDPosition);
		Double distance = (Double) event.getData(variableDISPosition);

		User user = new User();
		user.setDistance(distance);
		user.setSensorId(sid);
		user.setUserID(uid);
		if (userDetails.containsKey(uid)) {
			ArrayList<User> users = userDetails.get(uid);
			users.add(user);
			if (users.size() == 6) {
				userDetails.remove(uid);
				User us = new Algoritham().manageUser(users);
				Object[] objects = { us.getUserID(), us.getX(), us.getY() };
				InEvent inEvent = new InEvent("outputstream",
						System.currentTimeMillis(), objects);
				nextProcessor.process(inEvent);
			}
		} else {
			ArrayList<User> users = new ArrayList<User>();
			users.add(user);
			userDetails.put(uid, users);
		}

	}

}
