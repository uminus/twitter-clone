# Twitter clone

Twitter clone with Kotlin.

## Features

- [ ] Signup, Login, Logout
- [ ] Tweet
- [ ] Follow users
- [ ] Timeline(home, users)
- [ ] Like tweet
- [ ] Profile Page

## Models

```mermaid
classDiagram
  class User {
    +String id
    +String profile
    +Array~Tweet~ tweets
    +Array~User~ followers
  }
  class Tweet {
    +String: id
    +Array~User~ likes
  }

  User --> Tweet: tweet
  Tweet --> User: like
  User --> User: follower
```

## APIs

use GraphQL.