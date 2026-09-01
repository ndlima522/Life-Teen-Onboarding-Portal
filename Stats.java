public class Stats {
    protected int height;
    protected int age;
    protected String name;
    protected int oldHeight;
    protected int oldAge;
    protected int oldGrade;

    public Stats(String name, int height, int age, int oldHeight, int oldAge, int oldGrade) {
        this.name = name;
        this.height = height;
        this.age = age;
        this.oldHeight = oldHeight;
        this.oldAge = oldAge;
        this.oldGrade = oldGrade;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOldHeight() {
        return oldHeight;
    }

    public void setOldHeight(int oldHeight) {
        this.oldHeight = oldHeight;
    }

    public int getOldAge() {
        return oldAge;
    }

    public void setOldAge(int oldAge) {
        this.oldAge = oldAge;
    }

    public int getOldGrade() {
        return oldGrade;
    }

    public void setOldGrade(int oldGrade) {
        this.oldGrade = oldGrade;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Height: " + height + ", Age: " + age +
                ", Old Height: " + oldHeight + ", Old Age: " + oldAge + "Old Grade:" + oldGrade;
    }
}