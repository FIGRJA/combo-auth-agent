package org.figrja.combo_auth_ahent.ely.by;

import org.figrja.combo_auth_ahent.Premain;
import org.figrja.combo_auth_ahent.config.debuglogger.LoggerMain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

public class httpHelper {
    static LoggerMain LOGGER = Premain.LOGGER;

    public httpHelper() {
    }

    public static String getRequest(URL url) throws Exception {
        StringBuilder result = new StringBuilder();
        LOGGER.debug(url.toString());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line;
        try {
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Throwable var7) {
            try {
                reader.close();
            } catch (Throwable var6) {
                var7.addSuppressed(var6);
            }

            throw var7;
        }

        reader.close();
        LOGGER.debug("code: " + conn.getResponseCode());
        return result.toString();
    }

    public static URL constantURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException var2) {
            throw new Error("Couldn't create constant for " + url, var2);
        }
    }

    public static String buildQuery(Map<String, Object> query) {
        if (query == null) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();

            for (Map.Entry<String, Object> stringObjectEntry : query.entrySet()) {
                if (builder.length() > 0) {
                    builder.append('&');
                }

                try {
                    builder.append(URLEncoder.encode((String) (stringObjectEntry).getKey(), "UTF-8"));
                } catch (UnsupportedEncodingException var6) {
                    LOGGER.info("Unexpected exception building query");
                }

                if ((stringObjectEntry).getValue() != null) {
                    builder.append('=');

                    try {
                        builder.append(URLEncoder.encode((stringObjectEntry).getValue().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException var5) {
                        LOGGER.info("Unexpected exception building query");
                    }
                }
            }

            return builder.toString();
        }
    }

    public static URL concatenateURL(URL url, String query) {
        try {
            return url.getQuery() != null && url.getQuery().length() > 0 ? new URL(url.getProtocol(), url.getHost(), url.getFile() + "&" + query) : new URL(url.getProtocol(), url.getHost(), url.getFile() + "?" + query);
        } catch (MalformedURLException var3) {
            throw new IllegalArgumentException("Could not concatenate given URL with GET arguments!", var3);
        }
    }

    public static resultElyGson makeRequest(URL url) throws Exception {
        String jsonResult = getRequest(url);
        LOGGER.debugRes(jsonResult);
        resultElyGson result = Premain.fromGson(jsonResult, resultElyGson.class);
        if (result == null) {
            return null;
        } else if (result.getError() != null) {
            return null;
        } else {
            //result.setId(str2uuid(result.id));
            return result;
        }
    }

    private static UUID str2uuid(String uuid) {
        String comUUID = uuid.length() == 32 ? uuid.substring(0, 8) +
                "-" + uuid.substring(8, 12) +
                "-" + uuid.substring(12, 16) +
                "-" + uuid.substring(16, 20) +
                "-" + uuid.substring(20) : uuid;
        return UUID.fromString(comUUID);
    }


}