package com.system.prediction.services;

import com.system.pojo.ProcessedRecord;
import com.system.settings.AppSettings;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.Attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictionModelService {
    public Map<Boolean, List<ProcessedRecord>> splitData(List<ProcessedRecord> processedRecords) {
        int index = (int) (processedRecords.size() * AppSettings.trainDataRatio);

        Map<Boolean, List<ProcessedRecord>> resultMap = new HashMap<>();
        resultMap.put(false, processedRecords.subList(0, index));
        resultMap.put(true, processedRecords.subList(index, processedRecords.size()));

        return resultMap;
    }

    public void wekaWeka(List<ProcessedRecord> trainData, List<ProcessedRecord> testData) throws Exception {
        ArrayList<Attribute> atts = createAttributes();

        Instances trainDataInstances = createInstances(atts, trainData);
        Instances testDataInstances = createInstances(atts, testData);

        System.out.println(trainDataInstances);
    }


    private Instances createInstances(ArrayList<Attribute> atts, List<ProcessedRecord> recordList) {
        Instances instances = new Instances("MyRelation", atts, 0);

        for (ProcessedRecord processedRecord : recordList) {
            double[] vals = new double[instances.numAttributes()];
            vals[0] = processedRecord.getId();
            vals[1] = processedRecord.getActionType();
            vals[2] = instances.attribute(2).addStringValue(processedRecord.getActionTime());
            vals[3] = processedRecord.getStationId();
            vals[4] = instances.attribute(4).addStringValue(processedRecord.getStationName());
            vals[5] = instances.attribute(5).addStringValue(processedRecord.getStationLatitude());
            vals[6] = instances.attribute(6).addStringValue(processedRecord.getStationLongitude());
            vals[7] = processedRecord.getBikeId();
            vals[8] = processedRecord.getCurrentBikeCount();
            vals[9] = processedRecord.getInitialBikeCount();
            vals[10] = instances.attribute(10).addStringValue(processedRecord.getUserType());
            vals[11] = processedRecord.getBirthYear();
            vals[12] = processedRecord.getGender();
            vals[13] = processedRecord.getTemperature();
            vals[14] = processedRecord.getWindspeed();
            vals[15] = processedRecord.getPressure();
            instances.add(new DenseInstance(1.0, vals));
        }

        return instances;
    }

    private ArrayList<Attribute> createAttributes() {
        ArrayList<Attribute> atts = new ArrayList<>();
        atts.add(new Attribute("id"));
        atts.add(new Attribute("actionType"));
        atts.add(new Attribute("actionTime", (ArrayList<String>)null));
        atts.add(new Attribute("stationId"));
        atts.add(new Attribute("stationName", (ArrayList<String>)null));
        atts.add(new Attribute("stationLatitude", (ArrayList<String>)null));
        atts.add(new Attribute("stationLongitude", (ArrayList<String>)null));
        atts.add(new Attribute("bikeId"));
        atts.add(new Attribute("currentBikeCount"));
        atts.add(new Attribute("initialBikeCount"));
        atts.add(new Attribute("userType", (ArrayList<String>)null));
        atts.add(new Attribute("birthYear"));
        atts.add(new Attribute("gender"));
        atts.add(new Attribute("temperature"));
        atts.add(new Attribute("windspeed"));
        atts.add(new Attribute("pressure"));
        return atts;
    }
}
