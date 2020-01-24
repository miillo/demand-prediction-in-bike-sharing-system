import com.system.pojo.UserRequest;
import com.system.pojo.weather.WeatherAPI;
import com.system.weatherdata.services.WeatherService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import java.io.IOException;

public class WeatherServiceTest {

    private final WeatherService weatherService = new WeatherService();

    @Test
    public void weatherApiTest() {
        try {
            WeatherAPI weatherAPIResponse = weatherService
                    .getWeatherData(new UserRequest("72502", "2019-09-01", "2019-09-30"));
            assertNotNull(weatherAPIResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
