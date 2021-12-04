package com.example.demo.Controller;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.example.demo.Metrics.AwsMetrics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class VNTController {

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/cpu/{ipAddress}/{period}/{date}")
    public String getCPU(@PathVariable (value = "ipAddress") String ipAddress,
                        @PathVariable (value = "period") Integer period,
                        @PathVariable (value = "date") String date){

        List<Datapoint> output = AwsMetrics.MonitorCPU(ipAddress , period , date);
        JsonObject jsonObject = new JsonObject();
        JsonArray periodArr = new JsonArray();
        JsonArray cpuArr = new JsonArray();
        output.forEach(x-> {
            periodArr.add(String.valueOf(x.getTimestamp()));
            cpuArr.add(x.getAverage());
        });
        jsonObject.add("period" , periodArr);
        jsonObject.add("avgCpu" , cpuArr);
        return jsonObject.toString();
    }
}
