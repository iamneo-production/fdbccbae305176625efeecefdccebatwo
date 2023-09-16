package runner;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringDecorator;
//import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.LoggerHandler;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import utils.base64;
import utils.Screenshot;


import utils.EventHandler;
import utils.Reporter;
// import utils.Screenshot;
public class Testcase1 extends Base {

    java.util.logging.Logger log =  LoggerHandler.getLogger();
    base64 screenshotHandler = new base64();
    ExtentReports reporter = Reporter.generateExtentReport();;
    
    
    
    @Test(priority = 1, dataProvider="testData")
    public void Register(String username, String password, String depositAmount, String withdrawAmount) throws IOException {
        try {
            ExtentTest test = reporter.createTest("Registeration Page", "Execution for registeration");

            driver.get(prop.getProperty("url") + "/login");
            driver.manage().window().maximize();
            log.info("Browser Launched");
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.findElement(By.name("username")).sendKeys(username);
            log.info("Mail ID Sent");
            driver.findElement(By.id("password")).sendKeys(password);
            log.info("Password Sent");
            driver.findElement(By.id("submit")).click();
            log.info("Submit Button clicked");
            Duration timeout = Duration.ofSeconds(10);
            WebDriverWait wait = new WebDriverWait(driver,timeout);
            WebElement depositLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Deposit")));
            depositLink.click();
        Select accType=new Select(driver.findElement(By.id("selectedAccount")));
        log.info("Account Type has been selected");
        accType.selectByVisibleText("Individual Checking (Standard Checking)");
        driver.findElement(By.id("amount")).sendKeys(depositAmount);
        log.info("Amount has been sent");
        driver.findElement(By.xpath("//button[text()=' Submit']")).click();
        String base64Screenshot = screenshotHandler.captureScreenshotAsBase64(driver);
        test.pass("Test passed successfully", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        Screenshot.getScreenShot("Login_Screenshot");
        
        } catch (Exception ex) {
            ExtentTest test = reporter.createTest("Register Exception");
            Screenshot.getScreenShot("Login_Screenshot");
            String base64Screenshot = screenshotHandler.captureScreenshotAsBase64(driver);
            test.log(Status.FAIL, "Unable to Perform the Register", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            ex.printStackTrace();
        }
        WebDriverListener listener = new EventHandler();
        driver = new EventFiringDecorator<>(listener).decorate(driver);
        return;
    }

    @Test(priority = 2, dataProvider="testData")
    public void Valid_Login_TC(String username, String password, String depositAmount, String withdrawAmount) throws IOException {
        try {
            ExtentTest test = reporter.createTest("Basic log", "Execution for Login Function");
            driver.get(prop.getProperty("url") + "/Login");
            driver.findElement(By.name("username")).sendKeys(username);
            log.info("User name sent");
            driver.findElement(By.id("password")).sendKeys(password);
            log.info("Password Sent");
            driver.findElement(By.id("submit")).click();
            log.info("Submit Button clicked");
            test.pass("Test passed successfully");
        } 
        catch (Exception ex) {
            ExtentTest test = reporter.createTest("Valid_Login_TC Exception");
            Screenshot.getScreenShot("Valid_Login_TC");
            String base64Screenshot = screenshotHandler.captureScreenshotAsBase64(driver);
            test.log(Status.FAIL, "Unable to Perform Valid_Login_TC", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
            ex.printStackTrace();
        }
    }
    @Test(priority=3, dataProvider="testData")
  public void WithdrawTest(String username,  String password, String depositAmount, String withdrawAmount) throws IOException {
    try {
        ExtentTest test = reporter.createTest("Withdraw Test", "Execution for withdraw operation");      
        driver.get(prop.getProperty("url") + "/login");     
        driver.findElement(By.id("username")).sendKeys(username);
        log.info("User name sent");
        driver.findElement(By.id("password")).sendKeys(password);
        log.info("Password Sent");
        driver.findElement(By.id("submit")).click();
        log.info("Submit Button clicked");
        driver.findElement(By.linkText("Withdraw")).click();
        log.info("Withdraw Button click");
        Select accType = new Select(driver.findElement(By.id("selectedAccount")));
        log.info("Account type has been Selected");
        accType.selectByVisibleText("Individual Checking (Standard Checking)");
        driver.findElement(By.id("amount")).sendKeys(withdrawAmount);
        driver.findElement(By.xpath("//button[text()=' Submit']")).click();
        test.pass("Test passed successfully");
    }
catch(Exception ex){
        ex.printStackTrace();
        Screenshot.getScreenShot("WithdrawTest");
        ExtentTest test = reporter.createTest("WithdrawTestException");
        String base64Screenshot = screenshotHandler.captureScreenshotAsBase64(driver);
        test.log(Status.FAIL, "Unable to Perform the WithdrawTest ", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
       
    }

}

@DataProvider(name = "testData")
    public Object[][] readTestData() throws IOException {
        String excelFilePath = System.getProperty("user.dir") + "/src/test/java/resources/Testdata.xlsx";
        String sheetName = "Sheet1";
    
        try (FileInputStream fileInputStream = new FileInputStream(excelFilePath);
             Workbook workbook = WorkbookFactory.create(fileInputStream)) {
    
            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getLastRowNum();
    
            Object[][] searchDataArray = new Object[rowCount][4]; 
    
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
    
                searchDataArray[i - 1][0] = getStringCellValue(row.getCell(0));
                searchDataArray[i - 1][1] = getStringCellValue(row.getCell(1));
                searchDataArray[i - 1][2] = getStringCellValue(row.getCell(2));
                searchDataArray[i - 1][3] = getStringCellValue(row.getCell(3));
                
            }
    
            return searchDataArray;
        }
    }
    
    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.format("%d", (long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            default:
                return "";
        }
    }

@BeforeMethod
public void beforeMethod() throws MalformedURLException {
    openBrowser();

}

    @AfterMethod
    public void afterMethod() {
        driver.quit();
        reporter.flush();
        log.info("Browser closed");
        LoggerHandler.closeHandler();
    }
}
