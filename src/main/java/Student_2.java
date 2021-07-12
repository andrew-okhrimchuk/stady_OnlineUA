public class Student_2 {

    private String name;
    private  int age;
    private String group;

    @Override
    public boolean equals  (Object o){

        if (o==this) return true;
        if (o==null) return false;
        if (o instanceof Student_2 ) {

            Student_2 student_2 = (Student_2 ) o;
            return this.name.equals(student_2.name) && this.age == student_2 .age && this.group.equals(student_2.group);
        }
        else return false;
    }

    @Override
    public int hashCode (){
        int key = 25;
        return ((name == null ? 0 : name.hashCode ())*key + age) *key + group == null ? 0 : group.hashCode ();
    }

}