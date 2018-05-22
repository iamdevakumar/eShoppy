package com.eclat.firebreathers.epos.Modeclasses;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eclat on 9/5/2016.
 */
public class Config {

    public final String PRODUCT_ID = "product_id";
    public final String PRODUCT_NAME = "meta_title";
    public final String PRODUCT_DESC = "meta_description";
    public final String SPEC_NAME = "spec_name";
    public final String SPEC_VALUE = "spec_value";


    public final String PRO_THUMBURL="pro_thumburl";
    public final String NAME = "name";
    public final String EMAIL = "email";
    public final String MOBILE = "mobile";
    public final String OLD_PASS = "old_password";
    public final String NEW_PASS = "new_password";
    public final String ADDRESS = "address";
    public final String ADDRESS_ID = "address_id";
    public final String PASSWORD = "password";
    public final String DOB = "dob";
    public final String GENDER = "gender";
    public final String NETPRICE = "net_price";
    public final String PRICE = "price";
    public final String IMAGEURL = "image";
    public final String IMAGENAME = "image_name";
    public final String IMAGE = "cust_img";
    public final String CAT_NAME = "category_name";
    public final String SUBCAT_NAME = "subcategory_name";
    public final String CATEGORY_ID = "category_id";
    public final String CAT_ID = "cat_id";
    public final String SUBCATID = "subcat_id";
    public final String OFFER_ID = "offer_id";
    public final String OFFER_IMAGE_URL = "offer_image_url";


    public final String CUSTOMERID = "cust_id";
    public final String LOGIN_USERID = "customer_id";
    public final String LOGIN_MOBILE = "telephone";
    public final String LOGIN_CUST_NAME = "firstname";
    public final String LOGIN_CUST_IMG = "customer_image";
    public final String LOGIN_OTP_STATUS = "ismobilevalid";
    public final String LOGIN_CART_COUNT = "cart";
    public final String OTP_DELIMITER = "is";
    public final String OTP = "otp";
    public final String RESPONSE_STATUS = "status";
    public final String RESPONSE_DATA = "data";
    public final String RESPONSE_IMAGES = "image";
    public final String response_success = "SUCCESS";
    public final String response_failed = "FAILED";
    public final String response_serverfailed = "SERVER BUSY TRY LATER";
    public final String response_msgfailed = "MESSAGE SENDING FAILED";
    public final String response_alreadyreg = "ALREADY REGISTERED";
    public final String response_not_registered = "NOT_REGISTERED";
    public final String response_field_required = "FIELDS REQUIRED";
    public final String response_otpmismatch = "OTP MISMATCHED";

    public final String HOSTURL ="http://eepos.co.in/";
    //public final String HOSTURL ="http://192.168.0.200/";
 //  public final String HOSTURL ="http://epos.16mb.com/";

    public final String IMAGE_PATH_URL = "image/";


    public final String OFFER_URL = "webservice_opn/get_offers.php";
    public final String SINGLE_PRODUCT_URL = "webservice_opn/get_product_from_prdid.php";
    public final String SINGLE_OFFERS_URL = "webservice_opn/get_offers_for_base.php";
    public final String SINGLE_TRENDING_URL = "webservice_opn/get_trendingproducts_base.php";
    public final String RELAVENT_URL = "webservice_opn/get_related_product_from_prdid.php";
    public final String TRENDING_URL = "webservice_opn/get_trending_products.php";
    public final String Get_Product = "webservice_opn/get_product_from_catid.php?";
    public final String Get_Category = "webservice_opn/get_category.php";
    public final String Get_Subcat = "webservice_opn/get_subcat_from_catid.php";
    public final String Get_Advertisement = "webservice_opn/get_advertisement.php";
    public final String SIGNUP_URL = "webservice_opn/first_registration.php";
    public final String FULLSIGNUP_URL = "webservice_opn/full_registration.php";
    public final String LOGIN_URL = "webservice_opn/login.php";
    public final String OTP_URL = "webservice_opn/validate_mobileotp.php";
    public final String CHANGE_PASSWORD_URL = "webservice_opn/change_password.php";
    public final String WISHLIST_URL = "webservice_opn/wishlist.php";
    public final String WISHLISTADDREMOVE_URL = "webservice_opn/addremovewishlist.php";
    public final String COMPLETE_WISHLIST_URL = "webservice_opn/get_wishlist.php";
    public final String GET_CART_URL = "webservice_opn/get_cart.php";
    public final String CART_QTY_UPDATE = "webservice_opn/update_cart.php";
    public final String GENERATE_OTP_URL = "webservice_opn/generate_mobileotp.php";
    public final String FORGET_PASSWORD_URL = "webservice_opn/reset_password.php";
    public final String CART_COUNT_URL="webservice_opn/cart_count.php";
    public final String CART_ADDREMOVE_URL = "webservice_opn/addremovecart.php";


