package pl.coderslab.model;

import java.util.StringJoiner;

public class Plan {
    private int id;

    private String name;
    private String description;
    private String created;
    private int adminId;

    @Override
    public String toString() {
        return new StringJoiner( ", ", Plan.class.getSimpleName() + "[", "]" )
                .add( "id=" + id )
                .add( "name='" + name + "'" )
                .add( "description='" + description + "'" )
                .add( "created='" + created + "'" )
                .add( "adminId=" + adminId )
                .toString();
    }

    public Plan() {
    }

    public Plan(int id, String name, String description, String created, int adminId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = created;
        this.adminId = adminId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
