package org.com.wso2.ushan.algorithm;

import java.util.ArrayList;
import java.util.HashMap;

import org.com.wso2.ushan.bean.User;



public interface AlgoInterface {

	public Double[] nonIntersectingPointsOfTwoCircles(double x0, double y0,
			double r0, double x1, double y1, double r1);

	public Double[] nonIntersectingPointsOfThreeCircles(double x0, double y0,
			double r0, double x1, double y1, double r1, double x2, double y2,
			double r2);

	public ArrayList<User> manageUser(HashMap<String, ArrayList<User>> userDetails);

	public boolean isIntersect(double x0, double y0, double r0, double x1,
			double y1, double r1);

	public Double[] calculateThreeCircleIntersection(double x0, double y0,
			double r0, double x1, double y1, double r1, double x2, double y2,
			double r2);

}
