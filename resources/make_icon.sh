# Take an image and make the image its own icon:
sips -i ~/Desktop/Snake/resources/icon.png

# Extract the icon to its own resource file:
DeRez -only icns ~/Desktop/Snake/resources/icon.png > tmpicns.rsrc

# append this resource to the file you want to icon-ize.
Rez -append tmpicns.rsrc -o ~/Desktop/snake-game

# Use the resource to set the icon.
SetFile -a C ~/Desktop/snake-game

# clean up.
rm tmpicns.rsrc
# rm icon.png # probably want to keep this for re-use.
