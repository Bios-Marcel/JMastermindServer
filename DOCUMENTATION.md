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

## Success
- `message` : String ⇨ Success message

## Error : Object
- `id` : Integer ⇨ Error ID
- `message` : String ⇨ Error message

## Authenticate
- `username` : String ⇨ Username
- `client` : String ⇨ Client information
- `version` : String ⇨ Known API version

## Session : Object
- `uuid` : String ⇨ Session UUID

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

## Report : GetUser
- `reason` : String ⇨ Report reason

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
## GET
- `/status` : UserSession ⇨ Get current status ⇨ Game | Lobby | Error
- `/game` : SearchGame ⇨ Search for a lobby or create a lobby ⇨ Game | Lobby | Error
- `/lobbies` : Filter ⇨ List available lobbies ⇨ Lobby[] | Error
- `/user` : GetUser ⇨ Get user information ⇨ User | Error
- `/users` : Filter ⇨ List users ⇨ User[] | Error

## POST
- `/auth` : Authenticate ⇨ Authenticate ⇨ Session | Error
- `/lobby` : CreateLobby ⇨ Create lobby ⇨ Lobby | Error
- `/report` : Report ⇨ Report user ⇨ Success | Error

## DELETE
- `/lobby` : UserSession ⇨ Close or leave lobby ⇨ Success | Error

## PATCH
- `/lobby` : JoinLobby ⇨ Join lobby ⇨ Lobby | Error
