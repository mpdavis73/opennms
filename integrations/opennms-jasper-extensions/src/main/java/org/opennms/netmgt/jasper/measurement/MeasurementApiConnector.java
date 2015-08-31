package org.opennms.netmgt.jasper.measurement;

import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Posts a given query to a given url using optional username and password.
 */
class MeasurementApiConnector {

    private static final Logger LOG = LoggerFactory.getLogger(MeasurementApiConnector.class);

    private HttpURLConnection connection;

    public Result execute(final String url, final String username, final String password, final String query) throws IOException {
        log(url, username, password, query);
        connect(url, username, password);
        write(query.getBytes(), connection.getOutputStream());
        Result result = createResult(connection);
        LOG.debug("Request to URL '{}' returned with status: {} ({})", url, result.getResponseCode(), result.getResponseMessage());
        return result;
    }

    public void disconnect() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    private String createBasicAuthHeader(String username, String password) {
        final String pass = String.format("%s:%s", username, password);
        final String basicAuthHeader = "Basic " + new String(BaseEncoding.base64().encode(pass.getBytes()));
        return basicAuthHeader;
    }

    private void connect(final String url, final String username, final String password) throws IOException {
        connection = (HttpURLConnection) new URL(url).openConnection();
        if (isAuthenticationRequired(username, password)) {
            connection.setRequestProperty("Authorization", createBasicAuthHeader(username, password));
        }
        connection.setConnectTimeout(2500);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/xml");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.setInstanceFollowRedirects(false); // we do not want to follow redirects, otherwise 200 OK might be returned
        connection.connect();
    }

    private static void write(byte[] input, OutputStream outputStream) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        ByteStreams.copy(inputStream, outputStream);
    }

    protected static boolean isAuthenticationRequired(String username, String password) {
        return !Strings.isNullOrEmpty(username) && !Strings.isNullOrEmpty(password);
    }

    private static void log(String url, String username, String password, String query) {
        LOG.info("Connecting to {}", url);

        if (isAuthenticationRequired(username, password)) {
            LOG.info("Using authentication: YES");
            LOG.info("Using username {}", username);
            LOG.info("Using password {}", "*******");
        } else {
            LOG.info("Using authentication: NO");
        }
        LOG.info("Query Request: {}", query);
    }

    private static Result createResult(HttpURLConnection connection) throws IOException {
        Result result = new Result();
        result.setResponseCode(connection.getResponseCode());
        result.setResponseMessage(connection.getResponseMessage());
        if (result.wasSuccessful()) {
            result.setInputStream(connection.getInputStream());
        } else {
            result.setErrorStream(connection.getErrorStream());
        }
        return result;
    }
}
