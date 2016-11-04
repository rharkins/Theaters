package com.stgconsulting;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Richard Harkins on 7/19/2016.
 */
public class CinemarkPage extends SeleniumWebdriverBaseClass {
    //WebDriver driver = new FirefoxDriver();
    //driver.navigate().to("http://www.google.com");

//    private WebDriver driver;
//    public BufferedWriter webdriver_theaters_bw = TheatersOutput();

    public CinemarkPage() throws IOException, BiffException {
    }

    //    @BeforeMethod
//    public void startFF() {
//        /*File pathToBinary = new File("C:\\Users\\Richard Harkins\\AppData\\Local\\Mozilla Firefox\\firefox.exe");
//        FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
//        FirefoxProfile firefoxProfile = new FirefoxProfile();
//        driver = new FirefoxDriver(ffBinary,firefoxProfile);*/
//        // Note - Running Firefox v 37.0 for compatibility with Selenium WebDriver
//        //driver = new FirefoxDriver();
//
//        // Chromedriver settings
//        File file = new File("C:\\ChromeDriver\\chromedriver.exe");
//        System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriver\\chromedriver.exe");
//        driver = new ChromeDriver();
//
//
//        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//    }

//    @Test
//    public BufferedWriter TheatersOutput() throws IOException {
//        File outputFile = new File("C:/test/selenium_theaters_output.txt");
//        // if file doesnt exists, then create it
//        if (!outputFile.exists()) {
//            outputFile.createNewFile();
//        }
//        FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
//        BufferedWriter bw = new BufferedWriter(fw);
//        //bw.write("This is a test");
//        //bw.close();
//        return bw;
//    }

    @Test(enabled = false)
    private List<WebElement> cinemarkSearchOld(Boolean titleSearchDropdown, WebElement searchControl, String searchString) throws InterruptedException {
        System.out.println("In cinemarkSearch");
        // Find the Search dialog at the top of the screen
        if (titleSearchDropdown == true) {
            // Perform code to select an item from the title dropdown using searchString
        } else {
            // Perform code to search in the search dialog text box using searchString
            searchControl.click();
            searchControl.sendKeys(searchString);
            Thread.sleep(5000);
            //driver.switchTo().window("Cinemark - Theatres");
            Set<String> windowIterator = driver.getWindowHandles();
            List<WebElement> theatersResults = driver.findElements(By.xpath(".//*[div][@class='item']"));
            List<WebElement> theatersLinks = driver.findElements(By.xpath(".//*[div][@class='item']//a[contains(@href,'theatre-detail')]"));
            for (WebElement links : theatersResults) {
                String url = links.getAttribute("href");
                String text = links.getText();

                //System.out.println(window.document.title)
                System.out.println(text);
                System.out.println("--------------------");
//                System.out.println(url);
            }
            Assert.assertEquals(searchString, searchString);
            return theatersLinks;
        }
        return null;
    }

    @Test(enabled = false)
    private List<WebElement> cinemarkSearch(WebElement searchTextBoxControl, String searchString, WebElement searchButton) throws InterruptedException
    {
        System.out.println("In cinemarkSearch");
       // Perform code to search in the search dialog text box using searchString
        searchTextBoxControl.sendKeys(searchString);
        searchButton.click();
        Thread.sleep(5000);
        //driver.switchTo().window("Cinemark - Theatres");
        Set<String> windowIterator = driver.getWindowHandles();
        List<WebElement> theatersResults = driver.findElements(By.xpath("//section[@class='searchResultTheatres']//a/span[1]"));
        List<WebElement> theatersLinks = driver.findElements(By.xpath("//section[@class='searchResultTheatres']//a"));
        List<WebElement> theatersAddresses = driver.findElements((By.xpath("//section[@class='searchResultTheatres']//p")));
        int addressIndex = 0;
        for (WebElement links : theatersResults)
        {
            String url = links.getAttribute("href");
            String addressText = theatersAddresses.get(addressIndex).getText();
            String text = links.getText() + "\n" + addressText;
            //System.out.println(window.document.title)
            System.out.println(text);
            System.out.println("--------------------");
            addressIndex++;
//                System.out.println(url);
        }
        Assert.assertEquals(searchString, searchString);
        return theatersLinks;
    }

