// Brand
// A command to apply return a player's client brand.
// Created by CarbonGhost.

// The permission level needed in order to execute this command.
global_permission_level = 4;




__config() -> {
    'scope' -> 'player',
    'command_permission' -> global_permission_level,
    'commands' -> {
        '<target>' -> _(target) -> print(player(), str('%s has a client brand of "%s".', target, query(player(target), 'client_brand')));
    },
    'arguments' -> {
        'target' -> {
            'type' -> 'term',
            'suggester' -> _(args) -> (
                names = {}; 
                for(player('all'), names += _);
                keys(names);
            ),
        }
    }
}
