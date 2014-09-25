package org.com.wso2.ushan.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.ManagerFactoryParameters;

import org.com.wso2.ushan.bean.User;
import org.com.wso2.ushan.comparator.UserComparator;
import org.com.wso2.ushan.algorithm.AlgoInterface;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Algoritham implements AlgoInterface {

	private static HashMap<String, Double[]> sensors;
	private static Logger logger;

	static {
		logger = Logger.getLogger("LOG.txt");
		sensors = new HashMap<String, Double[]>();
		sensors.put("000", new Double[] { 0.0, 0.0 });
		sensors.put("001", new Double[] { 0.0, 100.0 });
		sensors.put("002", new Double[] { 0.0, 200.0 });
		sensors.put("003", new Double[] { 180.0, 0.0 });
		sensors.put("004", new Double[] { 180.0, 100.0 });
		sensors.put("005", new Double[] { 180.0, 200.0 });

	}

	public boolean isIntersect(double x0, double y0, double r0, double x1,
			double y1, double r1) {
		double dx, dy;
		dx = (x1 - x0);
		dy = (y1 - y0);

		/* finding distance between the centers of two circles */
		double d = Math.sqrt((dx * dx) + (dy * dy));

		/* check whether two circles are intersecting or not */
		if (d < (r0 + r1)) {
			return true;
		}
		return false;

	}

	public Double[] nonIntersectingPointsOfTwoCircles(double x0, double y0,
			double r0, double x1, double y1, double r1) {

		double dx, dy;

		dx = (x1 - x0);
		dy = (y1 - y0);

		/* finding distance between the centers of two circles */
		double d = Math.sqrt((dx * dx) + (dy * dy));

		/* if two circles are not intersecting */
		double k = d - (r0 + r1);
		double distance = (r0 + (k / 2)) / d;

		if (k < 0) {
			/* if two circles are intersecting */
			k = (r0 + r1) - d;
			distance = (r0 - (k / 2)) / d;
		}

		double x, y;

		/*
		 * If the line joining the centers of two circles adjacent cut the
		 * circle 1 at point A and cuts the circle 2 at point B, midpoint of AB
		 * is C = (x,y)
		 */
		x = x0 * (1 - distance) + distance * x1;
		y = y0 * (1 - distance) + distance * y1;
		System.out.println("x: " + x + " y :" + y);
		logger.info("Cordinate of middle of the two points  X: " + x + " Y: "
				+ y + " and error is " + k / 2);

		return new Double[] { x, y, k / 2 };
	}

	public Double[] nonIntersectingPointsOfThreeCircles(double x0, double y0,
			double r0, double x1, double y1, double r1, double x2, double y2,
			double r2) {

		Double[] points1 = nonIntersectingPointsOfTwoCircles(x0, y0, r0, x1,
				y1, r1);

		Double[] points2 = nonIntersectingPointsOfTwoCircles(x0, y0, r0, x2,
				y2, r2);

		/*
		 * A line is drawn joining the point C to the center of the other
		 * circle. This is done for all 3 pairs of adjacent circles and find the
		 * point of intersection of those 3 lines
		 */
		double x = (((x1 - points2[0]) * y1 - (y1 - points2[1]) * x1)
				* (x2 - points1[0]) - ((x2 - points1[0]) * y2 - (y2 - points1[1])
				* x2)
				* (x1 - points2[0]))
				/ ((x1 - points2[0]) * (y2 - points1[1]) - (y1 - points2[1])
						* (x2 - points1[0]));

		double y = ((x2 - points1[0]) * y2 - (y2 - points1[1]) * x2 + (y2 - points1[1])
				* x)
				/ (x2 - points1[0]);

		return null;
	}

	public Double[] calculateThreeCircleIntersection(double x0, double y0,
			double r0, double x1, double y1, double r1, double x2, double y2,
			double r2) {

		Double arr[] = new Double[3];

		double a, dx, dy, d, h, rx, ry;
		double point2_x, point2_y;

		/*
		 * dx and dy are the vertical and horizontal distances between the
		 * circle centers.
		 */
		dx = x1 - x0;
		dy = y1 - y0;

		/* Determine the straight-line distance between the centers. */
		d = Math.sqrt((dy * dy) + (dx * dx));

		/*
		 * 'point 2' is the point where the line through the circle intersection
		 * points crosses the line between the circle centers.
		 */

		/* Determine the distance from point 0 to point 2. */
		a = ((r0 * r0) - (r1 * r1) + (d * d)) / (2.0 * d);

		/* Determine the coordinates of point 2. */
		point2_x = x0 + (dx * a / d);
		point2_y = y0 + (dy * a / d);

		/*
		 * Determine the distance from point 2 to either of the intersection
		 * points.
		 */
		h = Math.sqrt((r0 * r0) - (a * a));

		/*
		 * Now determine the offsets of the intersection points from point 2.
		 */
		rx = -dy * (h / d);
		ry = dx * (h / d);

		/* Determine the absolute intersection points. */
		double intersectionPoint1_x = point2_x + rx;
		double intersectionPoint2_x = point2_x - rx;
		double intersectionPoint1_y = point2_y + ry;
		double intersectionPoint2_y = point2_y - ry;

		double subs1 = intersectionPoint1_x * intersectionPoint1_x
				+ intersectionPoint1_y * intersectionPoint1_y - 2 * x2
				* intersectionPoint1_x - 2 * y2 * intersectionPoint1_y + x2
				* x2 + y2 * y2 - r2 * r2;

		double subs2 = intersectionPoint2_x * intersectionPoint2_x
				+ intersectionPoint2_y * intersectionPoint2_y - 2 * x2
				* intersectionPoint2_x - 2 * y2 * intersectionPoint2_y + x2
				* x2 + y2 * y2 - r2 * r2;

		if (subs1 == subs2) {
			arr = nonIntersectingPointsOfTwoCircles(x0, y0, r0, x1, y1, r1);

		} else if (-2 < subs1 && subs1 < 2) {
			arr[0] = intersectionPoint1_x;
			arr[1] = intersectionPoint1_y;
			arr[2] = 0.0;

		} else {
			arr[0] = intersectionPoint2_x;
			arr[1] = intersectionPoint2_y;
			arr[2] = 0.0;
		}

		return arr;
	}

	public Double[] fourCircles(double x0, double y0, double x1, double y1,
			double x2, double y2, double x3, double y3) {

		Double[] arr = new Double[2];

		double[][] tempArr = { { x0, y0 }, { x1, y1 }, { x2, y2 }, { x3, y3 }, };
		double min = 0.0, temp_x = 0.0, temp_y = 0.0;
		int index = 0;

		for (int i = 0; i < 3; i++) {
			min = tempArr[i][0];
			for (int j = i + 1; j < 4; j++) {
				if (tempArr[j][0] < min) {
					min = tempArr[j][0];
					index = j;
				}
			}
			temp_x = tempArr[i][0];
			temp_y = tempArr[i][1];
			tempArr[i][0] = min;
			tempArr[i][1] = tempArr[index][1];
			tempArr[index][0] = temp_x;
			tempArr[index][1] = temp_y;
		}

		if (tempArr[0][1] > tempArr[3][1]) {
			temp_x = tempArr[0][0];
			temp_y = tempArr[0][1];
			tempArr[0][0] = tempArr[3][0];
			tempArr[0][1] = tempArr[3][1];
			tempArr[3][0] = temp_x;
			tempArr[3][1] = temp_y;
		}

		if (tempArr[2][1] > tempArr[1][1]) {
			temp_x = tempArr[2][0];
			temp_y = tempArr[2][1];
			tempArr[2][0] = tempArr[1][0];
			tempArr[2][1] = tempArr[1][1];
			tempArr[1][0] = temp_x;
			tempArr[1][1] = temp_y;
		}

		for (int i = 0; i < 4; i++) {
			System.out.println(tempArr[i][0] + ", " + tempArr[i][1]);
		}

		/*
		 * x1 = points1[0] = tempArr[0][0] y1 = points1[1] = tempArr[0][1] x2 =
		 * x2 = tempArr[3][0] y2 = y2 = tempArr[3][1] x3 = points2[0] =
		 * tempArr[2][0] y3 = points2[1] = tempArr[2][1] x4 = x1 = tempArr[1][0]
		 * y4 = y1 = tempArr[1][1]
		 */

		double x = (((tempArr[1][0] - tempArr[2][0]) * tempArr[1][1] - (tempArr[1][1] - tempArr[2][1])
				* tempArr[1][0])
				* (tempArr[3][0] - tempArr[0][0]) - ((tempArr[3][0] - tempArr[0][0])
				* tempArr[3][1] - (tempArr[3][1] - tempArr[0][1])
				* tempArr[3][0])
				* (tempArr[0][0] - tempArr[2][0]))
				/ ((tempArr[0][0] - tempArr[2][0])
						* (tempArr[3][1] - tempArr[0][1]) - (tempArr[1][1] - tempArr[2][1])
						* (tempArr[3][0] - tempArr[0][0]));

		double y = ((tempArr[3][0] - tempArr[0][0]) * tempArr[3][1]
				- (tempArr[3][1] - tempArr[0][1]) * tempArr[3][0] + (tempArr[3][1] - tempArr[0][1])
				* x)
				/ (tempArr[3][0] - tempArr[0][0]);

		// System.out.println(x + "\n" + y);

		arr[0] = x;
		arr[1] = y;

		return arr;
	}

	public ArrayList<User> manageUser(
			HashMap<String, ArrayList<User>> userDetails) {
		Double[] arr = new Double[3];
		ArrayList<User> users = new ArrayList<User>();
		double x0, y0, r0, x1, y1, r1, x2 = 0, y2 = 0, r2 = 0, x3 = 0, y3 = 0, r3 = 0;
		for (int i = 0; i < userDetails.size(); i++) {
			ArrayList<User> userDistances = userDetails.get(i + "");
			Collections.sort(userDistances, new UserComparator());
			User user = new User();
			user.setUserID(userDistances.get(0).getUserID());

			System.out.println();

			x0 = sensors.get(userDistances.get(0).getSensorId())[0];

			y0 = sensors.get(userDistances.get(0).getSensorId())[1];

			r0 = userDistances.get(0).getDistance();

			if (sensors.size() == 1) {
				if (r0 == 0) {
					// user is on sensor
					logger.info("User is on the sensor");
					arr[0] = x0;
					arr[1] = y0;
					arr[2] = 0.0;
				}

			} else {

				x1 = sensors.get(userDistances.get(1).getSensorId())[0];
				y1 = sensors.get(userDistances.get(1).getSensorId())[1];

				r1 = userDistances.get(1).getDistance();

				if (userDistances.size() > 2) {
					x2 = sensors.get(userDistances.get(2).getSensorId())[0];
					y2 = sensors.get(userDistances.get(2).getSensorId())[1];

					r2 = userDistances.get(2).getDistance();
				}
				if (userDistances.size() > 3) {
					r3 = userDistances.get(3).getDistance();
				}

				if (r2 == r3) {

					logger.info("There is four sensor");
					x3 = sensors.get(userDistances.get(3).getSensorId())[0];
					y3 = sensors.get(userDistances.get(3).getSensorId())[1];
					arr = fourCircles(x0, y0, x1, y1, x2, y2, x3, y3);

				} else if (userDistances.size() == 2) {
					logger.info("There are two sensor");
					arr = nonIntersectingPointsOfTwoCircles(x0, y0, r0, x1, y1,
							r1);

				} else if (isIntersect(x0, y0, r0, x1, y1, r1)
						&& isIntersect(x0, y0, r0, x2, y2, r2)
						&& isIntersect(x2, y2, r2, x1, y1, r1)) {
					logger.info("There are three intersecting circles");
					Double[] cordinates = calculateThreeCircleIntersection(x0,
							y0, r0, x1, y1, r1, x2, y2, r2);
					if (cordinates[2] == 0) {
						logger.info("Cordinate of intersecting points X :"
								+ cordinates[0] + " Y:" + cordinates[1]);
					} else {
						logger.info("one circle is contained in another and cordinates are X:"
								+ cordinates[0] + " Y:" + cordinates[1]);
					}

					arr = cordinates;
				} else {
					logger.info("There are three non intersecting circles");
					arr = nonIntersectingPointsOfThreeCircles(x0, y0, r0, x1,
							y1, r1, x2, y2, r2);

				}
			}
			user.setX(arr[0]);
			user.setY(arr[1]);
			user.setError(arr[2]);
			users.add(user);
		}

		return users;
	}

	public User manageUser(ArrayList<User> userDistances) {
		Double[] arr = new Double[3];

		double x0, y0, r0, x1, y1, r1, x2 = 0, y2 = 0, r2 = 0, x3 = 0, y3 = 0, r3 = 0;

		Collections.sort(userDistances, new UserComparator());
		User user = new User();
		user.setUserID(userDistances.get(0).getUserID());

		System.out.println();

		x0 = sensors.get(userDistances.get(0).getSensorId())[0];

		y0 = sensors.get(userDistances.get(0).getSensorId())[1];

		r0 = userDistances.get(0).getDistance();

		if (sensors.size() == 1) {
			if (r0 == 0) {
				// user is on sensor
				logger.info("User is on the sensor");
				arr[0] = x0;
				arr[1] = y0;
				arr[2] = 0.0;
			}

		} else {

			x1 = sensors.get(userDistances.get(1).getSensorId())[0];
			y1 = sensors.get(userDistances.get(1).getSensorId())[1];

			r1 = userDistances.get(1).getDistance();

			if (userDistances.size() > 2) {
				x2 = sensors.get(userDistances.get(2).getSensorId())[0];
				y2 = sensors.get(userDistances.get(2).getSensorId())[1];

				r2 = userDistances.get(2).getDistance();
			}
			if (userDistances.size() > 3) {
				r3 = userDistances.get(3).getDistance();
			}

			if (r2 == r3) {

				logger.info("There is four sensor");
				x3 = sensors.get(userDistances.get(3).getSensorId())[0];
				y3 = sensors.get(userDistances.get(3).getSensorId())[1];
				arr = fourCircles(x0, y0, x1, y1, x2, y2, x3, y3);

			} else if (userDistances.size() == 2) {
				logger.info("There are two sensor");
				arr = nonIntersectingPointsOfTwoCircles(x0, y0, r0, x1, y1, r1);

			} else if (isIntersect(x0, y0, r0, x1, y1, r1)
					&& isIntersect(x0, y0, r0, x2, y2, r2)
					&& isIntersect(x2, y2, r2, x1, y1, r1)) {
				logger.info("There are three intersecting circles");
				Double[] cordinates = calculateThreeCircleIntersection(x0, y0,
						r0, x1, y1, r1, x2, y2, r2);
				if (cordinates[2] == 0) {
					logger.info("Cordinate of intersecting points X :"
							+ cordinates[0] + " Y:" + cordinates[1]);
				} else {
					logger.info("one circle is contained in another and cordinates are X:"
							+ cordinates[0] + " Y:" + cordinates[1]);
				}

				arr = cordinates;
			} else {
				logger.info("There are three non intersecting circles");
				arr = nonIntersectingPointsOfThreeCircles(x0, y0, r0, x1, y1,
						r1, x2, y2, r2);

			}
			
		}
		user.setX(arr[0]);
		user.setY(arr[1]);
		user.setError(arr[2]);
		return user;
	}

	public JSONObject getUserLocations(Object obj) throws Exception {
		HashMap<String, ArrayList<User>> userDetails = new HashMap<String, ArrayList<User>>();
		try {
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray readings = (JSONArray) jsonObject.get("readings");

			Iterator i = readings.iterator();

			// take each value from the json array separately
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				String sid = (String) innerObj.get("sid");
				String did = (String) innerObj.get("did");
				double distance = (Double) innerObj.get("distance");
				User user = new User();
				user.setUserID(did);
				user.setSensorId(sid);
				user.setDistance(distance);

				if (userDetails.containsKey(did)) {
					ArrayList<User> users = userDetails.get(did);
					users.add(user);
					userDetails.put(did, users);
				} else {
					ArrayList<User> users = new ArrayList<User>();
					users.add(user);
					userDetails.put(did, users);
				}

			}
			ArrayList<User> users = manageUser(userDetails);
			JSONArray list = new JSONArray();
			for (User user : users) {
				JSONObject innerObj = new JSONObject();
				innerObj.put("did", user.getUserID());
				innerObj.put("x", user.getX());
				innerObj.put("y", user.getY());
				innerObj.put("error", user.getError());

				list.add(innerObj);
			}
			JSONObject returnObj = new JSONObject();
			returnObj.put("output", list);
			return returnObj;
		} catch (Exception ex) {
			logger.info(ex + "");
			throw ex;
		}

	}
}
