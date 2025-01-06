# Entity Handlers

Entity handlers can be used to display info about the entity the player is looking at.

<procedure title="Adding an Entity Handler" id="adding_an_entity_handler">
<step>Make a class which will hold our Handler.</step>
<step>Make a function which will be our handler.
<code-block lang="Java">
private static boolean handleMyEntity(Entity entity, Player player) {
    if (/* your condition */) {
        // Your logic here
        WITAPI.updateBar(yourInfo, player);
        return true; // Successfully handled the entity
    }

    return false; // Not your entity, skip it
}
</code-block>
</step>
<step>Now that you have made the Handler, you must register it.
<code-block lang="Java">
WITAPI.addEntityHandler(YourClass::handleMyEntity);
</code-block>
</step>
<step>That's it, you have made you entity handler!</step>
<step>A complete example of an entity handler (this uses the internal [WAILAManager]):
<code-block lang="Java" src="../../src/main/java/me/darksoul/whatIsThat/compatibility/EliteMobsCompat.java">
</code-block>
</step>
</procedure>