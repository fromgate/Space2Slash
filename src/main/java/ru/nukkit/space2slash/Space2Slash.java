package ru.nukkit.space2slash;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class Space2Slash extends PluginBase implements Listener {
    private ArrayList<String> history = new ArrayList<String>();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    static Iterable<String> reversed(final ArrayList<String> original) {
      return new Iterable<String>() {
        public Iterator<String> iterator() {
          final ListIterator<String> i = original.listIterator(original.size());
          return new Iterator<String>() {
              public boolean hasNext() { return i.hasPrevious(); }
              public String next() { return i.previous(); }
              public void remove() { i.remove(); }
          };
        }
      };
    }
    
    String preprocessCommand(String message) {
      if (message.startsWith("!") && !history.isEmpty()) {
        if (message.equals("!!")) {
          return history.get(history.size() - 1);
        }
        String prefix = message.substring(1);
        if (Character.isDigit(prefix.codePointAt(0))) {
          try {
            int index = Integer.parseInt(prefix);
            if (index > 0 && index <= history.size()) {
              return history.get(index - 1);
            }
          } catch (NumberFormatException ex) {
          }
        } else {
          for (String command : reversed(history)) {
            if (command.startsWith(prefix)) {
              return command;
            }
          }
        }
      }
      if (message.startsWith(" ")) {
        message = message.trim();
        history.add(message);
        return message;
      }
      return null;
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onChatCommand (PlayerChatEvent event) {
        String message = preprocessCommand(event.getMessage());
        if (message != null) {
          this.getServer().dispatchCommand(event.getPlayer(), message);
          event.setCancelled();
        }
    }

    // This handler will be removed after fixing #197
    @EventHandler (priority = EventPriority.LOWEST)
    public void onCommandPreprocess (PlayerCommandPreprocessEvent event) {
        String message = preprocessCommand(event.getMessage());
        if (message != null) {
          this.getServer().dispatchCommand(event.getPlayer(), message);
          event.setCancelled();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      boolean accept = false;
      switch (command.getName()) {
      case "history":
        for (String arg : args) {
          switch (arg) {
          case "--clear": case "-c":
            history.clear();
            accept = true;
            break;
          }
        }
        if (!accept) {
          ListIterator<String> i = history.listIterator();
          while (i.hasNext()) {
            int index = i.nextIndex() + 1;
            String name = i.next();
            sender.sendMessage(TextFormat.DARK_GREEN + "[" + index + "] " + name);
          }
          accept = true;
        }
        break;
      }
      return accept;
    }
}
