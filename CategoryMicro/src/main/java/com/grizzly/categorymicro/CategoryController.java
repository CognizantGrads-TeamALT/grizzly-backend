package com.grizzly.categorymicro;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;


@RestController
public class CategoryController {

    @RequestMapping("/")
    public String index() {
        String spam = new String();
        spam = "<br/>1 little bug in the code, 1 little bug, take it down, patch it around ";
        Random r = new Random();
        int i = 0;
        while(i < 100){
            i++;
            int temp = r.nextInt(1000);
            spam.concat(temp + " little bugs in the code<br/>" + temp + " little bugs in the code, " + temp + " little bugs, take one down, patch it around ");

        }
        spam.concat("<br/> finally no bugs in the code! <br/><br/><br/><br/> ...That's the wrong output.");


        return "Greetings from Spring Boot!" +
                "<br/>Lawrence's line (but added via pull request)." +
                "<br/>Jenni's Line" +           
                "<br/>Jonathon's line. "+
                "<br/> Jenni's second attempt" +
                "<br/>Steve's been here." +
                "<br/>Sophie was here too" +
                "<br/>Bentley's been here." +
                "<br/>Jonathon Returned, Team ALT > Team TS" +
                "<br/> trying to learning git flow" +
                spam;
    }

}
