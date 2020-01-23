import com.system.pojo.UserRequest;
import com.system.weatherdata.services.WeatherService;
import org.junit.Test;

import java.io.IOException;

public class WeatherServiceTest {

    private final WeatherService weatherService = new WeatherService();

    @Test
    public void weatherApiTest() {
        try {
            weatherService.testWeather(new UserRequest("72502", "2019-09-01", "2019-09-30"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
