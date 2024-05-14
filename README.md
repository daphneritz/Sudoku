# Sudoku using JavaFX

## TODO: notes for you to implement
1. loadBoard() method should throw an exception if the file is not a valid sudoku board (DONE)
1. when saving: check if the file already exists, and ask the user if they want to overwrite it ("DONE")
1. Undo the last move
    * requires a way to store a stack of moves (DONE)
1. Undo, show values entered: show all the values we've entered since we loaded the board (DONE)
1. Hint, Show Hint: highlight all cells where only one legal value is possible (DONE)
1. on right-click handler: show a list of possible values that can go in this square (DONE)

## Also add two interesting features of your own
* This is for the final 10 points to get to 100. 
    * If your definition of "interesting" is "the minimum I can do to finish this assignment", then you may end up with A- instead of A. Try for something genuinely interesting.
1. checks to see if board is correctly solved and displays a win alert
    - also a clear board function so you can start a new game afterwards (was originally part of the win board thing but i figured you might want to admire your sudoku board so i moved it somewhere else); 
2. can navigate through board with arrow keys (it actually drove me crazy not being able to do that)

