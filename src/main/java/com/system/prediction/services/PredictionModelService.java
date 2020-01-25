package com.system.prediction.services;

import com.system.pojo.ProcessedRecord;
import com.system.settings.AppSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PredictionModelService {
    public Map<Boolean, List<ProcessedRecord>> splitData(List<ProcessedRecord> processedRecords) {
        int index = processedRecords.size() * AppSettings.trainDataRatio;

        Map<Boolean, List<ProcessedRecord>> resultMap = new HashMap<>();
        resultMap.put(false, processedRecords.subList(0, index));
        resultMap.put(true, processedRecords.subList(index, processedRecords.size()));

        return resultMap;
    }
}
