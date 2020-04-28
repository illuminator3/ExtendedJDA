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

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User

@SuppressWarnings("unused" /* API */)
abstract
class Command
    implements Serializable
{
    private final String name,
                         description
    private final List<String> aliases

    Command(final String name, final String description, final List<String> aliases)
    {
        this.name = name
        this.description = description
        this.aliases = aliases
    }

    String getName()
    {
        return this.name
    }

    String getDescription()
    {
        return this.description
    }

    List<String> getAliases()
    {
        return this.aliases
    }

    abstract void onExecute(final JDA jda, final User user, final TextChannel channel, final String... args)
}