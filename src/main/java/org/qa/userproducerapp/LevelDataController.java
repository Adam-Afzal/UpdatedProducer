package org.qa.userproducerapp;

import com.google.gson.Gson;
import org.qa.userproducerapp.constants.Constants;
import org.qa.userproducerapp.dal.LevelRepository;
import org.qa.userproducerapp.model.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class LevelDataController {


    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private Gson gson;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/questions/getAll")
    private List<Level> getAllQuestions(){

        String questions = restTemplate.getForObject(Constants.QUESTIONS_API, String.class);
        log.info("getting all users. total {}", levelRepository.count());
        List<Level> levels = new ArrayList<>();
        //repo.findAll().forEach(levels::add);
        levelRepository.findAll().forEach(levels::add);
        //repo.findAll().forEach(level -> levels.add(level));

        return levels;
    }

    @RequestMapping(value = "/questions/{level}", method = RequestMethod.GET)
    private String getLevel(@PathVariable int level){

        String question = restTemplate.getForObject(Constants.QUESTIONS_API_LEVEL_SELECT + "{" + level + "}", String.class);
Gson gson  = new Gson();
        Level levelConv = gson.fromJson(question,Level.class);


        if(levelRepository.existsById(levelConv.getId())){


        } else{
            levelRepository.save(levelConv);
        }
        return question;




    }

}
