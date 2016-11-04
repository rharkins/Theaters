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
import java.io.File;
import java.io.IOException;

/**
 * Created by Richard Harkins on 7/19/2016.
 */
public class Utils extends SeleniumWebdriverBaseClass {

    public Utils() throws IOException, BiffException {
    }

    public void createXMLFile() throws ParserConfigurationException, TransformerException, IOException {
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
        StreamResult result = new StreamResult(new File("C:\\test\\file.xml"));

        // Output to console for testing
        // StreamResult result = new StreamResult(System.out);

        transformer.transform(source, result);

        int PRETTY_PRINT_INDENT_FACTOR = 4;
        String TEST_XML_STRING =
                //"<?xml version=\"1.0\" ?><test attrib=\"moretest\">Turn this to JSON</test>";
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><company><Staff id=\"1\"><firstname>Richard</firstname><lastname>Harkins </lastname><nickname>Rich</nickname><firstname>Kim</firstname><lastname>Harkins </lastname><nickname>Kimberly</nickname><firstname>Mitchell</firstname><lastname>Harkins</lastname><nickname>Mitch</nickname></Staff></company>";

            try {
                JSONObject xmlJSONObj = org.json.XML.toJSONObject(TEST_XML_STRING);
                String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
                System.out.println(jsonPrettyPrintString);
            } catch (JSONException je) {
                System.out.println(je.toString());
            }
        }


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
    public void Test_Launcher() throws IOException, WriteException, InterruptedException, BiffException, TransformerException, ParserConfigurationException {
        createXMLFile();
        CinemarkPage mySeleniumTest = new CinemarkPage();
        mySeleniumTest.WebpageTest();
    }

}
