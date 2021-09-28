package edu.sdsu.cs635.aoop.model;

/**
 * Student class holds vital information pertaining to a student.
 * A student is probationary if his GPA < 2.85
 * A student has a perfect score if his GPA = 4.0
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

  public Double getGpa() {
    return gpa;
  }

  public boolean amIProbationary() {
    return gpa < 2.85;
  }

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
