package com.example.cupofjoe.constant.route;

import javax.naming.ldap.PagedResultsControl;

public class Routes {
    public static final String SERVER_LOCAL = "";

    public static final String BASE_URL = SERVER_LOCAL + "/api/v1";

    //my user controller
    public static final String USER = BASE_URL + "/user";
    public static final String REGISTER = USER + "/register";
    public static final String VALIDATE_OTP = USER + "/validate-otp";
    public static final String ADD_INFORMATION = USER + "/add-information";
    public static final String LOGIN = USER + "/login";
    public static final String LOGOUT = USER + "/logout";

    //cafe
    public static final String CAFE = BASE_URL + "/cafe";

    //oder
    public static final String ORDERS = BASE_URL + "/order";
    public static final String ADD_ORDER = ORDERS + "/add-order";

    public static final String ORDER_TO_ME = ORDERS + "/to-me";

    public static final String ORDER_BY_ME = ORDERS + "/by-me";
    public static final String MARK_AS_PREPARED = ORDERS + "/prepared/{orderId}";
    public static final String MARK_AS_COMPLETE = ORDERS + "/completed/{orderId}";

}
