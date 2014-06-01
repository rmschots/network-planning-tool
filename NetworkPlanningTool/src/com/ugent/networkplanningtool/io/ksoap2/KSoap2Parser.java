package com.ugent.networkplanningtool.io.ksoap2;

import android.graphics.Point;

import com.ugent.networkplanningtool.data.AccessPoint;
import com.ugent.networkplanningtool.data.FloorPlan;
import com.ugent.networkplanningtool.data.ServiceData.CSVResult;
import com.ugent.networkplanningtool.data.ServiceData.DeusRequest;
import com.ugent.networkplanningtool.data.ServiceData.DeusResult;
import com.ugent.networkplanningtool.data.enums.Frequency;
import com.ugent.networkplanningtool.data.enums.FrequencyBand;
import com.ugent.networkplanningtool.data.enums.RadioModel;
import com.ugent.networkplanningtool.data.enums.RadioType;
import com.ugent.networkplanningtool.io.ASyncTaskException;
import com.ugent.networkplanningtool.io.xml.XMLIO;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing the functionality to parse SOAP messages of the web service
 */
public class KSoap2Parser {
    /**
     * Parses the given SoapObject and the request type
     * @param so the soap object
     * @param requestType the request type
     * @return the parsed results
     * @throws ASyncTaskException thrown when something goes wrong while parsing
     */
    public static DeusResult parseDeusResult(SoapObject so, DeusRequest.RequestType requestType) throws ASyncTaskException {
        if (so.hasProperty("errormsg")) {
            throw new ASyncTaskException(so.getPropertyAsString("errormsg"));
        }

        // TODO parse unused data (atm, we only parse data we use)

        Double[] infoArray = new Double[3];
        int infoIndex = 0;
        PropertyInfo ai = new PropertyInfo();
        for (int i = 0; i < so.getPropertyCount(); i++) {
            so.getPropertyInfo(i, ai);
            if (ai.getName().equals("info")) {
                infoArray[infoIndex] = Double.parseDouble(ai.getValue().toString());
                infoIndex++;
            }
        }
        List<AccessPoint> accessPoints = new ArrayList<AccessPoint>();
        if (so.hasProperty("accesspoints")) {
            accessPoints = parseAccessPoints(so.getPropertyAsString("accesspoints"));
        }

        String benchmarks = so.getPropertyAsString("benchmarks");
        List<CSVResult> csv = null;
        if (so.hasProperty("csv")) {
            csv = parseCSV(so.getPropertyAsString("csv"));
        }

        FloorPlan normalizedPlan = null;
        if (so.hasProperty("normalizedPlan") && !so.getPropertyAsString("normalizedPlan").equals("anyType{}")) {
            try {
                normalizedPlan = XMLIO.loadFloorPlan(so.getPropertyAsString("normalizedPlan"));
            } catch (Exception e) {
                throw new ASyncTaskException("Error parsing service response normalized xml");
            }
        }
        FloorPlan optimizedPlan = null;
        if (so.hasProperty("optimizedPlan") && !so.getPropertyAsString("optimizedPlan").equals("anyType{}")) {
            try {
                optimizedPlan = XMLIO.loadFloorPlan(so.getPropertyAsString("optimizedPlan"));
            } catch (Exception e) {
                throw new ASyncTaskException("Error parsing service response normalized xml");
            }
        }

        return new DeusResult(requestType, accessPoints, benchmarks, csv, null, null, infoArray, null, null, normalizedPlan, optimizedPlan);
    }

    /**
     * Parses the location specific results.
     * (floor,X,Y,download speed,upload speed,path loss,RX power,TX power,SAR,electric field,PDLOS(non-relevant),PDDIF(non-relevant),(non-relevant),room number
     * @param csvString String to parse
     * @return the parsed results
     */
    private static List<CSVResult> parseCSV(String csvString) {
        List<CSVResult> orList = new ArrayList<CSVResult>();
        if (csvString.startsWith("L")) {
            csvString = csvString.substring(csvString.indexOf('\n') + 1);
        }
        String[] orStrings = csvString.split("\n");
        for (String orString : orStrings) {
            String[] attrStrings = orString.split(",");
            int i = 0;
            Double level = Double.parseDouble(attrStrings[i++]);
            Double x = Double.parseDouble(attrStrings[i++]);
            Double y = Double.parseDouble(attrStrings[i++]);
            Double download = Double.parseDouble(attrStrings[i++]);
            Double upload = Double.parseDouble(attrStrings[i++]);
            Double pathloss = attrStrings[i++].equals("null") ? null : Double.parseDouble(attrStrings[i - 1]);
            Double powerRX = attrStrings[i++].equals("null") ? null : Double.parseDouble(attrStrings[i - 1]);
            Double powerTX = attrStrings[i++].equals("null") ? null : Double.parseDouble(attrStrings[i - 1]);
            Double absorption = Double.parseDouble(attrStrings[i++]);
            Double eField = Double.parseDouble(attrStrings[i++]);
            Double pdLos = 0.0;
            Double pdDif = 0.0;
            if (attrStrings.length != 12) {
                pdLos = Double.parseDouble(attrStrings[i++]);
                pdDif = Double.parseDouble(attrStrings[i++]);
            }
            Double drawingSize = Double.parseDouble(attrStrings[i]);
            Double roomNumber = Double.parseDouble(attrStrings[i++]);
            CSVResult or = new CSVResult(new Point(x.intValue(), y.intValue()),
                    level.intValue(), download, upload, pathloss, powerRX,
                    powerTX, absorption, eField, pdLos, pdDif, roomNumber.intValue(),
                    drawingSize.intValue());
            orList.add(or);
        }
        return orList;
    }

    // 1633.0;263.0;250;\n\tWiFi;14.0;2;DLink;2400;2462

    /**
     * Parses the access point data from the results
     * @param apString the ap data as string
     * @return the resulting list of access points
     */
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
