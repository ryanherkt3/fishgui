# Fish Shoal GUI

A set of classes which make up a GUI in which 'fish' swim inside, written in Java.

## Classes

**IMPORTANT:** Please read the Javadoc comments in each class to understand how they work.

Fish: 

* A class which contains the attributes for a Fish data structure. 
* Implements the Runnable interface.
* Has a constructor with a FishShoal as the parameter, a run method, get methods for the size, and the x and y co-ordinates; methods to move and kill the fish, and methods to draw and eat the fish.

FishShoal:

* A class which represents a fish shoal.
* Has a default constructor initializing a list of fish.
* Has methods to add and remove fish from the shoal, and a method to check if a parameter fish can eat any of the other fish in a shoal (provided it can do so).

FishGUI:

* The main class. Allows the user to add fish to the fish shoal.
