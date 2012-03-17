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

import static com.bigrez.service.impl.EmailMDBImpl.EMAIL_ADDRESSEE;
import static com.bigrez.service.impl.EmailMDBImpl.EMAIL_SUBJECT;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.bigrez.domain.Address;
import com.bigrez.domain.Property;
import com.bigrez.domain.Reservation;
import com.bigrez.domain.ReservationRate;


/**
 * Stateless Session Bean implementation of {@link EmailServices}.
 *
 * @author Philip Aston
 */
@Stateless
@Local
@Interceptors(LoggingInterceptor.class)
public class EmailServicesImpl implements EmailServices {

  @Resource(mappedName="bigrez.jms.connectionfactory")
  private ConnectionFactory jmsConnectionFactory;

  @Resource(mappedName="bigrez.jms.emailQueue")
  private Destination emailQueue;

  /**
   * Accessor for unit tests.
   */
  void setConnectionFactory(ConnectionFactory connectionFactory) {
    this.jmsConnectionFactory = connectionFactory;
  }

  /**
   * Accessor for unit tests.
   */
  void setQueue(Destination queue) {
    this.emailQueue = queue;
  }

  @Override
  public void sendReservationCancelledEmail(String addressee,
                                            Reservation reservation) {
    send(new ReservationCancelledEmail(addressee, reservation));
  }

  @Override
  public void sendReservationConfirmedEmail(String addressee,
                                            Reservation reservation) {
    send(new ReservationConfirmedEmail(addressee, reservation));
  }

  private void send(ReservationEmail email) {
    try {
      Connection connection = null;

      try {
        connection = jmsConnectionFactory.createConnection();

        final Session session =
          connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        session.createProducer(emailQueue).send(email.toJMSMessage(session));
      }
      finally {
        if (connection != null) {
          connection.close();
        }
      }
    }
    catch (JMSException e) {
      throw new EJBException("JMS problem", e);
    }
  }

  private static abstract class ReservationEmail {

    private final String addressee;
    private final Reservation reservation;
    private final String details;

    ReservationEmail(String addressee, Reservation reservation) {
      this.addressee = addressee;
      this.reservation = reservation;

      final Property property = reservation.getRoomType().getProperty();
      final Address address = property.getAddress();

      final StringWriter stringWriter = new StringWriter();
      final PrintWriter message = new PrintWriter(stringWriter);

      message.printf("Confirmation number: %s\n",
                     reservation.getConfirmationNumber());
      message.printf("Arrival date: %tA %<tB %<td %<tY\n",
                     reservation.getArrivalDate());
      message.printf("Departure date: %tA %<tB %<td %<tY\n\n",
                     reservation.getDepartureDate());
      message.println(property.getDescription());
      message.println(address.getAddress1());

      if (address.getAddress2() != null) {
        message.println(address.getAddress2());
      }

      message.printf("%s, %s %s\n",
                     address.getCity(),
                     address.getStateCode(),
                     address.getPostalCode());
      message.printf("Telephone: %s\n\n", property.getPhone());
      message.printf("Room type: %s\n",
                     reservation.getRoomType().getDescription());
      for (ReservationRate rate : reservation.getReservationRates()) {
        message.printf("  %d @ %s %.2f\n",
                       rate.getNumberOfNights(),
                       rate.getPrice().getCurrency().getSymbol(),
                       rate.getPrice().getAmount());
      }

      details = stringWriter.toString();
    }

    public Message toJMSMessage(Session session) throws JMSException {
      final TextMessage result = session.createTextMessage();

      result.setStringProperty(EMAIL_ADDRESSEE, addressee);
      result.setStringProperty(EMAIL_SUBJECT, createSubject());
      result.setText(createBody());

      return result;
    }

    protected abstract String createSubject();

    protected abstract String createBody();

    protected Reservation getReservation() {
      return reservation;
    }

    protected String getDetails() {
      return details;
    }
  }

  private static final class ReservationCancelledEmail
    extends ReservationEmail {

    ReservationCancelledEmail(String addressee, Reservation reservation) {
      super(addressee, reservation);
    }

    @Override protected String createSubject() {
      return String.format(
        "Reservation %s has been cancelled",
        getReservation().getConfirmationNumber());
    }

    @Override protected String createBody() {
      final StringBuilder result = new StringBuilder();

      result.append("\nThe following reservation has been cancelled.\n\n");
      result.append(getDetails());

      return result.toString();
    }
  }

  private static final class ReservationConfirmedEmail
    extends ReservationEmail {

    ReservationConfirmedEmail(String addressee, Reservation reservation) {
      super(addressee, reservation);
    }

    @Override protected String createSubject() {
      return String.format(
        "Reservation confirmed: %s, %tB %<td - %tB %<td",
        getReservation().getRoomType().getProperty().getDescription(),
        getReservation().getArrivalDate(),
        getReservation().getDepartureDate());
    }

    @Override protected String createBody() {
      final StringBuilder result = new StringBuilder();

      result.append("\nReservation confirmed.\n\n");
      result.append(getDetails());
      result.append("\nThank you for choosing bigrez.com.\n");

      return result.toString();
    }
  }
}