# Changelog

## v1.9.4-SNAPSHOT
* [[`489216eb79`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/489216eb79)] - Run Intellij's code cleanup and reformat code for easier developement (Tim Bernhard)
* [[`6856dcb2ba`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/6856dcb2ba)] - Use basic autocompletion using OpenStreetmap when having entered lat, long (see also #7) (Tim Bernhard)
* [[`ffd17e375d`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/ffd17e375d)] - Implement copy button to fix #14 (Tim Bernhard)
* [[`9d2c5c073c`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/9d2c5c073c)] - Refactor copy-previous to use java cloning (possibly fix #16, collector copying) (Tim Bernhard)
* [[`fea6575ce0`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/fea6575ce0)] - Fix issue where missing transaction commit led to dets not being deleted (#15) (Tim Bernhard)
* [[`6a1dfa7eda`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/6a1dfa7eda)] - Catch more exceptions when initializing SpecimenDetailsViewPane (Tim Bernhard)
* [[`89e95b8fcd`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/89e95b8fcd)] - Add possiblity to set field max height in imagecapture preferences (Tim Bernhard)


## v1.9.3-SNAPSHOT
* [[`2bb30feb55`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/2bb30feb55)] - Load imagecapture version from maven (Tim Bernhard)
* [[`20552cad9d`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/20552cad9d)] - Add MouseWheelScrollListener to propagate scrolling of Collector, Parts, Notes and Numbers to parent (Tim Bernhard)
* [[`46ac09b61b`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/46ac09b61b)] - Increse scroll speed & limit jTable sizes to partially fix #10 (Tim Bernhard)
* [[`fff5142055`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/fff5142055)] - Fix issue #13 where georeference datum returned to default (Tim Bernhard)
* [[`a6d913b580`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/a6d913b580)] - Fix issue #13 where every cbDatum was WSG84 (Tim Bernhard)
* [[`5784366c09`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/5784366c09)] - Fix image staying after image switch, issue #9 (Tim Bernhard)
* [[`b2317d46d9`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/b2317d46d9)] - Improve (slightly) stability &  helpfulness of error messages (Tim Bernhard)
* [[`d1cd163952`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/d1cd163952)] - Cleanup publishing scripts (Tim Bernhard)
* [[`253f8ce290`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/253f8ce290)] - Let draft deploy act on any taged branch (Tim Bernhard)
* [[`757de6802a`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/757de6802a)] - remove duplicates in changelog originating from double deploy (Tim Bernhard)
* [[`8e37bf0469`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/8e37bf0469)] - Update changelog & publish Version 1.9.2-SNAPSHOT (Tim Bernhard)


## v1.9.2-SNAPSHOT
* [[`28b3e8c20d`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/28b3e8c20d)] - Fix issue where a failed login required a restart (Tim Bernhard)
* [[`e091534ee3`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/e091534ee3)] - Fix issue where history did not work anymore (Tim Bernhard)

## v1.9.1
* [[`bfbedcd97b`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/bfbedcd97b)] - Configure travis to automatically build and release new versions (Tim Bernhard)
* [[`eb1beebd34`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/eb1beebd34)] - Improve editing of specimen (Tim Bernhard)
* [[`5725e6d71d`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/5725e6d71d)] - Improve layout of search dialog (Tim Bernhard)
* [[`866dfdd1da`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/866dfdd1da)] - Fix issue where image could not be switched (Tim Bernhard)
* [[`7b3a41dc7e`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/7b3a41dc7e)] - Fix issue where search would crash (Tim Bernhard)
* [[`4861afc524`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/4861afc524)] - Switch georeference method & datum according to [ETHEntColl#6](https://github.com/ETHEntColl/DataShot_DesktopApp_ETH/issues/6) (Tim Bernhard)
* [[`5060701b0d`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/5060701b0d)] - Implement window size keeping mechanism for edit panel (Tim Bernhard)
* [[`f879f3ee84`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/f879f3ee84)] - Deactivate next/prev button if not applicable anyway (Tim Bernhard)
* [[`08c8e40908`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/08c8e40908)] - Add autocomplete to Search Dialog (Tim Bernhard)
* [[`b18e143734`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/b18e143734)] - Improve look & feel by using always native one (Tim Bernhard)
* [[`4972993f83`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/4972993f83)] - Enable packaging with dependencies upon compilation (Tim Bernhard)
* [[`0ac486b4d1`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/0ac486b4d1)] - Improve startup performance by reducing nr. of db requests (Tim Bernhard)
* [[`c5c7915a6a`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/c5c7915a6a)] - Enable loading of db properties from imagecapture config to plain way for CI (Tim Bernhard)
* [[`171b9ca301`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/171b9ca301)] - Use MigLayout to improve login dialog layout (Tim Bernhard)
* [[`83b5b6b74f`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/83b5b6b74f)] - Refactor SpecimenDetailsViewPane to improve structure and scaling (Tim Bernhard)
* [[`5727d648ca`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/5727d648ca)] - Increase update rate of det & georeference button (Tim Bernhard)
* [[`c17920f0b3`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/c17920f0b3)] - Add option to automatically update database (required: flyway db table) (Tim Bernhard)
* [[`2391859955`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/2391859955)] - Fix issue leading to much waiting time when executing simple search (Tim Bernhard)
* [[`d3187d764d`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/d3187d764d)] - Updates to improve search & file processing reliability (Tim Bernhard)
* [[`475c8d8e11`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/475c8d8e11)] - Working towards cleaner error handling (Tim Bernhard)
* [[`87b8c3d843`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/87b8c3d843)] - Reduce time required for template detection (Tim Bernhard)
* [[`e746f5cd11`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/e746f5cd11)] - Upgrade much dependencies. Solve some followed issues. (Tim Bernhard)
* [[`974dced2a5`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/974dced2a5)] - Add CHANGELOG from Allie's GitHub (Tim Bernhard)
* [[`fad2e5efc3`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/fad2e5efc3)] - Fix wrong icon on report saved dialog (Tim Bernhard)
* [[`c80a0ede6a`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/c80a0ede6a)] - Make the configuration easier (Tim Bernhard)
* [[`a0acbc6fb1`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/a0acbc6fb1)] - Fix build errors (Tim Bernhard)
* [[`dd5cf58a5b`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/dd5cf58a5b)] - Merge in Allies changes (Tim Bernhard)
* [[`947dd9a8f7`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/947dd9a8f7)] - Hibernate config changes (Althea Parker)
* [[`8048ec39cb`](https://github.com/BernhardWebstudio/DataShot_DesktopApp/commit/8048ec39cb)] - Fix copy previous with complex fields: numbers, collectors, parts (Althea Parker)


## Up to tag v1.6.0 resp. 15.8.2019

### New Features and Fixes
The following features and fixes have been added to the ETH version of Datashot during 2016-2018 and have been released and are currently running in production at the ETH Entomological Collection.

The original version of DataShot was developed at Harvard University and can be found here:
https://github.com/MCZbase/DataShot_DesktopApp

1. Configuration changes
Some changes were made to the pom.xml file to use different versions of libraries than originally specified. Tesseract was also disabled from the properties configuration file among other things. An ETH specific regex expression was also added to the properties file to deal with ETH specific image names. 
2. EXIF handling
This was handled in a different way than in the original version
3. Bugfix: "database one file" 
The “database one file” was not working in most cases, so a workaround was found
4. Bugfix: fields not saving
Some fields were not saving, and this was fixed 
5. Bugfix: Specimen Collectors
6. Bugfix: “ID BY” text disappears
7. Feature: combo boxes loaded by DB
Implement “remember by feature” for several combo boxes: for countries, change text field to combo box and remember what the user entered before (read from DB), set lifestage to adult as default, change “id by” to combo box. Additionally: drop-down menu options in the “State/Province” field when doing a “Search” that are linked to DB records; in the “Number” field (top left of data entry screen) under “Type” - three additional options: “GBIFCH barcode tag”, “CSCF barcode tag”, “Swiss Grid” coordinates; several other options that can be removed from the Number “Type” field: “MCZ Slide number”; “Drawer number”; “Lycaenidae Morphology Ref.”; “MCZ Butterfly Exhibit, 2000”
8. Feature: NatureOfId
Set the “NatureOfID” to “expert ID”. It was set to “legacy”
9. Bugfix: Status bar issue
10. Bugfix: state/province
State/province didn't return any results
11. Change: Remove higher geography from search dialog and search results screen
12. Change: search table columns
Rearrange columns in the search table
13. Feature: delete specimen
There was not a way to delete a specimen from Datashot, we needed to do it manually. This feature was added, so that a specimen and all related data can be deleted, with the appropriate warnings and confirmations.

### Unreleased Features
The following features and fixes have been added to the ETH version of Datashot during 2018-2019 and have not been released in production at the ETH Entomological collection
1. Feature: Copy from Previous 
After saving a record and moving to the next one, copy over circa 30 fields excluding taxonomic information, and additionally, copy complex table components, specimen parts, numbers, collectors, Dets & Georeferences. This is done by pressing a button “copy previous”
2. Feature: Copy from Previous Taxon 
Copy only the taxonomic data only (feature disabled – button hidden - possible to re-enable)
3. Feature: Add alcohol to prep type
4. Feature: Make the verbatim date longer
The verbatim field had a limit on the length of the field a user could enter.
5. Feature: Specify default template
Currently, Datashot cycles through all the templates in the database during pre-processing for each image, which greatly increases the waiting time when pre-processing hundreds of images at once. This feature allows Datashot to retrieve the template specified in the configuration file, and also, to configure the system for the following 3 cases: 1) use all templates 2) specify only one template 3) to have Datashot use 2+ templates
6. Feature: "find missing images" feature
During pre-processing, some images do not get processed and this algorithm will cycle through all the images entered on a certain date, and identify the missing ones, and print this in a screen dialog.
7. Feature: search by interpreted date
Add a new search field to the search dialog in order to return search results when the user specifies the interpreted date.
