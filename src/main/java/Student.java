public class Student {

    private String name;
    private int age;
    private String group;

    @Override
    public boolean equals (Object o) {
        if (o == this) {
            return true;
        }
        if (o == null ) return false;
        if (o instanceof Student) {
            Student user = (Student) o;
            return user.age == this.age && user.name.equals(this.name) && user.group.equals(this.group);
        }
        else
            return false;
    }

    @Override
    public int hashCode() {
        int key = 25;
        return ((name == null ? 0 : name.hashCode())*key + age)*key + group == null ? 0 : group.hashCode();
    }
}