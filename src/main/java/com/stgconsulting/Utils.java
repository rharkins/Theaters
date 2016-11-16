package com.stgconsulting;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.Test;
import org.w3c.dom.Attr;
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

/**
 * Created by Richard Harkins on 7/19/2016.
 */
public class Utils extends SeleniumWebdriverBaseClass {

    public Utils() throws IOException, BiffException {
    }

    @Test
    public void Test_Launcher() throws IOException, WriteException, InterruptedException, BiffException, TransformerException, ParserConfigurationException, JSONException {
        Utils myUtils = new Utils();
        CarmikePage myCarmikeTest = new CarmikePage();
        myCarmikeTest.WebpageTest();
        CinemarkPage mySeleniumTest = new CinemarkPage();
        mySeleniumTest.createXMLFile();
        mySeleniumTest.WebpageTest();
//        cleanup();

    }

}
