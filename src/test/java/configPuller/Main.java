package configPuller;

import java.io.IOException;
import java.net.URISyntaxException;

import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;

import stepDefinition.CommonSteps;

public class Main extends BaseClass{

	public static void main(String[] args) throws IOException, URISyntaxException {
		// TODO Auto-generated method stub

		String organization = "B And E Group Pvt Ltd";
		String category = "AE Banking";

		prop.setProperty("Backend", "52.52.125.4");
		port.setProperty("BACKEND_PORT", "3360");
		String pathForSaving = "C:\\Users\\murali\\workspace\\UDS";


	    TestCenter.getInstance().setAccessToken("l!5ZElx+DJy3#9</gZA_V9q5d2=hD9");
	    
	    CommonSteps.orgMap.put("organization", organization);
	    CommonSteps.orgMap.put("category", category);
	    
	    PullerandWriter.saveCallConfigFiles(pathForSaving);
	    PullerandWriter.saveCallAlertConfigFiles(pathForSaving);
	    
	}

}