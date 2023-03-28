package com.example.demo.security.endpoint;


import com.example.demo.config.Oauth2MetaProperties;
import com.example.demo.security.util.PemReader;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;

import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

@Slf4j
public class Oauth2RequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {
    private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    private final Oauth2MetaProperties oauth2MetaProperties;

    public Oauth2RequestEntityConverter(Oauth2MetaProperties oauth2MetaProperties) {
        this.oauth2MetaProperties = oauth2MetaProperties;
        this.defaultConverter.addParametersConverter(this::appleParameterConverter);
    }

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        return defaultConverter.convert(authorizationGrantRequest);
    }

    private MultiValueMap<String, String> appleParameterConverter(OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest) {
        if (!authorizationCodeGrantRequest.getClientRegistration().getClientName().equals("apple")) return null;
        Oauth2MetaProperties.AppleProperties appleProperties = oauth2MetaProperties.getApple();
        ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
        PrivateKey privateKey = null;

        try {
            privateKey = getApplePrivateKey();
        } catch (Exception e) {
            log.error("can not generate private key for sign in with apple");
            e.printStackTrace();
        }

        String client_secret = Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleProperties.getKeyId())
                .setHeaderParam(JwsHeader.ALGORITHM, "ES256")
                .setIssuer(appleProperties.getIssuer())
                .setAudience(appleProperties.getTeamId())
                .setSubject(clientRegistration.getClientId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 5)))
                .signWith(SignatureAlgorithm.ES256, privateKey)
                .compact();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("client_secret", client_secret);
        return parameters;
    }

    private PrivateKey getApplePrivateKey() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        byte[] p8Key = null;
        FileReader keyFileReader = new FileReader(ResourceUtils.getFile("classpath:key/AuthKey_924CJL97F3.p8"));
        PemReader pemReader = new PemReader(keyFileReader);
        p8Key = pemReader.decodePemFile();
        pemReader.close();
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(p8Key));
    }

}
