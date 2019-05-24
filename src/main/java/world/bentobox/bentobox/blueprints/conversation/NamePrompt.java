package world.bentobox.bentobox.blueprints.conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.user.User;

public class NamePrompt extends StringPrompt {

    private GameModeAddon addon;

    public NamePrompt(GameModeAddon addon) {
        this.addon = addon;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        User user = User.getInstance((Player)context.getForWhom());
        return user.getTranslation("commands.admin.blueprint.management.name.prompt");
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        User user = User.getInstance((Player)context.getForWhom());
        // Convert color codes
        input = ChatColor.translateAlternateColorCodes('&', input);
        if (ChatColor.stripColor(input).length() > 32) {
            context.getForWhom().sendRawMessage("Too long");
            return this;
        }
        // Make a uniqueid
        StringBuilder uniqueId = new StringBuilder(ChatColor.stripColor(input).toLowerCase().replace(" ", "_"));
        // Check if this name is unique
        int max = 0;
        while (max++ < 32 && addon.getPlugin().getBlueprintsManager().getBlueprintBundles(addon).containsKey(uniqueId.toString())) {
            uniqueId.append("x");
        }
        if (max == 32) {
            context.getForWhom().sendRawMessage(user.getTranslation("commands.admin.blueprint.management.name.pick-a-unique-name"));
            return this;
        }
        context.setSessionData("uniqueId", uniqueId.toString());
        context.setSessionData("name", input);
        return new NameSuccessPrompt(addon);
    }

}

