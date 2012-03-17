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

import java.util.List;

import com.bigrez.domain.Offer;
import com.bigrez.domain.Property;
import com.bigrez.domain.Rate;
import com.bigrez.domain.RoomType;
import com.bigrez.jpa.DomainEntity;


/**
 * Property services. Allows Properties, RoomTypes, Rates, and Offers to be
 * created, queried, modified, and deleted.
 *
 * <p>{@link #findByCityAndState} and all methods that return a single
 * {@link Property} ensure that the lazy {@link Property#getOffers()} and
 * {@link Property#getRoomTypes()} associations are populated. Clients should
 * not rely on these associations being populated for a {@code Property}
 * obtained otherwise.</p>
 *
 * @author Philip Aston
 */
public interface PropertyServices {

  /**
   * Persist the supplied property, creating or updating the database record
   * as required.
   *
   * <p>The lazy @link Property#getOffers()} and
   * {@link Property#getRoomTypes()} associations will be populated.</p>
   *
   * @param property The property. Will be unchanged by this call.
   * @return The created or updated property.
   */
  Property createOrUpdate(Property property);

  /**
   * Find a property by its external identity.
   *
   * <p>The lazy @link Property#getOffers()} and
   * {@link Property#getRoomTypes()} associations will be populated.</p>
   *
   * @param externalIdentity The identity.
   * @return The property.
   * @throws NotFoundException If the property was not found in the database.
   * @see DomainEntity#getExternalIdentity()
   */
  Property findPropertyByExternalIdentity(String externalIdentity)
    throws NotFoundException;

  /**
   * Find all properties.
   *
   * <p>The lazy @link Property#getOffers()} and
   * {@link Property#getRoomTypes()} associations will <em>not</em> be
   * populated.</p>
   *
   * @return The properties.
   */
  List<Property> findAll();

  /**
   * Find properties by city and state code.
   *
   * <p>The lazy @link Property#getOffers()} and
   * {@link Property#getRoomTypes()} associations will be populated.</p>
   *
   * @param city The city, or {@code null} for all cities.
   * @param state The state code, or {@code null} for all states.
   * @return The properties.
   */
  List<Property> findByCityAndState(String city, String state);

  /**
   * Find all the cities for the properties..
   *
   * @return The cities, ordered alphabetically.
   */
  List<String> getAllCities();

  /**
   * Find all the state codes for the properties..
   *
   * @return The state codes, ordered alphabetically.
   */
  List<String> getAllStateCodes();

  /**
   * Delete the supplied property from the database,
   *
   * TODO barf if reservations
   *
   * @param property The property.
   * @throws EntityNotFoundException If the property does not exist.
   */
  void delete(Property property) throws EntityNotFoundException;

  /**
   * Persist the supplied room type, creating or updating the database record
   * as required.
   *
   * @param roomType The room type. Will be unchanged by this call.
   * @return The created or updated room type.
   * @throws EntityNotFoundException If {@code roomType}'s property does not
   *   exist.
   */
  RoomType createOrUpdate(RoomType roomType) throws EntityNotFoundException;

  /**
   * Find a room type by its external identity.
   *
   * @param externalIdentity The identity.
   * @return The room type.
   * @throws NotFoundException If the room type was not found in the database.
   * @see DomainEntity#getExternalIdentity()
   */
  RoomType findRoomTypeByExternalIdentity(String externalIdentity)
    throws NotFoundException;

  /**
   * Delete the supplied room type from the database,
   *
   * TODO barf if reservations
   *
   * @param roomType The room  type.
   * @throws EntityNotFoundException If the room type does not exist.
   */
  void delete(RoomType roomType) throws EntityNotFoundException;

  /**
   * Persist the supplied rate, creating or updating the database record
   * as required.
   *
   * @param rate The rate. Will be unchanged by this call.
   * @return The created or updated rate.
   * @throws EntityNotFoundException If {@code rate}'s roomType does not exist.
   */
  Rate createOrUpdate(Rate rate) throws EntityNotFoundException;

  /**
   * Find a rate by its external identity.
   *
   * @param externalIdentity The identity.
   * @return The rate.
   * @throws NotFoundException If the rate was not found in the database.
   * @see DomainEntity#getExternalIdentity()
   */
  Rate findRateByExternalIdentity(String externalIdentity)
    throws NotFoundException;

  /**
   * Find the rates for a particular RoomType. A separate finder is required
   * because the association between Rate and RoomType is unidirectional.
   *
   * @param roomType The room type.
   * @return The rates.
   * @throws EntityNotFoundException If {@code roomType} does not exist.
   */
  List<Rate> findRatesByRoomType(RoomType roomType)
    throws EntityNotFoundException;

  /**
   * Delete the supplied rate from the database,
   *
   * @param rate The rate.
   * @throws EntityNotFoundException If the rate does not exist.
   */
  void delete(Rate rate) throws EntityNotFoundException;

  /**
   * Persist the supplied offer, creating or updating the database record
   * as required.
   *
   * @param offer The offer. Will be unchanged by this call.
   * @return The created or updated offer.
   * @throws EntityNotFoundException If {@code offer}'s property does not exist.
   */
  Offer createOrUpdate(Offer offer) throws EntityNotFoundException;

  /**
   * Find an offer by its external identity.
   *
   * @param externalIdentity The identity.
   * @return The offer.
   * @throws NotFoundException If the offer was not found in the database.
   * @see DomainEntity#getExternalIdentity()
   */
  Offer findOfferByExternalIdentity(String externalIdentity)
    throws NotFoundException;

  /**
   * Search for appropriate offers.
   *
   * @param property The property to search for offers. If not {@code null},
   *  the city and state are ignored.
   * @param city The city to search for offers. Only used if {@code property}
   *  is {@code null}.
   * @param state The state code of the state to search for offers. Only used
   *  if {@code property} is {@code null}.
   * @param maximumOffers The maximum number of offers to return.
   * @return The list of offers.
   * @throws EntityNotFoundException If {@code property} does not exist in the
   *  database.
   */
  List<Offer> getOffersForDisplay(Property property,
                                  String city,
                                  String state,
                                  int maximumOffers)
    throws EntityNotFoundException;

  /**
   * Delete the supplied offer from the database,
   *
   * @param offer The offer.
   * @throws EntityNotFoundException If the offer does not exist.
   */
  void delete(Offer offer) throws EntityNotFoundException;
}
