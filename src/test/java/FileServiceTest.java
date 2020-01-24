import com.system.bikesharing.services.FileService;
import com.system.pojo.UserRequest;
import org.junit.Test;

import java.io.IOException;

public class FileServiceTest {

    private FileService fileService = new FileService();

    @Test
    public void readStationDataTest() {
        try {
            fileService.readStationsData(new UserRequest("1010", "2019-09-01", "2019-09-30"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
