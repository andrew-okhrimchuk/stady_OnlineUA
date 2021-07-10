

public class Student {

    private final String name;
    private final Age age;

    public Student (String name, Age age) {
        this.name = name;
        Age ageClone = new Age ();
        ageClone.setAge(age.getAge());
        this.age = ageClone;
    }

    public Student setName(String name) {
        return new Student (name, this.age);
    }

    public Student setAge(Age age) {
        return new Student (this.name, age);
    }

    public String getName (){
        return this.name;
    }

    public String getAge (){
        Age ageClone = new Age ();
        ageClone.setAge(age.getAge());
        return ageClone;
    }

}