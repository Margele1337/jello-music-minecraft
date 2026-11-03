# Jello-music

A Minecraft 1.7.10 Forge mod, currently a work in progress.

- Mod ID: `jellomusic`
- Package: `com.margele1337.jellomusic`

## Requirements

- Java 8 to run Minecraft 1.7.10
- Java 17 or newer for the Gradle build (configured automatically via toolchains)

## Build

```bash
./gradlew build
```

The mod jar is written to `build/libs/jellomusic-*.jar`.

## Development

```bash
./gradlew runClient     # Java 8 client
./gradlew runClient25   # Java 25 client with HotSwap support
```

## License

MIT
