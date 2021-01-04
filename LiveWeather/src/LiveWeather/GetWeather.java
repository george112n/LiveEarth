package LiveWeather;
import java.io.IOException;
import java.io.PrintStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import OpenWeatherMap.Current;

public class GetWeather extends DefaultHandler
{
	private static Current current;

	Location pLocation;
	long[] coord;
	
	String[] DataReturned;
	
	boolean testValueVaryWeather = true;
	
	public static Current entry(String URL)
	{
		current = new Current();
		try
		{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setErrorHandler(new MyErrorHandler(System.err));

			xmlReader.setContentHandler((ContentHandler) new GetWeather());
			xmlReader.parse(URL);
		}
		catch (SAXException e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.GetWeather - SAXException: Error reading in api xml weather file");			
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.GetWeather - IOException: Error reading in api xml weather file");			
			e.printStackTrace();
		}
		catch (Exception e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.GetWeather - Exception: Error reading in api xml weather file");
			e.printStackTrace();
		}
		return current;
	}
	
	public static void main(String[] args)
	{		
		try
		{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setNamespaceAware(true);
			SAXParser saxParser = spf.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setErrorHandler(new MyErrorHandler(System.err));

			xmlReader.setContentHandler((ContentHandler) new GetWeather());
			xmlReader.parse("http://api.openweathermap.org/data/2.5/weather?lat=51.441592&lon=0.367756&appid=ac594611afb90c97e2382439671e9112&mode=xml");
		}
		catch (SAXException e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.GetWeather - SAXException: Error reading in api xml weather file");			
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.GetWeather - IOException: Error reading in api xml weather file");			
			e.printStackTrace();
		}
		catch (Exception e)
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LiveWeather.GetWeather - Exception: Error reading in api xml weather file");
			e.printStackTrace();
		}
	}
	
	public void startDocument() throws SAXException
	{
		current = new Current();
	}

	public void startElement(String namespaceURI, String localName, String qName,  Attributes atts) throws SAXException
	{
		int i;
		int iAttributes = atts.getLength();
	//	System.out.println("Section: "+localName);
	//	System.out.println("Attributes: "+iAttributes);
		
		//Checks the element name for precipitation
		if (localName.equals("precipitation"))
		{
			//Goes through the attributes of precipitation until it finds mode
			for (i = 0 ; i < iAttributes ; i++)
			{
				//	System.out.print("Member "+i+": " + atts.getLocalName(i) +" = ");
				//	System.out.println(atts.getValue(i));
				if (atts.getLocalName(i).equals("mode"))
				{
					current.prec.setMode(atts.getValue(i));
				}
			}
		}
		//Checks the element name for weather
		else if (localName.equals("weather"))
		{
			//Goes through the attributes of weather until it finds number
			for (i = 0 ; i < iAttributes ; i++)
			{
				//	System.out.print("Member "+i+": " + atts.getLocalName(i) +" = ");
				//	System.out.println(atts.getValue(i));
				if (atts.getLocalName(i).equals("number"))
				{
					current.weather.setNumber(Integer.parseInt(atts.getValue(i)));
				}
			}
		}
	}

	public void endDocument() throws SAXException
	{
		
	}
}

class MyErrorHandler implements ErrorHandler
{
	private PrintStream out;

	MyErrorHandler(PrintStream out)
	{
		this.out = out;
	}

	private String getParseExceptionInfo(SAXParseException spe)
	{
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
