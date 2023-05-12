////////////////////////////////////////////////////////////////////////////////
// Customize - READ THIS BEFORE USING THIS SCRIPT ----------------------------//
////////////////////////////////////////////////////////////////////////////////
//
// This script allows players to customize the name, lore, and custom model data
// of their items. Name and lore customization can be achieved with no work on
// your part, but custom model support is done via a resource pack, and
// configuration of the `aliases.json` file.
// 
// If you use the custom model feature you will need to load the script, then
// open the `aliases.json` file which will be created in your `/scripts` folder.
// The format is the Minecraft item name, follow by a key which identifies your
// model, this can include spaces, caps, etc; then the CustomModelData number.
//
// Example:
//
// `aliases.json````````````````
// "stone_sword": {
//   "Stone Sword Model A": 100,
//   "Stone Sword Model B": 101,
//   "Stone Sword Model C": 102
// },
// "diamond_axe": {
//   "Diamond Axe Model A": 300,
//   "Diamond Axe Model B": 301,
//   "Diamond Axe Model C": 302
// }
// `````````````````````````````
// 
// Once you have added your models, run /[script name] reload.
//
// To load / reload the entire script run `/script load [script_name]`


////////////////////////////////////////////////////////////////////////////////
// Script - Any modifications beyond this point are at your own risk ---------//
////////////////////////////////////////////////////////////////////////////////


// Aliases file ------------------------------------------------------------- //

aliases(item) -> global_aliases : item;


init_aliases() -> (
  data = read_file('aliases', 'json');
  default = {};

  if(data == null,
    data = write_file('aliases', 'json', default);
  );

  return(data);
);


// Command handlers --------------------------------------------------------- //

command_base() -> (
  print(
    format(
      str('w %s.sc v%s', system_info('app_name'), join('.', global_version)),
      '^w View source...',
      '@https://github.com/prosperitymc/Prosperity-Scripts',
    );
  );
);


command_model_list() -> (
  map(global_aliases,
    item = _;
    msg = format(str('wb %s: ', item_display_name(_)));
    
    map(global_aliases : item,
      alias = _;

      msg += format(
        str('wi %s', alias),
        str('^w Model ID: %s\nClick to apply...', global_aliases : item : alias),
        str('?/%s model set %s', system_info('app_name'), alias),
      );
      if(_i + 1 < length(global_aliases : item), msg += ', ');
    );
    
    print(msg);
  );
);


command_model_set(model) -> (
  if(error_hand_empty(), return());

  if(keys(aliases(holding() : 0)) ~ model == null,
    print(format('r That is not a valid model'));

    return(true);
  );

  item = holding() : 0;
  model_id = global_aliases : item : model;
  data = parse_nbt(holding() : 2);

  put(data, 'CustomModelData', model_id);
  replace_held_item(data);

  sound('minecraft:block.amethyst_block.chime', pos(player()), 100);
  print(
    'Set the model of your ' 
    + format(str('wi %s to ', item_display_name(item)))
    + format(
      str('wi %s', model),
      str('^w Model ID: %s', model_id),
    );
  );
);


command_model_clear() -> (
  if(error_hand_empty(), return());

  item = holding() : 0;
  data = parse_nbt(holding() : 2);

  if(data : 'CustomModelData' == null,
		print(format('r This item does not have a custom model to clear'));
		
    return();
	);

  delete(data : 'CustomModelData');
	replace_held_item(data);

	sound('minecraft:ui.toast.out', pos(player()), 100);
	print('Cleared the custom model data from this item');
);


command_lore_set(text) -> (
  if(error_hand_empty(), return());
  
  item = holding() : 0;
  data = parse_nbt(holding() : 2);

  if(data : 'display' == null, put(data, 'display', {}));
	
	put(data : 'display', { 'Lore' -> [str('{"text":"%s"}', text)] });
	replace_held_item(data);

  sound('minecraft:ui.cartography_table.take_result', pos(player()), 100);
  print(
    'Set the lore of your ' 
    + format(str('wi %s to ', item_display_name(item)))
    + format(str('wi %s', text)),
  );
);


