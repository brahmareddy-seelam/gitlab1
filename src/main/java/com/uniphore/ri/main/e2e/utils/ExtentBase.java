package com.uniphore.ri.main.e2e.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
//
public class ExtentBase {
//public ExtentReports extent;
//public static ExtentTest scenarioDef;
//public static ExtentTest features;
//public String location=System.getProperty("user.dir")+"/target/SparkReport/Spark.html";
//
//
	private static final String CODE1 = "{\n    \"theme\": \"standard\",\n    \"encoding\": \"utf-8\n}";
    private static final String CODE2 = "{\n    \"protocol\": \"HTTPS\",\n    \"timelineEnabled\": false\n}";
//public ExtentBase() {
//	 ExtentSparkReporter spark = new ExtentSparkReporter("/target/SparkReport/Spark.html");
//	 ExtentReports extent = new ExtentReports();
//	 extent.attachReporter(spark);
//	 extent.createTest("TestName").pass("Test Passed");
//	 
//	 extent.flush();
////	spark.config().setTheme(Theme.DARK);
////	spark.config().setDocumentTitle("API Cucucmber Automation");
////	spark.config().setEncoding("utf-8");
////	spark.config().setReportName("Test Report");
////	extent.attachReporter(spark);
//	System.out.println();
//}

public static void main(String args[]) {
	  ExtentReports extent = new ExtentReports();
      ExtentSparkReporter spark = new ExtentSparkReporter("target/Spark/Spark.html");
      extent.attachReporter(spark);
      
      
      extent.createTest("ScreenCapture")
              .addScreenCaptureFromPath("extent.png")
              .pass(MediaEntityBuilder.createScreenCaptureFromPath("extent.png").build());

      extent.createTest("LogLevels")
              .info("info")
              .pass("pass")
              .warning("warn")
              .skip("skip")
              .fail("fail");

      extent.createTest("CodeBlock").generateLog(
              Status.PASS,
              MarkupHelper.createCodeBlock(CODE1, CODE2));

      extent.createTest("ParentWithChild")
              .createNode("Child")
              .pass("This test is created as a toggle as part of a child test of 'ParentWithChild'");

      extent.createTest("Tags")
              .assignCategory("MyTag")
              .pass("The test 'Tags' was assigned by the tag <span class='badge badge-primary'>MyTag</span>");

      extent.createTest("Authors")
              .assignAuthor("TheAuthor")
              .pass("This test 'Authors' was assigned by a special kind of author tag.");

      extent.createTest("Devices")
              .assignDevice("TheDevice")
              .pass("This test 'Devices' was assigned by a special kind of devices tag.");

      extent.createTest("Exception! <i class='fa fa-frown-o'></i>")
              .fail(new RuntimeException("A runtime exception occurred!"));

      extent.flush();
}
//	
}
