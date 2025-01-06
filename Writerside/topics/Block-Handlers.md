# Block Handlers

Block Handlers can be used to display info for the blocks that the player is looking at.

<procedure title="Adding a Block Handler" id="adding_a_block_handler">
<step>Make a class which will hold our handler funtion.</step>
<step>Make a function which will be the handler. 
<code-block lang="java">
private static boolean handleMyBlock(Block block, Player player) {
    // Check if this block belongs to your blocks
    if (/* your condition */) {
        // Add your logic
        WITAPI.updateBar(yourInfo, player); 
        // `yourInfo` is the string shown on the boss bar.
        // `player` is the one received by the handler from the plugin.

        return true; // Block successfully handled
    }

    return false; // Not your block, skip it
}
</code-block>
</step>
<step>Now that you have made a handler, you must register the handler so it's actually used by WIT, this can be done by:
<code-block lang="Java">
WITAPI.addBlockHandler(YourClass::handleMyBlock);
</code-block>
</step>
<step>That's it! you have now successfully made a Block Handler!</step>
<step>A complete example of a block handler (this uses the internal [WAILAManager]):
<code-block lang="Java" src="../../src/main/java/me/darksoul/whatIsThat/compatibility/MinetorioCompat.java">
</code-block>
</step>
</procedure>

<procedure title="Removing a Block Handler" id="removing_a_block_handler">
<step>Removing a block handler is a one-liner!:
<code-block lang="Java">
WITAPI.removeBlockHandler(YourClass::BlockHandlerFunc)
</code-block>
</step>
</procedure>