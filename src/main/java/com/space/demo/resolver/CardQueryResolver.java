package com.space.demo.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.space.demo.dto.Card;
import com.space.demo.service.external.TrelloService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CardQueryResolver implements GraphQLQueryResolver {

    public final TrelloService trelloService;

    public List<Card> getAllCards() {
        log.info("Getting all cards");
        return trelloService.getCards();
    }

}
