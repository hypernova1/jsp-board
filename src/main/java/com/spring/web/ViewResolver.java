package com.spring.web;

public class ViewResolver {

    private String prefix;
    private String suffix;
    private String path;

    public ViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getPath() {
        if (path.equals("")) return "error/404";
        if (path.contains("redirect:")) return path;
        return prefix + path + suffix;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
