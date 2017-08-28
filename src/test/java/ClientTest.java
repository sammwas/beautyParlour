import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
public class ClientTest{

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Client_instantiatesCorrectly_true(){
      Client newClient = new Client("Wendy", 1);
      assertEquals(true, newClient instanceof Client);
  }
  @Test
  public void getName_clientsInstantiateWithName_String() {
      Client newClient = new Client("Wendy", 1);
      assertEquals("Wendy", newClient.getName());
  }
  @Test
  public void getId_clientsInstantiateCorrectlyWithAnId_1() {
      Client newClient = new Client("Wendy", 1);
      newClient.save();
      assertTrue(newClient.getId() > 0);
  }
  //this test is related to the After Test above which clears the clients table after each test
  //the .all() method is static hence capable of affecting the whole class
  @Test
  public void clear_emptiesAllClientsFromArrayList_0() {
      Client newClient =  new Client("Wendy", 1);
      assertEquals(Client.all().size(), 0);
  }
  //using the find save method to make entries and later execute find method for acccurate assertion
  @Test
  public void find_returnsClientsWithSameStylistId_clientTwo() {
      Client clientOne = new Client("Maria", 1);
      clientOne.save();
      Client clientTwo = new Client("Shiro",1);
      clientTwo.save();
      assertEquals(Client.find(clientTwo.getId()),clientTwo);
  }
  @Test
  public void equals_returnsTrueIfDescriptionsAreTheSame() {
      Client clientOne = new Client("Maria", 1);
      Client clientTwo = new Client("Maria",1);
      assertTrue(clientOne.equals(clientTwo));
  }
  @Test
  public void save_assignsIdToClientObject() {
      Client newClient = new Client("Wendy",1);
      newClient.save();
      Client savedClient = Client.all().get(0);
      assertEquals(newClient.getId(), savedClient.getId());
  }
  @Test
  public void save_savesStylistIdIntoDB_true() {
      Stylist myStylist = new Stylist("Makena", "dreadlocks");
      myStylist.save();
      Client newClient = new Client("Wendy",myStylist.getId());
      newClient.save();
      Client savedClient = Client.find(newClient.getId());
      assertEquals(savedClient.getStylistId(), myStylist.getId());
  }
  @Test
  public void update_updateClientName_true() {
      Client newClient =  new Client("Wendy",1);
      newClient.save();
      newClient.update("Kendi");
      assertEquals("Kendi", Client.find(newClient.getId()).getName());
  }
  @Test
  public void delete_deletesClient_true() {
      Client newClient = new Client("Wendy", 1);
      newClient.save();
      int clientId = newClient.getId();
      newClient.delete();
      assertEquals(null, Client.find(clientId));
  }
}