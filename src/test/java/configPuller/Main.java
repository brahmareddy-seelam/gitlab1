package configPuller;

import java.io.IOException;
import java.net.URISyntaxException;

import com.uniphore.ri.main.e2e.BaseClass;
import com.uniphore.ri.main.e2e.TestCenter;

import stepDefinition.CommonSteps;

public class Main extends BaseClass{

	public static void main(String[] args) throws IOException, URISyntaxException {
		// TODO Auto-generated method stub

		String organization = "Hindi_ORG";
		String category = "BP_01";

		prop.setProperty("Backend", "3.140.134.237");
		port.setProperty("BACKEND_PORT", "3360");
		String pathForSaving = "C:\\Users\\murali\\workspace\\Hinglish";


	    TestCenter.getInstance().setAccessToken("l!5ZElx+DJy3#9</gZA_V9q5d2=hD9");
	    
	    CommonSteps.orgMap.put("organization", organization);
	    CommonSteps.orgMap.put("category", category);
	    
	    PullerandWriter.saveCallConfigFiles(pathForSaving);
//	    PullerandWriter.saveCallAlertConfigFiles(pathForSaving);
	    
	}

}