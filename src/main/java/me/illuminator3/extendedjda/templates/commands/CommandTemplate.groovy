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

import me.illuminator3.extendedjda.commands.CommandHandler
import me.illuminator3.extendedjda.templates.Template

@FunctionalInterface
interface CommandTemplate
    extends Template<CommandHandler>
{ /* EMPTY */ }