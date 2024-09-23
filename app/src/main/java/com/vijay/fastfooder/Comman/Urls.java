package com.vijay.fastfooder.Comman;

public class Urls {

    public static String webServiceAddress = "http://" +
            "192.168.185.103" +
            "/FastFooderAPI/";
    public static String loginUserWebService = webServiceAddress + "userLogin.php/";
    public static String registerUserWebService = webServiceAddress + "userregistertbl.php/";
    public static String forgetPasswordWebService = webServiceAddress + "userForgetPassword.php/";
    public static String myDetailsWebService = webServiceAddress + "myDetails.php/";
}
