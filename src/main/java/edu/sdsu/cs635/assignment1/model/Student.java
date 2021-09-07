package edu.sdsu.cs635.assignment1.model;

/**
 * Student class holds vital information pertaining to a student.
 * This class extends Comparable to give a custom implementation to compare Student objects by their name in lexicographical order
 */
public class Student implements Comparable<Student> {
    private final Long redId;
    private final String name;
    private final Double gpa;

    public Student(Long redId, String name, Double gpa) {
        this.redId = redId;
        this.name = name;
        this.gpa = gpa;
    }

    /**
     * Getter to compare with another students name.
     *
     * @return students' name.
     */
    public String getName() {
        return name;
    }

    public void printRedId() {
        System.out.println(redId);
    }

    public void printName() {
        System.out.println(name);
    }

    /**
     * @return true if students gpa < 2.85 else false
     */
    public boolean amIProbationary() {
        return gpa < 2.85;
    }

    /**
     * @return true if students gpa is equal to 4 else false
     */
    public boolean isPerfectScore() {
        return gpa.equals(4.00);
    }

    /**
     * @param otherStudent the comparison object
     * @return 0 if names are same, -1 if the otherStudents name is lexicographically greather than this student else return 1
     */
    @Override
    public int compareTo(Student otherStudent) {
        return this.name.toLowerCase().compareTo(otherStudent.getName().toLowerCase());
    }

    @Override
    public String toString() {
        return "{" +
                "\"redId\":" + redId +
                ", \"name\":\"" + name + '\"' +
                ", \"gpa\":" + String.format("%.2f", gpa) +
                '}';
    }
}
