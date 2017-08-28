import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class StylistTest {
@Rule
  public DatabaseRule database = new DatabaseRule();

@Test
public void stylist_instantiatesCorrectly_true() {
    Stylist myStylist = new Stylist("Matata", "Braids");
    assertEquals(true, myStylist instanceof Stylist);
}

@Test
public void getName_stylistInstantiatesWithName_Matata() {
    Stylist myStylist = new Stylist("Matata", "Braids");
    assertEquals("Matata", myStylist.getName());
}

@Test
public void getName_stylistInstantiatesWithSkills_Braids() {
    Stylist myStylist = new Stylist("Matata", "Braids");
    assertEquals("Braids", myStylist.getSkills());
}

@Test
public void getId_stylistInstantiatesWithAnId_1() {
    Stylist myStylist = new Stylist("Matata", "Braids");
    myStylist.save();
    assertTrue(myStylist.getId() > 0);  
}

@Test
public void all_returnsAllInstancesOfStylist_true() {
    Stylist stylistOne = new Stylist("Angie", "Bob");
    stylistOne.save();
    Stylist stylistTwo = new Stylist("Akinyi","Kunyoa");
    stylistTwo.save();
    assertEquals(true, Stylist.all().get(0).equals(stylistOne));
    assertEquals(true, Stylist.all().get(1).equals(stylistTwo));
}

@Test
public void clear_clearsAllStylistFromList_0() {
    Stylist newStylist = new Stylist("Mario", "curls");
    assertEquals(Stylist.all().size(), 0);
}

@Test
public void find_returnsStylistWithSameId_stylistTwo() {
    Stylist stylistOne = new Stylist("Angie", "Bob");
    stylistOne.save();
    Stylist stylistTwo = new Stylist("Akinyi","Kunyoa");
    stylistTwo.save();
    assertEquals(Stylist.find(stylistTwo.getId()),stylistTwo);

}

@Test
public void getClients_initiallyReturnsEmptyList_ArrayList() {
    Stylist newStylist = new Stylist("Mwende", "massage");
    assertEquals(0, newStylist.getClients().size());
}

@Test
public void find_returnsNullWhenNoClientsAreFound() {
    assertTrue(Stylist.find(4) == null);
}

@Test
public void equals_returnsTrueIfNamesAreTheSame() {
    Stylist stylistOne = new Stylist("Angie", "Bob");
    Stylist stylistTwo = new Stylist("Angie", "Bob");
    assertTrue(stylistOne.equals(stylistTwo));
}

@Test
public void save_savesIntoDatabase_true(){
  Stylist stylistOne = new Stylist("Angie", "Bob");
  stylistOne.save();
  assertTrue(Stylist.all().get(0).equals(stylistOne));
}

@Test
public void save_assignsIdToObject(){
  Stylist newStylist = new Stylist("Mwende", "massage");
  newStylist.save();
  Stylist savedStylist= Stylist.all().get(0);
  assertEquals(newStylist.getId(), savedStylist.getId());
}

@Test
public void getTasks_retrievesAllTasksFromDatabase_tasksList(){
  Stylist newStylist = new Stylist("Mwende", "massage");
  newStylist.save();
  Client firstClient=new Client("Ian", newStylist.getId());
  firstClient.save();
  Client secondClient= new Client("Vero", newStylist.getId());
  secondClient.save();
  Client[] clients=new Client[] {firstClient, secondClient };
  assertTrue(newStylist.getClients().containsAll(Arrays.asList(clients)));
}
}