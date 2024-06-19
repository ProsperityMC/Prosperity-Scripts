//// README //////////////////////////////////////////////////////////////////////////
// Hello, thanks for using the PoI script, you can find the source and latest       //
// updates here:                                                                    //
// https://github.com/ProsperityMC/Prosperity-Scripts                               //
//                                                                                  //
// Load, or reload the script with `/script load poi`                               //
//                                                                                  //
// If you wish to use the BlueMap integration then please follow these steps:       //
//                                                                                  //
//   1. Download BlueMap (https://modrinth.com/plugin/bluemap)                      //
//   2. Download BlueMap Marker Manager (https://modrinth.com/plugin/bmarker        //
//   3. Launch your world / server once with both mods installed                    //
//   4. Open the folder `/config/bluemap/maps`                                      //
//       4.1. Within that folder you should see a list of maps for your world       //
//            titled something like `world.conf` and `world_the_end.conf`           //
//       4.2. Open each of these files and change `name` to a different value       //
//            which you can remember                                                //
//   5. Then open the file `/<world>/scripts/poi.data/poi_config.json`              //
//       5.1. Enable the BlueMap integration                                        //
//       5.2. Map each dimension to the name of that map for your world.            //
//            This will allow players to create markers for that dimension          //
//   6. Start your server or enter your world                                       //
//   7. Load the script with `/script load poi`                                     //
//   8. Run `/poi map setup` and check your logs                                    //
//       8.1. If there are no logs from `BMM` the setup was successful              //
//       8.2. If logs read `Failed to access Map` then you have failed to           //
//            set a matching world name either in:                                  //
//              `/config/bluemap/maps/`                                             //
//              or `/<world>/scripts/poi.data/poi_config.json`                      //
//   9. Once you have fixed your configs run the setup command again                //
//                                                                                  //
// An unfortunate caveat to the BlueMap Marker integration is that the script       //
// is unable to report any errors that occur when adding or removing markers        //
// If you notice that BlueMap isn't updating properly make sure you check           //
// your configuration and reload the script. Most likely you named something        //
// incorectly.                                                                      //
//////////////////////////////////////////////////////////////////////////////////////
//// DEV NOTES ///////////////////////////////////////////////////////////////////////
// Program execution starts near the bottom of the file, denoted by "SCRIPT START"  //
// Functions that modify global variables begin and end with underscores: _func_()  //
//////////////////////////////////////////////////////////////////////////////////////

// Globals ------------------------- //

global_db = null;
global_db_ver = 0;
global_db_path = '/poi_data';
global_config = null;
global_config_ver = 0;
global_config_path = '/poi_config';
global_colors = {
    'white' -> 'w', 'yellow' -> 'y', 'magenta' -> 'm', 'red' -> 'r', 'cyan' -> 'c', 'lime' -> 'l',
    'light_blue' -> 't', 'dark_gray' -> 'f', 'gray' -> 'g', 'gold' -> 'd', 'purple' -> 'p',
    'brown' -> 'n', 'turquoise' -> 'q', 'green' -> 'e', 'navy_blue' -> 'v', 'black' -> 'k',
};


// Utils  -------------------------- //

// `msg` string
// @return ()
panic(msg) -> (
    alert = str('%s was exited because an invalid state was reached: %s', system_info('app_name'), msg);
    logger('fatal', alert);
    exit(alert);
    return();
);

// `name`  string of waypoint name
// @return Scarpet format
fmt_wp(name) -> (
    waypoint = global_db:'waypoints':name;
    pos = join(' ', waypoint:'position');
    dim = title(replace(waypoint:'dimension', '_', ' '));
    clr = global_colors:(waypoint:'color');
    onr = global_db:'players':(waypoint:'owner');

    return(format(
        str('%su %s', clr, name),
        str('^w %s\n%s in %s\nCreated by %s', name, pos, dim, onr),
        str('&%s', pos),
    ));
);

