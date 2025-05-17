package aston.room_booking.users_service.utils;

/**
 * Утильный класс для аннотации и разметки Swagger
 * Предоставляет шаблоны сообщений об ошибках
 * для Swagger
 *
 * @author 4ndr33w
 * @version 1.0
 */
public class ErrorDroConstants {

    public static final String INTERNAL_SERVER_ERROR_ERROR_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "statusCode":500,
                "message":"Internal Server Error"
            }
           """;

    public static final String INVALID_ID_ERROR_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "statusCode":400,
                "message":"Некорректный id"
            }
           """;

    public static final String INVALID_EMAIL_ERROR_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "statusCode":400,
                "message":"email is already in use"
            }
           """;

    public static final String INVALID_USERNAME_ERROR_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "statusCode":400,
                "message":"UserName is already in use"
            }
           """;

    public static final String REQUEST_BODY_ERROR_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "statusCode":400,
                "message":"Response body is required"
            }
           """;

    public static final String UNAUTHORIZED_ERROR_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "statusCode":401,
                "message":"Access Denied!"
            }
            """;
    public static final String FORBIDDEN_ERROR_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "statusCode":403,
                "message":"Access Denied!"
            }
            """;

    public static final String USER_NOT_FOUND_ERROR_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "statusCode":404,
                "message":"User not found"
            }
            """;

    public static final String NO_USERS_FOUND_ERROR_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "statusCode":204,
                "message":"No users found"
            }
            """;

    public static final String USER_DELETED_SUCCESSFUL_MESSAGE_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "message":"User is delete successful"
            }
            """;

    public static final String TOKEN_VALIDATION_ERROR_ERROR_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "statusCode":400,
                "message":"Token is invalid"
            }
            """;

    public static final String PASSWORD_IS_EMPTY_ERROR_DTO = """
            {
                "dateTime": "Fri May 09 00:07:16 GMT+05:00 2025",
                "statusCode":400,
                "message":"Поле 'password' - пустое или имеет некорректное наименование"
            }
            """;
}
