package br.com.diogotorresdev.moneyapi.api.config.token;

import br.com.diogotorresdev.moneyapi.api.security.AppUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        AppUser appUser = (AppUser) authentication.getPrincipal();

        Map<String, Object> addInfo = new HashMap<>();

        addInfo.put("userAccountName", appUser.getUserAccount().getUserAccountName());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);

        return accessToken;
    }
}
