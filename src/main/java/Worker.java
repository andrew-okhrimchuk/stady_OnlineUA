public class Worker {

    private String name;
    private int age;
    private int level;

    public Worker (String name,int age, int level){
        this.name = name;
        this.age = age;
        this.level = level;
    }

    public String getName(){
        return this.name;
    }

    public int getAge(){
        return this.age;
    }

    public int getLevel(){
        return this.level;
    }


    public void setName(String name){
        this.name = name;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setLevel(int level){
        this.level = level;
    }

    @Override
    public boolean equals (Object o) {
        if(o == this)return true;
        if(o == null)return false;
        if(o  instanceof Worker){
            Worker worker  = (Worker) o;
            return this.name.equals(worker.name) && this.age == worker.age &&  this.level == worker.level;
        }
        else return false;
    }

    @Override
    public int hashCode (){
        int key = 7;
        return ((this.name == null ? 0 : this.name.hashCode ())*key + age)*key + level;
    }

}