package de.marek.project1;

import javax.servlet.Servlet;

public class Route {

    public final Servlet servlet;
    public final String endpoint;

    public Route(String endpoint, Servlet servlet) {
        this.endpoint = endpoint;
        this.servlet = servlet;
    }
}
