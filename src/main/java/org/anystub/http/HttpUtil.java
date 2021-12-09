package org.anystub.http;

import org.anystub.Util;
import org.apache.http.*;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.message.BasicHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.anystub.Util.escapeCharacterString;

public class HttpUtil {

    private static final Logger LOGGER = Logger.getLogger(HttpUtil.class.getName());

    public static boolean globalAllHeaders = false;
    public static String[] globalHeaders = {};
    public static String[] globalBodyTrigger = {};
    public static String[] globalBodyMask = {};
    private static final String HEADER_MASK = "^[A-Za-z0-9\\-]+: .+";

    private HttpUtil() {
    }

    public static HttpResponse decode(Iterable<String> iterable) {
        BasicHttpResponse basicHttpResponse;

        Iterator<String> iterator = iterable.iterator();
        String[] protocol = iterator.next().split("[/.]");
        String code = iterator.next();
        String reason = iterator.next();

        basicHttpResponse = new BasicHttpResponse(new ProtocolVersion(protocol[0], parseInt(protocol[1]), parseInt(protocol[2])),
                parseInt(code),
                reason);

        String postHeader = null;
        while (iterator.hasNext()) {
            String header;
            header = iterator.next();
            if (!header.matches(HEADER_MASK)) {
                postHeader = header;
                break;
            }

            int i = header.indexOf(": ");
            basicHttpResponse.setHeader(header.substring(0, i), header.substring(i + 2));
        }

        if (postHeader != null) {
            BasicHttpEntity httpEntity = new BasicHttpEntity();

            byte[] bytes = Util.recoverBinaryData(postHeader);
            httpEntity.setContentLength(bytes.length);
            httpEntity.setContent(new ByteArrayInputStream(bytes));
            basicHttpResponse.setEntity(httpEntity);
        }

        return basicHttpResponse;
    }

    public static List<String> encode(HttpResponse httpResponse) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(httpResponse.getStatusLine().getProtocolVersion().toString());
        strings.add(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
        strings.add(httpResponse.getStatusLine().getReasonPhrase());

        for (Header h : httpResponse.getAllHeaders()) {
            strings.add(headerToString(h));
        }

        if (httpResponse.getEntity() != null) {
            try {
                BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(httpResponse.getEntity());
                httpResponse.setEntity(bufferedHttpEntity);
                extractEntity(bufferedHttpEntity)
                        .ifPresent(strings::add);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "buffering the entity failed", e);
            }
        }