// `name`  string of waypoint name
// @return Scarpet format
fmt_map_link(name) -> (
    waypoint = global_db:'waypoints':name;
    url_root = global_config:'bluemap_marker_integration':'url';
    dim = waypoint:'dimension';
    pos = waypoint:'position';
    map = if(dim == 'overworld',
        replace(global_db:'bluemap':dim:'map', '_overworld', ''),
    // else
        global_db:'bluemap':dim:'map';
    );

    url = str('%s/#%s:%d:%d:%d:800:0:0:0:0:flat', url_root, map, pos:0, pos:1, pos:2);
    
	return(format(
        str('cu View %s on BlueMap →', name),
        str('@%s', url),
    ));
);


// Config file --------------------- //

// @return ()
_config_init_() -> (
    config = read_file(global_config_path, 'json');

    if(config == null || config == { },
        write_file(
            global_config_path,
            'json',
            {
                'data_version' -> global_db_ver,
                'bluemap_marker_integration' -> {
                    'enabled' -> false,
                    'url'  -> 'http://localhost:8100',
                    'maps' -> {
                        'overworld'  -> 'change_me',
                        'the_nether' -> 'change_me',
                        'the_end'    -> 'change_me',
                    },
                },
                'can_add_waypoint_permission_level'        -> 0,
                'can_remove_own_waypoint_permission_level' -> 0,
            },
        );
        config = read_file(global_config_path, 'json');
        logger('info', 'No PoI config was found, or the file was empty, so a new one was generated');
    );

    config_ver = config:'data_version';
    if(config_ver != global_config_ver,
        db_migrate(config_ver, global_config_ver);
    );

    global_config = config;

    return();
);

// `ver_from` int
// `ver_to`   int
// @return ()
config_migrate(ver_from, ver_to) -> (
    panic('Config migrations not implemented, if you hit this you probably changed something you shouldn\'t have');
    return();
);


// Database operations ------------- //

// @return ()
_db_init_() -> (
    db = read_file(global_db_path, 'json');

    if(db == null || db == { },
        write_file(
            global_db_path,
            'json',
            {
                'data_version' -> global_db_ver,
                'players'      -> { },
                'waypoints'    -> { },
                'bluemap_sets' -> { },
            },
        );
        db = read_file(global_db_path, 'json');
        logger('info', 'No PoI database was found, or the file was empty, so a new one was generated');
    );

    db_ver = db:'data_version';
    if(db_ver != global_db_ver,
        db_migrate(db_ver, global_db_ver);
    );

    global_db = db;
    _db_sync_();

    return();
);

// @return ()
_db_sync_() -> (
    // Add new players to the UUID map
    for(filter(player('all'), global_db:'players':(_~'uuid') == null),
        global_db:'players' = global_db:'players' + { _~'uuid' -> _ };
    );

    // Update entries of players who have changed usernames
    for(filter(player('all'), global_db:'players':(_~'uuid') != _),
        delete(global_db:'players', _~'uuid');
        global_db:'players' = global_db:'players' + { _~'uuid' -> _ };
    );

    write_file(global_db_path, 'json', global_db);
    return();
);

// `ver_from` int
// `ver_to`   int
// @return ()
db_migrate(ver_from, ver_to) -> (
    panic('Database migrations not implemented, if you hit this you probably changed something you shouldn\'t have');
    return();
);


// Command handling ---------------- //

// @return ()
command_help() -> (
    messages = [
        ['help',            'Prints this message'],
        ['reload',            'Allows admins to reload the database and config'],
        ['add',                'Create a new waypoint'],
        ['remove',            'Remove one of your waypoints'],
        ['list',            'List waypoints'],
        ['map',                'Get a link to the web map'],
        ['map link',        'Get a link to a specific waypoint in the web map'],
        ['map setup',        'Set up the beb map integration'],
        ['edit name',        'Edit the name of a waypoint'],
        ['edit color',        'Edit the color of a waypoint'],
        ['edit position',    'Edit the position of a waypoint'],
    ];

    print(player(),
        str('Points-of-Interest (%s.sc) available commands:\n', system_info('app_name'))
        + '  ' + join('\n  ',
            map(messages, str('/%s %s - %s', system_info('app_name'), _:0, _:1));
        );
    );

    return();
);

