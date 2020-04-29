/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 		Copyright 2020 illuminator3 aka Jonas Hardt

 		Licensed under the Apache License, Version 2.0 (the "License");
 		you may not use this file except in compliance with the License.
 		You may obtain a copy of the License at

 		   http://www.apache.org/licenses/LICENSE-2.0

 		Unless required by applicable law or agreed to in writing, software
 		distributed under the License is distributed on an "AS IS" BASIS,
 		WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 		See the License for the specific language governing permissions and
 		limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package me.illuminator3.extendedjda.commands

import me.illuminator3.extendedjda.utils.TypeHolder
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.EventListener

import java.util.stream.Collectors

@SuppressWarnings("unused" /* API */)
final
class CommandHandler
    implements EventListener, Serializable
{
    private static final List<Command>          REGISTERED_COMMANDS         = new ArrayList<>()
    private static String                       COMMAND_PREFIX              = "!" /* standard */
    private static TypeHolder                   UNKOWN_COMMAND_MESSAGE      = new TypeHolder("Unknown command. Use '!help' for help.", [MessageEmbed.class, String.class]) /* standard */
    private static boolean                      REGISTERED                  = false

    static
    void setCommandPrefix(final String commandPrefix)
    {
        COMMAND_PREFIX = commandPrefix
    }

    static
    void setUnkownCommandMessage(final MessageEmbed unkownCommandMessage)
    {
        UNKOWN_COMMAND_MESSAGE.set(unkownCommandMessage)
    }

    static
    void setUnkownCommandMessage(final EmbedBuilder unkownCommandMessage)
    {
        UNKOWN_COMMAND_MESSAGE.set(unkownCommandMessage.build())
    }

    static
    void setUnkownCommandMessage(final String unkownCommandMessage)
    {
        UNKOWN_COMMAND_MESSAGE.set(unkownCommandMessage)
    }

    static
    void registerCommand(final Command command, final JDA jda)
    {
        if (REGISTERED_COMMANDS.contains(command)) throw new UnsupportedOperationException("Command is already registered")

        if (!REGISTERED)
        {
            REGISTERED = true

            jda.addEventListener(this)
        }

        REGISTERED_COMMANDS.add(command)
    }

    @Override
    void onEvent(final GenericEvent event)
    {
        if (event instanceof GuildMessageReceivedEvent)
        {
            def e = (GuildMessageReceivedEvent) event
            def cmd = e.getMessage().getContentRaw()

            if (!cmd.startsWith(COMMAND_PREFIX)) return

            def possible = REGISTERED_COMMANDS
                    .stream()
                    .filter { c ->
                        c.getName().equalsIgnoreCase(cmd.substring(1)) || c.getAliases().contains(cmd.substring(1))
                    }
                    .collect(Collectors.toList())

            if (possible.isEmpty())
            {
                def type = UNKOWN_COMMAND_MESSAGE.getType()
                def object = UNKOWN_COMMAND_MESSAGE.get()

                if (type == MessageEmbed.class) e.getChannel().sendMessage(object as MessageEmbed).queue()
                else e.getChannel().sendMessage(object as String).queue()

                return
            }

            possible
                    .get(0)
                    .onExecute(
                        e.getJDA(),
                        e.getMember().getUser(),
                        e.getChannel(),
                        cmd.split(" ") .length <= 1 ?  new String[0] : System.arraycopy(cmd.split(" "), 1, [], 0, cmd.split(" ").length - 1)
                    )
        }
    }
}