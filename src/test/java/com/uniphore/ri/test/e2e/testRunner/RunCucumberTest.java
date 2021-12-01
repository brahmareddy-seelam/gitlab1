package com.uniphore.ri.test.e2e.testRunner;
import org.junit.runner.RunWith;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features={"src/main/resources/features/"},
		glue={"stepDefinition"},
		plugin= {"pretty","html:target/cucumber.html","json:target/cucumber.json"},
		stepNotifications = true,
		monochrome=true)
public class RunCucumberTest  {
	
}