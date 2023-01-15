package me.barret.utils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


//Entity Effect
//Packet ID	- State	- Bound To - Field Name	- Field Type - Notes
//  0x68	  Play	  Client	  Entity ID	   VarInt	    x
//                                 Effect ID	VarInt	  (look up ids for pot effects)
//                                 Amplifier    Byte	   Notchian client displays effect level as Amplifier + 1.
//                                 Duration 	VarInt	   Duration in ticks.
//                                  Flags	    Byte	    Bit field, see below.
//                             Has Factor Data - Boolean -  Used in DARKNESS effect
//                              Factor Codec -   NBT Tag -   See below

public class addLocalGlow {


    public static void addGlow(Player player) {
        player.sendMessage("within addGlow debug");


        player.sendMessage("line 0");
        ProtocolManager Manager = ProtocolLibrary.getProtocolManager();
        player.sendMessage("line 1");
        PacketContainer packet = Manager.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        player.sendMessage("line 2");
        packet.getIntegers().write(0, player.getEntityId()); //Set packet's entity id
        player.sendMessage("line 3");
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
        player.sendMessage("line 4");
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
        player.sendMessage("line 5");
        dataWatcher.setEntity(player); //Set the new data watcher's target
        player.sendMessage("line 6");
        dataWatcher.setObject(0, serializer, (byte) (0x40)); //Set status to glowing, found on protocol page
        player.sendMessage("line 7");

        if (true) {
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
        }

        Manager.sendServerPacket(player,packet);
        /* else {
        packet.getWatchableCollectionModifier()
                .write(0, dataWatcher.getWatchableObjects());
    }

        event.setPacket(packet);

     packet.getDataValueCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created // PROBLEMATIC LINE CAUSES CRASH


     player.sendMessage("sending packet");
            Manager.sendServerPacket(player, packet);
            player.sendMessage("done sending packet");

    }
    */
    }

}
