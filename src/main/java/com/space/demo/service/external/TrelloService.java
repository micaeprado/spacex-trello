package com.space.demo.service.external;

import com.space.demo.client.TrelloClient;
import com.space.demo.client.dto.CardResponse;
import com.space.demo.client.dto.LabelResponse;
import com.space.demo.dto.Card;
import com.space.demo.dto.enumeration.CategoryEnum;
import com.space.demo.dto.input.CreateBugInput;
import com.space.demo.dto.input.CreateIssueInput;
import com.space.demo.dto.input.CreateTaskInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrelloService {

    private final TrelloClient trelloClient;

    public Card createIssue(CreateIssueInput input) {
        CardResponse response = trelloClient.createIssue(input);

        return Card.builder()
                .title(response.getName())
                .description(response.getDesc())
                .build();
    }

    public Card createBug(CreateBugInput input) {
        CardResponse response = trelloClient.createBug(input);

        return Card.builder()
                .title(response.getName())
                .description(response.getDesc())
                .build();
    }

    public Card createTask(CreateTaskInput input) {
        CardResponse response = trelloClient.createTask(input);
        List<LabelResponse> labels = trelloClient.getAllLabels();

        return Card.builder()
                .title(response.getName())
                .categories(response.getIdLabels().stream().map(id -> labels.stream().filter(l -> id.equals(l.getId())).findAny().map(LabelResponse::getName)).filter(Optional::isPresent).map(name -> CategoryEnum.valueOf(name.get().toUpperCase())).collect(Collectors.toList()))
                .build();
    }

    public List<Card> getCards() {
        List<CardResponse> response = trelloClient.getCards();
        List<LabelResponse> labels = trelloClient.getAllLabels();

        return response.stream()
                .map(card -> Card.builder()
                        .title(card.getName())
                        .description(card.getDesc())
                        .categories(card.getIdLabels().stream().map(id -> labels.stream().filter(l -> id.equals(l.getId())).findAny().map(LabelResponse::getName)).filter(Optional::isPresent).map(name -> CategoryEnum.valueOf(name.get().toUpperCase())).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
