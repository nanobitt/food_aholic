package com.foodaholic.utils;

import com.foodaholic.main.BuildConfig;
import com.foodaholic.items.ItemAbout;
import com.foodaholic.items.ItemCart;
import com.foodaholic.items.ItemMenuCat;
import com.foodaholic.items.ItemOrderList;
import com.foodaholic.items.ItemRestaurant;
import com.foodaholic.items.ItemUser;

import java.io.Serializable;
import java.util.ArrayList;

public class Constant implements Serializable{

	private static String SERVER_URL = BuildConfig.SERVER_URL;

	public static final String URL_ABOUT = SERVER_URL + "api.php";
	public static final String URL_ABOUT_US_LOGO = SERVER_URL + "images/";

	public static final String URL_REGISTER = SERVER_URL + "user_register_api.php";

	public static final String URL_LOGIN_1 = SERVER_URL + "user_login_api.php?email=";
	public static final String URL_LOGIN_2 = "&password=";

	public static final String URL_FORGOT_PASS = SERVER_URL + "user_forgot_pass_api.php?email=";

	public static final String URL_PROFILE = SERVER_URL + "user_profile_api.php?id=";
	public static final String URL_PROFILE_EDIT = SERVER_URL + "user_profile_update_api.php";

	public static final String URL_HOME = SERVER_URL + "api.php?home";
	public static final String URL_HOTEL_LIST = SERVER_URL + "api.php?all_restaurants";
	public static final String URL_SEARCH_HOTEL_LIST = SERVER_URL + "api.php?search_type=";
	public static final String URL_TOP_RATED = SERVER_URL + "api.php?top_rated";
	public static final String URL_SINGLE_HOTEL = SERVER_URL + "api.php?restaurant_id=";
	public static final String URL_HOTEL_BY_CAT = SERVER_URL + "api.php?cat_id=";
	public static final String URL_CAT = SERVER_URL + "api.php?cat_list";
	public static final String URL_MENU_CAT_BY_HOTEL = SERVER_URL + "api.php?menu_cat=";
	public static final String URL_MENU_BY_CAT = SERVER_URL + "api.php?menu_list_cat_id=";
	public static final String URL_CART = SERVER_URL + "api_cart_list.php?user_id=";
	public static final String URL_CLEAR_CART = SERVER_URL + "api_cart_item_empty.php?user_id=";
	public static final String URL_DELETE_ITEM_CART = SERVER_URL + "api_cart_item_delete.php?cart_id=";
	public static final String URL_ORDER_LIST = SERVER_URL + "api_user_order_list.php?user_id=";
	public static final String URL_CANCEL_ORDER = SERVER_URL + "api_order_cancel.php?user_id=";

	public static final String URL_RATING_1 = SERVER_URL + "api_rating.php?user_id=";
	public static final String URL_RATING_2 = "&rate=";
	public static final String URL_RATING_3 = "&msg=";
	public static final String URL_RATING_4 = "&restaurant_id=";

	public static final String URL_ADD_MENU_1 = SERVER_URL + "api_cart_add_update.php?user_id=";
	public static final String URL_ADD_MENU_2 = "&rest_id=";
	public static final String URL_ADD_MENU_3 = "&menu_id=";
	public static final String URL_ADD_MENU_4 = "&menu_name=";
	public static final String URL_ADD_MENU_5 = "&menu_qty=";
	public static final String URL_ADD_MENU_6 = "&menu_price=";

	public static final String URL_CHECKOUT_1 = SERVER_URL + "api_order_add.php?user_id=";
	public static final String URL_CHECKOUT_2 = "&order_address=";
	public static final String URL_CHECKOUT_3 = "&order_comment=";
	public static final String URL_CHECKOUT_4 = "&cat_ids=";

	public static final String URL_OFFER_AND_PROMOTIONS = SERVER_URL + "api_offer_and_promotion.php";

	public static final String URL_FOOD_OF_THE_DAY = SERVER_URL + "api_food_of_the_day.php";

	public static final String URL_FAQ = SERVER_URL + "api_faq.php";

	public static final String URL_REST_SERVICE_CHARGE = SERVER_URL + "api_rest_service_charge.php?rest_id=";

	public static final String TAG_ROOT = "FOOD_APP";
	public static final String TAG_FEATURED_REST = "featured_restaurant";
	public static final String TAG_LATEST_REST = "latest_restaurant";

	public static final String TAG_REST_ID = "restaurant_id";
	public static final String TAG_REST_NAME = "restaurant_name";
	public static final String TAG_REST_IMAGE = "restaurant_image";
	public static final String TAG_REST_ADDRESS = "restaurant_address";
	public static final String TAG_REST_TYPE = "restaurant_type";
	public static final String TAG_REST_MONDAY = "restaurant_open_mon";
	public static final String TAG_REST_TUESDAY = "restaurant_open_tues";
	public static final String TAG_REST_WEDNESDAY = "restaurant_open_wed";
	public static final String TAG_REST_THURSDAY = "restaurant_open_thur";
	public static final String TAG_REST_FRRIDAY = "restaurant_open_fri";
	public static final String TAG_REST_SATURDAY = "restaurant_open_sat";
	public static final String TAG_REST_SUNDAY = "restaurant_open_sun";
	public static final String TAG_REST_TOTAL_RATE = "total_rate";
	public static final String TAG_REST_AVG_RATE = "rate_avg";
	public static final String TAG_REST_ROOT = "restaurant";

