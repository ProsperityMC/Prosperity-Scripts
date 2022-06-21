// Customize
// v1.0.0
// A command to apply custom-model-data to an item.
// Created by CarbonGhost (Discord: https://discord.gg/rC38tvFSEU).

// The permission level needed in order to execute this command.
global_permission_level = 1;
// The link that will be provided to the user in the '/customize help' command. 
global_help_message_link = '';




// Script:
__config() -> {
    'scope' -> 'player',
    'command_permission' -> global_permission_level,
    'commands' -> 
    {
        'model <model>' -> _(model) -> customize_item(model),
        'model reset' -> _() -> customize_item(0),
        'help' -> _() -> print(
            player(), format('w You can use /customize to apply the custom model data property to your items')
            + if(global_help_message_link != '', format('w  ', 'u Learn more', '@' + global_help_message_link)) + '.';
        ),
    },
    'arguments' -> {
        'model' -> { 'type' -> 'int', 'min' -> 0, 'max' -> 16000000 },
    }
};
customize_item(model) -> (
    item = inventory_get(player(), query(player(), 'selected_slot'));
    if(model < 0,
        print(player(), format('r A model cannot be a negative number.'));
    );
    if(item == null,
        print(player(), format('r You must be holding an item to execute this command.')),
    // else
        inventory_set(player(), query(player(), 'selected_slot'), 1, item : 0, str('{CustomModelData:%d}', model));
        if(model == 0,
            print(player(), str('Reset the model of your %s.', item : 0)),
        // else
            print(player(), str('Applied model %s to your %s.', model, item : 0));
        );
    );
);
