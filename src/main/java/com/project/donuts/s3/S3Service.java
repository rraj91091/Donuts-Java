package com.project.donuts.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final S3Client s3Client;

    public S3Service(
            @Value("${aws.s3.region}") String awsRegion,
            @Value("${aws.credentials.access-key}") String accessKey,
            @Value("${aws.credentials.secret-key}") String secretKey
    ) {
        this.s3Client = S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }


    public PutObjectResponse putObject(String bucketName, String key, byte[] contents) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Client.putObject(request, RequestBody.fromBytes(contents));
    }

    public byte[] getObject(String bucketName, String objectKey) {
        try {
            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            ResponseBytes<GetObjectResponse> responseResponseBytes = s3Client.getObjectAsBytes(objectRequest);
            return responseResponseBytes.asByteArray();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    public List<S3Object> listAllObjectsInBucket(String bucketName) {
        String nextContinuationToken = null;
        List<S3Object> s3Objects = new ArrayList<>();

        do {
            ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .continuationToken(nextContinuationToken);

            ListObjectsV2Response response = s3Client.listObjectsV2(requestBuilder.build());
            s3Objects.addAll(response.contents());

            nextContinuationToken = response.nextContinuationToken();
        } while (nextContinuationToken != null);

        return s3Objects;
    }

}