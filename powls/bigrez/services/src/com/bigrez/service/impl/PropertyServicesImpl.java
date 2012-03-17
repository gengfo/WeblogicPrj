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

import static java.lang.Math.min;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.bigrez.domain.Offer;
import com.bigrez.domain.Property;
import com.bigrez.domain.Rate;
import com.bigrez.domain.RoomType;
import com.bigrez.service.EntityNotFoundException;
import com.bigrez.service.NotFoundException;
import com.bigrez.service.PropertyServices;


/**
 * Stateless Session Bean implementation of {@link PropertyServices}.
 *
 * <p>Unlike the {@link EntityManager#remove}, the delete methods do not
 * throw IllegalArgumentException if the supplied argument is a detached
 * object. Instead, they look up the corresponding entity in the database
 * and remove that. </p>
 *
 * @author Philip Aston
 */
@Stateless
@Local
@Interceptors(LoggingInterceptor.class)
public class PropertyServicesImpl implements PropertyServices {

  @PersistenceContext
  private EntityManager entityManager;

  private Random random = new Random();

  /**
   * For unit tests.
   */
  void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  private final JPABaseDAO<Property> propertyDAO =
    new JPABaseDAO<Property>() {
      @Override protected EntityManager getEntityManager() {
        return entityManager;
      }
    };

  private final JPABaseDAO<Offer> offerDAO =
    new JPABaseDAO<Offer>() {
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

  private final JPABaseDAO<Rate> rateDAO =
    new JPABaseDAO<Rate>() {
      @Override protected EntityManager getEntityManager() {
        return entityManager;
      }
    };

  /**
   * {@inheritDoc}
   */
  @Override
  public Property createOrUpdate(Property property) {
    final Property result = propertyDAO.createOrUpdate(property);

    // Ensure lazy fields are populated.
    result.getOffers();
    result.getRoomTypes();

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Property findPropertyByExternalIdentity(String externalIdentity)
    throws NotFoundException {

    final Property result =
      propertyDAO.findByExternalIdentity(externalIdentity);

    // Ensure lazy fields are populated.
    result.getOffers();
    result.getRoomTypes();

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(Property property) throws EntityNotFoundException {
    propertyDAO.delete(propertyDAO.checkExists(property));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RoomType createOrUpdate(RoomType roomType)
    throws EntityNotFoundException {

    // This check is a convenience for the caller. Otherwise it would
    // receive a PersistenceException when our transaction commits.
    propertyDAO.checkExists(roomType.getProperty());

    return roomTypeDAO.createOrUpdate(roomType);
  }

  /**
   * {@inheritDoc}
   * @throws NotFoundException
   */
  @Override
  public RoomType findRoomTypeByExternalIdentity(String externalIdentity)
    throws NotFoundException {
    return roomTypeDAO.findByExternalIdentity(externalIdentity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(RoomType roomType) throws EntityNotFoundException {
    roomTypeDAO.delete(roomTypeDAO.checkExists(roomType));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Rate createOrUpdate(Rate rate) throws EntityNotFoundException {
    // This check is a convenience for the caller. Otherwise it would
    // receive a PersistenceException when our transaction commits.
    roomTypeDAO.checkExists(rate.getRoomType());

    return rateDAO.createOrUpdate(rate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Rate findRateByExternalIdentity(String externalIdentity)
    throws NotFoundException {
    return rateDAO.findByExternalIdentity(externalIdentity);
  }

  /**
   * {@inheritDoc}
   * @throws EntityNotFoundException
   */
  @Override
  public List<Rate> findRatesByRoomType(RoomType roomType)
    throws EntityNotFoundException {
    // Calling checkExists will throw EntityNotFoundException appropriately,
    // but also satisfies the OpenJPA requirement that entity query parameters
    // are managed by the current EM.
    return rateDAO.find(Rate.QUERY_BY_ROOMTYPE,
                        roomTypeDAO.checkExists(roomType));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(Rate rate) throws EntityNotFoundException {
    rateDAO.delete(rateDAO.checkExists(rate));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Offer createOrUpdate(Offer offer) throws EntityNotFoundException {

    // This check is a convenience for the caller. Otherwise it would
    // receive a PersistenceException when our transaction commits.
    propertyDAO.checkExists(offer.getProperty());

    return offerDAO.createOrUpdate(offer);
  }

  /**
   * {@inheritDoc}
   * @throws NotFoundException
   */
  @Override
  public Offer findOfferByExternalIdentity(String externalIdentity)
    throws NotFoundException {
    return offerDAO.findByExternalIdentity(externalIdentity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(Offer offer) throws EntityNotFoundException {
    offerDAO.delete(offerDAO.checkExists(offer));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Property> findAll() {
    return propertyDAO.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Property> findByCityAndState(String city, String state) {
    if (isEmpty(city)) {
      if (isEmpty(state)) {
        return propertyDAO.find(Property.QUERY_ALL_POPULATED);
      }
      else {
        return propertyDAO.find(Property.QUERY_BY_STATE, state);
      }
    }
    else {
      if (isEmpty(state)) {
        return propertyDAO.find(Property.QUERY_BY_CITY, city);
      }
      else {
        return propertyDAO.find(Property.QUERY_BY_CITY_AND_STATE, city, state);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<String> getAllCities() {
    return
      entityManager.createNamedQuery(Property.QUERY_CITIES).getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<String> getAllStateCodes() {
    return
      entityManager.createNamedQuery(Property.QUERY_STATE_CODES)
      .getResultList();
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public List<Offer> getOffersForDisplay(Property property,
                                         String city,
                                         String state,
                                         int maximumOffers)
    throws EntityNotFoundException {

    final List<Offer> offers;

    if (property != null) {
      offers = propertyDAO.checkExists(property).getOffers();
    }
    else {
      if (!isEmpty(state)) {
        if (!isEmpty(city)) {
          offers = offerDAO.find(Offer.QUERY_BY_CITY_AND_STATE, city, state);
        }
        else {
          offers = offerDAO.find(Offer.QUERY_BY_STATE, state);
        }
      }
      else if (!isEmpty(city)) {
        offers = offerDAO.find(Offer.QUERY_BY_CITY, city);
      }
      else {
        offers = emptyList();
      }
    }

    final List<Offer> result =
      new ArrayList<Offer>(offers)
        .subList(0, min(offers.size(), maximumOffers));

    // Fill out the rest with random properties.
    final int remainingSlots = maximumOffers - result.size();

    if (remainingSlots > 0) {
      // This won't scale to large numbers of offers. For that, we'd need
      // to delegate the random selection to the database.
      final List<Offer> otherOffers = new ArrayList<Offer>(offerDAO.findAll());
      otherOffers.removeAll(result);
      Collections.shuffle(otherOffers, random);

      result.addAll(
        otherOffers.subList(0, min(otherOffers.size(), remainingSlots)));
    }

    return result;
  }

  private static final boolean isEmpty(String s) {
    return s == null || s.trim().length() == 0;
  }
}
