package com.example.demo.security.util;

import io.jsonwebtoken.lang.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Base64;
import java.util.stream.Stream;

public class PemReader extends BufferedReader {
    private static final String BEGIN = "-----BEGIN PRIVATE KEY-----";
    private static final String END = "-----END PRIVATE KEY-----";

    public PemReader(Reader var1) {
        super(var1);
    }

    public byte[] decodePemFile() throws IOException {
        if (!super.ready()) {
            throw new IOException("Pem file buffered reader is not ready yet");
        }

        Stream<String> lines = super.lines();
        StringBuilder sb = new StringBuilder();

        lines.filter(s -> !s.equals(BEGIN) && !s.equals(END))
                .map(s -> s.replaceAll(System.lineSeparator(), ""))
                .forEach(sb::append);

        byte[] decodedPemFile = Base64.getDecoder().decode(sb.toString());
        Assert.notEmpty(decodedPemFile, "pem file can not be empty");
        return decodedPemFile;
    }

}
