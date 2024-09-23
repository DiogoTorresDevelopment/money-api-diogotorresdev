package br.com.diogotorresdev.moneyapi.api.config;

import br.com.diogotorresdev.moneyapi.api.config.property.MoneyApiProperty;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Autowired
    private MoneyApiProperty properties;

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(properties.getS3().getAccessKeyId(),properties.getS3().getSecretAccessKey());

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        if(!amazonS3.doesBucketExistV2(properties.getS3().getBucket())) {
            amazonS3.createBucket(new CreateBucketRequest(properties.getS3().getBucket()));

            BucketLifecycleConfiguration.Rule rule =
                    new BucketLifecycleConfiguration.Rule()
                        .withId("Regra de expiração de arquivos temporários")
                        .withFilter(new LifecycleFilter(
                                new LifecycleTagPredicate(new Tag("expired", "true"))))
                        .withExpirationInDays(1)
                        .withStatus(BucketLifecycleConfiguration.ENABLED);

            BucketLifecycleConfiguration config = new BucketLifecycleConfiguration()
                    .withRules(rule);

            amazonS3.setBucketLifecycleConfiguration(properties.getS3().getBucket(), config);
        }

        return amazonS3;
    }

}
