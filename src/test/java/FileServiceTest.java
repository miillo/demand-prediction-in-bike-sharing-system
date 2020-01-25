import com.system.bikesharing.services.FileService;
import com.system.pojo.Station;
import com.system.pojo.Trip;
import com.system.pojo.UserRequest;
import com.system.settings.AppSettings;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class FileServiceTest {

    private FileService fileService = new FileService();

    @Before
    public void init() {
        AppSettings.readConfig();
    }

    @Test
    public void readStationDataTest() {
        try {
            List<Station> data = fileService.readStationsData(new UserRequest("1010", "2019-09-01", "2019-09-30"));
            data.forEach(System.out::println);
            assertThat(data).hasSize(17);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readTripDataTest() {
        try {
            AppSettings.readConfig();
            List<Trip> data = fileService.readTripsData(new UserRequest("1010", "2019-09-01", "2019-09-30"));
            data.forEach(System.out::println);
            assertThat(data).hasSize(13);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
