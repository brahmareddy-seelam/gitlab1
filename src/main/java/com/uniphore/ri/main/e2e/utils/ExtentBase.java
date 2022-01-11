//package com.uniphore.ri.main.e2e.utils;
//
//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.reporter.ExtentSparkReporter;
//import com.aventstack.extentreports.reporter.configuration.Theme;
//
//public class ExtentBase {
//public ExtentReports extent;
//public static ExtentTest scenarioDef;
//public static ExtentTest features;
//public String location=System.getProperty("user.dir")+"/target/SparkReport/Spark.html";
//
//
//public ExtentBase() {
//	 ExtentSparkReporter spark = new ExtentSparkReporter("ExtentSpark.html");
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
//
//public static void main(String args[]) {
//	new ExtentBase();
//	System.out.println();
//}
//	
//}
