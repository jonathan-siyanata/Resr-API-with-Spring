package za.co.jaliatechnologies.buildingarestapiwithspring;

import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class Foo {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;
    private int id;

    public Foo(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
