# Twitter clone

Twitter clone with Kotlin.

## Features

- [ ] GraphQL API
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
    +String name
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

use GraphQL.