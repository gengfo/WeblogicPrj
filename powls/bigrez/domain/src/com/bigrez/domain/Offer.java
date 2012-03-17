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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.bigrez.jpa.DomainEntity;


/**
 * Entity representing a promotional offer.
 *
 * @author Philip Aston
 */
@Entity
@NamedQueries ({
  @NamedQuery(name = Offer.QUERY_BY_CITY_AND_STATE,
              query = "select o from Offer o where" +
                      " o.property.address.city = ?1" +
                      " and o.property.address.stateCode = ?2"),
  @NamedQuery(name = Offer.QUERY_BY_CITY,
              query = "select o from Offer o where" +
                      " o.property.address.city = ?1"),
  @NamedQuery(name = Offer.QUERY_BY_STATE,
              query = "select o from Offer o where" +
                      " o.property.address.stateCode = ?1")
 })
public class Offer extends DomainEntity {

  private static final long serialVersionUID = 1L;

  public static final String QUERY_BY_CITY_AND_STATE =
    "queryOfferByCityAndState";
  public static final String QUERY_BY_CITY =
    "queryOfferByCity";
  public static final String QUERY_BY_STATE =
    "queryOfferByState";

  @ManyToOne(cascade = CascadeType.PERSIST)
  private Property property;

  private String imageFile;

  private String caption;

  private String description;

  public Offer() {
  }

  public Offer(Property property,
               String imageFile,
               String caption,
               String description) {
    setProperty(property);
    setImageFile(imageFile);
    setCaption(caption);
    setDescription(description);
  }

  public Property getProperty() {
    return property;
  }

  public void setProperty(Property newProperty) {

    if (property != null) {
      if (property == newProperty) {
        return;
      }
      else {
        property.getMutableOffers().remove(this);
      }
    }

    // newProperty can be null if the Offer is being removed.
    // If so, we're careful not to set guestProfile to null, since some JPA
    // implementations (including TopLink) may then attempt an UPDATE to this
    // Offer which violates the foreign key constraint.

    if (newProperty != null) {
      property = newProperty;

      assert !property.getMutableOffers().contains(this);
      property.getMutableOffers().add(this);
    }
  }

  public String getImageFile() {
    return imageFile;
  }

  public void setImageFile(String imageFile) {
    this.imageFile = imageFile;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override protected String toStringDescription() {
    return getDescription();
  }
}