        return strings;
    }


    public static byte[] extractEntity(HttpRequest httpRequest) {

        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest request = (HttpEntityEnclosingRequest) httpRequest;
            try {
                BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(request.getEntity());
                request.setEntity(bufferedHttpEntity);
                return extractEntityData(bufferedHttpEntity);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "buffering the entity failed", e);
            }
        }
        return null;
    }

    public static Optional<String> extractEntity(HttpEntity entity) {
        byte[] bytes = extractEntityData(entity);
        if (bytes == null) {
            return Optional.empty();
        }
        String entityText = Util.toCharacterString(bytes);
        if (entityText.matches(HEADER_MASK)) {
            entityText = Util.addTextPrefix(entityText);
        }

        return Optional.of(entityText);
    }

    public static byte[] extractEntityData(HttpEntity entity) {
        if (entity == null) {
            return null;
        }

        byte[] bytes = null;

        try {

            if (entity instanceof BasicHttpEntity) {
                BasicHttpEntity basicHttpEntity = (BasicHttpEntity) entity;

                try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
                    basicHttpEntity.writeTo(byteArray);
                    bytes = byteArray.toByteArray();
                }
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

                basicHttpEntity.setContent(inputStream);

            } else if (!entity.isStreaming()) {
                // put to cover:1. entity instanceof StringEntity
                // comes from https://github.com/OpenFeign/feign
                // when Content-Type: application/json; charset=UTF-8
                // 2. entity instanceof ByteArrayEntity
                // comes from org.springframework.web.client.RestTemplate
                // 3. HttpEntityWrapper
                try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
                    entity.writeTo(byteArray);
                    bytes = byteArray.toByteArray();
                }
            } else {
                LOGGER.warning(() -> String.format("content: unavailable %s %s", entity.getClass().getName(), entity));
            }


        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Stringify entity failed", e);
        }
        return bytes;
    }

    public static List<String> encode(HttpRequest httpRequest, HttpHost httpHost) {
        ArrayList<String> strings = new ArrayList<>();

        strings.add(httpRequest.getRequestLine().getMethod());
        strings.add(httpRequest.getRequestLine().getProtocolVersion().toString());

        String fullUrl =
                ((Function<String, String>) (String o) -> {
                    if (httpHost != null && !o.contains(httpHost.toString())) {
                        return httpHost.toString() + o;
                    }
                    return o;
                }).apply(httpRequest.getRequestLine().getUri());

        strings.addAll(encodeHeaders(httpRequest));
        strings.add(fullUrl);

        if (matchBodyRule(fullUrl)) {
            byte[] bytes = extractEntity(httpRequest);
            if (bytes != null) {
                if (Util.isText(bytes)) {
                    String bodyText = maskBody(new String(bytes, StandardCharsets.UTF_8));
                    strings.add(escapeCharacterString(bodyText));
                } else {
                    // omit changes fot binary data
                    // TODO: implement search substring for binary data
                    strings.add(Util.toCharacterString(bytes));
                }
            }
        }

        return strings;
    }

    public static List<String> encode(HttpRequest httpRequest) {
        return encode(httpRequest, null);
    }

    public static List<String> encodeHeaders(HttpRequest httpRequest) {

        boolean currentAllHeaders = HttpUtil.globalAllHeaders;

        AnySettingsHttp settings = AnySettingsHttpExtractor.discoverSettings();

        if (settings != null) {
            currentAllHeaders = settings.allHeaders();
        }

        Header[] currentHeaders = httpRequest.getAllHeaders();
        Arrays.sort(currentHeaders, Comparator.comparing(Header::getName));


        if (currentAllHeaders) {
            return stream(currentHeaders)
                    .map(HttpUtil::headerToString)
                    .collect(Collectors.toList());
        }


        Set<String> headersToAdd = new HashSet<>();
        if (settings != null) {
            headersToAdd.addAll(asList(settings.headers()));
        }
        if ((settings == null || !settings.overrideGlobal()) && globalHeaders != null) {
            headersToAdd.addAll(asList(globalHeaders));
        }

        return stream(currentHeaders)
                .filter(header -> headersToAdd.contains(header.getName()))
                .map(HttpUtil::headerToString)
                .collect(Collectors.toList());

    }


    private static boolean matchBodyRule(String url) {
        Set<String> currentBodyTriggers = new HashSet<>();

        AnySettingsHttp settings = AnySettingsHttpExtractor.discoverSettings();

        if (settings != null) {
            currentBodyTriggers.addAll(asList(settings.bodyTrigger()));
        }

        if ((settings == null || !settings.overrideGlobal()) && globalBodyTrigger != null) {
            currentBodyTriggers.addAll(asList(globalBodyTrigger));
        }


        return currentBodyTriggers.stream()
                .anyMatch(url::contains);
    }


    private static String maskBody(String s) {
        Set<String> currentBodyMask = new HashSet<>();

        AnySettingsHttp settings = AnySettingsHttpExtractor.discoverSettings();
        if (settings != null) {
            currentBodyMask.addAll(asList(settings.bodyMask()));
        }

        if ((settings != null || !settings.overrideGlobal()) && globalBodyMask != null) {
            currentBodyMask.addAll(asList(globalBodyMask));
        }

        return currentBodyMask.stream()
                .reduce(s, (r, m) -> r.replaceAll(m, "..."));
    }

    public static String headerToString(Header h) {
        return String.format("%s: %s", h.getName(), h.getValue());
    }

}
