package org.example;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.example.ReadXml.*;


public class App
{
    public static void main(String[] args) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new RuntimeException(e);
        }

        XMLHandler handler = new XMLHandler();

        try {
            parser.parse(new File("AS_ADDR_OBJ.xml"), handler);
            parser.parse(new File("AS_ADM_HIERARCHY.XML"), handler);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }


        ArrayList<String> list = new ArrayList<>();
        System.out.println("Задание № 1:");
        list.add("1422396");
        list.add("1450759");
        list.add("1449192");
        list.add("1451562");
        System.out.println(getAddress("2010-01-01", list));
        System.out.println("Задание № 2:");
        System.out.println(getAddressChainsById());
    }
}