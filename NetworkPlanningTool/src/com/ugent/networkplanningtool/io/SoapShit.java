package com.ugent.networkplanningtool.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.AttributeInfo;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.Transport;
import org.xmlpull.v1.XmlPullParserException;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.FrequencyBand;
import com.ugent.networkplanningtool.data.Frequency;
import com.ugent.networkplanningtool.data.Network;
import com.ugent.networkplanningtool.data.DeusResult;
import com.ugent.networkplanningtool.data.RadioModel;
import com.ugent.networkplanningtool.data.RadioType;
import com.ugent.networkplanningtool.io.ksoap2.marshal.MarshalDouble;
import com.ugent.networkplanningtool.model.FloorPlanModel;
import com.ugent.networkplanningtool.model.OptimizeResultModel;

import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;

public class SoapShit extends AsyncTask<String, Void, SoapObject> {
	private static final String METHOD_NAME = "optimize";
	private static final String SOAP_ACTION = "http://wicaweb2.intec.ugent.be:80/DeusService/Deus/optimize";
	private static final String NAMESPACE = "http://web.deus.wica.intec.ugent.be/";
	private static final String URL = "http://wicaweb2.intec.ugent.be/DeusService/Deus?wsdl";

	// you can get these values from the wsdl file

	public static SoapObject soap() throws IOException, XmlPullParserException {
		String s2 = FloorPlanIO.getXMLAsString(FloorPlanModel.getInstance());
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); //set up request
		request.addProperty("client_version", "1.8.0.a");
		request.addProperty("xml", s2);
		request.addProperty("model", "sidp");
		request.addProperty("grid_size", 20.0);
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
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //put all required data into a soap envelope
		
		envelope.setOutputSoapObject(request);  //prepare request
		MarshalDouble md = new MarshalDouble();
        md.register(envelope);
		HttpTransportSE httpTransport = new HttpTransportSE(URL);  
		httpTransport.debug = true;  //this is optional, use it if you don't want to use a packet sniffer to check what the sent message was (httpTransport.requestDump)
		httpTransport.call(SOAP_ACTION, envelope); //send request
		SoapObject result=(SoapObject)envelope.getResponse(); //get response
		
		double[] infoArray = new double[3];
		int infoIndex = 0;
		PropertyInfo ai = new PropertyInfo();
		for(int i = 0; i < result.getPropertyCount(); i ++){
			result.getPropertyInfo(i, ai);
			if(ai.getName().equals("info")){
				infoArray[infoIndex] = Double.parseDouble(ai.getValue().toString());
				System.out.println(infoArray[infoIndex]);
				infoIndex++;
			}
		}
		
		List<AccessPoint> apList = parseAccessPoints(result.getPropertyAsString("accesspoints"));
		String benchmarks = result.getPropertyAsString("benchmarks");
		List<DeusResult> orList = parseOptimizeResults( result.getPropertyAsString("csv"));
		String normalizedPlan = result.getPropertyAsString("normalizedPlan");
		String optimizedPlan = result.getPropertyAsString("optimizedPlan");
		
		OptimizeResultModel.getInstance().loadModel(apList, benchmarks, orList, infoArray, "", normalizedPlan, optimizedPlan);
		System.out.println(OptimizeResultModel.getInstance());

		return result;
		
	}

	@Override
	protected SoapObject doInBackground(String... params) {
		SoapObject so = null;
		try {
			so = soap();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return so;
	}

	private static List<DeusResult> parseOptimizeResults(String csvString) {
		List<DeusResult> orList = new ArrayList<DeusResult>();
		String[] orStrings = csvString.split("\n");
		for (String orString : orStrings) {
			String[] attrStrings = orString.split(",");
			double level = Double.parseDouble(attrStrings[0]);
			double x = Double.parseDouble(attrStrings[1]);
			double y = Double.parseDouble(attrStrings[2]);
			double download = Double.parseDouble(attrStrings[3]);
			double upload = Double.parseDouble(attrStrings[4]);
			double pathloss = Double.parseDouble(attrStrings[5]);
			double powerRX = Double.parseDouble(attrStrings[6]);
			double powerTX = Double.parseDouble(attrStrings[7]);
			double absorption = Double.parseDouble(attrStrings[8]);
			double eField = Double.parseDouble(attrStrings[9]);
			double roomNumber = Double.parseDouble(attrStrings[10]);
			double drawingSize = Double.parseDouble(attrStrings[11]);
			DeusResult or = new DeusResult((int) level, new Point((int) x,
					(int) y), (int) download, (int) upload, pathloss, powerRX,
					powerTX, absorption, eField, (int) roomNumber,
					(int) drawingSize);
			orList.add(or);
		}
		return orList;
	}

	// 1633.0;263.0;250;\n\tWiFi;14.0;2;DLink;2400;2462
	private static List<AccessPoint> parseAccessPoints(String apString) {
		List<AccessPoint> apList = new ArrayList<AccessPoint>();
		String[] apStrings = apString.split("\n\t?");
		for (int i = 0; i + 1 < apStrings.length; i += 2) {
			String[] attrs = apStrings[i].split(";");
			String[] radioAttrs = apStrings[i + 1].split(";");
			double x = Double.parseDouble(attrs[0]);
			double y = Double.parseDouble(attrs[1]);
			int height = Integer.parseInt(attrs[2]);
			RadioType type = RadioType.getRadioTypeByText(radioAttrs[0]);
			double gain = Double.parseDouble(radioAttrs[1]);
			double power = Double.parseDouble(radioAttrs[2]);
			RadioModel model = RadioModel.getRadioModelByText(radioAttrs[3]);
			FrequencyBand frequency = FrequencyBand
					.getFrequencyBandByText(radioAttrs[4]);
			Frequency frequencyBand = Frequency.getFreqByNumber(Integer
					.parseInt(radioAttrs[5]));
			AccessPoint ap = new AccessPoint(new Point((int) x, (int) y), "",
					height, type, model, frequency, frequencyBand, (int) gain,
					(int) power, null);
			apList.add(ap);
		}
		return apList;
	}

}
