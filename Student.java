public class Student extends Stats{
    public int grade;
    public Student(String name, int height, int age, int grade, int oldHeight, int oldAge, int oldGrade) {
        super(name, height, age, oldHeight, oldAge, oldGrade);
        this.grade = grade;
    }
    public int getGrade() {
        return grade;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }
    @Override
    public String toString() {
        return name +"," + height +"," + age +"," + grade +"," + oldHeight +"," + oldAge +"," + oldGrade +"\n";
    }
}