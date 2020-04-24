package com.example.natterapi.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.logging.log4j.util.Strings.EMPTY;


@Slf4j
public final class HttpServletUtils {

    private HttpServletUtils() {
    }

    /**
     * Returns Request URI for the given request.
     *
     * @param request {@link HttpServletRequest}
     * @return Request URI.
     */
    public static String getRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * Returns Headers info for the given request.
     *
     * @param request {@link HttpServletRequest}
     * @return Map including HTTP Headers
     */
    public static Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    /**
     * Returns the HTTPRequest body if any.
     *
     * @param request {@link HttpServletRequest}
     * @return String containing HTTP Request Body.
     */
    public static String getRequestBodyAsString(HttpServletRequest request) {
        try {
            if (request.getInputStream().isFinished()) {
                return "Request Body already consumed or request has no body.";
            } else {
                return IOUtils.toString(request.getInputStream(), UTF_8);
            }

        } catch (IOException e) {
            log.error("Exception getting HttpServletRequest body as string", e);
            return EMPTY;
        }

    }

}
