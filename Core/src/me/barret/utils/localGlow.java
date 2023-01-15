package me.barret.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


//Entity Effect
//Packet ID	- State	- Bound To - Field Name	- Field Type - Notes
//  0x68	  Play	  Client	  Entity ID	   VarInt	    x
//                                 Effect ID	VarInt	  (look up ids for pot effects)
//                                 Amplifier    Byte	   Notchian client displays effect level as Amplifier + 1.
//                                 Duration 	VarInt	   Duration in ticks.
//                                  Flags	    Byte	    Bit field, see below.
//                             Has Factor Data - Boolean -  Used in DARKNESS effect
//                              Factor Codec -   NBT Tag -   See below



public class localGlow {


    public static void addGlow(Player player, int lvl) {




    for (Entity glowReceiver :player.getLocation().getWorld().getNearbyEntities(player.getLocation(),15,15,15))
    {
        if(glowReceiver instanceof Player)
                if (glowReceiver != player) {


                  //  player.sendMessage("within addGlow debug");


                    ProtocolManager Manager = ProtocolLibrary.getProtocolManager();

                    PacketContainer packet = Manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);

                    packet.getIntegers().write(0, glowReceiver.getEntityId()); //Set packet's entity id                 //not sure whether this is the person who sees glow or person who gets glow.

                    WrappedDataWatcher dataWatcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this

                    WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason

                    dataWatcher.setEntity(player); //Set the new data watcher's target

                    dataWatcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page


                    final List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();

                    for (final WrappedWatchableObject entry : dataWatcher.getWatchableObjects()) {
                        if (entry == null) continue;

                        final WrappedDataWatcher.WrappedDataWatcherObject watcherObject = entry.getWatcherObject();
                        wrappedDataValueList.add(
                                new WrappedDataValue(
                                        watcherObject.getIndex(),
                                        watcherObject.getSerializer(),
                                        entry.getRawValue()
                                )
                        );
                    }

                    packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);


                    Manager.sendServerPacket(player, packet);
                }
            }

    }

public static void removeLocalGlow(Player player, int lvl)
{

        //player.sendMessage("attempting glow remove");

    for (Player glowReceiver : Bukkit.getOnlinePlayers()) {





            ProtocolManager Manager = ProtocolLibrary.getProtocolManager();

            PacketContainer packet = Manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);

            packet.getIntegers().write(0, glowReceiver.getEntityId()); //Set packet's entity id                 //not sure whether this is the person who sees glow or person who gets glow.

            WrappedDataWatcher dataWatcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this

            WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason

            dataWatcher.setEntity(player); //Set the new data watcher's target

            dataWatcher.setObject(0, serializer, (byte) (0)); //REMOVES GLOW EFFECT


            final List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();

            for (final WrappedWatchableObject entry : dataWatcher.getWatchableObjects()) {
                if (entry == null) continue;

                final WrappedDataWatcher.WrappedDataWatcherObject watcherObject = entry.getWatcherObject();
                wrappedDataValueList.add(
                        new WrappedDataValue(
                                watcherObject.getIndex(),
                                watcherObject.getSerializer(),
                                entry.getRawValue()
                        )
                );
            }

            packet.getDataValueCollectionModifier().write(0, wrappedDataValueList);


            Manager.sendServerPacket(player, packet);


        }

    }
}



