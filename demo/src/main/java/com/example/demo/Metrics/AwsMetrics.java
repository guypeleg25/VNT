package com.example.demo.Metrics;

import java.util.*;

import com.amazonaws.services.cloudwatch.model.*;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.example.demo.Util.MyUtil;
import org.springframework.stereotype.Service;

@Service
public class AwsMetrics {


    public static List<Datapoint> MonitorCPU(String ipAddress , Integer period , String date) {
        List<Datapoint> dataPoint = new ArrayList<>();
        AWSCredentialsProvider awsCredentialsProvider = new AWSCredentialsProvider() {
            @Override
            public void refresh() {
            }

            @Override
            public AWSCredentials getCredentials() {
                AWSCredentials awsCredentials = null;
                try {
                    awsCredentials = new AWSCredentials() {

                        public String getAWSSecretKey() {
                            return "xxx";
                        }

                        public String getAWSAccessKeyId() { return "yyy";
                        }
                    };
                } catch (Exception e) {
                    throw new AmazonClientException(
                            "can not load your aws credentials, please check your credentials !!", e);
                }
                return awsCredentials;
            }
        };
        try{

            AmazonCloudWatch cw =  AmazonCloudWatchClientBuilder.standard()
                    .withCredentials(awsCredentialsProvider).withRegion("us-east-1").build();
            long offsetInMilliseconds = MyUtil.parseDateToMilliseconds(date);

            AmazonEC2 ec2 = AmazonEC2ClientBuilder.standard().withCredentials(awsCredentialsProvider).withRegion("us-east-1").build();
            DescribeInstancesResult describeInstances = ec2.describeInstances();

            System.out.println("My EC2" + describeInstances.getReservations());

            Instance id = describeInstances.getReservations().stream().
                    flatMap(a->a.getInstances().stream()).
                    filter(b->b.getPrivateIpAddress().equals(ipAddress)).
                    findFirst().orElse(null);

            Dimension dimension = new Dimension().withName("InstanceId")
                    .withValue(id.getInstanceId());

            GetMetricStatisticsRequest request = new GetMetricStatisticsRequest()
                    .withStartTime(new Date(new Date().getTime() - offsetInMilliseconds)).withNamespace("AWS/EC2")
                    .withPeriod(period)
                    .withMetricName("CPUUtilization").withStatistics("Average").withEndTime(new Date()).withDimensions(dimension);

            GetMetricStatisticsResult getMetricStatisticsResult = cw.getMetricStatistics(request);

            System.out.println("request " + request.toString());
            System.out.println("DataPoint Size : " + getMetricStatisticsResult.getDatapoints().size());

             dataPoint = getMetricStatisticsResult.getDatapoints();
            Collections.sort(dataPoint,Comparator.comparing(Datapoint::getTimestamp));
        }catch(AmazonServiceException ase){
            ase.printStackTrace();
        }
        return dataPoint;
    }
}
