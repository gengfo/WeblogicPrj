// Copyright (C) 2009 Philip Aston
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
import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;

import java.math.BigDecimal;

import org.mockito.ArgumentMatcher;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.ejb.EJBException;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Before;
import org.junit.Test;

import com.bigrez.domain.Address;
import com.bigrez.domain.Money;
import com.bigrez.domain.Property;
import com.bigrez.domain.Reservation;
import com.bigrez.domain.ReservationRate;
import com.bigrez.domain.RoomType;


/**
 * Unit tests for {@link EmailServicesImpl}.
 *
 * @author Philip Aston
 */
public class TestEmailServicesImpl {

  // Look at this nonsense. You've got to love the JMS API.
  private final ConnectionFactory connectionFactory =
    mock(ConnectionFactory.class);
  private final Connection connection = mock(Connection.class);
  private final Session session = mock(Session.class);
  private final Destination queue = mock(Destination.class);
  private final MessageProducer producer = mock(MessageProducer.class);
  private final TextMessage message = mock(TextMessage.class);

  private final Property property = new Property();
  private final RoomType roomType = new RoomType();
  private final Reservation reservation = new Reservation();
  private final ReservationRate rate = new ReservationRate();

  @Before public void setup() throws Exception {
    when(connectionFactory.createConnection()).thenReturn(connection);
    when(connection.createSession(false, Session.AUTO_ACKNOWLEDGE))
    .thenReturn(session);
    when(session.createTextMessage()).thenReturn(message);
    when(session.createProducer(queue)).thenReturn(producer);

    property.setAddress(new Address("a1", "a2", "city", "state", "code"));
    roomType.setProperty(property);
    reservation.setRoomType(roomType);
    rate.setReservation(reservation);
    rate.setNumberOfNights(99);
    rate.setPrice(new Money(new BigDecimal(1.23)));
  }

  @Test public void testSendReservationCancelledEmail() throws Exception {
    final EmailServicesImpl emailServices = new EmailServicesImpl();
    emailServices.setConnectionFactory(connectionFactory);
    emailServices.setQueue(queue);

    emailServices.sendReservationCancelledEmail("hello", reservation);

    verify(connectionFactory).createConnection();
    verify(connection).createSession(false, Session.AUTO_ACKNOWLEDGE);
    verify(connection).close();
    verify(session).createProducer(queue);
    verify(session).createTextMessage();
    verify(producer).send(message);
    verify(message).setStringProperty(EMAIL_ADDRESSEE, "hello");
    verify(message).setStringProperty(eq(EMAIL_SUBJECT),
                                      contains("cancelled"));
    verify(message).setText(argThat(new ArgumentMatcher<String>() {
      @Override public boolean matches(Object argument) {
        final String s = (String)argument;
        return s.contains("has been cancelled") &&
               s.contains("99 @ " + rate.getPrice().getCurrency().getSymbol() +
                          " 1.23");
      }}));

    verifyNoMoreInteractions(connectionFactory,
                             connection,
                             session,
                             queue,
                             producer,
                             message);
  }

  @Test public void testSendReservationConfirmedEmail() throws Exception {
    property.setAddress(new Address("a1", null, "city", "state", "code"));

    final EmailServicesImpl emailServices = new EmailServicesImpl();
    emailServices.setConnectionFactory(connectionFactory);
    emailServices.setQueue(queue);

    emailServices.sendReservationConfirmedEmail("a@b.com", reservation);

    verify(connectionFactory).createConnection();
    verify(connection).createSession(false, Session.AUTO_ACKNOWLEDGE);
    verify(connection).close();
    verify(session).createProducer(queue);
    verify(session).createTextMessage();
    verify(producer).send(message);
    verify(message).setStringProperty(EMAIL_ADDRESSEE, "a@b.com");
    verify(message).setStringProperty(eq(EMAIL_SUBJECT),
                                      contains("confirmed"));
    verify(message).setText(argThat(new ArgumentMatcher<String>() {
      @Override public boolean matches(Object argument) {
        final String s = (String)argument;
        return s.contains("Reservation confirmed") &&
               s.contains("99 @ " + rate.getPrice().getCurrency().getSymbol() +
                          " 1.23");
      }}));

    verifyNoMoreInteractions(connectionFactory,
                             connection,
                             session,
                             queue,
                             producer,
                             message);

    final JMSException jmsException = new JMSException("");

    when(connectionFactory.createConnection()).thenThrow(jmsException);

    try {
      emailServices.sendReservationConfirmedEmail("hello", reservation);
      fail("Expected EJBException");
    }
    catch (EJBException e) {
      assertSame(jmsException, e.getCausedByException());
    }
  }
}
