// Copyright (C) 2008, 2009 Philip Aston
// All rights reserved.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package com.bigrez.service.impl;

import static com.bigrez.testutilties.AssertUtilties.asSet;
import static com.bigrez.testutilties.AssertUtilties.assertContainsAll;
import static com.bigrez.testutilties.AssertUtilties.assertNotEquals;
import static com.bigrez.testutilties.Serializer.serialize;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import com.bigrez.domain.Address;
import com.bigrez.domain.Money;
import com.bigrez.domain.Offer;
import com.bigrez.domain.Property;
import com.bigrez.domain.Rate;
import com.bigrez.domain.RoomType;
import com.bigrez.jpa.DomainEntity;
import com.bigrez.service.EntityNotFoundException;
import com.bigrez.service.NotFoundException;
import com.bigrez.service.PropertyServices;
import com.bigrez.testutilties.AbstractEntityManagerTests;
import com.bigrez.testutilties.RandomObjectFactory;


/**
 * Unit tests for {@link PropertyServices}.
 *
 * @author Philip Aston
 */
public class TestPropertyServices extends AbstractEntityManagerTests {

  private PropertyServicesImpl propertyServices;

  @SuppressWarnings("unchecked")
  private List<Offer> findAllOffers() {
    return getEntityManager().createQuery("select o from Offer o")
           .getResultList();
  }

  @Before public void setUp() throws Exception {
    propertyServices = new PropertyServicesImpl();
    propertyServices.setEntityManager(getEntityManager());

    // Delete existing entities. Remember, we rollback this transaction after
    // the test so this only affects the view seen by test transaction.
    getEntityManager().createQuery("delete from Offer").executeUpdate();
    getEntityManager().createQuery("delete from Rate").executeUpdate();
    getEntityManager().createQuery("delete from Inventory").executeUpdate();
    getEntityManager().createQuery("delete from ReservationRate").executeUpdate();
    getEntityManager().flush();
    getEntityManager().createQuery("delete from Reservation").executeUpdate();
    getEntityManager().flush();
    getEntityManager().createQuery("delete from RoomType").executeUpdate();
    getEntityManager().createQuery("delete from Property").executeUpdate();
  }

  private static final RandomObjectFactory randomObjectFactory =
    new RandomObjectFactory();
  private static final Random random = new Random();

  private abstract class CRUDTestTemplate<T extends DomainEntity> {
    public void run() throws Exception {
      final T t1 = createInstance();

      assertNull(t1.findEntity(getEntityManager()));

      try {
        assertEquals(0, t1.getExternalIdentity());
        fail("Expected IllegalStateException");
      }
      catch (IllegalStateException e) {
      }

      // A side effect of the createInstance() above is that our transient
      // entity t1 may be in a cascade persist relationship, and will become
      // managed when flushed. We do so not, otherwise we'll end up with
      // t1 and t2 being distinct entities.
      getEntityManager().flush();

      final boolean t1Managed = getEntityManager().contains(t1);

      final T t2 = createOrUpdate(t1);

      assertTrue(!t1Managed || t1.equals(t2));

      assertNotNull(t2.getExternalIdentity());
      assertSame(t2, findByExternalIdentity(t2.getExternalIdentity()));

      getEntityManager().flush();
      getEntityManager().clear();

      final T t3 = t2.<T>findEntity(getEntityManager());
      assertNotSame(t2, t3);
      assertEquals(t2, t3);

      modifyField(t3);
      getEntityManager().flush();
      getEntityManager().clear();

      final T t4 = t2.<T>findEntity(getEntityManager());
      assertModified(t4);

      modifyField(t4);
      createOrUpdate(t4);

      getEntityManager().flush();
      getEntityManager().clear();

      final T t5 = t2.<T>findEntity(getEntityManager());
      assertNotNull(t5);
      assertModified(t5);

      final T t6 = createInvalidInstance();

      if (t6 != null) {
        try {
          createOrUpdate(t6);
          fail("Expected NotFoundException");
        }
        catch (EntityNotFoundException e) {
          checkNotFoundException(t6, e);
        }
      }

      getEntityManager().clear();

      final T t7 = createInstance();

      try {
        delete(t7);
        fail("Expected NotFoundException");
      }
      catch (EntityNotFoundException e) {
      }

      persistAndFlush(t7);

      assertNotNull(t7.findEntity(getEntityManager()));

      delete(t7);

      assertNull(t7.findEntity(getEntityManager()));

      try {
        findByExternalIdentity(t7.getExternalIdentity());
        fail("Expected NotFoundException");
      }
      catch (NotFoundException e) {
      }

      // Try once more with a detached object.
      final T t8 = createInstance();

      getEntityManager().getTransaction().rollback();
      getEntityManager().getTransaction().begin();

      try {
        delete(t8);
        fail("Expected NotFoundException");
      }
      catch (EntityNotFoundException e) {
      }
    }

