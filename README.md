# NETtoPLUGIN

NETtoPLUGIN is a mindustry plugin that connects to discord and allows you to interact with your mindustry server.

Inspired (and technically cloned) by J-VdS discordplugin. You can check out his plugin [here!](https://github.com/J-VdS/DiscordPlugin)

## Building a plugin

This plugin only works on jdk15. Since gradle 6.6 will error when building with jdk17

Open this downloaded repository in command prompt or terminal. and run the following command
```bash
./gradlew jar
```

If you receive a permission denied error. Set the execution flag
```bash
chmod +x gradlew
```

## Usage

Put the .jar executable (It should be in **./build/libs**) to mindustry server(**whatever_your_mindustry_server_folder./config/mods**).

Use settings template for vast usage and automation (Its in **./settings_file/settings.json**)

Run mindustry server as normal
```bash
java -jar your_server_jar.jar
```

## Contributing
Pull requests are welcome. For something urgent. Or wanting to post criticisms related comments. Please open an issue first to discuss on what you want to have.

For contributions. Please make a pull request so that i can review and decide whether it should be added or not
