package org.com.wso2.ushan.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.config.SiddhiConfiguration;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;

public class CEPTest {

	private int count;
	private double betaZero;
	private static HashMap<String, Double[]> sensors;

	@Before
	public void init() {
		count = 0;
	}

	@Test
	public void testRegression() throws InterruptedException {

		SiddhiConfiguration siddhiConfiguration = new SiddhiConfiguration();

		List<Class> list = new ArrayList<Class>();
		list.add(org.com.wso2.ushan.window.CEP_Window.class);

		siddhiConfiguration.setSiddhiExtensions(list);
		SiddhiManager siddhiManager = new SiddhiManager(siddhiConfiguration);

		siddhiManager
				.defineStream("define stream cseEventStream (sid string, did string,distance double)");
		String queryReference = siddhiManager.addQuery("from cseEventStream#window.custom:getCordinate(sid,did,distance)"
				+ "insert into outputstream ;");
		siddhiManager.addCallback(queryReference, new QueryCallback() {
			@Override
			public void receive(long timeStamp, Event[] inEvents,
					Event[] removeEvents) {
				EventPrinter.print(timeStamp, inEvents, removeEvents);
			}
		});

		sensors = new HashMap<String, Double[]>();
		sensors.put("000", new Double[] { 0.0, 0.0 });
		sensors.put("001", new Double[] { 0.0, 100.0 });
		sensors.put("002", new Double[] { 0.0, 200.0 });
		sensors.put("003", new Double[] { 180.0, 0.0 });
		sensors.put("004", new Double[] { 180.0, 100.0 });
		sensors.put("005", new Double[] { 180.0, 200.0 });

		Random random = new Random();

		InputHandler inputHandler = siddhiManager
				.getInputHandler("cseEventStream");

		for (int i = 0; i < 5; i++) {
			String did = String.valueOf(i);

			double x = random.nextInt(200);
			double y = random.nextInt(200);
			System.out.println(i+"Users real location x: "+x+"y: "+y);
			HashMap<String, Double> map = new HashMap<String, Double>();
			map = generateUserCordinate(x, y);

			for (int j = 0; j < map.size(); j++) {
				inputHandler.send(new Object[] { "00" + String.valueOf(j),
						String.valueOf(i), map.get("00" + j) });
			}
		}

		Thread.sleep(1500);
		siddhiManager.shutdown();
	}

	private static HashMap<String, Double> generateUserCordinate(double x,
			double y) {
		double dx, dy, r = 0.0;
		HashMap<String, Double> map = new HashMap<String, Double>();

		for (int i = 0; i < sensors.size(); i++) {
			Double[] doubles = sensors.get("00" + i);
			dx = x - doubles[0];
			dy = y - doubles[1];
			r = Math.sqrt((dx * dx) + (dy * dy));
			map.put("00" + i, r);
		}

		return map;
	}
}