    protected abstract T createInstance();
    protected abstract T createOrUpdate(T t) throws EntityNotFoundException;
    protected abstract T findByExternalIdentity(String externalIdentity)
      throws NotFoundException;
    protected abstract void modifyField(T t);
    protected abstract void assertModified(T t);
    protected abstract T createInvalidInstance();
    protected abstract void checkNotFoundException(T t,
                                                   EntityNotFoundException e);
    protected abstract void delete(T t) throws EntityNotFoundException;
  }

  @Test public void testCreateOrUpdateProperty() throws Exception {
    new CRUDTestTemplate<Property>() {
      private String lastDescription;

      @Override protected Property createInstance() {
        return createTransientProperty();
      }

      @Override
      protected Property createOrUpdate(Property t)
        throws EntityNotFoundException {
        return propertyServices.createOrUpdate(t);
      }

      @Override
      protected Property findByExternalIdentity(String externalIdentity)
        throws NotFoundException {
        return
          propertyServices.findPropertyByExternalIdentity(externalIdentity);
      }

      @Override protected void modifyField(Property t) {
        lastDescription = "x" +
          truncate(randomObjectFactory.generateParameter(String.class), 20);
        t.setDescription(lastDescription);
      }
      @Override
      protected void assertModified(Property t) {
        assertEquals(lastDescription, t.getDescription());
      }

      @Override protected Property createInvalidInstance() {
        return null;
      }

      @Override
      protected void checkNotFoundException(Property t,
                                            EntityNotFoundException e) {
        fail("Called");
      }

      @Override
      protected void delete(Property t) throws EntityNotFoundException {
        propertyServices.delete(t);
      }
    }.run();
  }

  @Test public void testCreateOrUpdateRoomType() throws Exception {

    new CRUDTestTemplate<RoomType>() {
      private String lastDescription;

      @Override protected RoomType createInstance() {
        return createTransientRoomType(createProperty());
      }

      @Override
      protected RoomType createOrUpdate(RoomType t)
        throws EntityNotFoundException {
        return propertyServices.createOrUpdate(t);
      }

      @Override
      protected RoomType findByExternalIdentity(String externalIdentity)
        throws NotFoundException {
        return
          propertyServices.findRoomTypeByExternalIdentity(externalIdentity);
      }

      @Override protected void modifyField(RoomType t) {
        lastDescription = "x" +
          truncate(randomObjectFactory.generateParameter(String.class), 20);
        t.setDescription(lastDescription);
      }

      @Override protected void assertModified(RoomType t) {
        assertEquals(lastDescription, t.getDescription());
      }

      @Override protected RoomType createInvalidInstance() {
        return createTransientRoomType(createTransientProperty());
      }

      @Override
      protected void checkNotFoundException(RoomType t,
                                            EntityNotFoundException e) {
        assertSame(t.getProperty(), e.getEntity());
      }

      @Override
      protected void delete(RoomType t) throws EntityNotFoundException {
        propertyServices.delete(t);
      }
    }.run();
  }

  @Test public void testCreateOrUpdateRoomTypeDetachedProperty()
    throws Exception {
    // We use a separate EM to create a persistent Property.

    final EntityManager em1 = getEntityManagerFactory().createEntityManager();
    final Property p = createTransientProperty();
    em1.getTransaction().begin();
    em1.persist(p);
    em1.getTransaction().commit();

    try {
      // Now the test.
      final RoomType rt = createTransientRoomType(p);
      propertyServices.createOrUpdate(rt);
    }
    finally {
      // Need to rollback this first to avoid a deadlock.
      getEntityManager().getTransaction().rollback();

      em1.getTransaction().begin();
      em1.remove(p);
      em1.getTransaction().commit();
      em1.close();
    }
  }

