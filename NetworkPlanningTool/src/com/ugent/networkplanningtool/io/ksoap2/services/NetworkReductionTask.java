package com.ugent.networkplanningtool.io.ksoap2.services;

import com.ugent.networkplanningtool.data.DeusRequest;
import com.ugent.networkplanningtool.data.DeusResult;
import com.ugent.networkplanningtool.io.ksoap2.AbstractWebServiceTask;
import com.ugent.networkplanningtool.io.ksoap2.marshal.MarshalDouble;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class NetworkReductionTask extends AbstractWebServiceTask<DeusRequest, DeusResult> {
    private static final String METHOD_NAME = "symbioselect";
    private static final String SOAP_ACTION = "http://wicaweb2.intec.ugent.be:80/DeusService/Deus/symbioselect";
    private static final String NAMESPACE = "http://web.deus.wica.intec.ugent.be/";
    private static final String URL = "http://wicaweb2.intec.ugent.be/DeusService/Deus?wsdl";

    public NetworkReductionTask() {
    }

    @Override
    protected DeusResult performTaskInBackground(DeusRequest dr)
            throws Exception {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); //set up request
        request.addProperty("client_version", dr.getClientVersion());
        request.addProperty("xml", dr.getXml());
        request.addProperty("model", dr.getModel());
        request.addProperty("grid_size", dr.getGridSize());
        request.addProperty("default_type", dr.getDefaultType());
        request.addProperty("receiver_name", dr.getReceiverName());
        request.addProperty("receiver_gain", dr.getReceiverGain());
        request.addProperty("receiver_height", dr.getReceiverHeight());
        request.addProperty("interference", dr.getInterference());
        request.addProperty("shadow_margin", dr.getShadowMargin());
        request.addProperty("fade_margin", dr.getFadeMargin());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //put all required data into a soap envelope

        envelope.setOutputSoapObject(request);  //prepare request
        MarshalDouble md = new MarshalDouble();
        md.register(envelope);
        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        httpTransport.debug = true;  //this is optional, use it if you don't want to use a packet sniffer to check what the sent message was (httpTransport.requestDump)

        ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
        headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));

        httpTransport.call(SOAP_ACTION, envelope, headerPropertyArrayList); //send request
        SoapObject result = (SoapObject) envelope.getResponse(); //get response
        return parseDeusResult(result);
    }
}