// @return  ()
_command_reload_() -> (
    if(player()~'permission_level' < 4,
        print(player(), format('r You don\'t have permission to do that'));
        return();
    );

    _config_init_();
    _db_init_();
    print(player(), 'The config and database have been reloaded');

    return();
);

// `pos`    [int, int, int]
// `color`  string of color
// `name`   string
// @return  ()
_command_add_(pos, color, name) -> (
    wp = global_db:'waypoints';
    time = unix_time();
    owner = player()~'uuid';
    dim = player()~'dimension';

    if(player()~'permission_level' < global_config:'can_add_waypoint_permission_level',
        print(player(), format('r You don\'t have permission to do that'));
        return();
    );
    if(wp:name != null,
        print(player(), 'Waypoint ' + fmt_wp(name) + ' already exists');
        return();
    );

    if(global_config:'bluemap_marker_integration':'enabled' == true,
        set = global_db:'bluemap':dim:'marker_set';

        run('bmarker create poi');
        run(str('bmarker-setup id %s', time));
        run(str('bmarker-setup label %s', name));
        run(str('bmarker-setup marker_set %s', set));
        run(str('bmarker-setup position %d %d %d', pos));
        run('bmarker-setup build');
    );

    global_db:'waypoints' = global_db:'waypoints' + {
        name -> {
            'created_time' -> time,
            'owner'          -> owner,
            'dimension'       -> dim,
            'position'       -> pos,
            'color'           -> color,
        },
    };
    _db_sync_();

    print(player(), 'Added new waypoint: ' + fmt_wp(name));

    return();
);

// `name`  string of waypoint key
// @return ()
_command_remove_(name) -> (
    wp = global_db:'waypoints';
    waypoint = wp:name;
    wp_allowed_remove = filter(wp, wp:_:'owner' == player()~'uuid');

    if(player()~'permission_level' < global_config:'can_remove_own_waypoint_permission_level',
        print(player(), format('r You don\'t have permission to do that'));
        return();
    );
    if(wp:name == null,
        print(player(), format('r This waypoint does not exist'));
        return();
    );
    if(wp_allowed_remove~name == null,
        print(player(), fmt_wp(waypoint) + ' ' + format('r cannot be removed by you'));
        return();
    );

    if(global_config:'bluemap_marker_integration':'enabled' == true,
        run(str('bmarker delete %s %s %s',
            global_db:'bluemap':(waypoint:'dimension'):'map',
            global_db:'bluemap':(waypoint:'dimension'):'set',
            waypoint:'created_time', // Because the ids of BM markers are the timestamp
        ));
    );

    delete(global_db:'waypoints', name);
    _db_sync_();

    print(player(), str('Removed the waypoint: %s', name));

    return();
);

// `name` string of waypoint key
// @return    ()
command_info(name) -> (
    waypoint = global_db:'waypoints':name;

    if(waypoint == null,
        print(player(), 'This waypoint does not exist');
        return();
    );

    pos   = join(' ', waypoint:'position');
    dim   = title(replace(waypoint:'dimension', '_', ' '));
    owner = global_db:'players':(waypoint:'owner');
    date  = convert_date(unix_time());

    print(player(), fmt_wp(name));
    print(player(), str('  %s in %s', pos, dim));
    print(player(), str('  Created by %s', owner));
    print(player(), str('  Added on %s', join('-', [date:0, date:1, date:2])));
    if(global_config:'bluemap_marker_integration':'enabled' == true,
        print(player(), '  ' + fmt_map_link(name));
    );

    return();
);

