package edu.sdsu.cs635.model;

/**
 * Student class holds vital information pertaining to a student.
 * This class extends Comparable to give a custom implementation to compare
 * Student objects by their name in lexicographical order.
 * Implementing Comparable also gives an abstraction so that Student can be
 * added in tree
 */
public class Student {
  private final Long redId;
  private final String name;
  private final Double gpa;

  public Student(Long redId, String name, Double gpa) {
    this.redId = redId;
    this.name = name;
    this.gpa = gpa;
  }

  public String getName() {
    return name;
  }

  public void printRedId() {
    System.out.println(redId);
  }

  public void printName() {
    System.out.println(name);
  }

  public Double getGpa() {
    return gpa;
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

  @Override
  public String toString() {
    return "{" +
       "\"redId\":" + redId +
       ", \"name\":\"" + name + '\"' +
       ", \"gpa\":" + String.format("%.2f", gpa) +
       '}';
  }
}
