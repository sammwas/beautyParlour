import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;


public class Stylist {
  private String name;
  private String skills;
  private int id;

  public Stylist(String name, String skills) {
    this.name = name;
    this.skills = skills;
  }

  public String getName() {
    return name;
  }

  public String getSkills() {
    return skills;
  }

  public List<Client> getClients() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM clients WHERE stylistid=:id";
      return con.createQuery(sql)
      .addParameter("id", this.id)
      .executeAndFetch(Client.class);
    }
  }

  public int getId() {
    return id;
  }

  @Override
    public boolean equals(Object otherStylist){
      if (!(otherStylist instanceof Stylist)) {
        return false;
      } else {
        Stylist newStylist = (Stylist) otherStylist;
        return this.getName().equals(newStylist.getName()) &&
              this.getId() == newStylist.getId() &&
              this.getSkills().equals(newStylist.getSkills());
      }
    }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO stylists (name, skills) VALUES (:name, :skills)";
      this.id=(int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("skills", this.skills).executeUpdate().getKey();
    }
  }

  public static Stylist find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM stylists WHERE id= :id";
      Stylist stylist = con.createQuery(sql).addParameter("id", id).executeAndFetchFirst(Stylist.class);
      return stylist;
    }
  }

  public static List<Stylist> all() {
    String sql = "SELECT * FROM stylists";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Stylist.class);
    }
  }

  public void update(String name, String skills) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE stylists SET name = :name, skills =:skills  WHERE id = :id";
      con.createQuery(sql)
        .addParameter("name", name)
        .addParameter("skills", skills)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM stylists WHERE id = :id";
      con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  }



}
