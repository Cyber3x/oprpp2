package hr.fer.oprpp2.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("initTime", System.currentTimeMillis());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
