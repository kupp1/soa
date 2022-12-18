package ru.ifmo.se;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import ru.ifmo.se.common.model.FlatPageResponse;
import ru.ifmo.se.common.remote.FlatServiceResponse;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class FlatServiceAccessor {
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FlatServiceAccessor() {
        httpClient = getHttpClient();
        objectMapper = new ObjectMapper();
    }

    @SneakyThrows(IOException.class)
    public FlatServiceResponse loadFlatPageResponse(String url) {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);

        InputStream content = response.getEntity().getContent();

        Integer httpStatusCode = response.getStatusLine().getStatusCode();

        if (httpStatusCode == HttpStatus.SC_OK) {
            FlatPageResponse responseEntity = objectMapper.readValue(content, FlatPageResponse.class);
            return new FlatServiceResponse(httpStatusCode, responseEntity);
        } else {
            return new FlatServiceResponse(httpStatusCode, null);
        }
    }

    @SneakyThrows({NoSuchAlgorithmException.class, KeyStoreException.class, KeyManagementException.class})
    private CloseableHttpClient getHttpClient() {
        TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory> create()
                        .register("https", sslsf)
                        .register("http", new PlainConnectionSocketFactory())
                        .build();
        BasicHttpClientConnectionManager connectionManager =
                new BasicHttpClientConnectionManager(socketFactoryRegistry);

        return HttpClients.custom().setSSLSocketFactory(sslsf)
                .setConnectionManager(connectionManager).build();
    }
}
