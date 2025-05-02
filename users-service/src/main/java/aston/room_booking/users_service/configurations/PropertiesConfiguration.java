package aston.room_booking.users_service.configurations;

import lombok.Getter;

import java.util.Properties;

/**
 * Утильный класс конфигурации;
 * <br/>
 * Осуществляет загрузку в константы значения из текстового файла конфигурации
 *
 * @version 1.0
 * @author 4ndr33w
 */
public class PropertiesConfiguration {

    /**
     * Получить параметры из конфигурационного файла
     */
    @Getter
    private static final Properties properties;
    /**
     * Получить список констант
     */
    @Getter
    private static final Properties constants;

    static {
        try {
            properties = new Properties();
            properties.load(PropertiesConfiguration.class.getClassLoader().getResourceAsStream("application.yaml"));

            constants = new Properties();
            constants.load(PropertiesConfiguration.class.getClassLoader().getResourceAsStream("constants.properties"));
        }catch (Exception e) {
            throw new RuntimeException("Error while loading application configuration", e);
        }
    }
}