command_lore_clear() -> (
  if(error_hand_empty(), return());

  data = parse_nbt(holding() : 2);
  
  if(data : 'display' == null,
		print(format('r This item has no lore to clear'));
		return();
	);

  delete(data : 'display' : 'Lore');
	replace_held_item(data);

	sound('minecraft:ui.toast.out', pos(player()), 100);
	print('Cleared custom lore from this item');
);


command_name_set(text) -> (
  if(error_hand_empty(), return());
  
  item = holding() : 0;
  data = parse_nbt(holding() : 2);

  if(data : 'display' == null, put(data, 'display', {}));
	
	put(data : 'display', { 'Name' -> str('{"text":"%s"}', text) });
	replace_held_item(data);

  sound('minecraft:ui.cartography_table.take_result', pos(player()), 100);
  print(
    'Set the name of your ' 
    + format(str('wi %s to ', item_display_name(item)))
    + format(str('wi %s', text)),
  );
);


command_name_clear() -> (
  if(error_hand_empty(), return());

  data = parse_nbt(holding() : 2);
  
  if(data : 'display' == null ||
    data : 'display' : 'Name' == null,
		print(format('r This item has no name to clear'));
		return();
	);

  delete(data : 'display' : 'Name');
	replace_held_item(data);

	sound('minecraft:ui.toast.out', pos(player()), 100);
	print('Cleared custom name from this item');
);


command_reload() -> (
  if(player() ~ 'permission_level' < 2,
    print(format('r You do not have permission to do that'));
    return();
  );

  global_aliases = init_aliases();

  print('Reloaded model aliases file...');
);


command_help() -> (
  messages = [
    '/%s model set <model> - Sets the current item to a model defined by the custom resource pack',
    '/%s model clear - Clears the current item\'s custom model',
    '/%s lore set <text> - Adds lore text to your item',
    '/%s lore clear - Removed the lore text from your item',
    '/%s name set <text> - Changes the name of your item',
    '/%s name clear - Removes the custom name from your item',
    '/%s reload - Reloads the model aliases file',
    '/%s help - Prints this very message',
  ];

  print(join('\n', map(messages, str(_, system_info('app_name')))));
);


// Utils -------------------------------------------------------------------- //

holding() -> (
	i = inventory_get(player(), player() ~ 'selected_slot');
	if(i : 2 == null, i : 2 = {});

  return(i);
);


replace_held_item(nbt_as_map) -> (
	inventory_set(
		player(),
		player() ~ 'selected_slot',
		holding() : 1,
		holding() : 0,
		encode_nbt(nbt_as_map),
	);
);


error_hand_empty() -> (
	if(holding() == null,
		print(format('r You must be holding an item to execute this command'));
		
    return(true);
	);
);


// Globals ------------------------------------------------------------------ //

global_version = [2, 0, 1];
global_aliases = init_aliases();


// Scarpet config ----------------------------------------------------------- //

__config() -> {
  'scope' -> 'player',
  'commands' -> {
    '' -> _() -> command_base(),
    'model list' -> _() -> command_model_list(),
    'model set <model>' -> _(model) -> command_model_set(model),
    'model clear' -> _() -> command_model_clear(),
    'lore set <text>' -> _(text) -> command_lore_set(text),
    'lore clear' -> _() -> command_lore_clear(),
    'name set <text>' -> _(text) -> command_name_set(text),
    'name clear' -> _() -> command_name_clear(),
    'reload' -> _() -> command_reload(),
    'help' -> _() -> command_help(),
  },
  'arguments' -> {
    'model' -> {
      'type' -> 'text',
      'suggester' -> _(arg) -> keys(aliases(holding() : 0)),
    },
    'text' -> {
      'type' -> 'text',
      'suggest' -> [],
    },
  },
};