    public void theaterSelect(List<WebElement> theaterList, String theater)
    {
        Boolean theaterFound = false;
        for (WebElement theaterListElement : theaterList)
        {
            System.out.println(theaterListElement.getText());
            if (theaterListElement.getText().equals(theater))
            {
                theaterFound = true;
                System.out.println("Found it");
//                System.out.println(theaterListElement.getText());
//                System.out.println(theater);
                theaterListElement.click();
                break;
            }
        }
        if (theaterFound == false) {
            System.out.println("Theater " + theater + " not found");
        }
    }

    public void getAllMovies() throws InterruptedException {
        List<WebElement> allMovies = driver.findElements(By.xpath("//div[@class='col-xs-12 col-sm-10']"));
        List<WebElement> dayButtons = driver.findElements(By.xpath("//li[@data-datevalue]"));
        WebElement currentDateAnchorElement = driver.findElement(By.xpath(".//li//a[@id=0]/.."));
        String movieDate = currentDateAnchorElement.getAttribute("data-datevalue");
        String movieName = null;
        WebElement imageURL = null;
        String imageXpath = null;

        for (WebElement movieElement : allMovies)
        {
            movieName = movieElement.findElement(By.tagName("h2")).getAttribute("innerHTML");
            System.out.println("--------------------");
            System.out.println(movieDate);
            System.out.println(movieElement.getText());
//            System.out.println(movieElement.getAttribute("id"));
//            System.out.println(movieName);
            //div[@class='hidden-xs col-sm-2']//img[contains(@alt,'Inferno Poster')]
            imageXpath = "//div[@class='hidden-xs col-sm-2']//img[contains(@alt,'" + movieName + " Poster')]";
//            imageURL = movieElement.findElement(By.xpath(".//img"));
//            imageURL = movieElement.findElement(By.xpath("//img[@class='img-responsive lazyloaded' and contains(@alt,'Poster')]"));
            if (movieName.contains("'"))
            {
                continue;
            }
            imageURL = movieElement.findElement(By.xpath(imageXpath));
            System.out.println(imageURL.getAttribute("data-srcset"));
//            Thread.sleep(5000);
        }
    }

    public void getNextSevenDaysMovies() throws InterruptedException {
        List<WebElement> movies = null;
        List<WebElement> dayButtons = driver.findElements(By.xpath("//ul[@id='showdatesMore']//li[@role='presentation']"));
        WebElement currentDateAnchorElement = driver.findElement(By.xpath(".//li//a[@id=0]/.."));
        WebElement moviesImageURL = null;
        String movieDate = currentDateAnchorElement.getAttribute("data-datevalue");
        String movieName = null;
        WebElement imageURL = null;
        String dateXpath = null;
        String imageXpath = null;
        for (int index = 1; index < 7; index++)
        {
            dayButtons.get(index).click();
            Thread.sleep(5000);
            dateXpath = ".//li//a[@id=" + index + "]/..";
            currentDateAnchorElement = driver.findElement(By.xpath(dateXpath));
            movieDate = currentDateAnchorElement.getAttribute("data-datevalue");
            movies = driver.findElements(By.xpath("//div[@class='col-xs-12 col-sm-10']"));
            for (WebElement movieElement : movies)
            {
                movieName = movieElement.findElement(By.tagName("h2")).getAttribute("innerHTML");
                System.out.println("--------------------");
                System.out.println(movieDate);
//                System.out.println(movieElement.getAttribute("id"));
                System.out.println(movieElement.getText());
                imageXpath = "//div[@class='hidden-xs col-sm-2']//img[contains(@alt,'" + movieName + " Poster')]";
                if (movieName.contains("'"))
                {
                    continue;
                }
                imageURL = movieElement.findElement(By.xpath(imageXpath));
                System.out.println(imageURL.getAttribute("data-srcset"));
            }
        }
    }