// `name`     string of waypoint key
// `field`    string of 'name' | 'color' | 'position'
// `value`    string | string of color | [int, int, int]
// @return    ()
_command_edit_(name, field, value) -> (
    wp = global_db:'waypoints';
    waypoint = wp:name;
    wp_allowed_edit = filter(wp, wp:_:'owner' == player()~'uuid');

    if(player()~'permission_level' < global_config:'can_add_waypoint_permission_level',
        print(player(), format('r You don\'t have permission to do that'));
        return();
    );
    if(waypoint == null,
        print(player(), format(str('r %s does not exist', name)));
        return();
    );
    if(wp_allowed_edit~name == null,
        print(player(), fmt_wp(name) + ' ' + format('r cannot be edited by you'));
        return();
    );
    if(value == '' || value == null,
        print(player(), format('r The value provided is invalid'));
        return();
    );

    if(field == 'name',
        if(global_config:'bluemap_marker_integration':'enabled' == true,
            run(str('bmarker edit %s %s %s',
                global_db:'bluemap':(waypoint:'dimension'):'map',
                global_db:'bluemap':(waypoint:'dimension'):'set',
                waypoint:'created_time', // Because the ids of BM markers are the timestamp
            ));
            run(str('bmarker-setup label %s', value));
            run('bmarker-setup build');
            run('bmarker-setup cancel');
        );

        new_waypoint = copy(waypoint);
        delete(global_db:'waypoints', name);
        global_db:'waypoints' = global_db:'waypoints' +  { value -> new_waypoint };
        _db_sync_();

        print(player(), 'Updated ' + fmt_wp(value) + str(' (previous name was %s)', name));

        return();
    );

    if(field == 'color',
        prev_color = copy(global_db:'waypoints':name:'color');

        global_db:'waypoints':name:'color' = value;
        _db_sync_();

        print(player(), 'Updated the color of ' + fmt_wp(name) + str(' (previously %s)', prev_color));

        return();
    );

    if(field == 'position',
        prev_pos = copy(global_db:'waypoints':name:'position');

        if(global_config:'bluemap_marker_integration':'enabled' == true,
            run(str(
                'bmarker edit %s %s %s',
                global_db:'bluemap':(waypoint:'dimension'):'map',
                global_db:'bluemap':(waypoint:'dimension'):'set',
                waypoint:'created_time', // Because the ids of BM markers are the timestamp
            ));
            run(str('bmarker-setup position %d %d %d', value));
            run('bmarker-setup build');
            run('bmarker-setup cancel');
        );

        global_db:'waypoints':name:'position' = value;
        _db_sync_();

        print(player(), 'Updated the position of ' + fmt_wp(name) + str(' (previously %d %d %d)', prev_pos));

        return();
    );

    // We shouldn't reach this...
    print(player(), 'No valid field was provided so presumably nothing changed');

    return();
);

// `format` string of 'owner' | 'dimension' | 'color' | 'distance' | ''
// @return ()
command_list(format) -> (
    wp = global_db:'waypoints';

    if(format == 'owner',
        print(player(), 'unimplemented');
        return(),
    format == 'dimension',   // else if
        print(player(), 'unimplemented');
        return(),
    format == 'color',       // else if
        print(player(), 'unimplemented');
        return(),
    format == 'distance',    // else if
        print(player(), 'unimplemented');
        return(),
    );

    if(length(wp) == 0,
        print(player(), 'There are no waypoints');
        return();
    );

    print(player(), str('There %s %d waypoint%s:',
        if(length(wp) > 1, 'are', 'is'),
           length(wp),
        if(length(wp) > 1, 's', ''),
    ));
    map(keys(wp), print(player(), fmt_wp(_)));

    return();
);

// @return ()
command_map() -> (
    if(global_config:'bluemap_marker_integration':'enabled' != true,
        print(player(), format('r That feature is disabled in the config'));
        return();
    );

    print(player(), format('cu View BlueMap →', str('@%s', global_config:'bluemap_marker_integration':'url')));

    return();
);

// `name` string of waypoint key
// @return ()
command_map_link(name) -> (
    if(global_config:'bluemap_marker_integration':'enabled' != true,
        print(player(), format('r That feature is disabled in the config'));
        return();
    );
    if(global_db:'waypoints':name == null,
        print(player(), 'That waypoint does not exist');
        return();
    );

    print(player(), fmt_map_link(name));

    return();
);

