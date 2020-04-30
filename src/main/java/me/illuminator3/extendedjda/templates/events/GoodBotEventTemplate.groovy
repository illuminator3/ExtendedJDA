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

package me.illuminator3.extendedjda.templates.events

import me.illuminator3.extendedjda.events.Event
import me.illuminator3.extendedjda.events.EventListener
import me.illuminator3.extendedjda.events.EventManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class GoodBotEventTemplate
    implements EventListener, EventTemplate
{
    @Event
    void onChat(final GuildMessageReceivedEvent e)
    {
        if (e.getMessage().getContentRaw().replace(" ", "").contains("goodbot")) e.getChannel().sendMessage("Beep boop").queue()
    }

    @Override
    void inject(JDA api, EventManager eventManager)
    {
        eventManager.registerListener(this, api)
    }
}