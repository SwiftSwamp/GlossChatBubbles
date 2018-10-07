package com.volmit.gloss.chatbubbles;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.volmit.gloss.api.GLOSS;
import com.volmit.gloss.api.intent.TemporaryDescriptor;
import com.volmit.volume.bukkit.VolumePlugin;
import com.volmit.volume.bukkit.command.CommandTag;
import com.volmit.volume.bukkit.pawn.Async;
import com.volmit.volume.bukkit.task.A;
import com.volmit.volume.bukkit.util.data.Edgy;
import com.volmit.volume.lang.format.F;
import com.volmit.volume.math.M;

@CommandTag("&8[&5GCB&8]:&7 ")
public class GlossChatBubbles extends VolumePlugin
{
	@Async
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void on(AsyncPlayerChatEvent e)
	{
		int m = 0;

		for(String i : F.wrapWords(e.getMessage(), 32).split("\n"))
		{
			new A(5 * m)
			{
				@Override
				public void run()
				{
					bubble(i, e.getPlayer());
				}
			};
			m++;
		}
	}

	@Edgy
	@Async
	private void bubble(String msg, Player p)
	{
		TemporaryDescriptor d = GLOSS.getSourceLibrary().createTemporaryDescriptor("chat-" + p.getUniqueId() + "-" + M.ms() + UUID.randomUUID().toString(), p.getEyeLocation().clone().add(0, 1, 0), 5000);
		d.addLine("&s&7" + msg);
		int trk = GLOSS.getContextLibrary().getView(p).getTrackedBubbles();

		d.bindPosition(() ->
		{
			int size = GLOSS.getContextLibrary().getView(p).getTrackedBubbles();
			int index = trk;
			double h = GLOSS.getIntentLibrary().getStackSpread() * ((size + index));
			double g = GLOSS.getIntentLibrary().getStackSpread() * (index);
			double m = size * GLOSS.getIntentLibrary().getStackSpread();
			double f = Math.min(index, size) * GLOSS.getIntentLibrary().getStackSpread();
			double j = Math.max((m) - (((m / 2D) - ((h + g) / 2D)) + (f * 2D)), 0);
			double v = d.getHealth() < 2000 ? (Math.pow(1D - ((double) d.getHealth() / 2000D), 16) * 10D) : 0;

			return p.getEyeLocation().clone().add(0, 0.86 + j + v, 0);
		});

		d.setLocation(p.getEyeLocation());

		GLOSS.getContextLibrary().getView(p).setTrackedBubbles(GLOSS.getContextLibrary().getView(p).getTrackedBubbles() + 1);

		new A(20 * 5)
		{
			@Override
			public void run()
			{
				GLOSS.getContextLibrary().getView(p).setTrackedBubbles(GLOSS.getContextLibrary().getView(p).getTrackedBubbles() - 1);
			}
		};

		GLOSS.getSourceLibrary().register(d);
	}
}
