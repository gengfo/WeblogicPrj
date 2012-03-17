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

import static java.util.Collections.nCopies;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.bigrez.domain.GuestProfile;
import com.bigrez.domain.Inventory;
import com.bigrez.domain.Money;
import com.bigrez.domain.Property;
import com.bigrez.domain.Rate;
import com.bigrez.domain.Reservation;
import com.bigrez.domain.ReservationRate;
import com.bigrez.domain.RoomType;
import com.bigrez.service.EntityNotFoundException;
import com.bigrez.service.EntityNotTransientException;
import com.bigrez.service.NotFoundException;
import com.bigrez.service.ReservationServices;
import com.bigrez.service.RoomTypeUnavailableException;
import com.bigrez.service.ReservationServices.AvailabilitySummary.Availability;


/**
 * Stateless Session Bean implementation of {@link ReservationServices}.
 *
 * @author Philip Aston
 */
@Stateless
@Local
@Interceptors(LoggingInterceptor.class)
public class ReservationServicesImpl implements ReservationServices {

  @PersistenceContext
  private EntityManager entityManager;

  @EJB
  private EmailServices emailServices;

  /**
   * For unit tests.
   */
  void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /**
   * For unit tests.
   */
  void setEmailServices(EmailServices emailServices) {
    this.emailServices = emailServices;
  }

  private final JPABaseDAO<Inventory> inventoryDAO =
    new JPABaseDAO<Inventory>() {
      @Override protected EntityManager getEntityManager() {
        return entityManager;
      }
    };

  private final JPABaseDAO<Property> propertyDAO =
    new JPABaseDAO<Property>() {
      @Override protected EntityManager getEntityManager() {
        return entityManager;
      }
    };

  private final JPABaseDAO<Rate> rateDAO =
    new JPABaseDAO<Rate>() {
      @Override protected EntityManager getEntityManager() {
        return entityManager;
      }
    };

  private final JPABaseDAO<Reservation> reservationDAO =
    new JPABaseDAO<Reservation>() {
      @Override protected EntityManager getEntityManager() {
        return entityManager;
      }
    };

  private final JPABaseDAO<ReservationRate> reservationRateDAO =
    new JPABaseDAO<ReservationRate>() {
      @Override protected EntityManager getEntityManager() {
        return entityManager;
      }
    };

  private final JPABaseDAO<RoomType> roomTypeDAO =
    new JPABaseDAO<RoomType>() {
      @Override protected EntityManager getEntityManager() {
        return entityManager;
      }
    };

  private final JPABaseDAO<GuestProfile> guestProfileDAO =
    new JPABaseDAO<GuestProfile>() {
      @Override protected EntityManager getEntityManager() {
        return entityManager;
      }
    };

