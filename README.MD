# Minescape Scraping Mod

Forge mod to scrape data from the [Minescape](https://minescape.net/) UI to create JSON files.

## Install

Install [Forge Mod Loader](https://files.minecraftforge.net/).

Drop the jar file (see the releases page) into .minecraft/mods folder (create this folder if it does not exist).

THIS MOD IS FOR 1.16.4 ONLY, CURRENTLY

## Usage
The mod is only relevant on the Minescape server (https://minescape.net/).

To use, first configure the mod in the "Mods" menu on the title screen. The options dictate
what is present in the output files. The less of the options that are "on",
the less the size of the output file will be.

After configuration and logging into the GamesLabs Minescape server, you can press the scrape key ("k" by default) in
order to begin scraping.

Scraping will take a minute or two, as it flips through pages of items/mobs in order to scrape information from the UI.
You shouldn't do anything while it's scraping, or you may interrupt the scraping process.

Finally, the mod will output json files with the information it has scraped into the ".minecraft/scraping" folder.

## Possible Issues
- One possible issue that I've encountered is that high-latency to the server can cause errors with scraping. 
  If you run into this issue, try connecting through another proxy, or connecting to a less populated instance.
- If the "k" key is already a used key-binding, you can configure it just like any other key-binding in the config menu.

