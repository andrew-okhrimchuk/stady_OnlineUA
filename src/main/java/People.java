

public class People {

    private final String name;
    private final Age age;

    public People(String name, Age age) {
        this.name = name;
        Age ageClone = new Age ();
        ageClone.setAge(age.getAge());
        this.age = ageClone;
    }

    public People setName(String name) {
        return new People(name, this.age);
    }

    public People setAge(Age age) {
        return new People(this.name, age);
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