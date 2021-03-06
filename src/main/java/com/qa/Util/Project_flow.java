package com.qa.Util;



public class Project_flow {

	
	/*
	 * first we create login feature file then execute it which will enerate the stepdefinition.
	 * we will copy them in step definitinition file Loginpagesteps.
	 * 
======================
Loginpage.feature


	 * Feature: Login page feature

Scenario: Login page title
Given user is on login page
When user gets the title of the page
Then page title should be "Login - My Store"

======================


	
	
============================
driverfactory.java 

	 * Then create driverfactory.java 
	 * * This method is used to initialize the thradlocal driver on the basis of given
	 * browser
	 * 
	 * public WebDriver init_driver(String browser) {

		System.out.println("browser value is: " + browser);

		if (browser.equals("chrome")) {
			WebDriverManager.chromedriver().setup();
			tlDriver.set(new ChromeDriver());
			
=============================
			
ApplicationHooks.java

	 * Then we use ApplicationHooks.java
	 * 
	 * Hooks will run before each scenario. order=0 will run 1st in @before. 
	 order=1 will run 1st in @after.
	 
	 so as per hooks before running each scenario, order=0 hook will initilalize prop file and order=1 hook will
	 launch the new driver using driverfactory class object as below.
	 
	 @Before(order = 0)
	 	public void getProperty() {
		configReader = new ConfigReader();
		prop = configReader.init_prop();
		
		
	 
	@Before(order = 1)
	public void launchBrowser() {
		String browserName = prop.getProperty("browser");
		driverFactory = new DriverFactory();
		driver = driverFactory.init_driver(browserName);
	 
	 after running each sceanrion , order=1 hook  will run 1st which checks if any sceanrio is failed and if yes it will
	 attach failed screenshot the same scenario.
	 
	 after that order=0 hook will run which will quit  the browser.
	 
	 @After(order = 1)
	public void tearDown(Scenario scenario) {
		if (scenario.isFailed()) {
		String screenshotName = scenario.getName().replaceAll(" ", "_");
		byte[] sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			scenario.attach(sourcePath, "image/png", screenshotName);
	
		@After(order = 0)
	public void quitBrowser() {
		driver.quit();
	}
	
===============================
LoginPage.java page class

public class LoginPage {

	private WebDriver driver;

	// 1. By Locators: OR
	private By emailId = By.id("email");
	private By password = By.id("passwd");
	private By signInButton = By.id("SubmitLogin");
	private By forgotPwdLink = By.linkText("Forgot your password?111");

	// 2. Constructor of the page class:
	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}

	// 3. page actions: features(behavior) of the page the form of methods:

	public String getLoginPageTitle() {
		return driver.getTitle();
	}

	public boolean isForgotPwdLinkExist() {
		return driver.findElement(forgotPwdLink).isDisplayed();
	}

	public void enterUserName(String username) {
		driver.findElement(emailId).sendKeys(username);
	}

	public void enterPassword(String pwd) {
		driver.findElement(password).sendKeys(pwd);
	}

	public void clickOnLogin() {
		driver.findElement(signInButton).click();
	}

	public AccountsPage doLogin(String un, String pwd) {
		System.out.println("login with: " + un + " and " + pwd);
		driver.findElement(emailId).sendKeys(un);
		driver.findElement(password).sendKeys(pwd);
		driver.findElement(signInButton).click();
		return new AccountsPage(driver);
	}
	


Created loginpage class object inside stepdefinitions LoginPageSteps and then call page class methods.

so feature file will 1st trigger .

Scenario: Login with correct credentials
Given user is on login page
When user enters username "dec2020secondbatch@gmail.com"
And user enters password "Selenium@12345"
And user clicks on Login button
Then user gets the title of the page
And page title should be "My account - My Store"


Then it will call step def and from there page class methods are called. 

private LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
title = loginPage.getLoginPageTitle();

Page class will use the values passed from
feature file.

========================
	
LoginPageSteps.java
All assertions has to be done in test class inside stepdefinitoons not in the main class.


public class LoginPageSteps {

	private static String title;
	private LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
	/*
	 * Created loginpage class object and then call its methods like getLoginPageTitle, isForgotPwdLinkExist.
	 * All assertions has to be done in test class inside stepdefinitoons not in the main class.

	

	@Given("user is on login page")
	public void user_is_on_login_page() {

		DriverFactory.getDriver()
				.get("http://automationpractice.com/index.php?controller=authentication&back=my-account");
	}

	@When("user gets the title of the page")
	public void user_gets_the_title_of_the_page() {
		title = loginPage.getLoginPageTitle();
		System.out.println("Page title is: " + title);
	}

	@Then("page title should be {string}")
	public void page_title_should_be(String expectedTitleName) {
		Assert.assertTrue(title.contains(expectedTitleName));
	}

	@Then("forgot your password link should be displayed")
	public void forgot_your_password_link_should_be_displayed() {
		Assert.assertTrue(loginPage.isForgotPwdLinkExist());
	}

	@When("user enters username {string}")
	public void user_enters_username(String username) {
		loginPage.enterUserName(username);
	}

	@When("user enters password {string}")
	public void user_enters_password(String password) {
		loginPage.enterPassword(password);
	}

	@When("user clicks on Login button")
	public void user_clicks_on_login_button() {
		loginPage.clickOnLogin();
	}

=======================
package testrunners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/test/resources/AppFeatures"},
		glue = {"stepdefinitions", "AppHooks"},
		plugin = {"pretty",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"timeline:test-output-thread/"
							
		         }
		
		)

public class MyTestRunner {

}

Now run the testrunner file as above.

=====================

we can now fail the test by changing the page locators.

LoginPage.java page class

public class LoginPage {

	private WebDriver driver;

	// 1. By Locators: OR
	private By emailId = By.id("email");
	private By password = By.id("passwd");

	private By forgotPwdLink = By.linkText("Forgot your password?111");

This hook will execute 1st with order=1

Which says that if scenario is getting failed then take a screenshot.

this will fail the Forgot password scenario and screenshot will be taken.

Go to the cucumber report url.

This shows the screenshot of the failed test along with error message.

===========================

Now create  accountspage.feature.

Feature: Account Page Feature

Background:
Given user has already logged in to application
|username|password|
|dec2020secondbatch@gmail.com|Selenium@12345|

#We are using the concept of datatables in background .
#We are not using concept of examples using data driven.

@accounts
Scenario: Accounts page title
Given user is on Accounts page
When user gets the title of the page
Then page title should be "My account - My Store"

@accounts
Scenario: Accounts section count
Given user is on Accounts page
Then user gets accounts section
#Here also we are using datatable concept using user gets account section details.
|ORDER HISTORY AND DETAILS|
|MY CREDIT SLIPS|
|MY ADDRESSES|
|MY PERSONAL INFORMATION|
|MY WISHLISTS|
|Home|
And accounts section count should be 6



We are using the concept of datatables in background .
We are not using concept of examples using data driven.

Here also we are using datatable concept using user gets account section details.

11, 12th line is not flagged as we already have its definition.

run the feature file using run configuation that will generate step definitions.

==========================

public class AccountsPageSteps {

	private LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
   * Create loginpage class object.
	private AccountsPage accountsPage;

	@Given("user has already logged in to application")
	public void user_has_already_logged_in_to_application(DataTable credTable) {
		
   * import io.cucumber.datatable.DataTable; for datatable.

	 List<Map<String, String>> credList = credTable.asMaps();
	 
   *  Use .asMaps which will return list<Map>.
	 
	 String userName = credList.get(0).get("username");
	 
	 String password = credList.get(0).get("password");

   * Index 0 will give us 1st map.
   * We need to get the value of key username,password from feature file.

   * Map will be created like username as key, password as key and their corresponding values below.
   * So credlist.get(0) will give 1st map and from there we are taking key as username.
   * 
   * accountspage.feature.
   * Background:
   * Given user has already logged in to application
   * |username|password|
   * |dec2020secondbatch@gmail.com|Selenium@12345|

   * 
   * In loginpage.java class, we are creating 1 dologin() method to do login.

   *    public AccountsPage doLogin(String un, String pwd) {
   *	System.out.println("login with: " + un + " and " + pwd);
   *		driver.findElement(emailId).sendKeys(un);
   *		driver.findElement(password).sendKeys(pwd);
   *		driver.findElement(signInButton).click();
   *		return new AccountsPage(driver);


   * After login in the loginpage with username, password we reaches to the accountpage so above method dologin should
   * Return accountpage class object.
   * 
   * 
   * In same class AccountsPageSteps.java create below Loginpageclass object.
   * 	private LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
   *    Create loginpage class object.
		
 

		DriverFactory.getDriver()
				.get("http://automationpractice.com/index.php?controller=authentication&back=my-account");
		accountsPage = loginPage.doLogin(userName, password);
/*
 *   Above username, password is coming from accountspage feature file in the form of datatable credtable.
 *   So now we have used accountpage referenece and saved details of loginpage as loginpage succesful login will return accountpage.
 
 * accountspage.feature.
 * Background:
 * Given user has already logged in to application
 * |username|password|
 * |dec2020secondbatch@gmail.com|Selenium@12345|

	}

	@Given("user is on Accounts page")
	public void user_is_on_accounts_page() {
		String title = accountsPage.getAccountsPageTitle();
 *   Calling accountpagetitle method after hitting the url and loginpage is done using username,password.
		System.out.println("Accounts Page title is: " + title);
	}

	@Then("user gets accounts section")
	public void user_gets_accounts_section(DataTable sectionsTable) {

		List<String> expAccountSectionsList = sectionsTable.asList();
	
		/*
		 We will get above data table in the form of list from feature file.
		 |ORDER HISTORY AND DETAILS|
|MY CREDIT SLIPS|
|MY ADDRESSES|
|MY PERSONAL INFORMATION|
|MY WISHLISTS|
|Home|
		 
		
		System.out.println("Expected accounts section list: " + expAccountSectionsList);

		List<String> actualAccountSectionsList = accountsPage.getAccountsSectionsList();
		/*
		 * above will return the actual account list.
		 * 
		 * private By accountSections = By.cssSelector("div#center_column span");
		 * public List<String> getAccountsSectionsList() {

		List<String> accountsList = new ArrayList<>();
		List<WebElement> accountsHeaderList = driver.findElements(accountSections);

		for (WebElement e : accountsHeaderList) {
			String text = e.getText();
			System.out.println(text);
			accountsList.add(text);
		}

		return accountsList;
		 
		System.out.println("Actual accounts section list: " + actualAccountSectionsList);

		Assert.assertTrue(expAccountSectionsList.containsAll(actualAccountSectionsList));

	}

	@Then("accounts section count should be {int}")
	public void accounts_section_count_should_be(Integer expectedSectionCount) {
		Assert.assertTrue(accountsPage.getAccountsSectionCount() == expectedSectionCount);
	}

}

===============================
AccountsPage.java


package com.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AccountsPage {

	private WebDriver driver;

	private By accountSections = By.cssSelector("div#center_column span");

	public AccountsPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getAccountsPageTitle() {
		return driver.getTitle();
	}

	public int getAccountsSectionCount() {
		return driver.findElements(accountSections).size();
	}

	public List<String> getAccountsSectionsList() {

		List<String> accountsList = new ArrayList<>();
		List<WebElement> accountsHeaderList = driver.findElements(accountSections);

		for (WebElement e : accountsHeaderList) {
			String text = e.getText();
			System.out.println(text);
			accountsList.add(text);
		}

		return accountsList;

	}

}
=========================

AccountsPage.java

package com.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AccountsPage {

	private WebDriver driver;

	private By accountSections = By.cssSelector("div#center_column span");

	public AccountsPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getAccountsPageTitle() {
		return driver.getTitle();
	}

	public int getAccountsSectionCount() {
		return driver.findElements(accountSections).size();
	}

	public List<String> getAccountsSectionsList() {

		List<String> accountsList = new ArrayList<>();
		List<WebElement> accountsHeaderList = driver.findElements(accountSections);

		for (WebElement e : accountsHeaderList) {
			String text = e.getText();
			System.out.println(text);
			accountsList.add(text);
		}

		return accountsList;

	}

}
==============================

pom.xml

* add extent dependency in pom.xml.

	<dependency>
			<groupId>tech.grasshopper</groupId>
			<artifactId>extentreports-cucumber6-adapter</artifactId>
			<version>2.8.4</version>
			<scope>test</scope>
		</dependency>
		
===============================

extent.properties

* add below properties file in src/test/resources

extent.reporter.spark.start=true
extent.reporter.spark.out=test-output/SparkReport/Spark.html
extent.reporter.spark.config=src/test/resources/extent-config.xml

extent.reporter.spark.out=test-output/SparkReport/

screenshot.dir=test-output/
screenshot.rel.path=../
extent.reporter.pdf.start=true
extent.reporter.pdf.out=test output/PdfReport/ExtentPdf.pdf
#basefolder.name=reports
#basefolder.datetimepattern=d-MMM-YY HH-mm-ss
extent.reporter.spark.vieworder=dashboard,test,category,exception,author,device,log
systeminfo.os=Mac
systeminfo.user=Naveen
systeminfo.build=1.1
systeminfo.AppName=AutomationPractice


===================
extent_config.xml

<!--add below xml file in src/test/resources.-->
<!--This file xml is basically used to show the format of extent report-->



<?xml version="1.0" encoding="UTF-8"?>
<extentreports>
	<configuration>
		<!-- report theme -->

plugin = {"pretty",
	      "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
-->
		<!-- standard, dark -->

		<theme>dark</theme>
		<!-- document encoding -->

		<!-- defaults to UTF-8 -->

		<encoding>UTF-8</encoding>
		<!-- protocol for script and stylesheets -->

		<!-- defaults to https -->

		<protocol>http</protocol>
		<!-- title of the document -->
		<documentTitle>Extent</documentTitle>
		<!-- report name - displayed at top-nav -->

		<reportName>Grasshopper Report</reportName>
		<!-- location of charts in the test view -->

		<!-- top, bottom -->

		<testViewChartLocation>bottom</testViewChartLocation>
		<!-- custom javascript -->

		<scripts>

<![CDATA[
$(document).ready(function() {
});
]]>
		</scripts>
		<!-- custom styles -->
		<styles>
<![CDATA[
]]>
		</styles>
	</configuration>
</extentreports>

============================
MyTestRunner.java

<!-- Then we need to add extentcucumber adapter in testrunner..
* Then run this testrunner as junit which will generate extent spark html reports under SparkReport
  as per extent.properties.

* Under test output spark report gets generated.
* Screenshot created under test-output.
* 
* Open spark report folder index.html file
* Go to the browser and paste the path to open extent html report.
* 
* Bug section shows failed assertion.
* Also we get failed screenshot.
* 
* Tags @accounts shows 2 tests were there and both got passed.

* All these system environments info comes from extentconfig.xml like user:Naveen, os:Mac.
* 
* Title of the report and report name can be changed also from xml file.
  Like Extent and Grassshopper.

* Chart is coming at bottom as we used bottom.

* Theme used is dark. We can use standard also if we need in white colour.

* Now open test output folder , pdf report.
* Also the failed test details shown in report.

package testrunners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/test/resources/AppFeatures"},
		glue = {"stepdefinitions", "AppHooks"},
		plugin = {"pretty",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"timeline:test-output-thread/"
							
		         }
		
		)

public class MyTestRunner {

}

=========================

Project flow ends:

	 */
	
	 
	
}
