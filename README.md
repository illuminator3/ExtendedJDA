# ExtendedJDA

Advanced JDA library for either event listening or command handling

### Examples

Using the CommandHandle class:
```java
import me.illuminator3.extendedjda.commands.*;
import java.util.Arrays;

public class MyClass {
    public static void commandTest(JDA api) {
        CommandHandler.setCommandPrefix("?");
        CommandHandler.setUnkownCommandMessage("Unknown command.");
        
        CommandHandler.registerCommand(new Command("say", "Says a message", Arrays.asList("tell", "msg")) {
            @Override
            public void onExecute(JDA jda, User user, TextChannel channel, String... args) {
                if (args.length == 0) {
                    channel.sendMessage("Requires arguments! Usage: !say <Message>");
        
                    return;
                }
        
                String message = IntStream.range(0, args.length).mapToObj(index -> args[index]).collect(Collectors.toList());
        
                channel.sendMessage(message).queue();
            }
        }, api);
    }
}
```
Example of the EventManager class:
```java
import me.illuminator3.extendedjda.events.*;
import me.illuminator3.extendedjda.events.filters.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MyClass {
    public static void eventTest(JDA api) {
        EventListener listener = new EventListener() {
            @Event(Priority.HIGHEST)
            public void onMessageReceived(GuildMessageReceivedEvent event) {
                if (event.getMessage().getContentRaw().replace(" ", "").contains("goodbot")) {
                    event.getChannel().sendMessage("Beep boop").queue();
                }
            }
        }

        EventManager.registerListener(listener, api);

        EventFilter antiBotFilter = (event, handler) -> event instanceof GuildMessageReceivedEvent && ((GuildMessageReceivedEvent) event).getAuthor().isBot();

        EventManager.registerFilter(antiBotFilter, api);
    }
}
```

## Built with

* [Groovy](https://groovy-lang.org/) - A multi-faceted language for the Java platform
* [JDA](https://github.com/DV8FromTheWorld/JDA/) - JDA (Java Discord API)
* [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details