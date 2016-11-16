package com.stgconsulting;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Richard Harkins on 7/19/2016.
 */
public class CinemarkPage extends SeleniumWebdriverBaseClass {
    DocumentBuilderFactory xmlDocFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder xmlDocBuilder = xmlDocFactory.newDocumentBuilder();
    Document xmlDoc = xmlDocBuilder.newDocument();
    Element xmlRootElement = xmlDoc.createElement("Movies");
    private BufferedWriter cinemark_theaters_bw = createAnyOutputFile("C:/test/cinemark_theaters.txt");
    private BufferedWriter cinemark_json_theaters_bw = createAnyOutputFile("C:/test/cinemark_json_theaters.txt");
    private BufferedWriter cinemark_xml_movies_bw = createAnyOutputFile("C:/test/cinemark_movies.xml");
    private BufferedWriter cinemark_csv_movies_bw = createAnyOutputFile("C:/test/cinemark_Movies_Output.csv");

    //WebDriver driver = new FirefoxDriver();
    //driver.navigate().to("http://www.google.com");

//    private WebDriver driver;
//    public BufferedWriter webdriver_theaters_bw = TheatersOutput();

    public CinemarkPage() throws IOException, BiffException, ParserConfigurationException {
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
    private List<WebElement> cinemarkSearchOld(Boolean titleSearchDropdown, WebElement searchControl, String searchString) throws InterruptedException, IOException {
        fileAndConsoleOutput(cinemark_theaters_bw, "In cinemarkSearch");
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
                fileAndConsoleOutput(cinemark_theaters_bw, text);
                fileAndConsoleOutput(cinemark_theaters_bw, "--------------------");
//                System.out.println(url);
            }
            Assert.assertEquals(searchString, searchString);
            return theatersLinks;
        }
        return null;
    }

    @Test(enabled = false)
    private List<WebElement> cinemarkSearch(WebElement searchTextBoxControl, String searchString, WebElement searchButton) throws InterruptedException, IOException {
        fileAndConsoleOutput(cinemark_theaters_bw, "In cinemarkSearch");
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
            fileAndConsoleOutput(cinemark_theaters_bw, text);
            fileAndConsoleOutput(cinemark_theaters_bw, "--------------------");
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
            fileAndConsoleOutput(cinemark_theaters_bw, theaterListElement.getText());
            if (theaterListElement.getText().equals(theater))
            {
                theaterFound = true;
                fileAndConsoleOutput(cinemark_theaters_bw, "Found it");
//                System.out.println(theaterListElement.getText());
//                System.out.println(theater);
                theaterListElement.click();
                break;
            }
        }
        if (theaterFound == false) {
            fileAndConsoleOutput(cinemark_theaters_bw, "Theater " + theater + " not found");
        }
    }

    public void getAllMovies() throws InterruptedException, IOException, TransformerException {
        List<WebElement> allMovies = driver.findElements(By.xpath("//div[@class='col-xs-12 col-sm-10']"));
        List<WebElement> dayButtons = driver.findElements(By.xpath("//li[@data-datevalue]"));
        WebElement currentDateAnchorElement = driver.findElement(By.xpath(".//li//a[@id=0]/.."));
        String movieDate = currentDateAnchorElement.getAttribute("data-datevalue");
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

            movieName = movieElement.findElement(By.tagName("h2")).getAttribute("innerHTML");
            movieInfoAndShowtimes = movieElement.getText();
            fileAndConsoleOutput(cinemark_theaters_bw, "--------------------");
            fileAndConsoleOutput(cinemark_theaters_bw, movieDate);
            fileAndConsoleOutput(cinemark_theaters_bw, movieInfoAndShowtimes);
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
            movieImagePath = imageURL.getAttribute("data-srcset");
            fileAndConsoleOutput(cinemark_theaters_bw, movieImagePath);

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

    public void getNextSevenDaysMovies() throws InterruptedException, IOException, TransformerException, JSONException {
        List<WebElement> movies = null;
        List<WebElement> dayButtons = driver.findElements(By.xpath("//ul[@id='showdatesMore']//li[@role='presentation']"));
        WebElement currentDateAnchorElement = driver.findElement(By.xpath(".//li//a[@id=0]/.."));
        WebElement moviesImageURL = null;
        String movieDate = currentDateAnchorElement.getAttribute("data-datevalue");
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

        for (int index = 1; index < 7; index++)
        {
            dayButtons.get(index).click();
            Thread.sleep(5000);
            dateXpath = ".//li//a[@id=" + index + "]/..";
            currentDateAnchorElement = driver.findElement(By.xpath(dateXpath));
            movieDate = currentDateAnchorElement.getAttribute("data-datevalue");
            movies = driver.findElements(By.xpath("//div[@class='col-xs-12 col-sm-10']"));

            // Movie Date element
            Element movieDateElement = xmlDoc.createElement("Date");
            xmlRootElement.appendChild(movieDateElement);
//            movieDateElement.setAttribute("innerHTML", movieDate);
            movieDateElement.setTextContent(movieDate);


            for (WebElement movieElement : movies)
            {
                movieName = movieElement.findElement(By.tagName("h2")).getAttribute("innerHTML");
                movieInfoAndShowtimes = movieElement.getText();
                fileAndConsoleOutput(cinemark_theaters_bw, "--------------------");
                fileAndConsoleOutput(cinemark_theaters_bw, movieDate);
//                System.out.println(movieElement.getAttribute("id"));
                fileAndConsoleOutput(cinemark_theaters_bw, movieInfoAndShowtimes);
                imageXpath = "//div[@class='hidden-xs col-sm-2']//img[contains(@alt,'" + movieName + " Poster')]";
                if (movieName.contains("'"))
                {
                    continue;
                }
                imageURL = movieElement.findElement(By.xpath(imageXpath));
                movieImagePath = imageURL.getAttribute("data-srcset");
                fileAndConsoleOutput(cinemark_theaters_bw, movieImagePath);

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

        cinemark_xml_movies_bw.write(strResult);

        // JSON output solution begins here
        int PRETTY_PRINT_INDENT_FACTOR = 4;
        String TEST_XML_STRING = strResult;
        //"<?xml version=\"1.0\" ?><test attrib=\"moretest\">Turn this to JSON</test>";
        //"<?xml version=\"1.0\" encoding=\"UTF-8\"?><company><Staff id=\"1\"><firstname>Richard</firstname><lastname>Harkins </lastname><nickname>Rich</nickname><firstname>Kim</firstname><lastname>Harkins </lastname><nickname>Kimberly</nickname><firstname>Mitchell</firstname><lastname>Harkins</lastname><nickname>Mitch</nickname></Staff></company>";
        fileAndConsoleOutput(cinemark_json_theaters_bw, "Outside xml try block");
        JSONObject xmlJSONObj = org.json.XML.toJSONObject(TEST_XML_STRING);
        JSONArray xmlJSONArray = xmlJSONObj.names();
        int xmlJSONArrayLength = xmlJSONArray.length();
        String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        try {
//            BufferedWriter json_bw = createJSONOutputFile();
            fileAndConsoleOutput(cinemark_json_theaters_bw, "Inside xml try block");
            fileAndConsoleOutput(cinemark_json_theaters_bw, jsonPrettyPrintString);
        } catch (Exception je) {
            fileAndConsoleOutput(cinemark_json_theaters_bw, je.toString());
        }

        JSONObject output;
        try
        {
            output = new JSONObject(xmlJSONObj);
            JSONArray docs = output.getJSONArray("Movies");
//            File csvFile = new File("C:/test/Movies_Output.csv");
            String csv = CDL.toString(docs);
            cinemark_csv_movies_bw.write(csv);
            cinemark_csv_movies_bw.close();
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

    public void createXMLFile() throws ParserConfigurationException, TransformerException, IOException, JSONException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("company");
        doc.appendChild(rootElement);

        // staff elements
        Element staff = doc.createElement("Staff");
        rootElement.appendChild(staff);

        // set attribute to staff element
        Attr attr = doc.createAttribute("id");
        attr.setValue("1");
        staff.setAttributeNode(attr);

        // shorten way
        // staff.setAttribute("id", "1");

        // firstname elements
        Element firstname = doc.createElement("firstname");
        firstname.appendChild(doc.createTextNode("yong"));
        staff.appendChild(firstname);

        // lastname elements
        Element lastname = doc.createElement("lastname");
        lastname.appendChild(doc.createTextNode("mook kim"));
        staff.appendChild(lastname);

        // nickname elements
        Element nickname = doc.createElement("nickname");
        nickname.appendChild(doc.createTextNode("mkyong"));
        staff.appendChild(nickname);

        // salary elements
        Element salary = doc.createElement("salary");
        salary.appendChild(doc.createTextNode("100000"));
        staff.appendChild(salary);

        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
//        StreamResult result = new StreamResult(new File("C:\\test\\file.xml"));
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);
        String strResult = writer.toString();


        // JSON output solution begins here
        int PRETTY_PRINT_INDENT_FACTOR = 4;
        String TEST_XML_STRING = strResult;
        //"<?xml version=\"1.0\" ?><test attrib=\"moretest\">Turn this to JSON</test>";
        //"<?xml version=\"1.0\" encoding=\"UTF-8\"?><company><Staff id=\"1\"><firstname>Richard</firstname><lastname>Harkins </lastname><nickname>Rich</nickname><firstname>Kim</firstname><lastname>Harkins </lastname><nickname>Kimberly</nickname><firstname>Mitchell</firstname><lastname>Harkins</lastname><nickname>Mitch</nickname></Staff></company>";
        fileAndConsoleOutput(cinemark_json_theaters_bw, "Outside xml try block");
        try {
//            BufferedWriter json_bw = createJSONOutputFile();
            JSONObject xmlJSONObj = org.json.XML.toJSONObject(TEST_XML_STRING);
            String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
            fileAndConsoleOutput(cinemark_json_theaters_bw, "Inside xml try block");
            fileAndConsoleOutput(cinemark_json_theaters_bw, jsonPrettyPrintString);
        } catch (JSONException je) {
            fileAndConsoleOutput(cinemark_json_theaters_bw, je.toString());
        }
    }
    // JSON output solution ends here

//        JsonObject obj = new JsonParser().parse(jsonString).getAsJsonObject();
//        Gson gsonbject = new Gson();
//        String xmlToJSON = gsonbject.toJson("<?xml version=\"1.0\" encoding=\"UTF-8\"?><company><Staff id=\"1\"><firstname>yong</firstname><lastname>mook kim</lastname><nickname>mkyong</nickname><salary>100000</salary></Staff></company>");

//        JsonFactory factory = new JsonFactory();
//
//        JsonGenerator generator = factory.createGenerator(
//                new File("C:\\test\\json_test.json"), JsonEncoding.UTF8);
//
//        generator.writeStartObject();
//        generator.writeStringField("brand", "Mercedes");
//        generator.writeNumberField("doors", 5);
//        generator.writeEndObject();
//
//        generator.close();
//
//        XmlMapper xmlMapper = new XmlMapper();
//        String xml = xmlMapper.readValue("<Simple><x>1</x><y>2</y></Simple>", );
//
//        String xmlToJSON = gsonbject.toJson(xml);
//        System.out.println(xmlToJSON);

    //    }

    @Test
    public void WebpageTest() throws IOException, BiffException, WriteException, InterruptedException, TransformerException, JSONException {
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

        fileAndConsoleOutput(cinemark_theaters_bw, "Accessing BufferedWriter object in WebPageTest");
        fileAndConsoleOutput(cinemark_theaters_bw, "Accessing BufferedWriter object in WebPageTest - Line 2");
        fileAndConsoleOutput(cinemark_theaters_bw, "Accessing BufferedWriter object in WebPageTest - Line 3");

//        Workbook myExcelWorkbook = Workbook.getWorkbook(new File("C:/test/ROLL 2016 - Contact Information and Initial Deposit.xls"));
//        WritableWorkbook myWritableExcelWorkbook = Workbook.createWorkbook(new File("C:/test/ROLL 2016 - Contact Information and Initial Deposit.xls"), myExcelWorkbook);
//        int numSheets = myExcelWorkbook.getNumberOfSheets();
        File workbookFile = new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls");
        String workBookFilePath = workbookFile.getAbsolutePath();
        fileAndConsoleOutput(cinemark_theaters_bw, workBookFilePath);
//        Workbook theatersExcelWorkbook = Workbook.getWorkbook(new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls"));
//        WritableWorkbook theatersWritableExcelWorkbook = Workbook.createWorkbook(new File("C:\\test\\ROLL 2016 - Contact Information and Initial Deposit.xls"), theatersExcelWorkbook);

        int numSheets = theatersWritableExcelWorkbook.getNumberOfSheets();
        fileAndConsoleOutput(cinemark_theaters_bw, "Number of sheets = " + numSheets);
//        Sheet mySheet = myExcelWorkbook.getSheet(0);
        Sheet mySheet = theatersWritableExcelWorkbook.getSheet(0);
        Cell myCell = mySheet.getCell(0, 0);
        String myContents = myCell.getContents();
        System.out.print(myContents);
        theatersWritableExcelWorkbook.write();
        theatersWritableExcelWorkbook.close();
        cinemark_theaters_bw.close();
        cinemark_json_theaters_bw.close();
        cinemark_xml_movies_bw.close();
        cinemark_csv_movies_bw.close();

    }
}