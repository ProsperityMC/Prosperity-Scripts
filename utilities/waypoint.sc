// Waypoint
// v0.0.0
// Created by CarbonGhost (Discord: https://discord.gg/rC38tvFSEU).

// If true the script will log all interactions with the waypoints database.
global_do_logs = true;




// Script:
__config() -> {
    'scope' -> 'player',
    'command_permission' -> 1,
    'commands' -> {
        'add <name> <coordinates> <color>' -> _(name, position, color) -> __waypoint_create(name, position, global_colors : color),
        'add <name> <coordinates>' -> _(name, position) -> __waypoint_create(name, position, 'w'),
        // 'remove <name>' -> _(),
        // 'query name <name>' -> _(),
        // 'query dimension <dimension>' -> _(),
        // 'query position <position>' -> _(),
        // 'modify name <name>' -> _(),
        // 'modify location <name> <position> -> _(),
        // 'modify color <name> <color>' -> _(),
        // 'list' -> _(),
        'database print' -> _() -> print(global_db),
        'database reset' -> _() -> (
            delete_file(global_path, 'shared_json'); 
            __setup();
        );
    },
    'arguments' -> {
        'name' -> { 
            'type' -> 'string',
            'suggest' -> ['"New waypoint"'],
        },
        'coordinates' -> { 'type' -> 'pos' },
        'color' -> { 
            'type' -> 'string',
            'options' -> ['white', 'yellow', 'magenta', 'red', 'cyan', 'lime', 'light_blue', 'dark_gray', 'gray', 'gold', 'purple', 'brown', 'turquoise', 'green', 'navy_blue', 'black'];
        },
    },
};

// Data version value for migrating databases if the format changes in the future.
global_data_version = 0;
// Variable for accessing the database.
// Initialized to the default database format.
global_db = {
    '_' -> 'DO NOT MODIFY THIS FILE WHILE THE SERVER IS RUNNING!',
    'data_version' -> global_data_version,
    'waypoints' -> [],
};
global_path = str('/%s/waypoints', system_info('world_name'));
// Map of color names and their Scarpet format eqivilents.
global_colors = {'white' -> 'w', 'yellow' -> 'y', 'magenta' -> 'm', 'red' -> 'r', 'cyan' -> 'c', 'lime' -> 'l', 'light_blue' -> 't', 'dark_gray' -> 'f', 'gray' -> 'g', 'gold' -> 'd', 'purple' -> 'p', 'brown' -> 'n', 'turquoise' -> 'q', 'green' -> 'e', 'navy_blue' -> 'v', 'black' -> 'k', };

__on_start() -> (
    __setup();
);

__on_stop() -> (
    __save_db();
);

__waypoint_create(name, position, color) -> (
    new_waypoint = {
        'data_version' -> global_data_version,
        'name' -> name,
        'position' -> position,
        'color' -> color,
        'dimension' -> query(player(), 'dimension'),
        'creation_date' -> unix_time(),
        'creator' -> query(player(), 'uuid'),
    };
    global_db : 'waypoints' += new_waypoint;
    __log('info', str('%s created a new waypoint.', player()));
    print('w Waypoint ' + __waypoint_text_component(new_waypoint) + ' created.');
    __save_db();
);

__waypoint_delete(name) -> (
    null;
);

__save_db() -> (
    task(_() -> (
            write_file(global_path, 'shared_json', global_db);
            __log('info', 'Saved the database.');
        );
    );
);

// Wrapper for the logger() function that respects the global_do_logs configuration.
__log(log_type, message) -> (
    if(global_do_logs == true,
        print(format('gi ' + str('[Debug]: %s', message)));
        logger(log_type, str('[Waypoints]: %s', message));
    );
);

// Rich text component for displaying waypoints in chat.
__waypoint_text_component(waypoint) -> (
    format(
        str('%s %s', waypoint : 'color', waypoint : 'name'),
        str('^w %s\n\nWaypoint in %s.\n%s', waypoint : 'name', waypoint : 'dimension', waypoint : 'position');
    );
);

// Function to verify the integrity of the database and sync it to global_db.
__setup() -> (
    if(read_file(global_path, 'shared_json') == null,
        __save_db();
        __log('info', 'No waypoints database detected for this world, creating a blank one.'),
    // else
        if(read_file(global_path, 'shared_json') : 'data_version' != global_data_version,
            __log('error', 'Incorrect data version. Please migrate your waypoints database.');
            __log('error', 'Database migration is not implemented, try changing your "data_version" to %d.', global_data_version);
        );
        global_db = read_file(global_path, 'shared_json');
        __save_db();
    );
);
