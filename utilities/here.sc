// Here
// A command which prints the player's location to chat.
// Created by CarbonGhost.

// The permission level needed in order to execute this command.
global_permission_level = 1;
// Number of decimal points to include. 
global_precision = 0;



// Script:
__config() -> {
    'scope' -> 'player',
    'command_permission' -> global_permission_level,
};
__command() -> (
    message = replace('x%.1f y%.1f z%.1f', '1', global_precision);
    pos = pos(player());
    // Account for the world scale difference.
    pos_overworld = pos(player()) * if(query(player(), 'dimension') == 'the_nether', [8, 1, 8], [1, 1, 1]);
    pos_nether = pos(player()) / if(query(player(), 'dimension') == 'overworld', [8, 1, 8], [1, 1, 1]);
    if(query(player(), 'dimension') != 'overworld' && query(player(), 'dimension') != 'the_nether',
        print(
            // Special case for The End and non-vanilla dimensions.
            format(
                'w ' + str(message, pos),
                str('^w %s in %s.\nClick to copy.', player(), replace(query(player(), 'dimension'), '(the_end)', 'The End')),
                str('&%s', str(message, pos));
            );
        ),
    // else
        print(
            format(
                'w ' + str(message + '; ', pos_overworld),
                str('^w %s in the Overworld.\nClick to copy.', player()),
                str('&%s', str(message, pos_overworld));
            ) +
            format(
                'g ' + str(message, pos_nether),
                str('^w %s in The Nether.\nClick to copy.', player()),
                str('&%s', str(message, pos_nether));
            );
        );
    );
    null; // Return null so the only output is content in print().
);
