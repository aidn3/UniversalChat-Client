
package com.aidn5;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

/**
 * Class used to send metrics data to analyze the usage of services.
 * <p>
 * The class is only using standard java 1.8 libraries to be lightweight.
 * Everything is in a single class
 * and made easy to use by having a single public method.
 *
 * @author aidn5
 * @version 1.3
 */
public final class Metrics {
    private static final Logger LOGGER = Logger.getLogger(Metrics.class.getName());
    private static final long INTERVAL = TimeUnit.MINUTES.toMillis(5);

    private static final URL METRICS_URL;

    static {
        try {
            METRICS_URL = new URL("https://metrics.aidn5.com/");
        } catch (MalformedURLException e) {
            throw new RuntimeException("metrics URL is malformed. this should not happen", e);
        }
    }

    /**
     * Start sending usage metrics periodically.
     * The period is determined by {@link #INTERVAL}.
     * <p>
     * <p>
     * Usage Example:
     *
     * <pre>
     * public static void main(String[] args) {
     *   if (SEND_USAGE) Metrices.sendUsage("Optimizer3000");
     *
     *   // rest of the code
     * }
     * </pre>
     *
     * @param projectId the project/service to meter its usage.
     * @throws IllegalArgumentException If {@code projectId} is empty.
     * @throws NullPointerException     If {@code projectId} is <code>null</code>.
     */
    // This is holy-war between IAE and NPE. I use both :)
    public static void sendUsage(String projectId) throws IllegalArgumentException, NullPointerException {
        new Metrics(projectId);
    }

    private final String projectId;

    private Metrics(String projectId) throws IllegalArgumentException, NullPointerException {

        if (projectId.trim().isEmpty()) // expected the NPException
            throw new IllegalArgumentException("projectId must not be null or empty");
        this.projectId = projectId.trim();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::sendMetrics, 0, INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void sendMetrics() {
        String payload = String.format("{\"projectId\":\"%s\"}", this.projectId.replace("\"", "\\\""));

        HttpsURLConnection c = null;
        try {
            c = (HttpsURLConnection) METRICS_URL.openConnection();
            c.setRequestMethod("POST");
            c.setRequestProperty("Content-Type", "application/json; utf-8");
            c.setDoOutput(true);
            c.setDoInput(false);

            try (OutputStream os = c.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = c.getResponseCode();
            LOGGER.log(Level.FINER, "server responded with " + code);

        } catch (IOException e) {
            LOGGER.log(Level.FINER, "can't send metrics data", e);

        } finally {
            if (c != null) c.disconnect();
        }
    }
}
