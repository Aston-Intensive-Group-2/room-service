package aston.room_booking.users_service.utils;

import aston.room_booking.users_service.configurations.PropertiesConfiguration;

public class StaticConstants {

    public static final long JWT_EXPIRATION_MS = 1000 * 60 * 60 * 24;
    public static final String JWT_SECRET = PropertiesConfiguration.getConstants().getProperty("JWT_SECRET"); // "yourSecretKeyShouldBeLongerAndMoreComplexdwfFV we f34 F F#ER#WDDWr3tr ";

    public static final String EMAIL_IS_ALREADY_IN_USE_EXCEPTION_MESSAGE = PropertiesConfiguration.getConstants().getProperty("EMAIL_IS_ALREADY_IN_USE_EXCEPTION_MESSAGE");
    public static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = PropertiesConfiguration.getConstants().getProperty("USER_NOT_FOUND_EXCEPTION_MESSAGE");
    public static final String NO_USERS_FOUND_EXCEPTION_MESSAGE = PropertiesConfiguration.getConstants().getProperty("NO_USERS_FOUND_EXCEPTION_MESSAGE");
    public static final String CANNOT_CREATE_NEW_USER_EXCEPTION_MESSAGE = PropertiesConfiguration.getConstants().getProperty("CANNOT_CREATE_NEW_USER_EXCEPTION_MESSAGE");
    public static final String INVALID_USERNAME_OR_PASSWORD_EXCEPTION_MESSAGE = PropertiesConfiguration.getConstants().getProperty("INVALID_USERNAME_OR_PASSWORD_EXCEPTION_MESSAGE");
    public static final String USERNAME_OR_PASSWORD_IS_NULL_EXCEPTION_MESSAGE = PropertiesConfiguration.getConstants().getProperty("USERNAME_OR_PASSWORD_IS_NULL_EXCEPTION_MESSAGE");
    public static final String BASIC_AUTHORIZATION_HEADER_IS_MISSING_EXCEPTION_MESSAGE = PropertiesConfiguration.getConstants().getProperty("BASIC_AUTHORIZATION_HEADER_IS_MISSING_EXCEPTION_MESSAGE");
    public static final String DATABASE_ACCESS_EXCEPTION_MESSAGE = PropertiesConfiguration.getConstants().getProperty("DATABASE_ACCESS_EXCEPTION_MESSAGE");
    public static final String BAD_REQUEST_EXCEPTION_MESSAGE = PropertiesConfiguration.getConstants().getProperty("BAD_REQUEST_EXCEPTION_MESSAGE");
    public static final String UNABLE_TO_FETCH_USER_EXCEPTION_MESSAGE = PropertiesConfiguration.getConstants().getProperty("UNABLE_TO_FETCH_USER_EXCEPTION_MESSAGE");
    public static final String ARGUMENT_IS_NULL_EXCEPTION_MESSAGE = PropertiesConfiguration.getConstants().getProperty("ARGUMENT_IS_NULL_EXCEPTION_MESSAGE");

    public static final String UNABLE_TO_CREATE_NEW_USER = PropertiesConfiguration.getConstants().getProperty("UNABLE_TO_CREATE_NEW_USER");
    public static final String SALT_KEY = PropertiesConfiguration.getConstants().getProperty("SALT_KEY");
}
