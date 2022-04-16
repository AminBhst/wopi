package com.viratech.wopihost.converter;

public class HTMLConstants {
    public static final String EMPTY_STYLE = "style=\"\"";
    public static final String SPAN_CLOSE = "</span>";
    public static final String OPEN_P = "  <p";
    public static final String STYLE_TAG = "--></style>";
    public static final String LINE_BREAK = "<br>";
    public static final String DOCUMENT_DIV = "<div class=\"document\">";
    public static final String styles =
            "<style>*{font-family:'B Yagut'}p,div{line-height: 2;}span{font-family:inherit;}</style>" +
                    "<style>.kateb_template_delivery_bcc{display:none}.kateb_template_delivery_cc{display:none}</style>" +
                    "<style>.delivery_hide_cc{display:none}</style>" +
                    "<style>.delivery_hide_bcc{display:none}</style>"+
                    "<style>.kateb_font_yagut{font-family:'B Yagut'}.kateb_font_titr{font-family:'B Titr'}.kateb_font_nazanin{font-family:'B Nazanin'}" +
                    ".kateb_font_lotus{font-family:'B Lotus'}.kateb_font_koodak{font-family:'B Koodak'}.kateb_font_zar{font-family:'B Zar'}</style>";

    public static final String STYLE_TAG_REPLACEMENT = "</style>\n" + "  \n" + "%s" + "</head>\n" + "<body>";
}
