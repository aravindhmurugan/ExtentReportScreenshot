import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class FreeCRMExtenttest {
	
	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extenttest;
	
	@BeforeTest
	public void setExtent() {
		extent = new ExtentReports(System.getProperty("user.dir")+"/test-output/ExtentReport.html",true);
		extent.addSystemInfo("Host Name", "Aravindh-PC");
		extent.addSystemInfo("User Name", "Aravindh");
		extent.addSystemInfo("Environment", "QA"); 
	}
	
	@AfterTest
	public void endReport() {
		extent.flush();
		extent.close();
	}
	

	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException{
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		// after execution, you could see a folder "FailedTestsScreenshots"
		// under src folder
		String destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/" + screenshotName + dateName+ ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}

	@BeforeMethod
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "E:/My Backups/chromedriver.exe");	
		driver = new ChromeDriver(); 
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.get("https://classic.crmpro.com");
		
	}

		@Test
		public void freeecrmtitletest() {
			extenttest = extent.startTest("freeecrmtitletest");
			String title = driver.getTitle();
			Assert.assertEquals(title, "CRMPRO  - CRM software for customer relationship management, sales, and support.");
		}
		
	@Test
	public void freecrmlogotest() {
		extenttest = extent.startTest("freecrmlogotest");
		boolean b = driver.findElement(By.xpath("//img[@class='img-responsive11']")).isDisplayed();
		Assert.assertTrue(b);
	}

		@AfterMethod
		public void tearDown(ITestResult result) throws IOException {
			
			if(result.getStatus()==ITestResult.FAILURE) {
				extenttest.log(LogStatus.FAIL, "TEST CASE FAILED IS " +result.getName());
				extenttest.log(LogStatus.FAIL, "TEST CASE FAILED IS " +result.getThrowable());
				
				String screenshotpath = FreeCRMExtenttest.getScreenshot(driver, result.getName());
				extenttest.log(LogStatus.FAIL, extenttest.addScreenCapture(screenshotpath));
			}
			
			else if(result.getStatus()==ITestResult.SKIP){
				extenttest.log(LogStatus.SKIP, "TEST CASE SKIPPED IS " +result.getName());
			}
			
			else if(result.getStatus()==ITestResult.SUCCESS) {
				extenttest.log(LogStatus.PASS, "TEST CASE PASSED IS " + result.getName());
			}
			
			extent.endTest(extenttest);
			driver.quit();
		}
		
	}
	

