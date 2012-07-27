/*
 * $Id: JavaBeanPanel.java 139 2004-10-25 12:06:22Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.masterdetail;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.table.TableModel;

import org.jdesktop.jdnc.incubator.rbair.JNTable;
import org.jdesktop.jdnc.incubator.rbair.masterdetail.kleopatra.*;
import org.jdesktop.jdnc.incubator.rbair.swing.binding.TableBinding;
import org.jdesktop.jdnc.incubator.rbair.swing.data.*;

/**
 * This demo panel implementation is for basic JavaBeans. It has no asynchronous
 * behavior because the beans have been pre-loaded into memory for the demo.
 * A very similar panel could be used if hibernate were being used, for example.
 * 
 * @author Richard Bair
 */
class JavaBeanPanel extends DemoPanel {

  private JavaBeanDataModel itemsDM;
  private JavaBeanDataModel userDM;
  private JavaBeanDataModel userItemsDM;
  private Transaction tx;
  private DataSource ds;

  JavaBeanPanel() {
  }

  /** this is the only change - to demonstrate adapting
   *  a DataModel to a TableModel and set it as a field
   *  to a wrapping DataModel.
   */
  protected void bindUserItemsTable(JNTable table, String tableFieldName) {
      new TableBinding(table.getTable(), userItemsDM, new String[]{"name", "description"});
//    WDataModelTableModel dataTableModel = new WDataModelTableModel(userItemsDM,
//        new String[] { "name", "description"});
//    WDefaultDataModel wrapperModel = new WDefaultDataModel();
//    wrapperModel.setDataSource(ds);
//    MetaData wrapperMetaData = new MetaData(tableFieldName, TableModel.class);
//    wrapperModel.addField(wrapperMetaData);
//    wrapperModel.setValue(tableFieldName, dataTableModel);
//    new WTableBinding(table.getTable(), wrapperModel, tableFieldName);
  }

  protected void preconfigure() {
    ds = new DefaultDataSource();
  }

