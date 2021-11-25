package com.space.demo.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.space.demo.client.dto.CardResponse;
import com.space.demo.client.dto.LabelResponse;
import com.space.demo.client.dto.MemberResponse;
import com.space.demo.dto.enumeration.CategoryEnum;
import com.space.demo.dto.input.CreateBugInput;
import com.space.demo.dto.input.CreateIssueInput;
import com.space.demo.dto.input.CreateTaskInput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Component
@Slf4j
public class TrelloClient {

    @Value("${trello.key}")
    private String key;

    @Value("${trello.token}")
    private String token;

    @Value("${trello.board}")
    private String boardId;

    @Value("${trello.list.to-do}")
    private String todoListId;

    @Value("${trello.list.other}")
    private String otherListId;

    private static final String CREATE_CARD = "https://api.trello.com/1/cards";
    private static final String GET_CARDS = "https://api.trello.com/1/boards/%s/cards";
    private static final String GET_LABELS = "https://api.trello.com/1/boards/%s/labels";
    private static final String GET_MEMBERS = "https://api.trello.com/1/boards/%s/members";

    public CardResponse createIssue(CreateIssueInput input) {
        HttpResponse<CardResponse> response;
        try {
            response = Unirest.post(CREATE_CARD)
                    .queryString("key", key)
                    .queryString("token", token)
                    .queryString("idList", todoListId)
                    .queryString("name", input.getTitle())
                    .queryString("desc", input.getDescription())
                    .asObject(CardResponse.class);
        } catch (UnirestException e) {
            log.error("Error trying to call {}", CREATE_CARD);
            throw new RestClientException(String.format("Error trying to call %s", CREATE_CARD), e);
        }
        log.info(String.format("Issue body: %s", response.getBody().toString()));

        return response.getBody();
    }

    public CardResponse createBug(CreateBugInput input) {
        HttpResponse<CardResponse> response;
        try {
            response = Unirest.post(CREATE_CARD)
                    .queryString("key", key)
                    .queryString("token", token)
                    .queryString("idList", otherListId)
                    .queryString("name", String.format("BUG-%s-%s", RandomStringUtils.randomAlphabetic(6), new Random().ints(100000, 999999).findFirst().getAsInt()))
                    .queryString("desc", input.getDescription())
                    .queryString("idLabels", this.getLabelByName(CategoryEnum.BUG.name()).getId())
                    .queryString("idMembers", this.getRandomMember().getId())
                    .asObject(CardResponse.class);
        } catch (UnirestException e) {
            log.error("Error trying to call {}", CREATE_CARD);
            throw new RestClientException(String.format("Error trying to call %s", CREATE_CARD), e);
        }

        log.info(String.format("Bug body: %s", response.getBody().toString()));

        return response.getBody();
    }

    public CardResponse createTask(CreateTaskInput input) {
        HttpResponse<CardResponse> response;
        try {
            response = Unirest.post(CREATE_CARD)
                    .queryString("key", key)
                    .queryString("token", token)
                    .queryString("idList", otherListId)
                    .queryString("name", input.getTitle())
                    .queryString("idLabels", this.getLabelByName(input.getCategory().name()).getId())
                    .asObject(CardResponse.class);
        } catch (UnirestException e) {
            log.error("Error trying to call {}", CREATE_CARD);
            throw new RestClientException(String.format("Error trying to call %s", CREATE_CARD), e);
        }

        log.info(String.format("Task body: %s", response.getBody().toString()));

        return response.getBody();
    }

    public List<CardResponse> getCards() {
        String uri = String.format(GET_CARDS, boardId);
        HttpResponse<CardResponse[]> response;
        try {
            response = Unirest.get(uri)
                    .queryString("key", key)
                    .queryString("token", token)
                    .asObject(CardResponse[].class);
        } catch (UnirestException e) {
            log.error("Error trying to call {}", uri);
            throw new RestClientException(String.format("Error trying to call %s", uri), e);
        }

        log.info(String.format("Cards of board with ID %s: %s", boardId, Arrays.toString(response.getBody())));

        return Arrays.asList(response.getBody());
    }

    private LabelResponse getLabelByName(String labelName) {
        List<LabelResponse> labels = this.getAllLabels();

        LabelResponse labelResponse = labels.stream()
                .filter(l -> labelName.equalsIgnoreCase(l.getName()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Label %s not found", labelName)));
        log.info("Label: " + labelResponse.toString());

        return labelResponse;
    }

    private MemberResponse getRandomMember() {
        List<MemberResponse> members = this.getMembers();
        return members.get(new Random().nextInt(members.size()));
    }

    private List<MemberResponse> getMembers() {
        String uri = String.format(GET_MEMBERS, boardId);
        HttpResponse<MemberResponse[]> response;
        try {
            response = Unirest.get(uri)
                    .queryString("key", key)
                    .queryString("token", token)
                    .asObject(MemberResponse[].class);
        } catch (UnirestException e) {
            log.error("Error trying to call {}", uri);
            throw new RestClientException(String.format("Error trying to call %s", uri), e);
        }

        log.info(String.format("Members of board with ID %s: %s", boardId, Arrays.toString(response.getBody())));

        return Arrays.asList(response.getBody());
    }

    public List<LabelResponse> getAllLabels() {
        String uri = String.format(GET_LABELS, boardId);
        HttpResponse<LabelResponse[]> response;
        try {
            response = Unirest.get(uri)
                    .queryString("key", key)
                    .queryString("token", token)
                    .asObject(LabelResponse[].class);
        } catch (UnirestException e) {
            log.error("Error trying to call {}", uri);
            throw new RestClientException(String.format("Error trying to call %s", uri), e);
        }

        log.info(String.format("Labels of board with ID %s: %s", boardId, Arrays.toString(response.getBody())));

        return Arrays.asList(response.getBody());
    }
}
