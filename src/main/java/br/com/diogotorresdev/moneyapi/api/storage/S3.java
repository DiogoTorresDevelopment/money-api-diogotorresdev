package br.com.diogotorresdev.moneyapi.api.storage;

import br.com.diogotorresdev.moneyapi.api.config.property.MoneyApiProperty;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Component
public class S3 {

    private static final Logger logger = LoggerFactory.getLogger(S3.class);

    @Autowired
    private MoneyApiProperty property;

    @Autowired
    private AmazonS3 amazonS3;

    public String saveTemporary(MultipartFile multipartFile) {
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize());

        String uniqueName = generateUniqueName(multipartFile.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    property.getS3().getBucket(),
                    uniqueName,
                    multipartFile.getInputStream(),
                    metadata
            ).withAccessControlList(acl);

            putObjectRequest.setTagging(new ObjectTagging(Arrays.asList(new Tag("expired","true"))));

            amazonS3.putObject(putObjectRequest);

            if (logger.isDebugEnabled()) {
                logger.debug("Arquivo {} enviado com sucesso para o S3.", multipartFile.getOriginalFilename());
            }

            return uniqueName;

        } catch (IOException e) {
            throw new RuntimeException("Problema ao tentar enviar arquivo para o S3.",e);
        }
    }

    public String configurationURL(String obj){
        return "\\\\" + property.getS3().getBucket() + ".s3.amazonaws.com/" + obj;
    }

    public void saveFile(String obj) {

        SetObjectTaggingRequest setObjectTaggingRequest = new SetObjectTaggingRequest(
                property.getS3().getBucket(), obj, new ObjectTagging(Collections.emptyList()));

        amazonS3.setObjectTagging(setObjectTaggingRequest);

    }

    private String generateUniqueName(String originalFilename) {

        return UUID.randomUUID().toString() + "_" + originalFilename;
    }


}
