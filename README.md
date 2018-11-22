# GriefPreventionUtilities

[![Build status](https://ci.appveyor.com/api/projects/status/x5bfx8vjj7dx08og?svg=true)](https://ci.appveyor.com/project/112madgamer/griefpreventionextras)

**THIS PLUGIN IS IN EARLY DEVELOPMENT DO NOT RUN ON A SERVER WHICH YOU CARE ABOUT**

Latest build can be found [here](https://ci.appveyor.com/project/112madgamer/griefpreventionextras/build/artifacts)

The aim of this plugin is to provide extra niche addons to GriefPrevention. 

Current command list is

**saveclaims**
* /saveclaims \<world1> \<world2> - Copys all claims from world1 to world 2 (caution as the copy doesn't check world2 and just blindley copys from world1)
* ``griefpreventionutils.saveclaims``

**claimcleanup**
* /claimcleanup <time> <check/delete> Removes all claims where the owner has not logged in for your specified time 
    * "check" will check what claims will be deleted and log them to console
    * "delete" will delete all claims that were shown in "check"
* ``griefpreventionutils.claimcleanup``

**edeleteclaims**
* /edeletelcaims Removes all claims inside your current WE selection
* ``griefpreventionutils.edeleteclaims``

**countclaims**
* /countclaims Counts all claims inside your current WE selection
* ``griefpreventionutils.countclaims``

**eclaims**
* /eclaims \<player> Shows all players claims inside a GUI with extra features (If user has doesn't have permission **griefpreventionutils.eclaims.admin** 
then they can only see there own claims)
* ``griefpreventionutils.eclaims``  || ``griefpreventionutils.eclaims.admin``

Current event hooks

* ClaimExpire - When a claim expires the area of the claim is regenerated using WorldEdit 
    Actions
    * Regen - Regenerates the claims when it expires using WE
    * Snapshot - Loads the WE snapshot of the claim from your seed world






