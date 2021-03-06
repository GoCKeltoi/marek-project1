package de.marek.project1.server;

import de.marek.project1.App;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import static com.google.common.base.Throwables.propagate;

class TomcatApp implements App {

    private final Tomcat t;

    public TomcatApp(Tomcat t) {
        this.t = t;
    }

    public void start() {
        try {
            t.start();
//            t.getServer().await();
        } catch (LifecycleException e) {
            propagate(e);
        }
    }

}
