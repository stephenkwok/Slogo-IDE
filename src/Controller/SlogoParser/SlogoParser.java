package Controller.SlogoParser;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class SlogoParser {
    private final Map<String, Pattern> mySymbols;

    public SlogoParser(String... bundles) {
        mySymbols = new HashMap<>();
        for (String bundle : bundles) {
            addPatterns(bundle);
        }
    }

    public void addPatterns(String syntax) {
        ResourceBundle resources = ResourceBundle.getBundle(syntax);
        Enumeration<String> iter = resources.getKeys();
        while (iter.hasMoreElements()) {
            String key = iter.nextElement();
            String regex = resources.getString(key);
            mySymbols.put(key, Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
        }
        mySymbols.remove("Command");
    }

    private String getSymbol(String text) {
        final String ERROR = "NO MATCH";
        for (Entry<String, Pattern> e : mySymbols.entrySet()) {
            if (match(text, e.getValue())) {
                return e.getKey();
            }
        }
        return ERROR;
    }

    private boolean match(String text, Pattern regex) {
        return regex.matcher(text).matches();
    }

    public List<Entry<String, String>> parseText(String input) {
        String WHITESPACE = "\\p{Space}";
        String[] text = input.split(WHITESPACE);
        List<Entry<String, String>> parsedText = new ArrayList<>();
        for (String s : text) {
            if (s.trim().length() > 0) {
                String symbol = getSymbol(s);
//                if (symbol.equals("Error")) {
//                    throw new Exception();
//                }
                parsedText.add(new SimpleEntry<>(symbol, s));
                System.out.println(String.format("%s : %s", symbol, s));
            }
        }
        System.out.println(parsedText);
        return parsedText;
    }

}