package com.bteuk.liveearth.weather;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;

public class WeatherGetter extends DefaultHandler {
    private static Current current;

    Location pLocation;
    long[] coord;

    String[] DataReturned;

    boolean testValueVaryWeather = true;

    public static Current entry(String URL) {
        current = new Current();
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setErrorHandler(new WeatherErrorHandler(System.err));

            xmlReader.setContentHandler(new WeatherGetter());
            xmlReader.parse(URL);
        } catch (SAXException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.GetWeather - SAXException: Error reading in api xml weather file");
            e.printStackTrace();
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.GetWeather - IOException: Error reading in api xml weather file");
            e.printStackTrace();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.GetWeather - Exception: Error reading in api xml weather file");
            e.printStackTrace();
        }
        return current;
    }

    public void startDocument() throws SAXException {
        current = new Current();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        int i;
        int iAttributes = atts.getLength();
        //Checks the element name for precipitation
        switch (localName) {
            case "precipitation":
                //Goes through the attributes of precipitation until it finds mode
                for (i = 0; i < iAttributes; i++) {
                    if (atts.getLocalName(i).equals("mode")) {
                        current.prec.setMode(atts.getValue(i));
                    }
                }
                break;
            //Checks the element name for weather
            case "weather":
                //Goes through the attributes of weather until it finds number
                for (i = 0; i < iAttributes; i++) {
                    if (atts.getLocalName(i).equals("number")) {
                        current.setNumber(Integer.parseInt(atts.getValue(i)));
                    }
                }
                break;
            //Checks the element name for sun
            case "sun":
                //Goes through the attributes of sun until it finds rise and set
                for (i = 0; i < iAttributes; i++) {
                    if (atts.getLocalName(i).equals("rise")) {
                        current.setSunrise((LocalDateTime.parse(atts.getValue(i))));
                    }
                    if (atts.getLocalName(i).equals("set")) {
                        current.setSunset((LocalDateTime.parse(atts.getValue(i))));
                    }
                }
                break;
            //Checks the element name for city
            case "city":
                //Goes through the attributes of city until it finds name
                for (i = 0; i < iAttributes; i++) {
                    if (atts.getLocalName(i).equals("name")) {
                        current.setCityName(atts.getValue(i));
                    }
                }
                break;
        }
    }
}

class WeatherErrorHandler implements ErrorHandler {
    private final PrintStream out;

    WeatherErrorHandler(PrintStream out) {
        this.out = out;
    }

    private String getParseExceptionInfo(SAXParseException spe) {
        String systemId = spe.getSystemId();

        if (systemId == null) {
            systemId = "null";
        }

        String info = "URI=" + systemId + " Line="
                + spe.getLineNumber() + ": " + spe.getMessage();

        return info;
    }

    public void warning(SAXParseException spe) throws SAXException {
        out.println("Warning: " + getParseExceptionInfo(spe));
    }

    public void error(SAXParseException spe) throws SAXException {
        String message = "Error: " + getParseExceptionInfo(spe);
        throw new SAXException(message);
    }

    public void fatalError(SAXParseException spe) throws SAXException {
        String message = "Fatal Error: " + getParseExceptionInfo(spe);
        throw new SAXException(message);
    }
}
