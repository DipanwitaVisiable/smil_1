package com.example.crypedu.Constants;

import com.example.crypedu.Helper.FreeTestAnswer;
import com.example.crypedu.Pojo.AnswerTable;

import java.util.HashMap;
import java.util.List;

/*
 *************************************************************************************************
 * Developed by Cryptonsoftech
 * Date: 30-05-2017
 * Details:
 * ***********************************************************************************************
 */
public final class Constants {

    public static final String openSans_Regular_font = "OpenSans-Regular.ttf";
    public static final String BubblegumSans_Regular_font = "BubblegumSans-Regular.otf";
    public static String USER_ROLE = "";
//    public static final String BASE_SERVER = "http://sudhirmemorialinstituteliluah.com/snrmemorialtrust/webservices/websvc/"; //old
//    public static final String BASE_SERVER = "https://nirbadhngo.com/snrmemorialtrust/webservices/websvc_smil/";// new changed in 30_06_2020
    public static final String BASE_SERVER = "http://www.primers.co.in/snrmemorialtrust/webservices/websvc_smil/";// new changed in 18_11_2021
    public static final String BASE_SERVER_MEDIWALLET = "https://sudhirmemorialinstituteliluah.com/mediwallet/index.php/main/";
//    public static final String server_name = "http://sudhirmemorialinstituteliluah.com/snrmemorialtrust/";
    public static final String server_name = "http://www.primers.co.in/snrmemorialtrust/"; //new changed in 18_11_2021
    //public static final String BUS_LOCATION = "http://gps.blazontrack.com/track/api/api.php?api=user&ver=1.0&key=34F0FB4067AAE3A&cmd=OBJECT_GET_LOCATIONS,";
    public static final String BUS_LOCATION = "http://gps.blazontrack.com/track/api/api.php?api=user&ver=1.0&key=34F0FB4067AAE3A&cmd=OBJECT_GET_LOCATIONS,";
//    public static final String FILE_UPLOAD_IN_DRIVE = "https://nirbadhngo.com/snrmemorialtrust/googledrive_cntr/googledrive/fileUpload";
    public static final String FILE_UPLOAD_IN_DRIVE = "http://www.primers.co.in/snrmemorialtrust/googledrive_cntr/googledrive/fileUpload";//new changed in 18_11_2021

    public static String PAY_ONLINE_URL = "";
    public static String PROFILENAME = "";
    public static int galleryFlag = -1;
    public static String USER_ID = "";
	public static String BUS_ID = "";
    public static int BUS_FLAG = 0;
    public static String BUS_LAT = "";
    public static String BUS_LONG = "";
    public static String FETCHED_DATE = "";
    public static String PATHOLOGY_SRVS_ID = "";
    public static String CHAMBER_ID = "";
    public static String CHAMBER_TYPE = "";
    public static String DEPARTMENT_ID = "";
    public static String MEDICAL_STORE_ID = "";
    public static String FIREBASE_ID = "";
    public static String colorAbsent = "#e1444f";
    public static String colorHoliday = "#564dca";
    public static String colorLeave = "#e79e24";
    public static String colorPresent = "#12baa9";
    public static String colorAccent = "#f58220";
    public static String colorRed = "#ff0000";
    public static String strCopyright = "\u00a9" + " SNR Memorial Trust. All Rights Reserved.";
    public static String PhoneNo = "";
    public static String USER_RATING = "";
    public static String communicationId = "";
    public static String GREETING_INVITATION_TYPE = "";
    public static String GREETING_URL = "";
    public static String GREETING_STATUS = "";
    public static Boolean flag = true;
    public static int updateFalg = 0;
    public static String DEVICE_IEMI = "";

    //For live video section
    public static String DAYS_ID = "";
    public static String DAYS_NAME = "";
    public static String PERIOD_ID = "";
    public static String PERIOD_NAME = "";

    //For worksheet section
    public static String SUBJECT_NAME = "";
    public static String SUBJECT_ID = "";
    public static String CHAPTER_NAME = "";
    public static String CHAPTER_ID = "";
    public static String PDF_NAME = "";
    public static String PDF_URL = "";

    //For Online test or Quiz test
    public static String QUIZ_ID="";
    public static String END_TIME="";
    public static String TOTAL_QUES="";
    public static String CHOOSE_QUESTION_ID="";
    public static String ANSWER_ID="";
    public static String STUDENT_ID="";
    public static int QUES_NO= 0 ;
    public static HashMap<String, String> answerStoreHash = new HashMap<>();
    public static HashMap<String, List<AnswerTable>> testquestionAnswerStoreHash = new HashMap<>();
    public static HashMap<String, String> answerQuestionStoreHash = new HashMap<>();
    public static HashMap<String, List<FreeTestAnswer>> questionAnswerStoreHash = new HashMap<>();
    public static HashMap<String, String> answerCheckHash = new HashMap<>();

  //For Assignment section
  public static String FROM_SCREEN_TO_SUBJECT="";
  public static String TOPIC_ID="";
  public static String ASSIGNMENT_TOPIC_STATUS="";
  public static String FROM_SCREEN="";

  //For zoom section
  public static String JWT_TOKEN_ZOOM = "";
  public static String FOLDER_NAME = "";

    private Constants() {
    }
}