  /**
   * {@inheritDoc}
   */
  @Override
  public List<AvailabilitySummary>
    calculateAvailabilitySummary(Property property,
                                 Date startDate,
                                 int numberOfMonths)
    throws EntityNotFoundException {

    final Property propertyFromDatabase = propertyDAO.checkExists(property);
    final List<RoomType> roomTypes = propertyFromDatabase.getRoomTypes();

    final Calendar calendar = Calendar.getInstance();
    alignDate(calendar, startDate);
    calendar.set(Calendar.DAY_OF_MONTH, 1);

    final Date alignedStartDate = calendar.getTime();
    final int startMonth = month(calendar, alignedStartDate);

    calendar.add(Calendar.MONTH, numberOfMonths);
    final Date alignedEndDate = calendar.getTime();

    final Map<RoomType, AvailabilitySummaryImpl> availability =
      new HashMap<RoomType, AvailabilitySummaryImpl>(roomTypes.size());

    for (RoomType roomType : roomTypes) {
      availability.put(roomType,
                       new AvailabilitySummaryImpl(roomType,
                                                   alignedStartDate,
                                                   numberOfMonths));
    }

    final List<Inventory> inventoriesForProperty=
      inventoryDAO.find(Inventory.QUERY_BY_PROPERTY_AND_DATES,
                        propertyFromDatabase, alignedStartDate, alignedEndDate);

    for (Inventory inventory : inventoriesForProperty) {
      final Integer key = month(calendar, inventory.getDay()) - startMonth;

      availability.get(inventory.getRoomType()).addControl(key, inventory);
    }

    return new ArrayList<AvailabilitySummary>(availability.values());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Integer> calculateAvailability(RoomType roomType,
                                             Date startTime,
                                             Date endTime)
    throws EntityNotFoundException {

    final Date startDate = alignDate(startTime);
    final Date endDate = alignDate(endTime);

    final List<Inventory> inventoryBetweenDates =
      inventoryDAO.find(Inventory.QUERY_BY_ROOMTYPE_AND_DATES,
                        roomTypeDAO.checkExists(roomType), startDate, endDate);

    final List<Integer> result =
      new ArrayList<Integer>(nCopies(numberOfDays(startDate, endDate),
                             UNCONTROLLED));

    for (Inventory i : inventoryBetweenDates) {
      result.set(numberOfDays(startDate, i.getDay()), i.getRoomsAvailable());
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateInventory(RoomType roomType,
                              Date startTime,
                              List<Integer> availableRoomsByDay)
    throws EntityNotFoundException {

    final RoomType roomTypeFromDatabase = roomTypeDAO.checkExists(roomType);

    final Calendar calendar = Calendar.getInstance();
    final Date startDate = alignDate(calendar, startTime);
    calendar.add(Calendar.DAY_OF_YEAR, availableRoomsByDay.size());
    final Date endDate = calendar.getTime();

    final List<Inventory> inventoryBetweenDates =
      inventoryDAO.find(Inventory.QUERY_BY_ROOMTYPE_AND_DATES,
                        roomTypeFromDatabase, startDate, endDate);

    // We don't use a JPQL statement here since we want optimistic locking
    // checks.
    for (Inventory inventory : inventoryBetweenDates) {
      inventoryDAO.delete(inventory);
    }

    calendar.setTime(startDate);

    for (Integer availableRooms : availableRoomsByDay) {
      if (availableRooms != UNCONTROLLED && availableRooms >= 0) {
        final Inventory inventory =
          new Inventory(roomTypeFromDatabase,
                        calendar.getTime(),
                        availableRooms);

        inventoryDAO.create(inventory);
      }

      calendar.add(Calendar.DAY_OF_YEAR, 1);
    }
  }

  private static int numberOfDays(Date startDate, Date endDate) {
    final long ONE_DAY = 1000 * 3600 * 24;

    return
      (int) ((endDate.getTime() + (ONE_DAY - 1) - startDate.getTime())/ONE_DAY);
  }

  private static int month(Calendar calendar, Date date) {
    calendar.setTime(date);
    return calendar.get(Calendar.YEAR) * 12 + calendar.get(Calendar.MONTH);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RateDetails> calculateRates(RoomType roomType,
                                          Date arrivalDate,
                                          Date departureDate)
    throws EntityNotFoundException {

    final List<RateDetails> result = new ArrayList<RateDetails>();

    // Returned rates are ordered by start date.
    final List<Rate> ratesBetweenDates =
      rateDAO.find(Rate.QUERY_BY_ROOMTYPE_AND_DATES,
                   roomTypeDAO.checkExists(roomType),
                   arrivalDate, departureDate);

    // There shouldn't be overlaps in Rates. Ideally we'd assert that.
    for (final Rate rate : ratesBetweenDates) {

      final Date rateStartDate = rate.getStartDate();
      final Date rateEndDate = rate.getEndDate();

      result.add(new RateDetailsImpl(
        rateStartDate.compareTo(arrivalDate) < 0 ? arrivalDate : rateStartDate,
        rateEndDate.compareTo(departureDate) > 0 ? departureDate : rateEndDate,
        rate.getPrice()));
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<AvailabilityAndRates> calculateRatesAndAvailabilty(
    Property property, Date arrivalTime, Date departureTime)
    throws EntityNotFoundException {

    final List<RoomType> roomTypes =
      propertyDAO.checkExists(property).getRoomTypes();

    final Calendar calendar = Calendar.getInstance();
    final Date arrivalDate = alignDate(calendar, arrivalTime);
    final Date departureDate = alignDate(calendar, departureTime);

    final List<AvailabilityAndRates> result =
      new ArrayList<AvailabilityAndRates>(roomTypes.size());

    for (RoomType roomType : roomTypes) {
      final List<Inventory> inventoryBetweenDates =
        inventoryDAO.find(Inventory.QUERY_BY_ROOMTYPE_AND_DATES,
                          roomType, arrivalDate, departureDate);

      final List<Date> blockingDates = new ArrayList<Date>();

      for (Inventory i : inventoryBetweenDates) {
        if (i.getRoomsAvailable() == 0) {
          calendar.setTime(arrivalDate);
          calendar.add(Calendar.DAY_OF_YEAR,
                       numberOfDays(arrivalDate, i.getDay()));
          blockingDates.add(calendar.getTime());
        }
      }

      final List<RateDetails> rates =
        calculateRates(roomType, arrivalDate, departureDate);

      result.add(new AvailabilityAndRatesImpl(roomType, rates, blockingDates));
    }

    return result;
  }

  /**
   * {@inheritDoc}
   * @throws EntityNotTransientException
   */
  @Override
  public Reservation createReservation(Reservation reservation,
                                       List<RateDetails> offeredRates)
    throws EntityNotFoundException,
           RoomTypeUnavailableException,
           EntityNotTransientException {

    reservationDAO.checkTransient(reservation);

    final RoomType roomTypeFromDatabase =
      roomTypeDAO.checkExists(reservation.getRoomType());

    final GuestProfile guestProfileFromDatabase =
      guestProfileDAO.checkExists(reservation.getGuestProfile());

    // Fix references to be to managed objects, not detached objects.
    // Otherwise, creating the reservation will fail as the persist will be
    // cascaded to the detached objects.
    reservation.setRoomType(roomTypeFromDatabase);
    reservation.setGuestProfile(guestProfileFromDatabase);

    // First update the inventory, checking we have availability for the
    // full duration.
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(alignDate(calendar, reservation.getArrivalDate()));

    final List<Date> unavailableDates = new ArrayList<Date>();

    final List<Integer> availability =
      calculateAvailability(roomTypeFromDatabase,
                            reservation.getArrivalDate(),
                            reservation.getDepartureDate());

    final ListIterator<Integer> i = availability.listIterator();

    while (i.hasNext()) {
      final int old = i.next();

      if (old == 0) {
        unavailableDates.add(calendar.getTime());
      }
      else {
        assert old > 0;
        i.set(old - 1);
      }

      calendar.add(Calendar.DAY_OF_YEAR, 1);
    }

    updateInventory(
      roomTypeFromDatabase, reservation.getArrivalDate(), availability);

    if (unavailableDates.size() > 0) {
      throw new RoomTypeUnavailableException(roomTypeFromDatabase,
                                              unavailableDates);
    }

    // Now create the reservation.

    // We flush the reservation first to avoid problems with the foreign
    // key constraint and JPA providers that don't do statement re-ordering,
    // such as plain OpenJPA 1.1.0.

    reservationDAO.create(reservation);

    entityManager.flush();

    for (RateDetails offeredRate : offeredRates) {
      final ReservationRate reservationRate =
        new ReservationRate(reservation,
                            offeredRate.getStartDate(),
                            offeredRate.getNumberOfNights(),
                            offeredRate.getPrice());

      reservation.addReservationRate(reservationRate);

      reservationRateDAO.create(reservationRate);
    }

    final String guestEmail = guestProfileFromDatabase.getEmail();

    if (guestEmail != null) {
      emailServices.sendReservationConfirmedEmail(guestEmail, reservation);
    }

    return reservation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public
    Reservation findReservationByExternalIdentity(String externalIdentity)
    throws NotFoundException {
    return reservationDAO.findByExternalIdentity(externalIdentity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteReservation(Reservation reservation)
    throws EntityNotFoundException {

    final Reservation reservationFromDatabase =
      reservationDAO.checkExists(reservation);

    final GuestProfile guestProfile = reservation.getGuestProfile();

    reservation.setGuestProfile(null);

    // Cascades to ReservationRates.
    reservationDAO.delete(reservationFromDatabase);

    final Date startDate = alignDate(reservation.getArrivalDate());
    final Date endDate = alignDate(reservation.getDepartureDate());

    final List<Integer> availability =
      calculateAvailability(reservationFromDatabase.getRoomType(),
                            startDate,
                            endDate);

    ListIterator<Integer> i = availability.listIterator();

    while (i.hasNext()) {
      final int old = i.next();

      assert old >= 0;

      i.set(old + 1);
    }

    updateInventory(
      reservationFromDatabase.getRoomType(), startDate, availability);

    final String guestEmail = guestProfile.getEmail();

    if (guestEmail != null) {
      emailServices.sendReservationCancelledEmail(guestEmail, reservation);
    }
  }

  /**
   * Package scope for unit tests.
   */
  static final class AvailabilitySummaryImpl implements AvailabilitySummary {

    private final RoomType roomType;
    private final Date startDate;
    private List<AvailabilityImpl> byMonth;

    AvailabilitySummaryImpl(RoomType roomType,
                            Date startDate,
                            int numberOfMonths) {
      this.roomType = roomType;
      this.startDate = startDate;

      byMonth = new ArrayList<AvailabilityImpl>(numberOfMonths);

      for (int i = 0; i < numberOfMonths; ++i) {
        byMonth.add(new AvailabilityImpl());
      }
    }

    void addControl(int month, Inventory inventory) {
      assert month >=0;
      assert month < byMonth.size();

      byMonth.get(month).addControl(inventory);
    }

    @Override public RoomType getRoomType() {
      return roomType;
    }

    @Override public Date getStartDate() {
      return startDate;
    }

    @Override public List<Availability> getAvailabilityByMonth() {
      return Collections.<Availability>unmodifiableList(byMonth);
    }
  }

  /**
   * Package scope for unit tests.
   *
   * Equality and toString() are defined for the unit tests.
   */
  static final class AvailabilityImpl implements Availability {
    private int controls = 0;
    private int closeOuts = 0;

    AvailabilityImpl() {
      this(0, 0);
    }

    /** Used by unit tests. */
    AvailabilityImpl(int controls, int closeOuts) {
      this.controls = controls;
      this.closeOuts = closeOuts;
    }

    void addControl(Inventory inventory) {
      ++controls;

      if (inventory.getRoomsAvailable() == 0) {
        ++closeOuts;
      }
    }

    @Override public int getControls() {
      return controls;
    }

    @Override public int getCloseOuts() {
      return closeOuts;
    }

    @Override public int hashCode() {
      return controls + 31 * closeOuts;
    }

    @Override public boolean equals(Object o) {
      if (o == this) {
        return true;
      }

      if (!(o instanceof AvailabilityImpl)) {
        return false;
      }

      final AvailabilityImpl other = (AvailabilityImpl)o;

      return controls == other.controls && closeOuts == other.closeOuts;
    }

    @Override public String toString() {
      return new Formatter().format("(%d, %d)", controls, closeOuts).toString();
    }
  }

  /**
   * Package scope for unit tests.
   */
  static final class RateDetailsImpl implements RateDetails {

    private final Date startDate;
    private int numberOfNights;
    private final Money price;

    RateDetailsImpl(Rate rate) {
      this(rate.getStartDate(), rate.getEndDate(), rate.getPrice());
    }

    RateDetailsImpl(Date startDate, Date endDate, Money price) {
      this.startDate = startDate;
      numberOfNights = numberOfDays(startDate, endDate);
      this.price = price;
    }

    @Override public int getNumberOfNights() {
      return numberOfNights;
    }

    @Override public Money getPrice() {
      return price;
    }

    @Override public Date getStartDate() {
      return startDate;
    }

    @Override public int hashCode() {
      int result = 17;
      result = 31 * result + startDate.hashCode();
      result = 31 * result + numberOfNights;
      result = 31 * result + price.hashCode();
      return result;
    }

    @Override public boolean equals(Object o) {
      if (o == this) {
        return true;
      }

      if (!(o instanceof RateDetailsImpl)) {
        return false;
      }

      final RateDetailsImpl other = (RateDetailsImpl)o;

      return numberOfNights == other.numberOfNights &&
             startDate.equals(other.startDate) &&
             price.equals(other.price);
    }

    @Override public String toString() {
      return new Formatter().format("RateDetails(%.2f, %tF, %d)",
          price.getAmount(), startDate, numberOfNights).toString();
    }
  }

  /**
   * Package scope for unit tests.
   */
  static final class AvailabilityAndRatesImpl implements AvailabilityAndRates {
    private final RoomType roomType;
    private final List<RateDetails> rateDetailsList;
    private final List<Date> blockingDatesList;

    AvailabilityAndRatesImpl(RoomType roomType,
                             List<RateDetails> rateDetailsList,
                             List<Date> blockingDatesList) {
      this.roomType = roomType;
      this.rateDetailsList = rateDetailsList;
      this.blockingDatesList = blockingDatesList;
    }

    @Override public RoomType getRoomType() {
      return roomType;
    }

    @Override public List<RateDetails> getRates() {
      return rateDetailsList;
    }

    @Override public List<Date> getBlockingDates() {
      return blockingDatesList;
    }

    @Override public int hashCode() {
      int result = roomType.hashCode();
      result = 31 * result + rateDetailsList.hashCode();
      result = 31 * result + blockingDatesList.hashCode();
      return result;
    }

    @Override public boolean equals(Object o) {
      if (o == this) {
        return true;
      }

      if (!(o instanceof AvailabilityAndRatesImpl)) {
        return false;
      }

      final AvailabilityAndRatesImpl other = (AvailabilityAndRatesImpl)o;

      return roomType.equals(other.roomType) &&
             rateDetailsList.equals(other.rateDetailsList) &&
             blockingDatesList.equals(other.blockingDatesList);
    }
  }

  private static Date alignDate(Date date) {
    return alignDate(Calendar.getInstance(), date);
  }

  private static Date alignDate(Calendar calendar, Date date) {
    calendar.setTime(date);
    calendar.set(Calendar.AM_PM, Calendar.AM);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }
}
