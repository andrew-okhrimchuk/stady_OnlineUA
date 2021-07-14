public class Student_3 {

    String name, group;
    int age;

    @Override
    public boolean equals (Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (o instanceof Student_3 ) {
            Student_3  stud = (Student_3 ) o;
            return this.name.equals(stud.name) && this.group.equals(stud.group) && this.age == stud.age;
        }
        else return false;
    }

    @Override
    public int hashCode (){
        int key = 25;

        return ((name == null ? 0 : name.hashCode())*key + age)*key + group == null ? 0 : group.hashCode();
    }
}