// @return ()
_command_map_setup_() -> (
    if(player()~'permission_level' < 4,
        print(player(), format('r Only OPs have permission to do that'));
        return();
    );
    if(global_config:'bluemap_marker_integration':'enabled' != true,
        print(player(), format('r That feature is disabled in the config'));
        return();
    );
    if(system_info('world_carpet_rules') : 'commandScriptACE' != 4,
        print(player(), format('r You need to update your Carpet configuration to use this feature'));
        print(player(), format('cu [Enable Scripts ACE]', '?/carpet setDefault commandScriptACE 4'));
        return();
    );

    delete(global_db, 'bluemap_sets');
    global_db = global_db + { 'bluemap_sets' -> { } };
    _db_sync_();

    // In theory there should be error handling here, but
    // BlueMap Marker doesn't actually send errors in a way
    // that Scarpet can pick up, so there isn't
    _config_init_();
    for(global_config:'bluemap_marker_integration':'maps',
        id = 'scriptmanaged' + join('', split('_', _));
        map = global_config:'bluemap_marker_integration':'maps':_;
        label = 'Points of interest: ' + title(join(' ', split('_', _)));

        run('bmarker set-create');
        run(str('bmarker-setup-set id %s', id));
        run(str('bmarker-setup-set map %s', map));
        run(str('bmarker-setup-set label %s', label));
        run('bmarker-setup-set build');
        // Exit case so we don't get stuck
        run('bmarker-setup-set cancel');

        global_db:'bluemap' = global_db:'bluemap' + {
            _ -> {
                'map' -> map,
                'set' -> id,
                'marker_set' -> id + '_' + map,
            },
        };
        _db_sync_();
    );

    print(player(), 'BlueMap Marker setup complete, please check your console or logs to confirm if it has been a success, if a `BMM` log is present informing you that anything failed, check your PoI config and rerun this command');

    return();
);


// Scarpet config ------------------ //

__config() -> {
    'scope' -> 'global',
    'commands' -> {
        'help'                     -> _() -> command_help(),
        'reload'                   -> _() -> _command_reload_(),
        'add <pos> <color> <name>' -> _(pos, color, name) -> _command_add_(pos, color, name),
        'remove <owned_waypoint>'  -> _(waypoint) -> _command_remove_(waypoint),
        'info <waypoint>'          -> _(waypoint) -> command_info(waypoint),
        'list'                     -> _() -> command_list(''),
        'map'                      -> _() -> command_map(),
        'map link'                 -> _() -> command_map(),
        'map link <waypoint>'      -> _(waypoint) -> command_map_link(waypoint),
        'map setup'                -> _() -> _command_map_setup_(),
        'edit name <owned_waypoint> to <name>'    -> _(waypoint, value) -> _command_edit_(waypoint, 'name', value),
        'edit color <owned_waypoint> to <color>'  -> _(waypoint, value) -> _command_edit_(waypoint, 'color', value),
        'edit position <owned_waypoint> to <pos>' -> _(waypoint, value) -> _command_edit_(waypoint, 'position', value),
    },
    'arguments' -> {
        'pos' ->     { 'type' -> 'pos' },
        'name' ->    { 'type' -> 'text',   'suggest' -> [''] },
        'color' ->   { 'type' -> 'term',   'options' -> keys(global_colors) },
        'format' ->  { 'type' -> 'string', 'options' -> ['owner', 'dimension', 'color', 'distance'] },
        'waypoint'-> { 'type' -> 'text',   'suggester' -> _(args) -> ( keys(global_db:'waypoints') ) },
        'owned_waypoint'-> {
            'type' -> 'string',
            'suggester' -> _(args) -> (
                wp = global_db:'waypoints';
                map(filter(wp, wp:_:'owner' == player()~'uuid'), str('"%s"', _));
            ),
        },
    },
};


// SCRIPT START -------------------- //

_config_init_();
_db_init_();

for(player('all'),
    if(global_config:'bluemap_marker_integration':'enabled' == true
    && player(_)~'permission_level' == 4
    && system_info('world_carpet_rules'):'commandScriptACE' != 4,
        print(_, format('r You need to update your Carpet configuration to use the BlueMap Marker integration or it will misbehave'));
        print(_, format('cu [Enable Scripts ACE]', '?/carpet setDefault commandScriptACE 4'));
    );
);


// SCRIPT END -----------------------//
 