package uk.co.osbald.sample;

import java.util.Date;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston (www.osbald.co.uk)
 * Date: 20-Nov-2006
 * Time: 14:46:41
 */

public class Module {
    String name;
    long id;
    Date from;

    public Module(long id, String name, Date from) {
        this.id = id;
        this.name = name;
        this.from = from != null ? from : new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public String toString() {
        return "Module{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", from=" + from +
                '}';
    }
}
