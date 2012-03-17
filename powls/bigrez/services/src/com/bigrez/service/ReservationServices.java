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

package com.bigrez.service;

import java.util.Date;
import java.util.List;

import com.bigrez.domain.Money;
import com.bigrez.domain.Property;
import com.bigrez.domain.Reservation;
import com.bigrez.domain.RoomType;
import com.bigrez.jpa.DomainEntity;


/**
 * Inventory services.
 *
 * @author Philip Aston
 */
public interface ReservationServices {

  /**
   * Constant used by {@link #calculateAvailability} and
   * {@link #updateInventory} to indicate that a room type has no inventory
   * control for a particular date.
   */
  public static final int UNCONTROLLED = -1;

  /**
   * Produce an AvailabilitySummary for a set of months for the given property.
   *
   * @param property The property.
   * @param startDate A date in the first month.
   * @param numberOfMonths Number of months of data required.
   * @return A summary for each of the property's room types.
   * @throws EntityNotFoundException If {@code property} does not exist in the
   *  database.
   */
  List<AvailabilitySummary> calculateAvailabilitySummary(Property property,
                                                         Date startDate,
                                                         int numberOfMonths)
    throws EntityNotFoundException;

  /**
   * Return the number of rooms available of the particular type for each
   * day between the given dates.
   *
   * @param roomType The room type.
   * @param startDate Start date, inclusive.
   * @param endDate End date, exclusive.
   * @return The number of rooms available for each day. If the room has no
   * inventory control on a particular day, the array entry for that day will
   * contain {@link #UNCONTROLLED}.
   * @throws EntityNotFoundException If {@code roomType} does not exist in the
   *  database.
   */
  List<Integer> calculateAvailability(RoomType roomType,
                                      Date startDate,
                                      Date endDate)
    throws EntityNotFoundException;

  /**
   * Overwrite the number of rooms available of the particular type for each
   * day, starting from {@code startDate}.
   *
   * @param roomType The room type.
   * @param startDate The first date to update.
   * @param availableRoomsByDay The number of available rooms for each day,
   * from {@code startDate} inclusive. If the room has no inventory control
   * on a particular day, the array entry for that day should contain
   * {@link #UNCONTROLLED}.
   * @throws EntityNotFoundException If {@code roomType} does not exist in the
   *  database.
   */
  void updateInventory(RoomType roomType,
                       Date startDate,
                       List<Integer> availableRoomsByDay)
    throws EntityNotFoundException;

  /**
   * Calculate a list of rates and availability for all the room types for
   * a given property.
   *
   * @param property The property to query.
   * @param arrivalDate Start date.
   * @param departureDate End date.
   * @return Availability and rate information for each room type.
   * @throws EntityNotFoundException If {@code property} does not exist in the
   *  database.
   */
  List<AvailabilityAndRates> calculateRatesAndAvailabilty(Property property,
                                                          Date arrivalDate,
                                                          Date departureDate)
    throws EntityNotFoundException;

  /**
   * Calculate a list of rates for the given room type.
   *
   * @param roomType The room type to query.
   * @param arrivalDate Start date.
   * @param departureDate End date.
   * @return A list of rates that apply, sorted by date.
   * @throws EntityNotFoundException If {@code roomType} does not exist in the
   *  database.
   */
  List<RateDetails> calculateRates(RoomType roomType,
                                   Date arrivalDate,
                                   Date departureDate)
    throws EntityNotFoundException;

  /**
   * Create a new reservation.
   *
   * <p>If this method returns without exception, the inventory records will
   * be updated and a confirmation email will be sent to the guest. Further,
   * {@code reservation} will reflect the new entity. In particular,
   * {@code reservation.toExternalIdentity()} will return a valid external
   * identity, and {@code reservation.getConfirmationNumber()} will return
   * the reservation confirmation code.
   * </p>
   *
   * @param reservation The reservation.
   * @param offeredRates The list of rates that were offered to the guest.
   *  Currently there are no constraints that the rates must apply to the
   *  same range of dates as the reservation.
   * @return The newly created {@code Reservation}.
   * @throws EntityNotFoundException If the room type referred to by {@code
   *  reservation} does not exist in the database.
   * @throws EntityNotFoundException If the guest profile referred to by {@code
   *  reservation} does not exist in the database.
   * @throws RoomTypeUnavailableException If the room type is no longer
   *  available for the duration. of the reservation.
   * @throws EntityNotTransientException If {@code reservation} is not transient.
   */
  Reservation createReservation(Reservation reservation,
                                List<RateDetails> offeredRates)
    throws EntityNotFoundException,
           RoomTypeUnavailableException,
           EntityNotTransientException;

  /**
   * Find a reservation by its external identity.
   *
   * @param externalIdentity The identity.
   * @return The reservation.
   * @throws NotFoundException If the reservation was not found in the database.
   * @see DomainEntity#getExternalIdentity()
   */
  Reservation findReservationByExternalIdentity(String externalIdentity)
    throws NotFoundException;

  /**
   * Cancel a reservation.
   *
   * <p>If this method returns without exception, the inventory records will
   * be updated and a confirmation email will be sent to the guest.
   * </p>
   *
   * @param reservation The reservation.
   * @throws EntityNotFoundException If the reservation does not exist in the
   *  database.
   * @throws EntityNotFoundException If the room type referred to by {@code
   *  reservation} does not exist in the database.
   */
  void deleteReservation(Reservation reservation)
    throws EntityNotFoundException;

  /**
   * Availability Summary.
   *
   * @see ReservationServices#calculateAvailabilitySummary(Property, Date, int)
   */
  interface AvailabilitySummary {

    /**
     * The room type to which this information applies.
     *
     * @return The room type.
     */
    RoomType getRoomType();

    /**
     * The start month for this summary.
     *
     * @return The month.
     */
    Date getStartDate();

    /**
     * An list of availability information.
     *
     * @return Availability information, ordered by date.
     */
    List<Availability> getAvailabilityByMonth();

    /**
     * Summary of availability for the month.
     */
    interface Availability {
      /**
       * @return The number of days in the month on which the availability of
       *         the room type is restricted.
       */
      int getControls();

      /**
       * @return The number of days in the month on which the room type is
       *         unavailable.
       */
      int getCloseOuts();
    }
  }

  /**
   * Rate details.
   *
   * @see ReservationServices#calculateRates(RoomType, Date, Date)
   */
  interface RateDetails {

    /**
     * The start date for this rate.
     *
     * @return The date.
     */
    Date getStartDate();

    /**
     * Number of nights from the start date that this rate applies.
     *
     * @return The number of nights.
     */
    int getNumberOfNights();

    /**
     * The rate.
     *
     * @return The rate.
     */
    Money getPrice();
  }

  /**
   * Availability and rate details.
   *
   * @see ReservationServices#calculateRatesAndAvailabilty(Property, Date, Date)
   */
  interface AvailabilityAndRates {

    /**
     * The room type to which this information applies.
     *
     * @return The room type.
     */
    RoomType getRoomType();

    /**
     * The rate details for the room type.
     *
     * @return The rate details, sorted by date.
     */
    List<RateDetails> getRates();

    /**
     * A list of dates for which the room type is unavailable.
     *
     * @return The dates. If this list is empty, the room type is available
     *  on all dates queried.
     */
    List<Date> getBlockingDates();
  }
}
