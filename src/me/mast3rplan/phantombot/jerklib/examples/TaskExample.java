/* 
 * Copyright (C) 2015 www.phantombot.net
 *
 * Credits: mast3rplan, gmt2001, PhantomIndex, GloriousEggroll
 * gloriouseggroll@gmail.com, phantomindex@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package me.mast3rplan.phantombot.jerklib.examples;

import me.mast3rplan.phantombot.jerklib.ConnectionManager;
import me.mast3rplan.phantombot.jerklib.Profile;
import me.mast3rplan.phantombot.jerklib.Session;
import me.mast3rplan.phantombot.jerklib.events.IRCEvent;
import me.mast3rplan.phantombot.jerklib.events.IRCEvent.Type;
import me.mast3rplan.phantombot.jerklib.events.JoinCompleteEvent;
import me.mast3rplan.phantombot.jerklib.events.MotdEvent;
import me.mast3rplan.phantombot.jerklib.tasks.TaskImpl;

/**
 * An example of how to use tasks.
 *
 * @author mohadib
 */
public class TaskExample
{

    public TaskExample()
    {
        ConnectionManager conman = new ConnectionManager(new Profile("scripy"));
        Session session = conman.requestConnection("irc.freenode.net");


        /* Add a Task to join a channel when the connection is complete 
         This task will only ever be notified of ConnectionCompleteEvents */
        session.onEvent(new TaskImpl("join_channels")
        {
            public void receiveEvent(IRCEvent e)
            {
                e.getSession().join("#me.mast3rplan.phantombot.jerklib");
            }
        }, Type.CONNECT_COMPLETE);


        /* Add a Task to say hello */
        session.onEvent(new TaskImpl("hello")
        {
            public void receiveEvent(IRCEvent e)
            {
                JoinCompleteEvent jce = (JoinCompleteEvent) e;
                jce.getChannel().say("Hello from JerkLib!");
            }
        }, Type.JOIN_COMPLETE);



        /* Add a Task to be notified on MOTD and JoinComplete events */
        session.onEvent(new TaskImpl("motd_join")
        {
            public void receiveEvent(IRCEvent e)
            {
                if (e.getType() == Type.MOTD)
                {
                    MotdEvent me = (MotdEvent) e;
                    com.gmt2001.Console.out.println(me.getMotdLine());
                } else
                {
                    JoinCompleteEvent je = (JoinCompleteEvent) e;
                    je.getChannel().say("Yay tasks!");
                }
            }
        }, Type.MOTD, Type.JOIN_COMPLETE);



        /* Add a Task that will be notified of all events */
        session.onEvent(new TaskImpl("print")
        {
            public void receiveEvent(IRCEvent e)
            {
                com.gmt2001.Console.out.println(e.getRawEventData());
            }
        });

    }

    public static void main(String[] args)
    {
        new TaskExample();
    }
}
