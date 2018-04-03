package com.yunshu.commonlib.okhttp;

import android.content.Context;

import com.yunshu.commonlib.BuildConfig;
import com.yunshu.commonlib.util.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Tyrese on 2017/12/13.
 *
 * SSLSocketFactory 工具类
 */

class SSLSocketFactoryWrapper {

    /**
     * 单向证书
     *
     * @param context 读取证书时需要用到Context.
     * @param certPath 服务端证书路径
     *
     * */
    static SSLSocketFactory buildSSLSocketFactory(Context context, String certPath) {
        KeyStore keyStore = null;
        try {
            keyStore = buildKeyStore(context, certPath);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslContext.getSocketFactory();

    }

    /**
     * 双向证书
     *
     * @param context 读取证书时需要用到Context.
     * @param clientPath 客户端证书路径
     * @param serverPath 服务端证书路径
     *
     * */
    static SSLSocketFactory getSSLFactory(Context context, String clientPath, String serverPath) {
        return getSSLFactory(context, clientPath,
                BuildConfig.CERT_CLIENT_PASSWORD, serverPath,
                BuildConfig.CERT_SERVER_PASSWORD, BuildConfig.CLIENT_STORE_PASSWORD);
    }

    /**
     * 双向证书
     *
     * @param context 读取证书时需要用到Context.
     * @param clientPath 客户端证书路径
     * @param clientCertPwd 客户端证书密码
     * @param serverPath 服务端证书路径
     * @param serverCertPwd 服务端证书密码
     * @param clientStorePwd 客户端store密码
     *
     * */
    private static SSLSocketFactory getSSLFactory(Context context, String clientPath, String clientCertPwd,
                                                  String serverPath, String serverCertPwd, String clientStorePwd) {
        SSLSocketFactory socketFactory = null;
        try {
            // 服务器端需要验证的客户端证书，其实就是客户端的keystore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance("BKS");

            //读取证书
            InputStream ksIn = context.getAssets().open(clientPath);
            InputStream tsIn = context.getAssets().open(serverPath);

            //加载证书
            keyStore.load(ksIn, clientCertPwd.toCharArray());
            trustStore.load(tsIn, serverCertPwd.toCharArray());
            StreamUtil.close(ksIn);
            StreamUtil.close(tsIn);

            //初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            trustManagerFactory.init(trustStore);
            keyManagerFactory.init(keyStore, clientStorePwd.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            //通过HttpsURLConnection设置链接
            socketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return socketFactory;
    }

    private static KeyStore buildKeyStore(Context context, String resourcePath) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);

        Certificate cert = readCert(context, resourcePath);
        //这里别名可以随便取
        keyStore.setCertificateEntry("ca", cert);

        return keyStore;
    }

    private static Certificate readCert(Context context, String resourcePath) {
        Certificate ca = null;

        CertificateFactory cf;
        try {
            InputStream inputStream = context.getAssets().open(resourcePath);
            cf = CertificateFactory.getInstance("X.509");
            ca = cf.generateCertificate(inputStream);

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ca;
    }
}