  @Test public void testCreateOrUpdateRate() throws Exception {

    new CRUDTestTemplate<Rate>() {
      private Money lastMoney;

      @Override protected Rate createInstance() {
        final Rate rate = new Rate();
        rate.setRoomType(createRoomType(createProperty()));
        rate.setStartDate(new Date(1));
        rate.setEndDate(new Date(2));
        rate.setPrice(new Money(new BigDecimal(1)));
        return rate;
      }

      @Override
      protected Rate createOrUpdate(Rate t) throws EntityNotFoundException {
        return propertyServices.createOrUpdate(t);
      }

      @Override
      protected Rate findByExternalIdentity(String externalIdentity)
        throws NotFoundException {
        return propertyServices.findRateByExternalIdentity(externalIdentity);
      }

      @Override protected void modifyField(Rate t) {
        lastMoney = new Money(new BigDecimal(random.nextInt(10000)));
        t.setPrice(lastMoney);
      }

      @Override protected void assertModified(Rate t) {
        assertEquals(lastMoney, t.getPrice());
      }

      @Override protected Rate createInvalidInstance() {
        final Rate rate = new Rate();
        rate.setStartDate(new Date(1));
        rate.setEndDate(new Date(2));
        rate.setRoomType(createTransientRoomType(createProperty()));
        rate.setPrice(new Money(new BigDecimal(1)));
        return rate;
      }

      @Override
      protected void checkNotFoundException(Rate t, EntityNotFoundException e) {
        assertSame(t.getRoomType(), e.getEntity());
      }

      @Override protected void delete(Rate t) throws EntityNotFoundException {
        propertyServices.delete(t);
      }
    }.run();
  }

  @Test public void testCreateOrUpdateOffer() throws Exception {

    new CRUDTestTemplate<Offer>() {
      private String lastCaption;

      @Override protected Offer createInstance() {
        return new Offer(createProperty(), "image", "caption", "description");
      }

      @Override
      protected Offer createOrUpdate(Offer t) throws EntityNotFoundException {
        return propertyServices.createOrUpdate(t);
      }

      @Override
      protected Offer findByExternalIdentity(String externalIdentity)
        throws NotFoundException {
        return propertyServices.findOfferByExternalIdentity(externalIdentity);
      }

      @Override protected void modifyField(Offer t) {
        lastCaption = "x" +
          truncate(randomObjectFactory.generateParameter(String.class), 20);
        t.setCaption(lastCaption);
      }

      @Override protected void assertModified(Offer t) {
        assertEquals(lastCaption, t.getCaption());
      }

      @Override protected Offer createInvalidInstance() {
        return new Offer(
          createTransientProperty(),"image", "caption", "description");
      }

      @Override
      protected void checkNotFoundException(Offer t,
                                            EntityNotFoundException e) {
        assertSame(t.getProperty(), e.getEntity());
      }

      @Override protected void delete(Offer t) throws EntityNotFoundException {
        propertyServices.delete(t);
      }
    }.run();
  }

  private Property p1;
  private Property p2;
  private Property p3;
  private Property p4;

  private void createProperties() {
    p1 = createTransientProperty();
    p1.setAddress(new Address("a1", "a2", "city", "ST", "zip"));
    p2 = createTransientProperty();
    p2.setAddress(new Address("a1", "a2", "New York", "NY", "zip"));
    p3 = createTransientProperty();
    p3.setAddress(new Address("a1", "a2", "Los Angeles", "CA", "zip"));
    p4 = createTransientProperty();
    p4.setAddress(new Address("a1", "a2", "San Francisco", "CA", "zip"));
    persistAndFlush(p1, p2, p3, p4);
  }

  @Test public void testPropertyFindAll() throws Exception {
    assertEquals(emptyList(), propertyServices.findAll());

    createProperties();
    getEntityManager().clear(); // Detach everything.

    final List<Property> findAllResult = propertyServices.findAll();

    assertEquals(asSet(p1, p2, p3, p4), asSet(findAllResult));

    // Now check we've not returned the Offers and RoomTypes.

    // Clearing, or even closing the EM is not enough to stop EclipseLink
    // from loading associations on demand.
    final Property trulyDetached = serialize(findAllResult.get(0));

    try {
      trulyDetached.getOffers().size();
      fail("Expected exception");
    }
    catch (Exception e) { // Exception is not standardised by JPA.
    }

    try {
      trulyDetached.getRoomTypes().size();
      fail("Expected exception");
    }
    catch (Exception e) {
    }
  }

