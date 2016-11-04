package com.stgconsulting;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Richard Harkins on 7/19/2016.
 */
public class SeleniumWebdriverBaseClass {
    public static WebDriver driver;
    static String baseWebPageURL = "http://www.cinemark.com";
    public static boolean browserStarted = false;
    //    public static HashMap<String, String> resortList = new HashMap<String, String>();
    public BufferedWriter theaters_bw = createOutputFile();
    File workbookFileInput = new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls");
    File workbookFileOutput = new File("C:\\test\\Theaters_Output.xls");
//    File workbookFileInput = new File("C://test//ROLL 2016 - Contact Information and Initial Deposit.xls");
//    File workbookFileOutput = new File("C://test//Theaters_Output.xls");
//    File workbookFileInput = new File("C:\test\ROLL 2016 - Contact Information and Initial Deposit.xls");
//    File workbookFileOutput = new File("C:\test\Theaters_Output.xls");
//    File workbookFileInput = new File("C:/test/ROLL 2016 - Contact Information and Initial Deposit.xls");
//    File workbookFileOutput = new File("C:/test/Theaters_Output.xls");
    Workbook theatersExcelWorkbook = Workbook.getWorkbook(workbookFileInput);
    WritableWorkbook theatersWritableExcelWorkbook = Workbook.createWorkbook(workbookFileOutput, theatersExcelWorkbook);

    public SeleniumWebdriverBaseClass() throws IOException, BiffException {
    }

    @BeforeMethod
    public void startup() throws IOException, BiffException {
        Initialize();
        startBrowser();
    }

    public void Initialize() throws IOException, BiffException {
        // Populate resortList HashMap
//        resortList.put("beaver mountain", "Beaver Mtn");
//        resortList.put("cherry peak", "Cherry Peak");
//        resortList.put("nordic valley", "Nordic Valley");
//        resortList.put("powder mountain", "Powder Mtn");
//        resortList.put("snowbasin", "Snowbasin");
//        resortList.put("alta", "Alta");
//        resortList.put("brighton", "Brighton");
//        resortList.put("snowbird", "Snowbird");
//        resortList.put("solitude", "Solitude");
//        resortList.put("deer valley", "Deer Valley");
//        resortList.put("park city", "Park City");
//        resortList.put("sundance", "Sundance");
//        resortList.put("brian head", "Brian Head");
//        resortList.put("eagle point", "Eagle Point");

        // Create BufferedWriter for output file

        // Create jxl Workbook

    }

    public static void startBrowser() {
        // Firefoxdriver settings
//        File pathToBinary = new File("C:\\Users\\Richard Harkins\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
//        FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
//        FirefoxProfile firefoxProfile = new FirefoxProfile();
//        driver = new FirefoxDriver(ffBinary, firefoxProfile);
        //driver = new FirefoxDriver();

        // Chromedriver settings
        File file = new File("C:\\ChromeDriver\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
        driver = new ChromeDriver();

        // Set all new windows to maximize
        driver.manage().window().maximize();
        // Set an implicit wait of 60 seconds to handle delays in loading and finding elements
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    }

    public BufferedWriter createOutputFile() throws IOException {
        File outputFile = new File("C:/test/theaters.txt");
        // If file doesnt exists, then create it
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        return bw;
    }

    @AfterMethod
    public void killBrowser() {
//        driver.close();
    }

}
