package ru.nukkit.space2slash;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.plugin.PluginBase;

public class Space2Slash extends PluginBase implements Listener {

    @Override
    public void onEnable(){
        this.getServer().getPluginManager().registerEvents(this,this);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onChatCommand (PlayerChatEvent event){
        if (!event.getMessage().matches("  \\S.*")) return;
        this.getServer().dispatchCommand(event.getPlayer(),event.getMessage().trim());
        event.setCancelled();
    }

    // This handler will be removed after fixing #197
    @EventHandler (priority = EventPriority.LOWEST)
    public void onCommandPreprocess (PlayerCommandPreprocessEvent event){
        if (!event.getMessage().matches("  \\S.*")) return;
        this.getServer().dispatchCommand(event.getPlayer(),event.getMessage().trim());
        event.setCancelled();
    }

}