    @Test
    public void WebpageTest() throws IOException, BiffException, WriteException, InterruptedException {
        // Start Firefox
//        startFF();
        // Open the webpage for Cinemark
        driver.get("http://www.cinemark.com");
        // Locate the Homepage Search Text Box
        WebElement homepageSearchTextBox = driver.findElement(By.xpath("//form[@class='navbar-form row2Search']/input"));
        // Locate the Homepage Search Button
        WebElement homepageSearchButton = driver.findElement(By.xpath("//form[@class='navbar-form row2Search']/button"));
//        // Send search text to the Homepage Search Text Box
//        homepageSearchTextBox.sendKeys("Trolls");
//        // Click the Find a movie By Title dropdown control
//        homepageSearchButton.click();
//        // Create the moviesList and populate it with all movies from the Fina a Movie By Title dropdown
//        List<WebElement> moviesList = driver.findElements(By.xpath(".//*[@id='header']/div[2]/div/div/div/div[1]/div/div/ul/li/a"));
////		Select moviesSelect = new Select(myDriver.findElement(By.className("drop")));
////		List<WebElement> moviesList = moviesSelect.getOptions();
////		System.out.println(moviesList.listIterator())
//        // Print value of last movie in moviesList
//        System.out.println(moviesList.get(moviesList.size() - 1).getText());
//        // Print size of moviesList
//        System.out.println(moviesList.size());
//        // Check value of last movie in list with testng Assert
//        String lastMovieValueToCompare = "Ben-Hur";
//        String lastMovieValue = moviesList.get(moviesList.size() - 1).getText();
//        Assert.assertEquals(lastMovieValueToCompare, lastMovieValue);
//        //Assert.assertEquals(lastMovieValueToCompare, lastMovieValue);
//
////        WebElement searchTextBox = driver.findElement(By.id("main_inp1"));
////        WebElement searchButton = driver.findElement(By.xpath(""))

        List<WebElement> theaterResults = cinemarkSearch(homepageSearchTextBox, ("South Jordan"), homepageSearchButton);
        theaterSelect(theaterResults, "Cinemark Sugar House");
        getAllMovies();
        getNextSevenDaysMovies();

        // Find the submit button for the Search dialog at the top of the screen
//        WebElement searchDialogSubmitButton = driver.findElement(By.cssSelector("#main_theatres_search>fieldset>input[src]"));
//        searchDialogSubmitButton.click();

        theaters_bw.write("Accessing BufferedWriter object in WebPageTest");
        theaters_bw.newLine();
        theaters_bw.write("Accessing BufferedWriter object in WebPageTest - Line 2");
        theaters_bw.newLine();
        theaters_bw.write("Accessing BufferedWriter object in WebPageTest - Line 3");
        theaters_bw.close();

//        Workbook myExcelWorkbook = Workbook.getWorkbook(new File("C:/test/ROLL 2016 - Contact Information and Initial Deposit.xls"));
//        WritableWorkbook myWritableExcelWorkbook = Workbook.createWorkbook(new File("C:/test/ROLL 2016 - Contact Information and Initial Deposit.xls"), myExcelWorkbook);
//        int numSheets = myExcelWorkbook.getNumberOfSheets();
        File workbookFile = new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls");
        String workBookFilePath = workbookFile.getAbsolutePath();
        System.out.println(workBookFilePath);
//        Workbook theatersExcelWorkbook = Workbook.getWorkbook(new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls"));
//        WritableWorkbook theatersWritableExcelWorkbook = Workbook.createWorkbook(new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls"), theatersExcelWorkbook);

        int numSheets = theatersWritableExcelWorkbook.getNumberOfSheets();
        System.out.println("Number of sheets = " + numSheets);
//        Sheet mySheet = myExcelWorkbook.getSheet(0);
        Sheet mySheet = theatersWritableExcelWorkbook.getSheet(0);
        Cell myCell = mySheet.getCell(0, 0);
        String myContents = myCell.getContents();
        System.out.print(myContents);
        theatersWritableExcelWorkbook.write();
        theatersWritableExcelWorkbook.close();


    }
}