	public static final String TAG_NONVEG="Non Veg";
	public static final String TAG_VEG="Veg";
	public static final String TAG_VEG_NONVEG="Veg/Non Veg";

	public static final String TAG_ID="id";

	public static final String TAG_CAT_ID="cid";
	public static final String TAG_CAT_NAME="category_name";
	public static final String TAG_CAT_IMAGE="category_image";

	public static final String TAG_MENU_ID="mid";
	public static final String TAG_MENU_NAME="menu_name";
	public static final String TAG_MENU_TYPE="menu_type";
	public static final String TAG_MENU_DESC="menu_info";
	public static final String TAG_MENU_PRICE="menu_price";
	public static final String TAG_MENU_IMAGE="menu_image";
	public static final String TAG_MENU_CAT="menu_cat_id";
	public static final String TAG_MENU_REST_ID="rest_id";
	public static final String TAG_MENU_QYT="menu_qty";
	public static final String TAG_MENU_TOTAL_PRICE="menu_total_price";
	public static final String TAG_MENU_PREVIOUS_PRICE = "previous_price";

	public static final String TAG_ORDER_ID = "order_id";
	public static final String TAG_ORDER_UNIQUE_ID = "order_unique_id";
	public static final String TAG_ORDER_ADDRESS = "order_address";
	public static final String TAG_ORDER_COMMENT = "order_comment";
	public static final String TAG_ORDER_DATE = "order_date";
	public static final String TAG_ORDER_ITEMS = "order_items";
	public static final String TAG_ORDER_REST_NAME = "rest_name";
	public static final String TAG_ORDER_STATUS = "status";

	public static final String TAG_RATING_REVIEW="rating_review";
	public static final String TAG_RATING_ID = "r_id";
	public static final String TAG_RATING = "rate";
	public static final String TAG_RATING_MSG = "review";

	public static final String TAG_MSG="msg";
	public static final String TAG_SUCCESS="success";

	public static final String TAG_NAME_USER="user_name";

	public static final String TAG_USER_ID="user_id";
	public static final String TAG_USER_NAME="name";
	public static final String TAG_USER_EMAIL="email";
	public static final String TAG_USER_PHONE="phone";
	public static final String TAG_USER_CITY="city";
	public static final String TAG_USER_ADDRESS="address";
	public static final String TAG_USER_IMAGE="user_image";

	public static final String TAG_CART_ID = "cart_id";
	public static final String TAG_CART_MENU_ID = "menu_id";
	public static final String TAG_CART_COUNT = "cart_items";

	public static final String TAG_OFFER_ID = "id";
	public static final String TAG_OFFER_HEADING = "offer_heading";
	public static final String TAG_OFFER_DESCRIPTION = "offer_description";
	public static final String TAG_OFFER_IMAGE_LINK = "image_link";
	public static final String TAG_OFFER_POST_DATE = "post_date";

	public static final String TAG_FAQ_QUES = "faq_question";
	public static final String TAG_FAQ_ANS = "faq_ans";

	public static final String TAG_REST_SERVICE_CHARGE_STATUS = "charge_status";
	public static final String TAG_REST_OPEN_STATUS = "rest_open_status";


	public static final String SHARED_PREF_LOGIN="login";
	public static final String SHARED_PREF_EMAIL="email";
	public static final String SHARED_PREF_PASSWORD="password";

	// Number of columns of Grid View
	public static final int NUM_OF_COLUMNS = 2;
	public static final int NUM_OF_COLUMNS_NAV = 3;

	// Gridview image padding
	public static final int GRID_PADDING = 4; // in dp

	public static ItemUser itemUser;

	public static Boolean isUpdate = false;
	public static Boolean isLogged = false;
	public static Boolean isCartRefresh = false;
	public static Boolean isFromCheckOut = false;
	public static final String SERVICE_CHARGE_APPLICABLE = "*Service charge applicable";
	public static final String SERVICE_CHARGE_NOT_APPLICABLE = "*Service charge not applicable";

	public static ItemAbout itemAbout;
	public static ArrayList<ItemRestaurant> arrayList_latest = new ArrayList<>();
	public static ArrayList<ItemMenuCat> arrayList_menuCat = new ArrayList<>();
	public static ArrayList<ItemCart> arrayList_cart = new ArrayList<>();
	public static ItemRestaurant itemRestaurant;
	public static ItemOrderList itemOrderList;


	public static String search_text = "", search_type = "Restaurant";
	public static String[] search_type_array = {"Restaurant","Menu"};
	public static int search_type_pos = 0;

	public static final String TAG_PENDING = "Pending";
	public static final String TAG_PROCESS = "Process";
	public static final String TAG_COMPLETE = "Complete";
	public static final String TAG_CANCEL = "Cancel";
	public static Boolean isCancelOrder = false;


	public static int menuCount = 0;



}