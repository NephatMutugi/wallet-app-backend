package org.basalt.main.common.utils;

/**
 * @ Author Nephat Muchiri
 * Date 13/04/2024
 */
public class StatusMessage {
    public static final String SUCCESS = "Request was successful";
    public static final String SUCCESS_ASYNC = "Request was received successfully";
    public static final String FAIL = "Request failed";
    public static final String USER_EXISTS = "User with provided username/ email already exists";
    public static final String RECORD_NOT_FOUND = "Record does not exists";
    public static final String TRANSACTIONS_NOT_FOUND = "Empty transaction history for this account";
    public static final String INVALID_SOURCE_APP = "Invalid Source App";
    public static final String INVALID_CONVERSATION_ID = "Invalid Conversation ID";
    public static final String MISSING_REQUIRED_HEADER = "Missing Required Header";
    public static final String HEADER_IS_REQUIRED = " header is required";
    public static final String CUSTOMER_SERVICE_UNAVAILABLE = "Request cannot be completed at this time. Try again later";
    public static final String NO_LOGGED_IN_CUSTOMER = "Customer is not logged in!";
    public static final String DUPLICATE_BANK = "Bank Account already exists with given account number";
    public static final String BANK_NOT_FOUND = "Bank does not exist";
    public static final String CUSTOMER_MSISDN_NOT_FOUND = "Customer not found with provided mobile number";
    public static final String WRONG_PASSWORD = "Wrong Password";
    public static final String INVALID_TOKEN = "Invalid Token";
    public static final String LOGOUT_SUCCESS = "Logged Out Successfully!";
    public static final String DUPLICATE_RECORD = "A record with the same details already exists";
    public static final String MISSING_FIELDS = "Missing Mandatory Fields";
}