  /* (non-Javadoc)
   * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#getItemsDM()
   */
  protected DataModel getItemsDM() {
    try {
      itemsDM = new JavaBeanDataModel(Item.class);
      itemsDM.setKeyFields(new String[] { "item_id"});
      itemsDM.setDataSource(ds);
      return itemsDM;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /* (non-Javadoc)
   * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#getUserDM()
   */
  protected DataModel getUserDM() {
    try {
      userDM = new JavaBeanDataModel(User.class);
      userDM.setKeyFields(new String[] { "userId"});
      userDM.setKey("seller");
      userDM.addField(new MetaData("address.street", String.class, "Street"));
      userDM.addField(new MetaData("address.city", String.class, "City"));
      userDM.addField(new MetaData("address.zipcode", String.class, "Zip"));
      userDM.setMasterFieldName("seller");
      userDM.setMasterDataModel(itemsDM);
      userDM.setDataSource(ds);
      return userDM;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /* (non-Javadoc)
   * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#getUserItemsDM()
   */
  protected DataModel getUserItemsDM() {
    try {
      userItemsDM = new JavaBeanDataModel(Item.class);
      userItemsDM.setKey("items");
      userItemsDM.setKeyFields(new String[] { "item_id"});
      userItemsDM.setMasterFieldName("items");
      userItemsDM.addField(new MetaData("name", String.class, "Name"));
      userItemsDM.addField(new MetaData("description", String.class,
          "Description"));
      userItemsDM.setMasterDataModel(userDM);
      userItemsDM.setDataSource(ds);
      return userItemsDM;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /* (non-Javadoc)
   * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#postconfigure()
   */
  protected void postconfigure() {
    try {
      List items = getItems();
      itemsDM.setJavaBean(items);
      tx = new DefaultTransaction();
      tx.addDataSource(ds);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static List getItems() throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss.S");
    List items = new ArrayList();
    User user = new User(1, "Snow", "White", "iluvapples", "sweetlips",
        "swhite@fairytaleserver.grimm", 1, false, sdf
            .parse("2004-07-03 08:30:00.0"), "1234 Cottage Way", "Woodsville",
        "55555");
    Item item = new Item(1, "Apple",
        "Insomniac? Can't get a decent nights sleep? Try this "
            + "delicious apple! You'll be amazed!", new BigDecimal(35.00),
        new BigDecimal(50.00), sdf.parse("2004-07-03 08:30:00.0"), sdf
            .parse("2004-07-03 08:30:00.0"),
        sdf.parse("2004-07-03 08:30:00.0"), sdf.parse("2004-07-03 08:30:00.0"),
        user);
    items.add(item);
    user = new User(2, "Robin", "Hood", "rhood", "marian",
        "tights@fairytaleserver.grimm", 1, false, sdf
            .parse("2004-07-03 08:30:00.0"), "The Big Oak Tree",
        "Sherwood Forest", "55555");
    item = new Item(2, "Silver Arrow",
        "Need to impress someone? Imagine yourself showing off this "
            + "amazing sterling silver arrow to your friends! You'll be the "
            + "hit of the party! ", new BigDecimal(9050.00), new BigDecimal(
            49999.99), sdf.parse("2004-07-03 08:30:00.0"), sdf
            .parse("2004-07-03 08:30:00.0"),
        sdf.parse("2004-07-03 08:30:00.0"), sdf.parse("2004-07-03 08:30:00.0"),
        user);
    items.add(item);
    item = new Item(3, "Quarterstaff", "Slightly used, excellent condition",
        new BigDecimal(0.00), new BigDecimal(45.00), sdf
            .parse("2004-07-03 08:30:00.0"),
        sdf.parse("2004-07-03 08:30:00.0"), sdf.parse("2004-07-03 08:30:00.0"),
        sdf.parse("2004-07-03 08:30:00.0"), user);
    items.add(item);
    user = new User(3, "Captain", "Nemo", "captain", "dive!",
        "captain@fairytaleserver.grimm", 1, false, sdf
            .parse("2004-07-03 08:30:00.0"), "4433 Dock #3", "Oceanside",
        "55555");
    item = new Item(4, "Calamari",
        "Nemo Enterprises brings you the *best* in deep sea "
            + "Calamari! Direct from the ocean, no preservatives, "
            + "all natural!", new BigDecimal(15.00), new BigDecimal(25.00), sdf
            .parse("2004-07-03 08:30:00.0"),
        sdf.parse("2004-07-03 08:30:00.0"), sdf.parse("2004-07-03 08:30:00.0"),
        sdf.parse("2004-07-03 08:30:00.0"), user);
    items.add(item);
    return items;
  }

  public static final class Address {

    public Address(String street, String city, String zipcode) {
      this.street = street;
      this.city = city;
      this.zipcode = zipcode;
    }

    private String street = "<enter street address>";
    private String zipcode = "";
    private String city = "";

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public String getStreet() {
      return street;
    }

    public void setStreet(String street) {
      this.street = street;
    }

    public String getZipcode() {
      return zipcode;
    }

    public void setZipcode(String zipcode) {
      this.zipcode = zipcode;
    }
  }

  public static final class Item {

    public Item(int itemId, String name, String description,
        BigDecimal initialPrice, BigDecimal reservePrice, Date startDate,
        Date endDate, Date approvalDateTime, Date created, User seller) {
      this.item_id = itemId;
      this.name = name;
      this.description = description;
      this.initialPrice = initialPrice;
      this.reservePrice = reservePrice;
      this.startDate = startDate;
      this.endDate = endDate;
      this.approvalDateTime = approvalDateTime;
      this.created = created;
      this.seller = seller;

      if (seller != null) {
        seller.getItems().add(this);
      }
    }

    private String name = "<enter item name>";
    private String description = "<enter description>";
    //java 1.5 syntax
    //		private BigDecimal initialPrice = BigDecimal.ZERO;
    //		private BigDecimal reservePrice = BigDecimal.ZERO;
    //java 1.4 syntax
    private BigDecimal initialPrice = new BigDecimal(0.00);
    private BigDecimal reservePrice = new BigDecimal(0.00);
    private Date startDate = new Date();
    private Date endDate = new Date();
    private Date approvalDateTime = new Date();
    private Date created = new Date();
    private User approvedBy = null;
    private User seller = null;
    private long item_id;

    public Date getApprovalDateTime() {
      return approvalDateTime;
    }

    public void setApprovalDateTime(Date approvalDateTime) {
      this.approvalDateTime = approvalDateTime;
    }

    public User getApprovedBy() {
      return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
      this.approvedBy = approvedBy;
    }

    public Date getCreated() {
      return created;
    }

    public void setCreated(Date created) {
      this.created = created;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Date getEndDate() {
      return endDate;
    }

    public void setEndDate(Date endDate) {
      this.endDate = endDate;
    }

    public BigDecimal getInitialPrice() {
      return initialPrice;
    }

    public void setInitialPrice(BigDecimal initialPrice) {
      this.initialPrice = initialPrice;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public BigDecimal getReservePrice() {
      return reservePrice;
    }

    public void setReservePrice(BigDecimal reservePrice) {
      this.reservePrice = reservePrice;
    }

    public User getSeller() {
      return seller;
    }

    public void setSeller(User seller) {
      if (this.seller != null && this.seller != seller) {
        this.seller.getItems().remove(this);
      }
      this.seller = seller;
      if (this.seller != null) {
        this.seller.getItems().add(this);
      }
    }

    public Date getStartDate() {
      return startDate;
    }

    public void setStartDate(Date startDate) {
      this.startDate = startDate;
    }

    /**
     * @return Returns the itemId.
     */
    public long getItem_id() {
      return item_id;
    }

    /**
     * @param itemId The itemId to set.
     */
    public void setItem_id(long itemId) {
      this.item_id = itemId;
    }
  }

  public static final class User {

    public User(int userId, String fn, String ln, String un, String passwd,
        String email, int ranking, boolean admin, Date created, String street,
        String city, String zip) {
      this.userId = userId;
      this.firstname = fn;
      this.lastname = ln;
      this.username = un;
      this.password = passwd;
      this.email = email;
      this.ranking = ranking;
      this.admin = admin;
      this.created = created;
      this.address = new Address(street, city, zip);
    }

    private String firstname = "<enter first name>";
    private String lastname = "<enter last name>";
    private String username = "";
    private String password = "";
    private String email = "";
    private int ranking = 0;
    private boolean admin = false;
    private Date created = new Date();
    private Address address = new Address("", "", "");
    private List items = new ArrayList();
    private int userId;

    public boolean isAdmin() {
      return admin;
    }

    public void setAdmin(boolean admin) {
      this.admin = admin;
    }

    public Date getCreated() {
      return created;
    }

    public void setCreated(Date created) {
      this.created = created;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getFirstname() {
      return firstname;
    }

    public void setFirstname(String firstName) {
      this.firstname = firstName;
    }

    public String getLastname() {
      return lastname;
    }

    public void setLastname(String lastName) {
      this.lastname = lastName;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public int getRanking() {
      return ranking;
    }

    public void setRanking(int ranking) {
      this.ranking = ranking;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String userName) {
      this.username = userName;
    }

    public Address getAddress() {
      return address;
    }

    public void setAddress(Address address) {
      this.address = address;
    }

    public List getItems() {
      return items;
    }

    /**
     * @return Returns the userId.
     */
    public int getUserId() {
      return userId;
    }

    /**
     * @param userId The userId to set.
     */
    public void setUserId(int userId) {
      this.userId = userId;
    }
  }
}