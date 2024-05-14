package sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Set;
import java.util.Stack;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;


public class Sudoku extends Application
{
    private Board board = new Board();
    public static final int SIZE = 9;
    private VBox root;
    private TextField[][] textFields = new TextField[SIZE][SIZE];
    private int width = 800;
    private int height = 800;
    private Stack<Move> moveStack = new Stack<Move>();
    private boolean loadingBoard = false;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        root = new VBox();

        //System.out.println(new File(".").getAbsolutePath());

        root.getChildren().add(createMenuBar(primaryStage));

        GridPane gridPane = new GridPane();
        root.getChildren().add(gridPane);
        gridPane.getStyleClass().add("grid-pane");

        // create a 9x9 grid of text fields
        for (int row = 0; row < SIZE; row++)
        {
            for (int col = 0; col < SIZE; col++)
            {
                textFields[row][col] = new TextField();
                TextField textField = textFields[row][col];
                
                // setting ID so that we can look up the text field by row and col
                // IDs are #3-4 for the 4th row and 5th column (start index at 0)
                textField.setId(row + "-" + col);
                gridPane.add(textField, col, row);
                // using CSS to get the darker borders correct
                if (row % 3 == 2 && col % 3 == 2)
                {
                    // we need a special border to highlight the bottom right
                    textField.getStyleClass().add("bottom-right-border");
                }
                else if (col % 3 == 2) { 
                    // Thick right border
                    textField.getStyleClass().add("right-border");
                }
                else if (row % 3 == 2) { 
                    // Thick bottom border
                    textField.getStyleClass().add("bottom-border");
                }

                // add a handler for when we select a textfield
                textField.setOnMouseClicked(event -> {
                    // toggle highlighting
                    if (textField.getStyleClass().contains("text-field-selected"))
                    {
                        // remove the highlight if we click on a selected cell
                        textField.getStyleClass().remove("text-field-selected");
                    }
                    else
                    {
                        // otherwise 
                        textField.getStyleClass().add("text-field-selected");
                    }
                });

                // add a handler for when we lose focus on a textfield
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue)
                    {
                        // remove the highlight when we lose focus
                        textField.getStyleClass().remove("text-field-selected");
                    }
                });

                textField.setOnKeyPressed(event -> {
                    int currentRow = GridPane.getRowIndex(textField);
                    int currentCol = GridPane.getColumnIndex(textField);
                    switch (event.getCode()) {
                        case UP:
                            if (currentRow > 0) {
                                TextField a=textFields[currentRow - 1][currentCol];
                               a.requestFocus();
                                if (a.getStyleClass().contains("text-field-selected"))
                                {
                                    // remove the highlight if we click on a selected cell
                                    a.getStyleClass().remove("text-field-selected");
                                }
                                else
                                {
                                    // otherwise 
                                    a.getStyleClass().add("text-field-selected");
                                }

                            }
                            break;
                        case DOWN:
                            if (currentRow < SIZE - 1) {
                                TextField a=textFields[currentRow + 1][currentCol];
                                a.requestFocus();
                                if (a.getStyleClass().contains("text-field-selected"))
                                {
                                    // remove the highlight if we click on a selected cell
                                    a.getStyleClass().remove("text-field-selected");
                                }
                                else
                                {
                                    // otherwise 
                                    a.getStyleClass().add("text-field-selected");
                                }
                            }
                            break;
                        case LEFT:
                            if (currentCol > 0) {
                                TextField a=textFields[currentRow][currentCol - 1];
                                a.requestFocus();
                                if (a.getStyleClass().contains("text-field-selected"))
                                {
                                    // remove the highlight if we click on a selected cell
                                    a.getStyleClass().remove("text-field-selected");
                                }
                                else
                                {
                                    // otherwise 
                                    a.getStyleClass().add("text-field-selected");
                                }
                            }
                            break;
                        case RIGHT:
                            if (currentCol < SIZE - 1) {
                                TextField a=textFields[currentRow][currentCol + 1];
                                a.requestFocus();
                                if (a.getStyleClass().contains("text-field-selected"))
                                {
                                    // remove the highlight if we click on a selected cell
                                    a.getStyleClass().remove("text-field-selected");
                                }
                                else
                                {
                                    // otherwise 
                                    a.getStyleClass().add("text-field-selected");
                                }
                            }
                            break;
                        default:
                            break;
                    }
                });

                // RIGHT-CLICK handler
                // add handler for when we RIGHT-CLICK a textfield
                // to bring up a selection of possible values
                textField.setOnContextMenuRequested(event -> {
                    // change the textfield background to red while keeping the rest of the css the same
                    textField.getStyleClass().add("text-field-highlight");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Possible values");
                    //  show a list of possible values that can go in this square
                    // get id of square
                    String id=textField.getId(); 
                    String[] parts=id.split("-");
                    int r=Integer.parseInt(parts[0]);
                    int c=Integer.parseInt(parts[1]);
                    // get possible values from board method
                    Set<Integer> possibleValues = board.getPossibleValues(r, c);
                    // convert the set of integers to a string that can be displayed
                    Integer[] valuesArray = possibleValues.toArray(new Integer[0]);
                    String values = "Possible values: ";
                    for (int i=0; i<valuesArray.length; i++)
                    {
                        values += valuesArray[i];
                        if (i < valuesArray.length - 1)
                        {
                            values += ", ";
                        }
                    }
                    // display the possible values
                    alert.setContentText(values);
                    alert.showAndWait();
                    textField.getStyleClass().remove("text-field-highlight");
                });

                // using a listener instead of a KEY_TYPED event handler
                // KEY_TYPED requires the user to hit ENTER to trigger the event
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (loadingBoard)
                    {
                        return;
                    }
                    if (!newValue.matches("[1-9]?")) {
                        // restrict textField to only accept single digit numbers from 1 to 9
                        textField.setText(oldValue);
                    }
                    String id = textField.getId();
                    String[] parts = id.split("-");
                    int r = Integer.parseInt(parts[0]);
                    int c = Integer.parseInt(parts[1]);
                    
                    if (newValue.length() > 0)
                    {
                        try
                        {
                            
                            System.out.printf("Setting cell %d, %d to %s\n", r, c, newValue);
                            int value = Integer.parseInt(newValue);
                            if (!(value>=1) || !(value<=9))
                            {
                                throw new Exception("Value " + value + " not in range");
                            }
                            else if (!board.getPossibleValues(r, c).contains(value))
                            {                                
                                throw new Exception();
                            } 
                            // add the move to the stack
                            addMove(r, c, value);
                            board.setCell(r, c, value);
                            boolean won=(board.won()); 
                            if (won){
                                Alert alert = new Alert(AlertType.INFORMATION);
                                alert.setTitle("Congratulations!");
                                alert.setHeaderText("You've solved the puzzle!");
                                alert.setContentText("You've solved the puzzle! Congratulations!");
                                alert.showAndWait();
                            }
                            // remove the highlight when we set a value
                            textField.getStyleClass().remove("text-field-selected");
                        }
                        catch (NumberFormatException e)
                        {
                            // ignore; should never happen
                            System.out.println("Invalid Value: number format section " + newValue);
                        }
                        catch (Exception e)
                        {
                            // if the value is not a possible value, catch the exception and show an alert
                            board.setCell(r, c, 0); 
                            updateBoard(); 
                            System.out.println("Invalid Value: " + newValue);
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Invalid Value");
                            alert.setHeaderText("Invalid Value: " + newValue);
                            alert.setContentText(e.getMessage());
                            alert.showAndWait();
                        }
                    }
                    else
                    {
                        board.setCell(r, c, 0);
                    }
                });
            }
        }

        // add key listener to the root node to grab ESC keys
        root.setOnKeyPressed(event -> {
            System.out.println("Key pressed: " + event.getCode());
            switch (event.getCode())
            {
                // check for the ESC key
                case ESCAPE:
                    // clear all the selected text fields
                    for (int row = 0; row < SIZE; row++)
                    {
                        for (int col = 0; col < SIZE; col++)
                        {
                            TextField textField = textFields[row][col];
                            textField.getStyleClass().remove("text-field-selected");
                        }
                    }
                    break;
                default:
                    System.out.println("you typed key: " + event.getCode());
                    break;
                
            }
        });

        Scene scene = new Scene(root, width, height);

        URL styleURL = getClass().getResource("/style.css");
		String stylesheet = styleURL.toExternalForm();
		scene.getStylesheets().add(stylesheet);
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
        	System.out.println("oncloserequest");
        });
    }

    private void updateBoard()
    {
        for (int row = 0; row < SIZE; row++)
        {
            for (int col = 0; col < SIZE; col++)
            {
                TextField textField = textFields[row][col];
                int value = board.getCell(row, col);
                if (value > 0)
                {
                    textField.setText(Integer.toString(value));
                }
                else
                {
                    textField.setText("");
                }
            }
        }
    }

    private MenuBar createMenuBar(Stage primaryStage)
    {
        MenuBar menuBar = new MenuBar();
    	menuBar.getStyleClass().add("menubar");

        //
        // File Menu
        //
    	Menu fileMenu = new Menu("File");

        addMenuItem(fileMenu, "Load from file", () -> {
            System.out.println("Load from file");
            FileChooser fileChooser = new FileChooser();
            // XXX: this is a hack to get the file chooser to open in the right directory
            // we should probably have a better way to find this folder than a hard coded path
			fileChooser.setInitialDirectory(new File("../puzzles"));
			File sudokuFile = fileChooser.showOpenDialog(primaryStage);
            if (sudokuFile != null)
            {
                System.out.println("Selected file: " + sudokuFile.getName());
                
                try {
                    //TODO: loadBoard() method should throw an exception if the file is not a valid sudoku board
                    loadingBoard = true;
                    board = Board.loadBoard(new FileInputStream(sudokuFile));
                    updateBoard();
                    loadingBoard=false;
                } catch (Exception e) {
                    // pop up and error window
                    Alert alert = new Alert(AlertType.ERROR);
    	            alert.setTitle("Unable to load sudoku board from file "+ sudokuFile.getName());
    	            alert.setHeaderText(e.getMessage());
                    alert.setContentText(e.getMessage());
                    e.printStackTrace();
                    if (e.getCause() != null) e.getCause().printStackTrace();
                    
                    alert.showAndWait();
                }
            }
        });

        // save to text
        addMenuItem(fileMenu, "Save to text", () -> {
            System.out.println("Save puzzle to text");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("../puzzles"));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null)
            {
                System.out.println("Selected file: " + file.getName());
                try {
                    writeToFile(file, board.toString());
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Unable to save to file");
                    alert.setHeaderText("Unsaved changes detected!");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });

        addMenuItem(fileMenu, "Clear Board", () -> {
            System.out.println("Clear Board");
            clearBoard();
        });
        
        addMenuItem(fileMenu, "Print Board", () -> {
            // Debugging method that just prints the board
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Board");
            alert.setHeaderText(null);
            alert.setContentText(board.toString());
            alert.showAndWait();
        });
        // add a separator to the fileMenu
        fileMenu.getItems().add(new SeparatorMenuItem());

        addMenuItem(fileMenu, "Exit", () -> {
            System.out.println("Exit");
            primaryStage.close();
        });

        menuBar.getMenus().add(fileMenu);

        //
        // Edit
        //
        Menu editMenu = new Menu("Edit");

        addMenuItem(editMenu, "Undo", () -> {
            //undo the last move
            System.out.println("Undo");
            if(!moveStack.isEmpty())
            {
                Move a=moveStack.pop();
                board.setCell(a.getRow(), a.getCol(), a.getPrevVal());
                updateBoard(); 
            }
        });

        addMenuItem(editMenu, "Show values entered", () -> {
            System.out.println("Show all the values we've entered since we loaded the board");
            //create alert 
            Move a=new Move();
            String values = "Values entered: "+a.getNewVals();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Values entered");
            alert.setHeaderText("Values entered since loading the board");
            
            // Create a TextArea for the content
            TextArea textArea = new TextArea(values);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            // Make sure the TextArea will grow vertically
            textArea.setMaxHeight(Double.MAX_VALUE);
            textArea.setMaxWidth(Double.MAX_VALUE);

            // Put the TextArea in a ScrollPane
            ScrollPane scrollPane = new ScrollPane(textArea);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);

            // Set the ScrollPane as the content of the Alert
            alert.getDialogPane().setContent(scrollPane);

            alert.showAndWait();    
        });

        menuBar.getMenus().add(editMenu);

        //
        // Hint Menu
        //
        Menu hintMenu = new Menu("Hints");

        addMenuItem(hintMenu, "Show hint", () -> {
            System.out.println("Show hint");
            //highlight ALL CELLS where only one legal value is possible
            // highlight is removed when a board is won and cleared 
            ArrayList<Integer> hints=new ArrayList<Integer>(); 
            for (int r=0; r<SIZE; r++)
            {
                for (int c=0; c<SIZE; c++)
                {
                    if (!board.hasValue(r, c))
                    {
                        Set<Integer> possibleValues = board.getPossibleValues(r, c);
                        if (possibleValues.size() == 1)
                        {
                            // add the hint to the list
                           hints.add(r);
                           hints.add(c);
                        }
                    }
                }
            }
            // highlight hints 
            for (int i=0; i<hints.size(); i+=2)
            {
                int r=hints.get(i);
                int c=hints.get(i+1);
                TextField textField = textFields[r][c];
                textField.getStyleClass().add("text-field-hint");
            }
        });

        menuBar.getMenus().add(hintMenu);

        return menuBar;
    }

    public void clearBoard(){
        board.clearBoard(); 
        for (int row = 0; row < SIZE; row++)
        {
            for (int col = 0; col < SIZE; col++)
            {
                // clear any highlights on the text fields
                TextField textField = textFields[row][col];
                textField.getStyleClass().remove("text-field-hint");
            }
        }
        updateBoard();
    }

    public void addMove(int row, int col, int newVal)
    {
        int prevVal = board.getCell(row, col);
        moveStack.push(new Move(row, col, prevVal, newVal));
    }

    private static void writeToFile(File file, String content) throws IOException
    {
        Files.write(file.toPath(), content.getBytes());
    }

    private void addMenuItem(Menu menu, String name, Runnable action)
    {
        MenuItem menuItem = new MenuItem(name);
        menuItem.setOnAction(event -> action.run());
        menu.getItems().add(menuItem);
    }


        
    public static void main(String[] args) 
    {
        launch(args);
    }
}
