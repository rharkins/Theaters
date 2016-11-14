package com.stgconsulting;

import jxl.Cell;
import jxl.Sheet;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Richard Harkins on 11/2/2016.
 */
public class CarmikePage extends SeleniumWebdriverBaseClass{
    DocumentBuilderFactory xmlDocFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder xmlDocBuilder = xmlDocFactory.newDocumentBuilder();
    Document xmlDoc = xmlDocBuilder.newDocument();
    Element xmlRootElement = xmlDoc.createElement("Movies");

    public CarmikePage() throws IOException, BiffException, ParserConfigurationException {
    }

    @Test(enabled = false)
    private List<WebElement> carmikeSearch(WebElement searchTextBoxControl, String searchString, WebElement searchButton) throws InterruptedException, IOException {
        fileAndConsoleOutput(theaters_bw, "In carmikeSearch");
        // Perform code to search in the search dialog text box using searchString
        searchTextBoxControl.sendKeys(searchString);
        searchButton.click();
        Thread.sleep(5000);
        //driver.switchTo().window("Carmike - Theatres");
        Set<String> windowIterator = driver.getWindowHandles();
        List<WebElement> theatersResults = driver.findElements(By.xpath("//div[@class='gridRow finder-results']"));
        List<WebElement> theatersLinks = driver.findElements(By.xpath("//a//div[@class='finder-results-cinema-head left']"));
        List<WebElement> theatersAddresses = driver.findElements(By.xpath("//div[@class='gridRow finder-results']//div//a//div[contains(@class,'address')]"));
        int addressIndex = 0;
        for (WebElement links : theatersResults)        {
            String url = links.getAttribute("href");
            String addressText = theatersAddresses.get(addressIndex).getText();
            String text = links.getText() + "\n" + addressText;
            //System.out.println(window.document.title)
            fileAndConsoleOutput(theaters_bw, text);
            fileAndConsoleOutput(theaters_bw, "--------------------");
            addressIndex++;
//                System.out.println(url);
        }
        Assert.assertEquals(searchString, searchString);
        return theatersLinks;
    }

    public void theaterSelect(List<WebElement> theaterList, String theater) throws IOException {
        Boolean theaterFound = false;
        for (WebElement theaterListElement : theaterList)
        {
            fileAndConsoleOutput(theaters_bw, theaterListElement.getText());
            String theaterListElementText = theaterListElement.getText();
            if (theaterListElementText.contains(theater))
            {
                theaterFound = true;
                fileAndConsoleOutput(theaters_bw, "Found it");
//                System.out.println(theaterListElement.getText());
//                System.out.println(theater);
                theaterListElement.click();
                break;
            }
        }
        if (theaterFound == false) {
            fileAndConsoleOutput(theaters_bw, "Theater " + theater + " not found");
        }
    }

    @Test
    public void WebpageTest() throws IOException, BiffException, WriteException, InterruptedException, TransformerException, JSONException {
        // Start Firefox
//        startFF();
        // Open the webpage for Cinemark
        driver.get("http://www.carmike.com");
        // Locate the Homepage Search Text Box
        WebElement homepageSearchTextBox = driver.findElement(By.xpath("//input[@data-finder-search-input='main']"));
        // Locate the Homepage Search Button
        WebElement homepageSearchButton = driver.findElement(By.xpath("//button[@data-finder-search-btn='main']"));
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

        List<WebElement> theaterResults = carmikeSearch(homepageSearchTextBox, ("84009"), homepageSearchButton);
        theaterSelect(theaterResults, "WYNNSONG 12");
//        getAllMovies();
//        getNextSevenDaysMovies();

        // Find the submit button for the Search dialog at the top of the screen
//        WebElement searchDialogSubmitButton = driver.findElement(By.cssSelector("#main_theatres_search>fieldset>input[src]"));
//        searchDialogSubmitButton.click();

        fileAndConsoleOutput(theaters_bw, "Accessing BufferedWriter object in WebPageTest");
        fileAndConsoleOutput(theaters_bw, "Accessing BufferedWriter object in WebPageTest - Line 2");
        fileAndConsoleOutput(theaters_bw, "Accessing BufferedWriter object in WebPageTest - Line 3");

//        Workbook myExcelWorkbook = Workbook.getWorkbook(new File("C:/test/ROLL 2016 - Contact Information and Initial Deposit.xls"));
//        WritableWorkbook myWritableExcelWorkbook = Workbook.createWorkbook(new File("C:/test/ROLL 2016 - Contact Information and Initial Deposit.xls"), myExcelWorkbook);
//        int numSheets = myExcelWorkbook.getNumberOfSheets();
        File workbookFile = new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls");
        String workBookFilePath = workbookFile.getAbsolutePath();
        fileAndConsoleOutput(theaters_bw, workBookFilePath);
//        Workbook theatersExcelWorkbook = Workbook.getWorkbook(new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls"));
//        WritableWorkbook theatersWritableExcelWorkbook = Workbook.createWorkbook(new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls"), theatersExcelWorkbook);

        int numSheets = theatersWritableExcelWorkbook.getNumberOfSheets();
        fileAndConsoleOutput(theaters_bw, "Number of sheets = " + numSheets);
//        Sheet mySheet = myExcelWorkbook.getSheet(0);
        Sheet mySheet = theatersWritableExcelWorkbook.getSheet(0);
        Cell myCell = mySheet.getCell(0, 0);
        String myContents = myCell.getContents();
        System.out.print(myContents);
        theatersWritableExcelWorkbook.write();
        theatersWritableExcelWorkbook.close();
        theaters_bw.close();
        json_theaters_bw.close();
        xml_movies_bw.close();;

    }
}
