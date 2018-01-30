package com.ng.ganhuoapi.network.okhttp;

import android.content.Context;

import com.ng.ganhuoapi.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Horrarndoo on 2017/9/12.
 * <p>
 */
public class TrustManager {

    private static final String TAG = "信息";

    public static SSLSocketFactory getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final X509TrustManager[] trustAllCerts = new X509TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();


            return sslSocketFactory;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }




    public static SSLSocketFactory getSocketFactory(Context context) {
        String password = context.getResources().getString(R.string.ssl_password);
        final String KEY_STORE_PASSWORD = password;//证书密码（应该是客户端证书密码）
        final String KEY_STORE_TRUST_PASSWORD = password;//授信证书密码（应该是服务端证书密码）
        InputStream ksIn = null;
        InputStream tsIn = null;
        try {
            ksIn = context.getResources().getAssets().open("client.p12");
            tsIn = context.getResources().getAssets().open("service.p12");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            trustStore.load(ksIn, KEY_STORE_TRUST_PASSWORD.toCharArray());
            // 服务器端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(tsIn, KEY_STORE_PASSWORD.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();
            return factory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                tsIn.close();
                tsIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        /**
      /*   * 方法说明：获取SslSocketFactory
         *
         * @param context 上下文
         * @return SSLSocketFactory
         *//*
    public static SSLSocketFactory getSslSocketFactory(Context context) {
        String password = context.getResources().getString(R.string.ssl_password);
        InputStream ksIn = null;
        InputStream tsIn = null;
        try {
            // 服务器端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance("PKCS12");

            ksIn = context.getResources().getAssets().open("client.p12");
            tsIn = context.getResources().getAssets().open("service.p12");
            keyStore.load(ksIn, password.toCharArray());
            trustStore.load(tsIn, password.toCharArray());
            return new SSLSocketFactory(keyStore, password, trustStore);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (ksIn != null) {
                try {
                    ksIn.close();
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            if (tsIn != null) {
                try {
                    tsIn.close();
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        }
        return null;
    }*/
}

