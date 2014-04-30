package md.varoinform.view.demonstrator;

import md.varoinform.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Vladimir Borovic
 * Date: 4/11/14
 * Time: 10:11 AM
 */
public class TemplateRenderer {
    private String template;

    public TemplateRenderer(String template) {
        this.template = template;
    }

    public String render(Map<String, Object> context){
        String variableGroup = "singleVariable";
        String collectionGroup = "collection";
        String singleVariablePattern = "(?<" + variableGroup +">\\{\\{\\s*[a-zA-Z]+\\s*\\}\\})";
        String collectionPattern = "(?<" + collectionGroup +">\\{%\\s*\\[[a-zA-z]+\\]\\s*.+?\\s*%\\})";
        Pattern pattern = Pattern.compile(singleVariablePattern + "|" + collectionPattern);
        Matcher matcher = pattern.matcher(template);
        String result = template;
        while (matcher.find()) {
            String variable = matcher.group(variableGroup);

            result = replaceVariable(variable, result, context);
            String collection = matcher.group(collectionGroup);
            result = replaceCollection(collection, result, context);
        }
        return result;
    }


    private String replaceVariable(String variable, String template, Map<String, Object> context) {
        if (variable == null) return template;
        String name = getVariableName(variable);
        Object value = context.get(name);
        String result = value == null? "": StringUtils.valueOf(value);

        return template.replace(variable, result);
    }

    private String getVariableName(String variable) {
        String pattern = "\\{\\{\\s*(?<name>[a-zA-Z]+)\\s*\\}\\}";
        return getGroup(variable, pattern, "name");
    }

    private String replaceCollection(String collection, String template, Map<String, Object> context) {
        if (collection == null) return template;
        String name = getVariableCollectionName(collection);
        Object value = context.get(name);

        StringBuilder result = new StringBuilder();
        Collection<?> collectionValues;
        if (value instanceof Collection) {
            collectionValues = (Collection<?>) value;
        } else if (value != null) {
            collectionValues = Arrays.asList(value);
        } else {
            collectionValues = new ArrayList<>();
        }
        for (Object element: collectionValues) {
            Map<String, Object> map;
            if (element instanceof Map){
                //noinspection unchecked
                map = (Map<String, Object>) element;
            } else {
                map = new HashMap<>();
                map.put(name, element);
            }
            TemplateRenderer renderer = new TemplateRenderer(getCollectionTemplate(collection));
            result.append(renderer.render(map));
        }
        return template.replace(collection, result.toString());

    }

    private String getVariableCollectionName(String collection) {
        String pattern = "\\{%\\s*\\[(?<template>[a-zA-Z]+)\\]\\s*.*?\\s*%\\}";
        return getGroup(collection, pattern, "template");
    }

    private String getCollectionTemplate(String collection) {
        String pattern = "\\{%\\s*\\[[a-zA-Z]+\\]\\s*(?<template>.+)\\s*%\\}";
        return getGroup(collection, pattern, "template");
    }

    private String getGroup(String text, String pattern, String groupName) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(text);
        //noinspection ResultOfMethodCallIgnored
        matcher.find();
        return matcher.group(groupName);
    }


    public static void main(String[] args) {
        Map<String, Object> context = new HashMap<>();
        context.put("abc", "label title");
        context.put("url", Arrays.asList("http:\\google.com", "ru.wikipedia.org"));
        Map<String, String> c2 = new HashMap<>();
        c2.put("x", "first");
        c2.put("y", "second");
        context.put("group", c2);

        String result = new TemplateRenderer("{{ abc }} 123456 {{abc}} - {% [url] <a href='{{ url }}'>{{ url }}</a> %} " +
                "{% [group] {{x}} {{y}} {{x}} %} {{ urlv }}</a>").render(context);
        System.out.println(result);
    }
}
