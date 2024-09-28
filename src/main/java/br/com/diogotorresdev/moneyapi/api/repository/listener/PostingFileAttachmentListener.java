package br.com.diogotorresdev.moneyapi.api.repository.listener;

import br.com.diogotorresdev.moneyapi.api.MoneyApiApplication;
import br.com.diogotorresdev.moneyapi.api.model.Posting;
import br.com.diogotorresdev.moneyapi.api.storage.S3;
import org.springframework.util.StringUtils;

import javax.persistence.PostLoad;

public class PostingFileAttachmentListener {

    @PostLoad
    public void postLoad(Posting posting){
        if(StringUtils.hasText(posting.getFileAttachment())){
            S3 s3 = MoneyApiApplication.getBean(S3.class);

            posting.setUrlFileAttachment(s3.configurationURL(posting.getFileAttachment()));
        }
    }

}
