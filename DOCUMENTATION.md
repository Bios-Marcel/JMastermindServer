# JMasterMindServer API documentation

# Explanation
- Session UUIDs (excluding `public`) are meant private

# Data exposure rules
- Public session UUIDs can be shared to any client
- Private session UUIDs are only shared to clients owning that session

# Message format
JSON, always of type `Message`

# Data types
## Message : Object
- `type` : String ⇨ Message type
- `message` : Object ⇨ Message

## Success : Object
- `message` : String ⇨ Success message

## Error : Object
- `id` : Integer ⇨ Error ID
- `message` : String ⇨ Error message

## Authenticate : Object
- `username` : String ⇨ Username
- `client` : String ⇨ Client information
- `version` : String ⇨ Known API version

## Session : Object
- `uuid` : String ⇨ Session UUID
- `lastDateTime` : String ⇨ Last request date and time

## UserSession : Object
- `sessionUUID` : String ⇨ Session UUID

## SearchGame : UserSession
- `name` : String ⇨ Lobby name
- `guessAttempts` : Integer ⇨ Allowed guess attempts

## CreateLobby : SearchGame
- `inviteOnly` : Boolean ⇨ Invite only

## JoinLobby : UserSession
- `lobbyUUID` : String ⇨ Lobby UUID to join

## GetUser : UserSession
- `userSessionUUID` : String ⇨ User session UUID

## Filter : UserSession
- `filter` : String ⇨ Search filter

## Lobby : Object
- `uuid` : String ⇨ Lobby UUID
- `participant` : User ⇨ Participant
- `creationDateTime` : String ⇨ Creation date and time

## Game : Object
- `opponent` : User ⇨ Opponent
- `turns` : Turn[] ⇨ Turns
- `winner` : User ⇨ Winner
- `creationDateTime` : String ⇨ Creation date and time

## Turn : Object
- `executionDateTime` : String ⇨ Turn execution date and time
- `colorCode` : Integer[] ⇨ Color code (0 = Red; 1 = Green; 2 = Blue; 3 = Purple)

## User : Object
- `sessionUUID` : String ⇨ Public session UUID
- `username` : String ⇨ Username

# Endpoints
## `/auth`
Authenticate user
### Request type
`Authenticate`
### Response type
`Session` or `Error`

## `/game`
Search for a lobby or create a lobby
### Request type
`SearchGame`
### Response type
`Game`, `Lobby` or `Error`

## `/lobby/close`
Close or leave lobby
### Request type
`UserSession`
### Response type
`Success` or `Error`

## `/lobby/create`
Create lobby
### Request type
`CreateLobby`
### Response type
`Lobby` or `Error`

## `/lobby/join`
Join lobby
### Request type
`JoinLobby`
### Response type
`Game`, `Lobby` or `Error`

## `/lobby/random`
Join or create a lobby
### Request type
`CreateLobby`
### Response type
`Game`, `Lobby` or `Error`

## `/status`
Get current status like current game state or lobby state
### Request  type
`UserSession`
### Response type
`Game`, `Lobby` or `Error`
