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
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.bigrez.jpa.DomainEntity;


/**
 * Entity representing a guest.
 *
 * @author Philip Aston
 */
@Entity
@NamedQueries ({
 @NamedQuery(name = GuestProfile.QUERY_BY_LOGON_AND_PASSWORD,
             query = "select distinct p from GuestProfile p " +
                     "where p.logon = ?1 and p.password = ?2")
})
public class GuestProfile extends DomainEntity {

  private static final long serialVersionUID = 1L;

  public static final String QUERY_BY_LOGON_AND_PASSWORD =
    "queryGuestProfileByLogonAndPassword";

  private String logon;

  private String password;

  private String firstName;

  private String lastName;

  private String phone;

  private String email;

  @Embedded
  private CardDetails card;

  @OneToMany(mappedBy="guestProfile",
             cascade=CascadeType.PERSIST,
             fetch=FetchType.EAGER)
  private List<Reservation> reservations = new ArrayList<Reservation>();

  public GuestProfile() {
  }

  public String getLogon() {
    return logon;
  }

  public void setLogon(String logon) {
    this.logon = logon;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public CardDetails getCard() {
    return card;
  }

  public void setCard(CardDetails card) {
    this.card = card;
  }

  public List<Reservation> getReservations() {
    return Collections.unmodifiableList(reservations);
  }

  /**
   * Allow {@link Reservation} to maintain the inverse references
   *
   * @return Mutable reservation list.
   */
  List<Reservation> getMutableReservations() {
    return reservations;
  }

  @Override protected String toStringDescription() {
    final StringBuilder result = new StringBuilder();

    result.append(getFirstName());
    result.append(" ");
    result.append(getLastName());

    return result.toString();
  }
}
