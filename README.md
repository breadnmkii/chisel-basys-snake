# Chisel BASYS-3 Snake

An attempt at a implementing a simple Snake game on the segment display!

Currently game is broken up into several core components:

1. GameModule
2. GridModule
3. DisplayDriver
4. InputModule
 

And miscellaneous HelperModules for any generic/reusable logics

### GameModule
Should include the main class that incorporates all of the following:

* Player orientation / location
* Player movement
* Integrate grid logic
* Score logic / Win condition

### GridModule
Performs necessary state tracking and collision detection logic for a 3x5 (grid) + 4 (apples) node game board

* Player collision
* Apple collision
* Player Out-of-bounds

As well as providing the interface signals to DisplayDriver

### DisplayDriver
The hardware IO interface to display the GridModule state onto 4 segment displays:

* 4 7-segment display + DP led (apple)
* Cycle one-hot between each display repeatedly

### InputModule
The player input module IO interface:

* Button debounce logic
* 4 buttons for Up, Right, Left, Down
* middle button for pause(?)




