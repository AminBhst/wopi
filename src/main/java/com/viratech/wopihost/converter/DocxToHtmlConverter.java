package com.viratech.wopihost.converter;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static com.viratech.wopihost.converter.HTMLConstants.*;

public class DocxToHtmlConverter {

    public static String convert(String fileName) throws Docx4JException, IOException {
        WordprocessingMLPackage mlPackage = WordprocessingMLPackage.load(new File(fileName));
        HTMLSettings htmlSettings = Docx4J.createHTMLSettings();
        htmlSettings.setWmlPackage(mlPackage);
        FileOutputStream os = new FileOutputStream("thisis.html");
        PhysicalFonts.addPhysicalFonts("BNazanin", Paths.get("/home/amin/Downloads/B_NAZANIN/B-NAZANIN.TTF").toUri().toURL());
        PhysicalFont physicalFont = PhysicalFonts.get("BNazanin");
        Mapper mapper = new IdentityPlusMapper();
        mapper.put("BNazanin", physicalFont);
        htmlSettings.setFontMapper(mapper);
        Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

        List<String> lines = Files.readAllLines(Paths.get("thisis.html"));
        return buildHtmlString(lines);
    }

    private static String buildHtmlString(List<String> lines) {
        StringBuilder html = new StringBuilder();
        correctHtmlLines(new LinkedList<>(lines)).forEach(line -> html.append("\n").append(line));
        return html.toString();
    }

    private static List<String> correctHtmlLines(LinkedList<String> lines) {
        boolean documentDivReached = false;
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            if (line.startsWith(STYLE_TAG)) {
                lines.set(i, String.format(STYLE_TAG_REPLACEMENT, styles));
                lines.remove(i + 1);
            } else if (line.contains(DOCUMENT_DIV) || documentDivReached) {
                documentDivReached = true;
                if (line.contains(EMPTY_STYLE) || (line.startsWith(OPEN_P) && !line.contains(SPAN_CLOSE))) {
                    lines.set(i, LINE_BREAK);
                }
            }
        }

        return lines;
    }


}
