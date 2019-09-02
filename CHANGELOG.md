#Changelog

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
