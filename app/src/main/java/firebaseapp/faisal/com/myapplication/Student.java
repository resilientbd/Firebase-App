package firebaseapp.faisal.com.myapplication;

/**
 * Created by ussl-01 on 3/21/2018.
 */

public class Student {
    private String id;
    private String name;
    private String department;
    private String contact;

    public Student() {
    }

    public Student(String id, String name, String department, String contact) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
