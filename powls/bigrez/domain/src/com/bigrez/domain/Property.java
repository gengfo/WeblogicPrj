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

package com.bigrez.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.bigrez.jpa.DomainEntity;


/**
 * Entity representing a property.
 *
 * @author Philip Aston
 */
@Entity
@NamedQueries ({
  @NamedQuery(name = Property.QUERY_BY_CITY_AND_STATE,
              query = "select distinct o from Property o " +
                      "left join fetch o.offers " +
                      "left join fetch o.roomTypes " +
                      "where o.address.city = ?1 " +
                      "and o.address.stateCode = ?2"),
  @NamedQuery(name = Property.QUERY_BY_CITY,
              query = "select distinct o from Property o " +
                      "left join fetch o.offers " +
                      "left join fetch o.roomTypes " +
                      "where o.address.city = ?1"),
  @NamedQuery(name = Property.QUERY_BY_STATE,
              query = "select distinct o from Property o " +
                      "left join fetch o.offers " +
                      "left join fetch o.roomTypes " +
                      "where o.address.stateCode = ?1"),
  @NamedQuery(name = Property.QUERY_ALL_POPULATED,
              query = "select distinct o from Property o " +
                      "left join fetch o.offers " +
                      "left join fetch o.roomTypes"),
  @NamedQuery(name = Property.QUERY_STATE_CODES,
              query = "select distinct o.address.stateCode from Property o" +
                      " order by o.address.stateCode"),
  @NamedQuery(name = Property.QUERY_CITIES,
              query = "select distinct o.address.city from Property o" +
                      " order by o.address.city")
 })
public class Property extends DomainEntity {

  private static final long serialVersionUID = 1L;

  public static final String QUERY_BY_CITY_AND_STATE =
    "queryPropertyByCityAndState";
  public static final String QUERY_BY_CITY =
    "queryPropertyByCity";
  public static final String QUERY_BY_STATE =
    "queryPropertyByState";
  public static final String QUERY_ALL_POPULATED =
    "queryPropertyAllPopulated";
  public static final String QUERY_STATE_CODES =
    "queryPropertyStateCodes";
  public static final String QUERY_CITIES =
    "queryPropertyCities";

  private String description;

  private String features;

  @Embedded
  private Address address;

  private String phone;

  private String imageFile;

  // We don't eagerly fetch roomTypes and offers, since we don't want
  // to load them if we query many properties. Instead, we use fetch
  // joins to eagerly fetch these fields when we query a single property.

  @OneToMany(mappedBy="property",
             cascade={CascadeType.PERSIST, CascadeType.REMOVE} )
  private List<RoomType> roomTypes = new ArrayList<RoomType>();

  @OneToMany(mappedBy="property",
             cascade={CascadeType.PERSIST, CascadeType.REMOVE})
  private List<Offer> offers = new ArrayList<Offer>();

  public Property() {
    super();
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFeatures() {
    return features;
  }

  public void setFeatures(String features) {
    this.features = features;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getImageFile() {
    return imageFile;
  }

  public void setImageFile(String imageFile) {
    this.imageFile = imageFile;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public List<RoomType> getRoomTypes() {
    return Collections.unmodifiableList(roomTypes);
  }

  /**
   * Allow {@link RoomType} to maintain the inverse references
   *
   * @return Mutable roomTypes list.
   */
  List<RoomType> getMutableRoomTypes() {
    return roomTypes;
  }

  public List<Offer> getOffers() {
    return Collections.unmodifiableList(offers);
  }


  /**
   * Allow {@link Offer} to maintain the inverse references
   *
   * @return Mutable offers list.
   */
  List<Offer> getMutableOffers() {
    return offers;
  }

  @Override protected String toStringDescription() {
    return getDescription();
  }
}
