# 2dPathFind

this project contains a 2-D path finding algorithm that implements Dijkstra's algorithm through recurrsion and a swing gui that's made up of a canvasPanel and a button panel. User can add barriers for the algorithm to navigate and change the start and end location through the button panel.
There are three files in this project, gui.java, canvasPanel.java and canvasPanelCells.java

the gui class contains the main method and construct the main Jframe and the button panel. 

the canvasPanel class contains the dikstraStart() method which is called by the canvasPanel object in the gui to initiate the path finding algorithm. 

the canvasPanelCells is made up of three 2D integer arrays which store the status, distance and the colors of each individual cells. 
