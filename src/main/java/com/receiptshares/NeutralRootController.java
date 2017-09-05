package com.receiptshares;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NeutralRootController {
    private static final Logger LOG = LoggerFactory.getLogger(NeutralRootController.class);

    @RequestMapping("/")
    public String index() {
        return "/html/index.html";
    }

    @RequestMapping("/{path:(?!(?:v1|html|css|fonts|images|js)).*}/**")
    public String forward() {
        return "forward:/";
    }
}
