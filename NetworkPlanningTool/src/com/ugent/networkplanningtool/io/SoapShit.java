package com.ugent.networkplanningtool.io;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.Transport;
import org.xmlpull.v1.XmlPullParserException;

import com.ugent.networkplanningtool.io.marshal.MarshalDouble;
import com.ugent.networkplanningtool.model.FloorPlanModel;

import android.os.AsyncTask;
import android.util.Log;

public class SoapShit extends AsyncTask<String, Void, SoapObject>{
	private static final String METHOD_NAME = "optimize";
	private static final String SOAP_ACTION = "http://wicaweb2.intec.ugent.be:80/DeusService/Deus/optimize";
	private static final String NAMESPACE = "http://web.deus.wica.intec.ugent.be/";
	private static final String URL = "http://wicaweb2.intec.ugent.be/DeusService/Deus?wsdl";
	//you can get these values from the wsdl file
	
	public static SoapObject soap() throws IOException, XmlPullParserException {		
		String s = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><!--<plan xmlns=\"http://www.wica.intec.ugent.be\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.wica.intec.ugent.be plan.xsd\">--><plan xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"plan.xsd\"><level name=\"\" number=\"0\"><extraWalls><wall x1=\"400\" y1=\"200\" x2=\"400\" y2=\"800\" type=\"wall\" thickness=\"10\"><material name=\"Layered Drywall\"/></wall><wall x1=\"1100\" y1=\"200\" x2=\"1700\" y2=\"200\" type=\"wall\" thickness=\"10\"><material name=\"Layered Drywall\"/></wall><wall x1=\"1700\" y1=\"500\" x2=\"1700\" y2=\"800\" type=\"wall\" thickness=\"10\"><material name=\"Layered Drywall\"/></wall><wall x1=\"1700\" y1=\"200\" x2=\"1700\" y2=\"500\" type=\"wall\" thickness=\"10\"><material name=\"Layered Drywall\"/></wall><wall x1=\"1100\" y1=\"500\" x2=\"1350\" y2=\"500\" type=\"wall\" thickness=\"30\"><material name=\"Concrete\"/></wall><wall x1=\"1350\" y1=\"500\" x2=\"1450\" y2=\"500\" type=\"door\" thickness=\"10\"><material name=\"Wood\"/></wall><wall x1=\"1450\" y1=\"500\" x2=\"1700\" y2=\"500\" type=\"wall\" thickness=\"30\"><material name=\"Concrete\"/></wall><wall x1=\"1100\" y1=\"200\" x2=\"1100\" y2=\"300\" type=\"wall\" thickness=\"10\"><material name=\"Brick\"/></wall><wall x1=\"1100\" y1=\"400\" x2=\"1100\" y2=\"300\" type=\"door\" thickness=\"30\"><material name=\"Glass\"/></wall><wall x1=\"1100\" y1=\"500\" x2=\"1100\" y2=\"400\" type=\"wall\" thickness=\"10\"><material name=\"Brick\"/></wall><wall x1=\"400\" y1=\"800\" x2=\"1350\" y2=\"800\" type=\"wall\" thickness=\"10\"><material name=\"Layered Drywall\"/></wall><wall x1=\"1350\" y1=\"800\" x2=\"1450\" y2=\"800\" type=\"window\" thickness=\"10\"><material name=\"Glass\"/></wall><wall x1=\"1450\" y1=\"800\" x2=\"1700\" y2=\"800\" type=\"wall\" thickness=\"10\"><material name=\"Layered Drywall\"/></wall><wall x1=\"400\" y1=\"200\" x2=\"600\" y2=\"200\" type=\"wall\" thickness=\"10\"><material name=\"Layered Drywall\"/></wall><wall x1=\"600\" y1=\"200\" x2=\"700\" y2=\"200\" type=\"window\" thickness=\"30\"><material name=\"Glass\"/></wall><wall x1=\"700\" y1=\"200\" x2=\"1100\" y2=\"200\" type=\"wall\" thickness=\"10\"><material name=\"Layered Drywall\"/></wall></extraWalls><dataconnpoint x=\"1573\" y=\"495\"/><powerconnpoint x=\"1268\" y=\"795\"/><accesspoint x=\"1633\" y=\"263\" height=\"250\" name=\"\" level=\"0\"><radio type=\"WiFi\" model=\"DLink\" frequency=\"2437\" frequencyband=\"2400\" gain=\"2\" power=\"14\" network=\"Network A\"/></accesspoint><accesspoint x=\"470\" y=\"728\" height=\"100\" name=\"\" level=\"0\"><radio type=\"WiFi\" model=\"Custom\" frequency=\"2437\" frequencyband=\"2400\" gain=\"2\" power=\"13\" network=\"Network B\"/></accesspoint><accesspoint x=\"513\" y=\"265\" height=\"300\" name=\"\" level=\"0\"><radio type=\"LTE Femtocell\" model=\"Custom\" frequency=\"2600\" frequencyband=\"2600\" gain=\"2\" power=\"15\" network=\"Network C\"/></accesspoint><activity type=\"HD video\" x=\"1633\" y=\"418\"/><activity type=\"No coverage\" x=\"668\" y=\"670\"/></level></plan>";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); //set up request
		request.addProperty("client_version", "1.8.0.a");
		request.addProperty("xml", s);
		request.addProperty("model", "sidp");
		request.addProperty("grid_size", 200.0);
		request.addProperty("default_type", "Surfing");
		request.addProperty("receiver_name", "3G phone");
		request.addProperty("receiver_gain", 0.0);
		request.addProperty("receiver_height", 100.0);
		request.addProperty("interference", 0.0);
		request.addProperty("shadow_margin", 7.0);
		request.addProperty("fade_margin", 5.0);
		request.addProperty("ap_type", "WiFi");
		request.addProperty("ap_frequency", 2400);
		request.addProperty("ap_power", 14);
		request.addProperty("ap_gain", 2);
		request.addProperty("ap_height", 250);
		request.addProperty("function", 1);
		request.addProperty("frequency_planning", true);
		
		// request.addProperty("version", ""+i); //variable name, value. I got the variable name, from the wsdl file!
		
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //put all required data into a soap envelope
		
		envelope.setOutputSoapObject(request);  //prepare request
		MarshalDouble md = new MarshalDouble();
        md.register(envelope);
		HttpTransportSE httpTransport = new HttpTransportSE(URL);  
		httpTransport.debug = true;  //this is optional, use it if you don't want to use a packet sniffer to check what the sent message was (httpTransport.requestDump)
		httpTransport.call(SOAP_ACTION, envelope); //send request
		SoapObject result=(SoapObject)envelope.getResponse(); //get response
		return result;
		
	}
	@Override
	protected SoapObject doInBackground(String... params) {
		SoapObject so = null;
		try {
			so = soap();
			Log.d("DEBUG","output: "+so);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return so;
	}
	
	
	
	
	
	
	
	
}
