// Link4D
// v1.0.0
// Created by CarbonGhost (Discord: https://discord.gg/rC38tvFSEU).
// A script which provides a two-way link between your Minecraft and Discord chat.
// Requires Discarpet (https://github.com/replaceitem/carpet-discarpet).

// The application ID for your bot.
global_bot_app_id = '987655240528588860';
// The ID of the channel you wish for the link to be setup in.
global_channel_id = '1010420378906140692';
// Translation text key determining which types of system messages are sent to Discord.
// Use `chat.type.text` to send chat messages, check the `en_us.json` language file for more keys.
global_system_message_whitelist = ['chat.type.text', 'multiplayer.player.joined', 'multiplayer.player.left'];
// The maximum number of messages that can be sent per tick. Don't change this unless you have a good reason.
global_max_messages_per_tick = 10;




// Script:
__config() -> {
  'scope' -> 'global',
  'bot'-> global_bot_app_id,
};

global_channel = dc_channel_from_id(global_channel_id);
global_recursions = 0;

__on_tick() -> global_recursions = 0;

__on_system_message(message, type) -> (
  // Prevent sending `n` number of messages per tick.
  if(global_recursions > global_max_messages_per_tick, return());
  global_recursions += 1;

  // Only send a message if it matches the system message type whitelist.
  if(global_system_message_whitelist ~ type != null,
    task(_(outer(message)) -> (
      dc_send_message(global_channel, { 'content' -> message, 'allowed_mentions' -> { 'mention_roles' -> false, 'mention_users' -> true, 'mention_everyone' -> false } });
    ));
  // else
    return();
  );
);

__on_discord_message(message) -> (
  if(message ~ 'user' ~ 'is_self', return());
  if(message ~ 'channel' ~ 'id' != global_channel ~ 'id', return());

  // Print the message for all players on the server.
  for(player('all'),
    // Print the message with the Discord user's role color.
    user_color = 'd';
    if(user_color == null, user_color = 'w');
    user_color = dc_get_user_color(message ~ 'user', message ~ 'server');
    if(user_color == null, user_color = '#FFFFFF');

    nickname = dc_get_display_name(message ~ 'user', message ~ 'server');

    print(
      _, // _ = current player in the loop.
      format(
        str('%s <%s>', user_color, nickname),
        str('^w Mention %s...', nickname),
        str('?%s ', message ~ 'user' ~ 'mention_tag')
      ) + format(str('w  %s', format_discord_message(message ~ 'readable_content')));
    );
  );
);

format_discord_message(message) -> (
  // TODO;
  return(message);
);
