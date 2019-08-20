import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class AssignmentGrid {
    private static int ROWS = 18;
    private static final int COLS = 4;
    public Student[][] grid;
    private ArrayList<Student> studentList;
    private int currentChart = -1;

    public AssignmentGrid() {
        grid = new Student[ROWS][COLS];
    }

    public boolean isInGrid(Location loc) {
        if (loc.getRow() < 0 || loc.getCol() < 0) return false;
        if (loc.getRow() >= ROWS || loc.getCol() >= COLS) return false;
        return true;
    }

    /***
     * For n students, if each student is to work with every other, there are (n-1) pairings which achieve this.
     * This method will construct a list in which adjacent students in the list should be paired together.  The
     * parameter n determines which of the (n-1) different class pairings is to be constructed.
     * @param n must be in range 0 <= n < N-1 where N is number of students in the class.
     * @return a copy of list representing one of the N - 1 class pairings
     */
    private ArrayList<Student> constructPairing(ArrayList<Student> list, int n) {
        if (list.size() % 2 == 1) {
            System.err.println("Warning: number of students should be even");
            System.err.println("Adding blank student");
            list.add(new Student("...their bad self"));
        }

        if (n < 0 || n >= list.size()-1) {
            System.err.println("Warning: with " + list.size() + " students, you can only choose charts from 0 to " + (list.size() - 2));
            n = 0;
        }

        ArrayList<Student> copy = (ArrayList<Student>) list.clone();
        ArrayList<Student> newlist = new ArrayList<>();

        Student center = copy.remove(0);    // remove first student
        // FIRST PAIR:  center and student n
        newlist.add(center);
        newlist.add(copy.get(n));

        int offset = 1;
        for (int pairNum = 0; pairNum < (list.size()/2)-1; pairNum++) {
            System.out.println("Matching student " + wrapIndexes(n + offset, copy.size() ) + " with " + wrapIndexes(n - offset, copy.size() ));
            Student p1 = copy.get( wrapIndexes(n + offset, copy.size() ));
            Student p2 = copy.get( wrapIndexes(n - offset, copy.size() ));
            newlist.add(p1);
            newlist.add(p2);
            offset++;
        }

        this.currentChart = n;

        return newlist;
    }

    private int wrapIndexes(int index, int size) {
        while (index < 0) index += size;
        index = index % size;
        return index;
    }

    private void placeStudents(ArrayList<Student> studentList) {
        ArrayList<Student> copy = (ArrayList<Student>) studentList.clone();

        for (int row = 0; row < ROWS; row++) {
            placePairAt(copy, row, 0);
            placePairAt(copy, row, 2);
        }
    }

    public void assignStudentsRandomly(ArrayList<Student> students) {
        this.studentList = students;
        resize();
        Collections.shuffle(this.studentList);
        placeStudents(this.studentList);
        this.currentChart = -1;
    }

    public void assignStudents(ArrayList<Student> students) {
        System.out.println("STUDENTS: " + students);
        this.studentList = students;
        resize();

        placeStudents(constructPairing(this.studentList, 0));
    }

    public void loadNextChart() {
        if (this.currentChart == -1) return;

        placeStudents(constructPairing(this.studentList, this.currentChart+1));
    }

    public void removeStudent(Student s) {
        this.studentList.remove(s);
    }

    public void removeStudent(String name) {
        for (int i = 0; i < studentList.size(); i++) {
            Student student =  studentList.get(i);
            if (student.getName().equals(name)) {
                studentList.remove(i);
            }
        }
    }

    private void placePairAt(ArrayList<Student> studentList, int row, int col) {
        if (studentList.size() >= 2) {
            Student s1 = studentList.remove(0);
            Student s2 = studentList.remove(0);
            grid[row][col] = s1;
            grid[row][col + 1] = s2;
        } else if (studentList.size() == 1) {
            grid[row][col] = studentList.remove(0);
        }
    }

    public Student[][] getGrid() {
        return grid;
    }

    public void removeStudentAt(Location loc) {
        if (!isInGrid(loc)) return;

        Student s = grid[loc.getRow()][loc.getCol()];
        if (s != null) {
            studentList.remove(s);
            grid[loc.getRow()][loc.getCol()] = null;
        }
    }

    private void resize() {
        if (studentList == null || studentList.size() == 0) return;
        ROWS = (studentList.size()/4)+1;
        grid = new Student[ROWS][COLS];
    }

    public int getChartNum() {
        return this.currentChart;
    }

    public int getTotalStudents() {
        return this.studentList.size();
    }
}