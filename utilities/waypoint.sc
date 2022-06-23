// Waypoint
// v0.0.0
// Created by CarbonGhost (Discord: https://discord.gg/rC38tvFSEU).

// If true the script will log all reads and writes to the waypoint database.
global_enable_logging = true;
// If true the script will print all reads and writes to the waypoint database to the game chat.
global_enable_chat_logging = true;




// Script:
__config() -> {
    'scope' -> 'player',
    'command_permission' -> 1,
    'commands' -> {
        'add <position> <color> <name>' -> _(position, color, name) -> __waypoint_create(name, position, color, query(player(), 'dimension')),
        'remove'                        -> _() -> null,
        'query dimension <dimension>'   -> _(dimension) -> print(__waypoint_query('dimension', dimension)),
        'query name <name>'             -> _(name) -> print(__waypoint_query('name', name)),
        'query'                         -> _() -> print(__waypoint_query('all', null)),
        'debug dumpData'                -> _() -> print(global_db),
        'debug reload'                  -> _() -> run(str('script load %s', system_info('app_name'))),
    },
    'arguments' -> {
        'name' -> { 
            'type' -> 'text',
            'suggest' -> [''],
            // TODO: Add suggester here.
        },
        'position' -> { 'type' -> 'pos' },
        'color' -> { 
            'type' -> 'string',
            'options' -> ['white', 'yellow', 'magenta', 'red', 'cyan', 'lime', 'light_blue', 'dark_gray', 'gray', 'gold', 'purple', 'brown', 'turquoise', 'green', 'navy_blue', 'black'];
        },
        'dimension' -> {
            'type' -> 'text',
            'suggest' -> ['overworld', 'the_nether', 'the_end'],
            // TODO: Add suggester here.
        }
    },
};

__on_start() -> (
    __setup_db();
);




// Library:
global_data_version = 0;
global_db = {
    '_' -> 'Do not modify this file while the script is loaded!',
    'data_version' -> global_data_version,
    'waypoints' -> {},
};
// Map between vanilla-style color names and their Scarpet format code equivilents.
global_colors = {'white' -> 'w', 'yellow' -> 'y', 'magenta' -> 'm', 'red' -> 'r', 'cyan' -> 'c', 'lime' -> 'l', 'light_blue' -> 't', 'dark_gray' -> 'f', 'gray' -> 'g', 'gold' -> 'd', 'purple' -> 'p', 'brown' -> 'n', 'turquoise' -> 'q', 'green' -> 'e', 'navy_blue' -> 'v', 'black' -> 'k', };
global_path = '/waypoints';

// Sets up the database and syncs it to the 'global_db' variable.
// If the database is blank or does not exist, this function will create a new default one.
// TODO: If the database is using an outdated data-version it will attempt to fix it.
__setup_db() -> (
    if(read_file(global_path, 'shared_json') == null,
        __sync_db();
        
        __log('info', 'No waypoints database detected for this world, creating a blank one.'),
    // else
        if(read_file('/waypoints', 'shared_json') : 'data_version' != global_data_version,
            __log('error', 'Incorrect data version. Please migrate your waypoints database.');
            __log('error', 'Database migration is not implemented, try changing your "data_version" to %d.', global_data_version);
        );

        global_db = read_file(global_path, 'shared_json');
        __sync_db();
    );
);

// Syncronizes the database with the 'global_db' variable.
// Runs in an async 'task()' function that will fail if called as the server is shutting down.
__sync_db() -> (
    task(_() -> (
            write_file(global_path, 'shared_json', global_db);

            __log('info', 'Saved the database.');
        );
    );
);

// Takes the paramaters for a waypoint and adds it to the database.
// Returns the new waypoint.
// Takes a 'name', 'position', 'color', and 'dimension'.
// 'name' can be any string.
// 'position' must be a an array with 3 integers.
// 'color' can be any color key defined in 'global_colors'.
// 'dimension' can be any valid dimension ID.
__waypoint_create(name, position, color, dimension) -> (
    waypoint_id = str('%d%d%d%d%d%d%d-%d%d%d%d%d%d%d', map(range(16), floor(rand(10))));
    new_waypoint = {
        'data_version' -> global_data_version,
        'name' -> name,
        'position' -> position,
        'color' -> global_colors : color,
        'dimension' -> dimension,
        'creation_date' -> unix_time(),
        'creator' -> query(player(), 'uuid'),
    };

    // Workaround to create a new key in the database and assign it to 'new_waypoint'.
    global_db : 'waypoints' += waypoint_id;
    global_db : 'waypoints' : waypoint_id = new_waypoint;
    __sync_db();

    __log('info', str('%s created a new waypoint "%s" at %s in %s.', player(), new_waypoint : 'name', new_waypoint : 'position', new_waypoint : 'dimension'));

    return(new_waypoint);
);

__waypoint_delete(name) -> (
    return(null);
);

// Takes a waypoint 'field' and a 'query' value to match.
// Returns a list of results that match that value. Will return an empty array if none are found.
// TODO: This should probably return 'null' if no results are found.
__waypoint_query(field, query) -> (
    results = [];
    
    if(field == 'all' && query == null,
        map(filter(keys(global_db : 'waypoints'), global_db : 'waypoints'), results += _),
    // else
        map(filter(keys(global_db : 'waypoints'), global_db : 'waypoints' : _ : field == query), results += _);
    );

    return(results);
);

// Takes a 'log_type' and a 'message' and logs the output.
// Respects the user's logging configuration.
// Valid log types are 'info', 'warn', 'error', 'fatal', and 'debug'.
// 'message' can be any string.
__log(log_type, message) -> (
    if(global_enable_chat_logging == true,
        print(format('gi ' + str('[%s]: %s', title(log_type), message)));
    );
    if(global_enable_logging == true,
        logger(log_type, str('[Waypoints]: %s', message));
    );
);

// Takes a valid 'waypoint' and returns a 'format()' for displaying extra information in chat messages.
__waypoint_text_component(waypoint) -> (
    format(
        str('%su %s', waypoint : 'color', waypoint : 'name'),
        str('^w Waypoint in %s.\n%s', title(replace(waypoint : 'dimension', '_', ' ')), waypoint : 'position');
    );
);
