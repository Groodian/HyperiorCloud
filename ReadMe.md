# Hyperior Cloud
This program is part of the `Hyperior Minecraft Server Network` project.
It is not meant to be run without the full setup of the different template servers: `lobby`, `proxy` and `minecraft party`

This repository contains the code of the program `Hyperior Cloud`.

This program manages the entire lifecycle of a template server, from copying and starting to stopping and deleting.

Logs are also collected and stored.

All communication between the started template servers is handled by this program.

All template servers are automatically scaled to ensure there are enough servers available for the number of players.

## About Hyperior
Hyperior is a Minecraft Server Network developed by Groodian,
with a focus on providing various mini-games and additional features.
Some of the features include a coin system, levels, leaderboards, personalized statistics, and cosmetics.

## Related Projects
Here is a list of all subprojects for the Hyperior Minecraft Server Network:

- [Hyperior Core](https://github.com/Groodian/HyperiorCore)
- [Hyperior Proxy](https://github.com/Groodian/HyperiorProxy)
- [Hyperior Cloud](https://github.com/Groodian/HyperiorCloud)
- [Hyperior Lobby](https://github.com/Groodian/HyperiorLobby)
- [Hyperior Cosmetics](https://github.com/Groodian/HyperiorCosmetics)
- [Hyperior Minecraft Party](https://github.com/Groodian/HyperiorMinecraftParty)

## Build
To compile this plugin, you need JDK 17 and an internet connection.

Clone this repo, run `./gradlew jar` from your terminal. You can find the compiled jar in the project root's `build/libs` directory.
