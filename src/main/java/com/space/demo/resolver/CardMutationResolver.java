package com.space.demo.resolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.space.demo.dto.Card;
import com.space.demo.dto.input.CreateBugInput;
import com.space.demo.dto.input.CreateIssueInput;
import com.space.demo.dto.input.CreateTaskInput;
import com.space.demo.service.external.TrelloService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CardMutationResolver implements GraphQLMutationResolver {

    public final TrelloService trelloService;

    public Card createIssue(CreateIssueInput input) {
        log.info("Creating a issue card for {}", input);
        return trelloService.createIssue(input);
    }

    public Card createBug(CreateBugInput input) {
        log.info("Creating a bug card for {}", input);
        return trelloService.createBug(input);
    }

    public Card createTask(CreateTaskInput input) {
        log.info("Creating a task card for {}", input);
        return trelloService.createTask(input);
    }
}
