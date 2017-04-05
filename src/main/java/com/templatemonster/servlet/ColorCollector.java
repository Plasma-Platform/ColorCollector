package com.templatemonster.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.templatemonster.pojo.json.JsonResponse;
import com.templatemonster.utils.Algorithm;
import com.templatemonster.utils.ColorReplacer;
import com.templatemonster.utils.Conf;
import com.templatemonster.utils.color.RGBCollector;
import com.templatemonster.utils.color.RGBItem;
import org.apache.commons.io.IOUtils;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColorCollector extends HttpServlet {

    private final Logger log = Logger.getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();

        try {

            String json = getString(request.getInputStream()).trim();
            log.debug("json=" + json);

            JsonResponse jsonResponse = mapper.readValue(json, JsonResponse.class);

            String md5 = jsonResponse.getMd5();

            String command = jsonResponse.getCommand();
            String url = jsonResponse.getData().getHref();

            String secretWord = Conf.getSecretWord();

            String md5Check = Algorithm.md5(command + url + secretWord).toUpperCase();

            if (!md5Check.equals(md5)) {
                throw new Exception("MD5 '" + md5 + "' is wrong. Must be '" + md5Check + "'");
            }

            if (!"collectImageColors".equals(command)) {
                throw new Exception("Wrong command '" + command + "'");
            }

            if (url == null || url.trim().isEmpty()) {
                throw new Exception("url is null");
            }

            json = collectImageColors(url);

            ServletOutputStream stream = response.getOutputStream();
            stream.print(json);

        } catch (Exception e) {
            e.printStackTrace();

            HashMap<String, String> error = new HashMap<>();
            error.put("error", "true");
            error.put("message", e.getMessage());

            ServletOutputStream stream = response.getOutputStream();
            stream.print(mapper.writeValueAsString(error));
        }
    }

    public static String getString(ServletInputStream sis) throws Exception {

        BufferedInputStream bin = null;

        String json = null;

        try {

            bin = new BufferedInputStream(sis);

            StringWriter writer = new StringWriter();

            IOUtils.copy(sis, writer);

            json = writer.toString();

        } finally {
            if (bin != null) {
                try {
                    bin.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (json == null) {
            throw new Exception("json is null");
        }

        return json;
    }

    private String collectImageColors(final String url) throws Exception {

        long t = System.currentTimeMillis();

        HashMap<RGBItem, List<RGBItem>> palette = (new RGBCollector()).collectFromURL(url);

        List<RGBItem> smallPaletteColors = new ArrayList<>(palette.keySet());

        HashMap<String, String> result = new HashMap<>();
        result.put("error", "false");
        result.put("message", "");

        for (RGBItem rgb : smallPaletteColors) {
            result.put(ColorReplacer.colorToNameMap.get(rgb.toString()), String.valueOf(Math.round(rgb.getPercentsOfPixelsWithWeightDivPixels() * 10)));
        }

        log.debug("collectImageColors(): " + (System.currentTimeMillis() - t) + "ms");

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(result);
    }
}
