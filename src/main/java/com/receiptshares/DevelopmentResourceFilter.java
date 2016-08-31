package com.receiptshares;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
class DevelopmentResourceFilter implements Filter {

    private static final Log LOGGER = LogFactory.getLog(DevelopmentResourceFilter.class);

    private static String resourceLocation;
    private boolean enabled;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        resourceLocation = System.getProperty("resourceLocation");
        enabled = resourceLocation != null;
        LOGGER.debug("Development resource filter " + (enabled ? "enabled" : "disabled"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!enabled) {
            chain.doFilter(request, response);
            return;
        }
        String uri = ((HttpServletRequest) request).getRequestURI();
        Resource resource = Resource.find(uri);
        if (resource != null) {
            resource.writeResource(uri, (HttpServletResponse) response);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private enum Resource {
        HTML("html", "text/html"), JS("js", "application/x-javascript"), CSS("css", "text/css");
        private static final Log LOGGER = LogFactory.getLog(Resource.class);
        private String name;
        private String contentType;

        Resource(String name, String contentType) {
            this.name = name;
            this.contentType = contentType;
        }

        static Resource find(String uri) {
            String lowerCase = uri.toLowerCase();
            for (Resource r : values()) {
                if (lowerCase.endsWith(r.name)) {
                    return r;
                }
            }
            return null;
        }

        boolean writeResource(String uri, HttpServletResponse response) {
            response.setContentType(contentType + "; charset=utf-8");
            try {
                Path pathToResource = Paths.get(DevelopmentResourceFilter.resourceLocation + "/" + name + "/" + uri);
                response.getOutputStream().write(Files.readAllBytes(pathToResource));
                return true;
            } catch (IOException ioe) {
                LOGGER.error("Can't write resource " + uri, ioe);
                return false;
            }
        }
    }
}
