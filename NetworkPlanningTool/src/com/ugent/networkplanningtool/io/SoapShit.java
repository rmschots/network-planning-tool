package com.ugent.networkplanningtool.io;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.Transport;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Log;

public class SoapShit extends AsyncTask<String, Void, SoapPrimitive>{
	private static final String METHOD_NAME = "verifyClient";
	private static final String SOAP_ACTION = "http://wicaweb2.intec.ugent.be:80/DeusService/Deus/verifyClient";
	private static final String NAMESPACE = "http://web.deus.wica.intec.ugent.be/";
	private static final String URL = "http://wicaweb2.intec.ugent.be/DeusService/Deus?wsdl";
	//you can get these values from the wsdl file
	
	public static SoapPrimitive soap(int i) throws IOException, XmlPullParserException {		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); //set up request
		
		request.addProperty("version", ""+i); //variable name, value. I got the variable name, from the wsdl file!
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //put all required data into a soap envelope
		
		envelope.setOutputSoapObject(request);  //prepare request
		HttpTransportSE httpTransport = new HttpTransportSE(URL);  
		httpTransport.debug = true;  //this is optional, use it if you don't want to use a packet sniffer to check what the sent message was (httpTransport.requestDump)
		httpTransport.call(SOAP_ACTION, envelope); //send request
		SoapPrimitive result=(SoapPrimitive)envelope.getResponse(); //get response
		return result;
		
	}
	@Override
	protected SoapPrimitive doInBackground(String... params) {
		SoapPrimitive so = null;
		try {
			int i = 0;
			so = soap(i);
			while(so.getValue().equals("false") && i < 50){
				i++;
				so = soap(i);
				Log.d("DEBUG","version ok? "+so+" "+i);
			}
			
			
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
