This javaagent repeats the logic of the combo-auth mod.
it can run on:
- [x] full-fledged servers  (**except fabric**)
- [x] bangeeCord 
- [X] velocity 
      
using plugins or mods you can track where the player is registered from (authentication server) _(example of a single possibility)_ ->
```
org.figrja.combo_auth_ahent.api.util.getPlayerBase().get(profileName).getLast()
```
