# The Space-X Challenge

## 1. Introduction

This development was done following this [challenge](https://doc.clickup.com/p/h/e12h-16043/f3e54f9ffd37f57) in the context of a job interview for [NaNLABS](https://www.nan-labs.com/) company.

## 2. Technologies

Java 11, Spring Boot, Lombok, GraphQL and Maven.

## 3. Cloning, configuration and deployment

In order to run a working copy of the application, you will need to follow these steps:

### 3.1. Clone the repository
```
$ git clone git@github.com:micaeprado/spacex-trello.git
$ cd spacex-trello
```
### 3.2. Configuration

Once you have deployed the application, you can use it following the next steps.

#### 3.2.1. Trello's API Key and Token configuration

To get started, you’ll need the Trello API Key and Token. 

You can get the API Key by logging in into Trello and visiting https://trello.com/app-key

Then you need to generate a token for yourself. On the same page where you found your [API key](https://trello.com/app-key), click the hyperlinked "Token" under the API key. Finally, you must accept the terms and you will be redirected to the page that has the token.

You need to configure them in the `application.yml` file, `trello.key` and `trello.token` properties.

```
trello:
  key: 
  token: 
  board:
    id: 
  list:
    to-do: 
    other: 
```

#### 3.2.2. Board configuration
You have to create a new board or use an existing one, from the Trello web page.

Then, you need to get the boardID and set it into the property `trello.board`.

To get the boardID, you need call this Trello endpoint:

`GET "https://api.trello.com/1/members/{id}/boards?key={your_api_key}&token={your_token}"`
 
* `id` id or username of the member (Trello user)
* `your_api_key` API Key obtained in the previous step 4.1
* `your_token` Token obtained in the previous step 4.1
    
#### 3.2.3. Lists configuration
Create at least two lists: a ToDo list for the issue cards, and another one for the bugs and tasks cards.

To get the lists IDs, you need to call the next Trello endpoint:

`GET "https://api.trello.com/1/boards/{id}/lists?key={your_api_key}&token={your_token}"`

* `id` Board ID
* `your_api_key` API Key obtained in the previous step 4.1
* `your_token` Token obtained in the previous step 4.1

Copy the lists IDs, and configure them into the `application.yml` file: `trello.list.to-do` and `trello.list.other` properties.

### 3.3. Run the API
```
$ mvn spring-boot run
```

## 4. Use

As the application was developted with GraphQL, it uses the same URL for every API call.

POST `{baseUrl}/trello`

### 4.1 Creating an Issue

Body:
```
mutation {
    createIssue(input: {
        title: String,
        description: String
    }) {
        title
        description
    } 
}
```
 
### 4.2 Creating a Bug

Body:
```
mutation {
 createBug(input: {
     description: String
 }) {
     title
     description
 } 
}
```
  
### 4.3 Creating a Task

Body:
```
mutation {
    createTask(input: {
        title: String,
        category: CategoryEnum // ['MAINTENANCE', 'RESEARCH', 'TEST', 'BUG']
    }) {
        title
        categories
    } 
}
```

### 4.4 Get all Cards

Body:
```
{
    getAllCards {
        title
        description
        categories
    }
}
```

## 5. Decisions and notes

### 5.1. Technologies:

I make this project using Java and Spring Boot because they are the language and framework with which I feel more comfortable. So first of all, I created the base project using [Spring Initializr](https://start.spring.io/).

I decided to use GraphQL because, regarding the requirements, the cards should be created calling the same endpoint. 

### 5.2. DTOs

``` java
public class Card {
    private String title;
    private String description;
    private List<CategoryEnum> categories; //Enum [MAINTENANCE, RESEARCH, TEST, BUG]
}

public class CreateIssueInput {
    private String title;
    private String description;
}

public class CreateBugInput {
    private String description;
}

public class CreateTaskInput {
    private String title;
    private CategoryEnum category;
}
```

The Card DTO represents a Trello's board card. It is used in the API calls responses.

The CreateIssueInput, CreateBugInput and CreateTaskInput DTOs are used in the mutation operations (create cards) as request bodies.

### 5.3. Operations

#### 5.3.1 Mutations
To create the different cards there are three mutations:
  
```
type CardMutations {
    # Create an issue card
    createIssue(input: CreateIssueInput!): Card!

    # Create a bug card
    createBug(input: CreateBugInput!): Card!

    # Create a task card
    createTask(input: CreateTaskInput!): Card!
}
```
 
#### 5.3.2 Queries
Query to get all the cards on the board:

```
type CardQueries {
    getAllCards: [Card]
}
```

## 6. Author

This project was developed by [Micaela Prado](https://www.linkedin.com/in/micaela-prado-90b8a7145).
