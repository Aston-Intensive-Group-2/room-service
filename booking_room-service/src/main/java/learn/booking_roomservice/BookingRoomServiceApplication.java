package learn.booking_roomservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BookingRoomServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookingRoomServiceApplication.class, args);
    }

}
