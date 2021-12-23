# Twitter clone

Twitter clone with Kotlin.

## Usage

```shell
export DB_URL="jdbc:h2:mem:twitter-clone;DB_CLOSE_DELAY=-1"
export DB_USER="USER"
export DB_PASSWORD="PASSWORD"
./gradlew run
```

## Features

- [ ] GraphQL API
  *[x] Signup, Login, Logout
  *[ ] Tweet
  *[ ] Follow users
  *[ ] Timeline(home, users)
  *[ ] Like tweet
- [ ] Profile Page

## Models

```mermaid
classDiagram
  class User {
    +String name
    +SHA256 password
    +String profile
  }
  class Tweet {
    +User user
    +String text
    +Datetime~UTC~ created_at
  }

  User --> Tweet: tweet
  Tweet --> User: Likes
  User --> User: Folowers
```

## APIs

```graphql
type Mutation {
  like(tweetId: String!): TweetData!
  login(name: String!, password: String!): UserData!
  logout(token: String!): Boolean!
  signup(name: String!, password: String!): UserData!
  tweet(text: String!): TweetData!
}

type Query {
  tweets(limit: Int, offset: Int, tweetIds: [String!], userIds: [String!]): [TweetData!]!
  users(id: String): [UserData!]!
}
```