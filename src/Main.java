import processing.core.PApplet;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// TODO:  type list of missing names and have them all removed

public class Main extends PApplet {
    private static final String DATA_DIR = "d:\\JavaWorkspaces\\DavidDobervich\\StudentPairGenerator\\data\\";
    private ArrayList<Student> students;
    private AssignmentGrid grid = new AssignmentGrid();
    private Display display;

    public void settings() {
        fullScreen();
    }

    public void setup() {
        display = new Display(this, 100, 100, width - 200, height - 200);
        String response = JOptionPane.showInputDialog("What block?");
        students = loadStudentsFrom(DATA_DIR + response + ".txt");
        grid.assignStudentsRandomly(students);
        display.initializeWithGrid(grid.getGrid());
    }

    public void draw() {
        background(255);
        display.drawGrid(grid.getGrid());

        int current = grid.getChartNum();
        if (current > -1) {
            int total = grid.getTotalStudents();
            text("" + current + " of " + (total-1), width - 200, height - 100);
            System.out.println("" + current + " of " + (total-1));
        }

        Location mouseAt = display.gridLocationAt(mouseX, mouseY);
        display.highlightLocation(mouseAt, grid);
    }

    public void keyReleased() {
        if (key == 'l') {
            String response = JOptionPane.showInputDialog("What block?");
            students = loadStudentsFrom(DATA_DIR + response + ".txt");

            grid.assignStudents(students);
            display.initializeWithGrid(grid.getGrid());
        }

        if (key == 'n') {
            grid.loadNextChart();
        }

        if (key == 'r') {
            grid.assignStudentsRandomly(students);
        }
    }

    public void mouseReleased() {
        Location loc = display.gridLocationAt(mouseX, mouseY);
        grid.removeStudentAt(loc);
    }

    private ArrayList<Student> loadStudentsFrom(String filepath) {
        ArrayList<Student> studentList = new ArrayList<>();
        String[] headers;

        try (Scanner scanner = new Scanner(new File(filepath))) {
            String firstLine = scanner.nextLine();
            headers = firstLine.split(",");

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                Student s = new Student(data[0]);
                s.addData(headers, data);
                studentList.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return studentList;
    }

    public static void main(String[] args) {
        PApplet.main("Main");
    }
}
