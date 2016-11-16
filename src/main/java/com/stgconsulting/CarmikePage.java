package com.stgconsulting;

import jxl.Cell;
import jxl.Sheet;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
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
    private BufferedWriter carmike_theaters_bw = createAnyOutputFile("C:/test/carmike_theaters.txt");
    private BufferedWriter carmike_json_theaters_bw = createAnyOutputFile("C:/test/carmike_json_theaters.txt");
    private BufferedWriter carmike_xml_movies_bw = createAnyOutputFile("C:/test/carmike_movies.xml");
    private BufferedWriter carmike_csv_movies_bw = createAnyOutputFile("C:/test/carmike_Movies_Output.csv");

    public CarmikePage() throws IOException, BiffException, ParserConfigurationException {
    }

    @Test(enabled = false)
    private List<WebElement> carmikeSearch(WebElement searchTextBoxControl, String searchString, WebElement searchButton) throws InterruptedException, IOException {
        fileAndConsoleOutput(carmike_theaters_bw, "In carmikeSearch");
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
            fileAndConsoleOutput(carmike_theaters_bw, text);
            fileAndConsoleOutput(carmike_theaters_bw, "--------------------");
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
            fileAndConsoleOutput(carmike_theaters_bw, theaterListElement.getText());
            String theaterListElementText = theaterListElement.getText();
            if (theaterListElementText.contains(theater))
            {
                theaterFound = true;
                fileAndConsoleOutput(carmike_theaters_bw, "Found it");
//                System.out.println(theaterListElement.getText());
//                System.out.println(theater);
                theaterListElement.click();
                break;
            }
        }
        if (theaterFound == false) {
            fileAndConsoleOutput(carmike_theaters_bw, "Theater " + theater + " not found");
        }
    }

    public void getAllMovies() throws InterruptedException, IOException, TransformerException {
        List<WebElement> allMovies = driver.findElements(By.xpath("//li[contains(@class,'gridCol-s-12 gridCol-m-6 gridCol-l-6 filmItem active')]//div[@class='filmItemContent']"));
        List<WebElement> dayButtons = driver.findElements(By.xpath("//div[@class='dateList']//label[not(contains(@class,'disabled'))]"));
        WebElement currentDateAnchorElement = driver.findElement(By.xpath("//div[@class='dateList']//label[not(contains(@class,'disabled'))][1]"));
        String movieDate = driver.findElement(By.xpath("//div[@class='dateList']//label[not(contains(@class,'disabled'))][1]//span[1]")).getAttribute("innerHTML");
        String movieName = null;
        String movieInfoAndShowtimes = null;
        WebElement imageURL = null;
        String imageXpath = null;
        String movieImagePath = null;

        // root elements
//        Document xmlDoc = xmlDocBuilder.newDocument();
//        Element xmlRootElement = xmlDoc.createElement("Movies");
        xmlDoc.appendChild(xmlRootElement);

        // Movie Date element
        Element movieDateElement = xmlDoc.createElement("Date");
        xmlRootElement.appendChild(movieDateElement);
//        movieDateElement.setAttribute("innerHTML", movieDate);
        movieDateElement.setTextContent(movieDate);

        for (WebElement movieElement : allMovies)
        {

            movieName = movieElement.findElement(By.tagName("a")).getAttribute("innerHTML");
            movieInfoAndShowtimes = movieElement.getText();
            fileAndConsoleOutput(carmike_theaters_bw, "--------------------");
            fileAndConsoleOutput(carmike_theaters_bw, movieDate);
            fileAndConsoleOutput(carmike_theaters_bw, movieInfoAndShowtimes);
//            System.out.println(movieElement.getAttribute("id"));
//            System.out.println(movieName);
            //div[@class='hidden-xs col-sm-2']//img[contains(@alt,'Inferno Poster')]
            imageXpath = "//li[contains(@class,'gridCol-s-12 gridCol-m-6 gridCol-l-6 filmItem active')]//div[@class='filmItemImageContain']//img";
//            imageURL = movieElement.findElement(By.xpath(".//img"));
//            imageURL = movieElement.findElement(By.xpath("//img[@class='img-responsive lazyloaded' and contains(@alt,'Poster')]"));
            if (movieName.contains("'"))
            {
                continue;
            }
            imageURL = movieElement.findElement(By.xpath(imageXpath));
            movieImagePath = imageURL.getAttribute("src");
            fileAndConsoleOutput(carmike_theaters_bw, movieImagePath);

            // Movie Name element
            Element movieNameElement = xmlDoc.createElement("Name");
            movieDateElement.appendChild(movieNameElement);
//            movieNameElement.setAttribute("innerHTML", movieName);
            movieNameElement.setTextContent(movieName);

            // Movie Info and Showtimes Element
            Element movieInfoAndShowtimesElement = xmlDoc.createElement("InfoAndShowtimes");
            movieNameElement.appendChild(movieInfoAndShowtimesElement);
//            movieInfoAndShowtimesElement.setAttribute("innerHTML", movieInfoAndShowtimes);
            movieInfoAndShowtimesElement.setTextContent(movieInfoAndShowtimes);

            // Image Path Element
            Element imagePathElement = xmlDoc.createElement("ImagePath");
            movieNameElement.appendChild(imagePathElement);
//            imagePathElement.setAttribute("innerHTML", movieImagePath);
            imagePathElement.setTextContent(movieImagePath);

//            Thread.sleep(5000);
        }

//        // write the content into xml file
//        TransformerFactory movieTransformerFactory = TransformerFactory.newInstance();
//        Transformer movieTransformer = movieTransformerFactory.newTransformer();
//        DOMSource movieSource = new DOMSource(xmlDoc);
////        StreamResult result = new StreamResult(new File("C:\\test\\file.xml"));
//        StringWriter movieWriter = new StringWriter();
//        StreamResult movieResult = new StreamResult(movieWriter);
//
//        // Output to console for testing
//        // StreamResult result = new StreamResult(System.out);
//
//        movieTransformer.transform(movieSource, movieResult);
//        String strResult = movieWriter.toString();
//
//        xml_movies_bw.write(strResult);

    }

    public void getAllAvailableDaysMovies() throws InterruptedException, IOException, TransformerException, JSONException {
        List<WebElement> movies = null;
        List<WebElement> dayButtons = driver.findElements(By.xpath("//div[@class='dateList']//label[not(contains(@class,'disabled'))]"));
        WebElement currentDateAnchorElement = driver.findElement(By.xpath("//div[@class='dateList']//label[not(contains(@class,'disabled'))][1]"));
        WebElement moviesImageURL = null;
        String movieDate = driver.findElement(By.xpath("//div[@class='dateList']//label[not(contains(@class,'disabled'))][1]//span[1]")).getAttribute("innerHTML");
        String movieName = null;
        String movieInfoAndShowtimes = null;
        WebElement imageURL = null;
        String dateXpath = null;
        String imageXpath = null;
        String movieImagePath = null;

        // root elements
//        Document xmlDoc = xmlDocBuilder.newDocument();
//        Element xmlRootElement = xmlDoc.createElement("Movies");
//        xmlDoc.appendChild(xmlRootElement);

        for (int index = 1; index < 5; index++) {
            if (dayButtons.get(index).isEnabled())
            {
            dayButtons.get(index).click();
            Thread.sleep(5000);
            dateXpath = "//div[@class='dateList']//label[not(contains(@class,'disabled'))][" + (index + 1) + "]//span[1]";
            currentDateAnchorElement = driver.findElement(By.xpath(dateXpath));
            movieDate = currentDateAnchorElement.getAttribute("innerHTML");
            movies = driver.findElements(By.xpath("//li[contains(@class,'gridCol-s-12 gridCol-m-6 gridCol-l-6 filmItem active')]//div[@class='filmItemContent']"));

            // Movie Date element
            Element movieDateElement = xmlDoc.createElement("Date");
            xmlRootElement.appendChild(movieDateElement);
//            movieDateElement.setAttribute("innerHTML", movieDate);
            movieDateElement.setTextContent(movieDate);


            for (WebElement movieElement : movies) {
                movieName = movieElement.findElement(By.tagName("a")).getAttribute("innerHTML");
                movieInfoAndShowtimes = movieElement.getText();
                fileAndConsoleOutput(carmike_theaters_bw, "--------------------");
                fileAndConsoleOutput(carmike_theaters_bw, movieDate);
                fileAndConsoleOutput(carmike_theaters_bw, movieInfoAndShowtimes);
//            System.out.println(movieElement.getAttribute("id"));
//            System.out.println(movieName);
                //div[@class='hidden-xs col-sm-2']//img[contains(@alt,'Inferno Poster')]
                imageXpath = "//li[contains(@class,'gridCol-s-12 gridCol-m-6 gridCol-l-6 filmItem active')]//div[@class='filmItemImageContain']//img";
//            imageURL = movieElement.findElement(By.xpath(".//img"));
//            imageURL = movieElement.findElement(By.xpath("//img[@class='img-responsive lazyloaded' and contains(@alt,'Poster')]"));
                if (movieName.contains("'")) {
                    continue;
                }
                imageURL = movieElement.findElement(By.xpath(imageXpath));
                movieImagePath = imageURL.getAttribute("src");
                fileAndConsoleOutput(carmike_theaters_bw, movieImagePath);

                // Movie Name element
                Element movieNameElement = xmlDoc.createElement("Name");
                movieDateElement.appendChild(movieNameElement);
//                movieNameElement.setAttribute("innerHTML", movieName);
                movieNameElement.setTextContent(movieName);

                // Movie Info and Showtimes Element
                Element movieInfoAndShowtimesElement = xmlDoc.createElement("InfoAndShowtimes");
                movieNameElement.appendChild(movieInfoAndShowtimesElement);
//                movieInfoAndShowtimesElement.setAttribute("innerHTML", movieInfoAndShowtimes);
                movieInfoAndShowtimesElement.setTextContent(movieInfoAndShowtimes);

                // Image Path Element
                Element imagePathElement = xmlDoc.createElement("ImagePath");
                movieNameElement.appendChild(imagePathElement);
//                imagePathElement.setAttribute("innerHTML", movieImagePath);
                imagePathElement.setTextContent(movieImagePath);

            }
        }
        }

        // write the content into xml file
        TransformerFactory movieTransformerFactory = TransformerFactory.newInstance();
        Transformer movieTransformer = movieTransformerFactory.newTransformer();
        DOMSource movieSource = new DOMSource(xmlDoc);
//        StreamResult result = new StreamResult(new File("C:\\test\\file.xml"));
        StringWriter movieWriter = new StringWriter();
        StreamResult movieResult = new StreamResult(movieWriter);

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        movieTransformer.transform(movieSource, movieResult);
        String strResult = movieWriter.toString();

        carmike_xml_movies_bw.write(strResult);

        // JSON output solution begins here
        int PRETTY_PRINT_INDENT_FACTOR = 4;
        String TEST_XML_STRING = strResult;
        //"<?xml version=\"1.0\" ?><test attrib=\"moretest\">Turn this to JSON</test>";
        //"<?xml version=\"1.0\" encoding=\"UTF-8\"?><company><Staff id=\"1\"><firstname>Richard</firstname><lastname>Harkins </lastname><nickname>Rich</nickname><firstname>Kim</firstname><lastname>Harkins </lastname><nickname>Kimberly</nickname><firstname>Mitchell</firstname><lastname>Harkins</lastname><nickname>Mitch</nickname></Staff></company>";
        fileAndConsoleOutput(carmike_json_theaters_bw, "Outside xml try block");
        JSONObject xmlJSONObj = org.json.XML.toJSONObject(TEST_XML_STRING);
        JSONArray xmlJSONArray = xmlJSONObj.names();
        int xmlJSONArrayLength = xmlJSONArray.length();
        String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        try {
//            BufferedWriter json_bw = createJSONOutputFile();
            fileAndConsoleOutput(carmike_json_theaters_bw, "Inside xml try block");
            fileAndConsoleOutput(carmike_json_theaters_bw, jsonPrettyPrintString);
        } catch (Exception je) {
            fileAndConsoleOutput(carmike_json_theaters_bw, je.toString());
        }

        JSONObject output;
        try
        {
            output = new JSONObject(xmlJSONObj);
            JSONArray docs = output.getJSONArray("Movies");
//            File csvFile = new File("C:/test/Movies_Output.csv");
            String csv = CDL.toString(docs);
            carmike_csv_movies_bw.write(csv);
            carmike_csv_movies_bw.close();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
        getAllMovies();
        getAllAvailableDaysMovies();

        // Find the submit button for the Search dialog at the top of the screen
//        WebElement searchDialogSubmitButton = driver.findElement(By.cssSelector("#main_theatres_search>fieldset>input[src]"));
//        searchDialogSubmitButton.click();

        fileAndConsoleOutput(carmike_theaters_bw, "Accessing BufferedWriter object in WebPageTest");
        fileAndConsoleOutput(carmike_theaters_bw, "Accessing BufferedWriter object in WebPageTest - Line 2");
        fileAndConsoleOutput(carmike_theaters_bw, "Accessing BufferedWriter object in WebPageTest - Line 3");

//        Workbook myExcelWorkbook = Workbook.getWorkbook(new File("C:/test/ROLL 2016 - Contact Information and Initial Deposit.xls"));
//        WritableWorkbook myWritableExcelWorkbook = Workbook.createWorkbook(new File("C:/test/ROLL 2016 - Contact Information and Initial Deposit.xls"), myExcelWorkbook);
//        int numSheets = myExcelWorkbook.getNumberOfSheets();
        File workbookFile = new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls");
        String workBookFilePath = workbookFile.getAbsolutePath();
        fileAndConsoleOutput(carmike_theaters_bw, workBookFilePath);
//        Workbook theatersExcelWorkbook = Workbook.getWorkbook(new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls"));
//        WritableWorkbook theatersWritableExcelWorkbook = Workbook.createWorkbook(new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls"), theatersExcelWorkbook);

        int numSheets = theatersWritableExcelWorkbook.getNumberOfSheets();
        fileAndConsoleOutput(carmike_theaters_bw, "Number of sheets = " + numSheets);
//        Sheet mySheet = myExcelWorkbook.getSheet(0);
        Sheet mySheet = theatersWritableExcelWorkbook.getSheet(0);
        Cell myCell = mySheet.getCell(0, 0);
        String myContents = myCell.getContents();
        System.out.print(myContents);
        theatersWritableExcelWorkbook.write();
        theatersWritableExcelWorkbook.close();
        carmike_theaters_bw.close();
        carmike_json_theaters_bw.close();
        carmike_xml_movies_bw.close();
        carmike_csv_movies_bw.close();

    }
}
