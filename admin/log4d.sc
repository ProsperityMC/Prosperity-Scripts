// Log4D
// A small script which pushes logs of your choice to a Discord channel.
// Created by CarbonGhost.
// Requires Discarpet (https://github.com/replaceitem/carpet-discarpet).

// Your bot's application ID.
global_app_id = '000000000000000000';
// Your output channel ID.
global_channel_id = '000000000000000000';
// The events you would like to log.
// Takes Minecraft translation keys, find these in your Minecraft language file.
global_log_types = [
'chat.type.text',
'chat.type.emote',
'chat.type.announcement',
'chat.type.admin',
'chat.type.team.text',
'multiplayer.disconnect',
'multiplayer.player.joined',
'multiplayer.player.joined.renamed',
'multiplayer.player.left',
'commands.'
];




// Script:
__config() -> {
    'scope' -> 'global', 
    'bot' -> global_app_id,
    'requires' -> {
        'discarpet' -> '*',
    };
};
global_channel = dc_channel_from_id(global_channel_id);
global_executions = 0;
__on_tick() -> (
    global_executions = 0;
);
__on_system_message(text, type) -> (
    global_executions += 1;
    if(global_executions < 10,
        if(global_log_types ~ type != null,
            task(_(outer(text)) -> (
                dc_send_message(global_channel, text);
            )); 
        );
    );
);
