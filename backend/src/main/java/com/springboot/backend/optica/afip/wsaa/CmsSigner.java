package com.springboot.backend.optica.afip.wsaa;

import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collections;

public class CmsSigner {

    public static byte[] sign(String xml, String p12Path, String p12Password, String alias) throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(p12Path), p12Password.toCharArray());

        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, p12Password.toCharArray());
        X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);

        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
                new JcaDigestCalculatorProviderBuilder().build())
                .build(new JcaContentSignerBuilder("SHA256withRSA").build(privateKey),
                        new JcaX509CertificateHolder(certificate)));
        generator.addCertificates(new org.bouncycastle.cert.jcajce.JcaCertStore(Collections.singletonList(certificate)));

        CMSProcessableByteArray cmsData = new CMSProcessableByteArray(xml.getBytes("UTF-8"));
        CMSSignedData signedData = generator.generate(cmsData, true);

        return signedData.getEncoded();
    }
}