  private void assertPopulated(Property property) throws Exception {
    // Serialize/deserialize to ensure detached.
    final Property trulyDetached = serialize(property);

    assertNotNull(trulyDetached.getOffers());
    assertNotNull(trulyDetached.getRoomTypes());
  }

  @Test public void testFindByCityAndState() throws Exception {
    assertEquals(emptyList(), propertyServices.findByCityAndState(null, null));
    assertEquals(emptyList(), propertyServices.findByCityAndState(null, "CA"));
    assertEquals(emptyList(),
      propertyServices.findByCityAndState("Loss Angeles", "CA"));

    createProperties();
    getEntityManager().clear(); // Detach everything.

    final List<Property> byState =
      propertyServices.findByCityAndState(null, "CA");

    assertEquals(asSet(p3, p4), asSet(byState));
    assertPopulated(byState.get(0));

    final List<Property> byCityAndState =
      propertyServices.findByCityAndState("Los Angeles", "CA");
    assertEquals(asSet(p3), asSet(byCityAndState));
    assertPopulated(byCityAndState.get(0));

    final List<Property> byCity =
      propertyServices.findByCityAndState("Los Angeles", "");
    assertEquals(asSet(p3), asSet(byCity));
    assertPopulated(byCity.get(0));

    final List<Property> all = propertyServices.findByCityAndState(null, null);
    assertEquals(asSet(p1, p2, p3, p4), asSet(all));
    assertPopulated(all.get(0));
  }

  @Test public void testGetOffersForDisplayNoSearchParameters()
    throws Exception {

    assertEquals(emptyList(),
                 propertyServices.getOffersForDisplay(null, null, null, 0));

    assertEquals(emptyList(),
                 propertyServices.getOffersForDisplay(null, "", "", 10));

    final Property property = createProperty();

    for (int i = 0; i < 20; ++i) {
      final Offer offer =
        new Offer(property, "image" + i, "caption" + i, "description" + i);
      getEntityManager().persist(offer);
    }

    // Clear since Property doesn't let us modify its offers list.
    getEntityManager().flush();
    getEntityManager().clear();

    final Set<Offer> allOffers = new HashSet<Offer>(findAllOffers());

    final List<Offer> randomOffers =
      propertyServices.getOffersForDisplay(null, "", null, 10);

    assertEquals(10, randomOffers.size());
    assertContainsAll(allOffers, randomOffers);
  }

  @Test public void testGetOffersForDisplayByProperty() throws Exception {

    final Property property1 = createProperty();
    final Property property2 = createProperty();

    for (int i = 0; i < 10; ++i) {
      final Offer offer = new Offer(i < 5 ? property1 : property2,
                                    "image" + i,
                                    "caption" + i,
                                    "description" + i);
      getEntityManager().persist(offer);
    }

    // Clear since Property doesn't let us modify its offers list.
    // Clear since Property doesn't let use modify its offers list.
    getEntityManager().flush();
    getEntityManager().clear();

    final Set<Offer> allOffers = new HashSet<Offer>(findAllOffers());

    final List<Offer> property2Offers =
      propertyServices.getOffersForDisplay(property2, null, "ignored", 5);
    assertEquals(5, property2Offers.size());

    for (Offer o : property2Offers) {
      assertEquals(property2, o.getProperty());
    }

    final List<Offer> property1Offers =
      propertyServices.getOffersForDisplay(property1, null, "ignored", 10);

    assertEquals(10, property1Offers.size());
    assertTrue(allOffers.containsAll(property1Offers));
    assertEquals(asSet(property2Offers), asSet(property1Offers.subList(5, 10)));
  }

