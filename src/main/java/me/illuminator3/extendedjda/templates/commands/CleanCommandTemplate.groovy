/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

package me.illuminator3.extendedjda.templates.commands

import me.illuminator3.extendedjda.commands.Command
import me.illuminator3.extendedjda.commands.CommandHandler
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User

final
class CleanCommandTemplate
    extends Command
    implements CommandTemplate
{
    CleanCommandTemplate()
    {
        super("clean", "Removes all messages from a channel", ["remove", "removeall", "cleanup"])
    }

    @Override
    void onExecute(JDA jda, User user, TextChannel channel, String... args)
    {
        channel.purgeMessages(channel.getIterableHistory().toList())
    }

    @Override
    void inject(JDA api, CommandHandler commandHandler)
    {
        commandHandler.registerCommand(this, api)
    }
}