package org.com.wso2.ushan.comparator;



import java.util.Comparator;

import org.com.wso2.ushan.bean.User;

public class UserComparator implements Comparator<User> {

	public int compare(User o1, User o2) {
		if (o1.getDistance() > o2.getDistance()) {
	        return 1;
	    } else if (o1.getDistance() < o2.getDistance()) {
	        return -1;
	    }
	    return 0;
	}

}