  @Test public void testGetOffersForDisplayByCityAndState() throws Exception {

    final Property property1 = createTransientProperty();
    property1.setAddress(new Address("line1", "line2", "Foo", "BA", "zip"));

    final Property property2 = createTransientProperty();
    property2.setAddress(new Address("1", "2", "Box Elder", "MO", "zip"));

    final Property property3 = createTransientProperty();
    property3.setAddress(new Address("line1", "line2", "Foo", "LA", "zip"));

    persistAndFlush(property1, property2, property3);

    for (int i = 0; i < 10; ++i) {
      final Property p = i < 5 ? property1 : (i < 9 ? property2 : property3);
      final Offer offer = new Offer(p,
                                    "image" + i,
                                    "caption" + i,
                                    "description" + i);
      getEntityManager().persist(offer);
    }

    getEntityManager().flush();

    assertEquals(2,
      propertyServices.getOffersForDisplay(null, "", "BA", 2).size());

    final List<Offer> baOffers =
      propertyServices.getOffersForDisplay(null, "", "BA", 20);

    assertEquals(10, asSet(baOffers).size());

    for (int i = 0; i < 5; ++i) {
      assertEquals(property1, baOffers.get(i).getProperty());
      assertNotEquals(property1, baOffers.get(i + 5).getProperty());
    }

    final List<Offer> fooOffers =
      propertyServices.getOffersForDisplay(null, "Foo", "", 6);

    assertEquals(6, asSet(fooOffers).size());

    assertContainsAll(fooOffers, baOffers.subList(0, 5));

    final List<Offer> fooBAOffers =
      propertyServices.getOffersForDisplay(null,"Foo", "BA", 10);

    assertEquals(10, fooBAOffers.size());
    assertContainsAll(baOffers.subList(0,5), fooBAOffers.subList(0, 5));

    try {
      propertyServices.getOffersForDisplay(
        createTransientProperty(), "", "", 100);
      fail("Expected NotFoundException");
    }
    catch (EntityNotFoundException e) {
    }
  }

  @Test public void testGetStateCodes() throws Exception {
    assertEquals(emptyList(), propertyServices.getAllStateCodes());

    final List<String> codes = Arrays.asList("XX", "AB", "ZY", "PU");

    for (String code : codes) {
      final Property p1 = createProperty();
      p1.setAddress(new Address("addr1", "addr2", "city", code, "zip"));
    }

    getEntityManager().flush();

    Collections.sort(codes);

    assertEquals(codes, propertyServices.getAllStateCodes());
  }

  @Test public void testGetCities() throws Exception {
    assertEquals(emptyList(), propertyServices.getAllCities());

    final List<String> cities = Arrays.asList(
      "New York", "Dallas", "San Francisco", "Chicago");

    for (String city : cities) {
      final Property p1 = createProperty();
      p1.setAddress(new Address("addr1", "addr2", city, "ST", "zip"));
    }

    getEntityManager().flush();

    Collections.sort(cities);

    assertEquals(cities, propertyServices.getAllCities());
  }

  private static String truncate(String s, int length) {
    if (s.length() > length) {
      return s.substring(0, length);
    }

    return s;
  }

  @Test public void testFindRateByRoomType() throws Exception {

    try {
      propertyServices.findRatesByRoomType(
        createTransientRoomType(createProperty()));
      fail("Expected EntityNotFoundException");
    }
    catch (EntityNotFoundException e) {
    }

    final RoomType roomType1 = createRoomType(createProperty());
    final RoomType roomType2 = createRoomType(createProperty());

    assertEquals(emptyList(), propertyServices.findRatesByRoomType(roomType1));

    final Rate r1 = new Rate();
    r1.setRoomType(roomType1);
    r1.setStartDate(new Date(1));
    r1.setEndDate(new Date(2));
    r1.setPrice(new Money(new BigDecimal(1)));

    final Rate r2 = new Rate();
    r2.setRoomType(roomType2);
    r2.setStartDate(new Date(1));
    r2.setEndDate(new Date(2));
    r2.setPrice(new Money(new BigDecimal(1)));

    final Rate r3 = new Rate();
    r3.setRoomType(roomType1);
    r3.setStartDate(new Date(1));
    r3.setEndDate(new Date(2));
    r3.setPrice(new Money(new BigDecimal(1)));

    persistAndFlush(r1, r2, r3);

    assertEquals(asSet(r1, r3),
                 asSet(propertyServices.findRatesByRoomType(roomType1)));

    assertEquals(asSet(r2),
      asSet(propertyServices.findRatesByRoomType(roomType2)));
  }
}
