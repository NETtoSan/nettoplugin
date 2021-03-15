# NETtoPLUGIN

NETtoPLUGIN is a mindustry plugin that connects to discord and allows you to interact with your mindustry server.

Inspired by J-VdS plugin. You can check out his plugin [here!](https://github.com/J-VdS/DiscordPlugin)

## Building a plugin

Make sure you have the latest JDK available (15 i guess) to compile the plugin or develop without issues.

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
