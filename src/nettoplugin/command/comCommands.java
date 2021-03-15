package nettoplugin.command;

//mindustry + arc
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.gen.Player;
import mindustry.gen.Call;

//javacord

import mindustry.world.modules.ItemModule;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import arc.util.*;
public class comCommands implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        Log.info("Hey there!");
    }
}
