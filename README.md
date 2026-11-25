# Hollow Knight — 2D Game Project

A desktop action platformer inspired by **Hollow Knight**, built with Java and [libGDX](https://libgdx.com/). Play as the Knight, explore connected rooms, fight enemies, navigate platforming challenges, and face the False Knight boss.

> This is an unofficial fan project. It is not affiliated with the creators of Hollow Knight.

## Features

- Knight movement, double jump, dash, wall slide, and directional Nail attacks
- Combat against Crawlid, Husk Hornhead, Winged Sentry, and Crystal Guardian enemies, plus the False Knight boss
- Mask based health and Soul; hold Focus to spend Soul and recover health
- Six Charms, with three normal equipment slots
- Tiled maps, Box2D physics and collision handling, and room transitions
- Main menu, in-game guide, inventory, achievements, and audio and brightness settings
- Four save slots that record location, health, Soul, play time, deaths, enemy kills, and achievements

## Requirements

- **JDK 25**, as configured in `build.gradle`
- A desktop environment with OpenGL support
- No separate Gradle installation; the repository includes the Gradle Wrapper

## Run the game

On Windows:

```powershell
git clone https://github.com/AliEs85ir/Hollo-Knight.git
cd Hollo-Knight
.\gradlew.bat :lwjgl3:run
```

On Linux or macOS, use `./gradlew :lwjgl3:run` instead. The first build may download dependencies. The Gradle run task uses `assets/` as its working directory, where the maps are located.

To build an executable JAR:

```powershell
.\gradlew.bat :lwjgl3:jar
```

The output is written to `lwjgl3/build/libs/`. The JAR packages the runtime dependencies and assets.

## Default controls

- **Left / Right Arrow:** Move
- **Up Arrow:** Jump; press again in the air for a double jump
- **Left Shift:** Dash
- **X:** Nail attack; hold `W` for an upward slash or `S` while airborne for a downward slash
- **A (hold):** Focus to heal using Soul
- **I:** Open or close the Charm inventory
- **Space:** Open the pause menu

These bindings come from `ControlManager` and `GameScreen`. The in-game guide currently lists `Esc` for pause, but the implemented key is `Space`.

### Debug shortcuts

- `Ctrl + S`: Teleport to the boss room
- `Ctrl + F`: Toggle free movement (Noclip)
- `Ctrl + E`: Restore one health Mask
- `Ctrl + D`: Refill Soul
- `Ctrl + G`: Toggle God Mode
- `Ctrl + T`: Equip all Charms

## Saving progress

Choose **Save & Exit** from the pause menu to save the current run and return to the main menu. Save slots are JSON files at `hollow_saves/slot_<number>.json`, relative to the application's working directory. When launched through Gradle, that directory is `assets/`. Audio and brightness settings are stored separately using libGDX Preferences.

## Project layout

```text
core/       Game logic, models, controllers, UI, and save service
lwjgl3/     Desktop launcher, run configuration, and JAR packaging
assets/     Tiled maps, sprites, animations, audio, and UI assets
gradle/     Gradle Wrapper files
```

The desktop entry point is `ir.Ali.hollowknightme.lwjgl3.Lwjgl3Launcher`. The project uses libGDX **1.14.2** and LWJGL **3.4.1**.

## Development

Compile the desktop module with:

```powershell
.\gradlew.bat :lwjgl3:compileJava
```

The main game source is under `core/src/main/java/ir/Ali/hollowknightme/`. The Tiled maps required by the game are in the root of `assets/`.

## Current limitations

- Some text in the in-game guide does not match the implemented controls or features. Use the controls above and the source code as the reference for this version.
- This repository does not contain a license file. Review the rights to the code and included visual and audio assets before redistributing them.
