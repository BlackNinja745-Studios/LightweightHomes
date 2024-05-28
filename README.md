# Lightweight Homes

Lightweight Homes is a simple, lightweight plugin to allow players to create homes!
Our focus is on providing only essential features, without extra complexity you'll never use, ensuring the plugin remains lightweight and highly performant.

![LightweightHomes Demo](https://github.com/BlackNinja745-Studios/LightweightHomes/blob/main/assets/demo.gif)

## Features

- `/home`, `/sethome`, and `/removehome` to use, create, and delete your home!
- Players are limited to one home each, simplifying usage.
- Fully configurable permissions.
- Operators can configure other players' homes.
- Extremely performant, homes are saved in [PersistentDataContainers](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/persistence/PersistentDataContainer.html) instead of config files.
- Simple code, reduced complexity leads to no bugs.

## Support
- Open an issue on [GitHub](https://github.com/BlackNinja745-Studios/LightweightHomes/issues)
- Ask for help on [Discord](https://discord.bn745studios.com)

## Commands
Command access can be configured with permissions. To configure other players' homes, `lightweighthomes.managehomes` is needed.

- `/home`: Teleports you back to your home, if it exists.
  - `/home [<player>]` teleports you to another player's home, `managehomes` required.
- `/sethome`: Creates your home at your current location.
  - `/sethome [<player>]` sets another player's home at your location, `managehomes` required.
  - `/sethome [<player>] [<x> <y> <z> <world>]` sets another player's house to a specified location, `managehomes` required.
- `/removehome`: Removes your home, if it exists.
  - `/removehome [<player>]` removes another player's home, `managehomes` required.

## Permisssions
All the functionality is controlled by permissions, so you can customize what players can do.
| Permission                     | Default | Description                                            |
|--------------------------------|---------|--------------------------------------------------------|
| `lightweighthomes.usehome`     | All     | Allows players to return to their home.                |
| `lightweighthomes.sethome`     | All     | Allows players to create a home.                       |
| `lightweighthomes.removehome`  | All     | Allows players to delete their home.                   |
| `lightweighthomes.managehomes` | OP      | Allows using, creating, and removing of other's homes. |
