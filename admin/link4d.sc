__config() -> {'scope'->'global','bot'->'987655240528588860'};

global_id = '988269663584682035';
global_channel = dc_channel_from_id(global_id);

global_rec = 0;

__on_tick() -> global_rec = 0;

__on_discord_message(message) -> (
    if(message~'channel'~'id'!=global_channel~'id',return()); //limit to chat channel only
    if(message~'user'~'is_self',return()); //ignore messages by the bot itself
    for(player('all'),
        col = 'd'; // this could be replaced with a custom way of fetching user color
        if(col == null,col = 'w');
        col = dc_get_user_color(message~'user',message~'server');
        if(col==null,col='#FFFFFF');
        print(_,format(str('%s [%s]',col,dc_get_display_name(message~'user',message~'server')))+format(str('w  %s',message~'readable_content')))
    );
);

__on_system_message(text,type) -> (
    if(global_rec > 10,return());
    global_rec += 1;
    if(!(type~'admin'),
        if((type~'commands.save.') == null, //don't send 'saving world' messages
            msg = parse_mentions(text,global_channel~'server'); //allow for pings from inside minecraft
            task(_(outer(msg)) -> dc_send_message(global_channel,msg)); //send to discord
        );
    );
);

parse_mentions(msg,server) -> (
    for(server~'users',
        msg = replace(msg,'@' + dc_get_display_name(_,server),_~'mention_tag');
    );
    msg = replace(msg, '@everyone','`@everyone`');
    msg;
);
