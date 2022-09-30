// Customize
// v2.0.0
// A script for customizing items with renaming, lore, and adding custom model data.
// Please note that the 'model' feature requires a resource pack which features 'CustomModelData'..
// Created by CarbonGhost (Discord: https://discord.gg/rC38tvFSEU).

// The permission level needed in order to execute this command.
global_permission_level = 1;




// Script:
__config() -> {
	'scope' -> 'player',
	'command_permission' -> global_permission_level,
	'commands' -> {
		'model set <model>' -> _(model) -> command_model_set(model),
		'model as <alias>'	-> _(alias) -> command_model_set(aliases_from_item(holding() : 0) : 0 : alias),
		'model clear'       -> _() 			-> command_model_clear(),
		'lore set <lore>'   -> _(lore) 	-> command_lore_set(lore),
		'lore clear'        -> _() 			-> command_lore_clear(),
		'name set <name>'   -> _(name) 	-> command_name_set(name),
		'name clear'        -> _() 			-> command_name_clear(),
		'help'              -> _() 			-> command_help(),
	},
	'arguments' -> {
		'model' 	-> { 'type' -> 'int', 'min' -> 0, 'max' -> 16000000, 'suggest' -> [] },
		'alias' 	-> {
			'type' -> 'text',
			'suggester' -> _(arg) -> (
				aliases_from_item(holding() : 0);
			),
		},
		'lore'		-> { 'type' -> 'text', 'suggest' -> [] },
		'name'  	-> { 'type' -> 'text', 'suggest' -> [] },
	},
};

__on_start() -> (
	logger('info', '[customize.sc] Customize your items! Use /customize help for more info.');
	init_config();
);

// Utilities ----------------------------------------------------------------- //

// Returns a list for item data: '[item, quantity, {nbt data}]'.
holding() -> (
	i = inventory_get(player(), player() ~ 'selected_slot');
	if(i : 2 == null, i : 2 = {});      // Ensure that NBT tags are never null.
	return(i);
);

message(s) -> print(player(), format(s));

// Replaces the item the player is holding with the same item and quantity, plus
// the NBT definied as a map in the first param.
replace_held_item(nbt_as_map) -> (
	inventory_set(
		player(),					  					  // Select the player entity.
		player() ~ 'selected_slot',	    // Inventory index of held item.
		holding() : 1, 							    // Quantity of held item.
		holding() : 0,							    // Name of held item.
		encode_nbt(nbt_as_map),         // Parse Scarpet map into NBT.
	);
);

return_if_hand_empty() -> (
	if(holding() == null,
		message('r You must be holding an item to execute this command');
		return();
	);
);

add_pair(map, k, v) -> (map += k; map : k = v; return(map));

// Configuration ------------------------------------------------------------- //

init_config() -> (
	global_config = read_file('/config', 'json');
	default_config = { '*' -> { 'default' -> 0 }, 'a_minecraft_item_name' -> { 'a model alias' -> 123 } };

	// Create a config file if one does not already exist.
	if(global_config == null, task(_(outer(default_config)) -> (write_file('/config', 'json', default_config))));
);

// Returns a list ['alias name', model].
aliases_from_item(item) -> (return(map(pairs(global_config : item), _)));

// Command Handling ---------------------------------------------------------- //

command_model_set(model) -> (
	return_if_hand_empty();

	data = parse_nbt(holding() : 2);

	add_pair(data, 'CustomModelData', model);
	replace_held_item(data);

	sound('minecraft:block.amethyst_block.chime', pos(player()));
	message(str('w Changed this item\'s to model %d', model));
);

command_model_clear() -> (
	return_if_hand_empty();

	data = parse_nbt(holding() : 2);

	if(data : 'CustomModelData' == null,
		message('r This item does not have a custom model to clear');
		return();
	);
	// Remove the 'CustomModelData' key from the 'data' map. While setting this to 0 would have
	// the same effect, to clear to the default model, this seems cleaner.
	delete(data : 'CustomModelData');
	replace_held_item(data);

	sound('minecraft:ui.toast.out', pos(player()));
	message('w Cleared this item\'s custom model');
);

command_lore_set(lore) -> (
	data = parse_nbt(holding() : 2);

	return_if_hand_empty();

	// Add the 'display' tag if it does not already exist on the item.
	if(data : 'display' == null, add_pair(data, 'display', {}));
	
	add_pair(data : 'display', 'Lore', [str('{"text":"%s"}', lore)]);
	replace_held_item(data);

	sound('minecraft:ui.cartography_table.take_result', pos(player()));
	message(str('w Added the lore to this item: "%s"', lore));
);

command_lore_clear() -> (
	data = parse_nbt(holding() : 2);

	return_if_hand_empty();

	if(data : 'display' == null,
		message('r This item has no lore to clear');
		return();
	);
	data = parse_nbt(holding() : 2);

	delete(data : 'display' : 'Lore');
	replace_held_item(data);

	sound('minecraft:ui.toast.out', pos(player()));
	message('w Cleared custom lore from this item');
);

command_name_set(name) -> (
	data = parse_nbt(holding() : 2);

	return_if_hand_empty();

	// Add the 'display' tag if it does not already exist on the item.
	if(data : 'display' == null, add_pair(data, 'display', {}));
	
	add_pair(data : 'display', 'Name', str('{"text":"%s"}', name));
	replace_held_item(data);

	sound('minecraft:ui.cartography_table.take_result', pos(player()));
	message(str('w Changed this item\'s name to: "%s"', name));
);

command_name_clear() -> (
	data = parse_nbt(holding() : 2);

	return_if_hand_empty();

	if(data : 'display' == null,
		message('r This item does not have a custom name to clear');
		return();
	);
	data = parse_nbt(holding() : 2);

	delete(data : 'display' : 'Name');
	replace_held_item(data);

	sound('minecraft:ui.toast.out', pos(player()));
	message('w Cleared the custom name from this item');
);

command_help() -> (
	message('w /customize model set <model> - Sets your held item to a specified custom model. Requires a resource pack');
	message('w /customize model as <alias> - Sets your held item to a specified custom model alias, which can be configured');
	message('w /customize model clear - Clears your held item to it\'s default model');
	message('w /customize lore set <lore> - Adds a lore attribute to your held item');
	message('w /customize lore clear - Clears the lore attribute of your held item');
	message('w /customize name set <name> - Renames your held item at no XP cost');
	message('w /customize name clear - Clears your held item\'s name');
);