    public final String homefrag = "homefrag";
    public final String categoryfrag = "categoryfrag";
    public final String contactus = "contactus";
    public final String colorsgallery = "colorsgalleryfrag";
    public final String product_all_frag = "product_all_frag";
    public final String product_base_frag = "product_base_frag";
    public final String product_offer_frag = "product_offer_frag";
    public final String product_trending_frag = "product_trending_frag";
    public final String single_product_frag = "single_product_frag";
    public final String subcategory_frag = "subcategory_frag";
    public final String fullspecfrag = "fullspecfrag";
    public final String loginfrag = "loginfrag";
    public final String signupfrag = "signupfrag";
    public final String forgetpassfrag = "forgetpassfrag";
    public final String cartactivity = "cartactivity";
    public final String fullregactivity = "fullregactivity";
    public final String offeractivity = "offeractivity";
    public final String wishlistactivity = "wishlistactivity";
    public final String loginactivity = "loginactivity";
    public final String hometosub = "hometosub";
    public final String QTY = "quantity";
    public final String RESPONSE_FLAG = "flag";
    public final String CART_FLAG = "cart";
    public final String drawername = "1";

    public static String cartdelproducts ="";
    public static String wishlistdelproducts ="";
    public static String wishlistdelprosubcatid ="";
    public static int optlayoutstate_onsignup = 0;
    public static String changepasswordresponse = "";
    public static String GET_PROID_FROM_RECYCLER = "";
    public static String wishlistbackstacksingleproid ="";
    public static String wishlistbackstacksinglesubcatid ="";
    public static String backstack_fullspec ="";
    public static String backstack_color ="";
    public static String backstack_state4 ="";
    public static String backstack_state3 ="empty";
    public static String backstack_state2 ="";
    public static String backstack_state ="";
    public static String exit_state_cart = "";
    public static String exit_state_fullreg = "";
    public static String exit_state_login = "";
    public static String exit_state_offer = "";
    public static String exit_state_wishlist = "";
    public static String categoryid = "";
    public static String sub_categoryid = "";
    public static String name = "";
    public static String mobile = "";
    public static String email = "";
    public static String userid = "";
    public static String userimg = "";
    public static String dob = "";
    public static String gender = "";
    public static String address = "";
    public static String otpstatus = "";
    public static String cartcount ="0";
    public static String fragtag = "";
    public static String categoryname = "";
    public static String productname = "";
    public static String subcategoryname = "";
    public static Bitmap drawerimg = null;
    public static String msg = null;
    public static String LOGIN_STATUS = "";
    public static List<String> imageurl = new ArrayList<>();
    public static List<String> specval = new ArrayList<>();
    public static List<String> specname = new ArrayList<>();
    public static String Title="";
    public static String Description="";
    public static String Price="";
    public static String Imageurl="";
    public static String Flag="";
    public static String Cart="";

    public static List<String> backstack_proid = new ArrayList<>();
    public static List<String> cartdel_proid = new ArrayList<>();
}