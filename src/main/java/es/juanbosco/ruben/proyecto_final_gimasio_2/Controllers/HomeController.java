package es.juanbosco.ruben.proyecto_final_gimasio_2.Controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui/index.html";
    }

    @GetMapping(value = "/swagger-ui/index2.html", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String serveIndex2() throws IOException {
        Resource resource = new ClassPathResource("static/swagger-ui/index2.html");
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}
