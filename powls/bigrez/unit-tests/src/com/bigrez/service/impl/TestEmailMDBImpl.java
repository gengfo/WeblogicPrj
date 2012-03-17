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
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.Provider.Type;
import javax.mail.internet.InternetAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests for {@link EmailMDBImpl}.
 *
 * @author Philip Aston
 */
public class TestEmailMDBImpl {

  private final Logger logger = mock(Logger.class);
  private final MessageDrivenContext context = mock(MessageDrivenContext.class);
  private final EmailMDBImpl emailMDB = new EmailMDBImpl();

  @Before public void setUp() throws Exception {
    emailMDB.setLogger(logger);
    emailMDB.setContext(context);
  }

  @After public void tearDown() throws Exception {
    verifyNoMoreInteractions(logger, context);
  }

  @Test public void testBadMessageJMS() throws Exception {
    final JMSException jmsException = new JMSException("");

    final Message message = mock(Message.class);
    when(message.getStringProperty(anyString())).thenThrow(jmsException);

    emailMDB.onMessage(message);

    verify(logger).log(
      eq(Level.SEVERE), contains("JMS internal error"), eq(jmsException));

    verify(context).setRollbackOnly();
  }


  @Test public void testBadMessage1() throws Exception {
    emailMDB.onMessage(mock(Message.class));

    verify(logger).warning(contains("invalid message"));
  }

  @Test public void testBadMessage2() throws Exception {
    final Message message = mock(TextMessage.class);

    emailMDB.onMessage(message);

    verify(logger).warning(contains("invalid message"));
  }

  @Test public void testBadMessage3() throws Exception {
    final TextMessage message = mock(TextMessage.class);
    when(message.getStringProperty(EMAIL_SUBJECT)).thenReturn("a subject");

    emailMDB.onMessage(message);

    verify(logger).warning(contains("invalid message"));
  }

  @Test public void testInvalidProvider() throws Exception {
    final Session mailSession = Session.getInstance(new Properties());
    mailSession.setProvider(
      new Provider(Type.TRANSPORT, "smtp", "foo", "phil", "0.1"));
    emailMDB.setMailSession(mailSession);

    final TextMessage message = mock(TextMessage.class);
    when(message.getStringProperty(EMAIL_SUBJECT)).thenReturn("a subject");
    when(message.getStringProperty(EMAIL_ADDRESSEE)).thenReturn("a@b.c");
    when(message.getText()).thenReturn("Hello");

    emailMDB.onMessage(message);

    verify(context).setRollbackOnly();
    verify(logger).warning(contains("Failed to send message"));
  }

  @Test public void testBadAddressee() throws Exception {
    final TextMessage message = mock(TextMessage.class);
    when(message.getStringProperty(EMAIL_SUBJECT)).thenReturn("a subject");
    when(message.getStringProperty(EMAIL_ADDRESSEE)).thenReturn("@@@");
    when(message.getText()).thenReturn("Hello");

    emailMDB.onMessage(message);

    verify(logger).warning(contains("Failed to parse email address"));
  }

  @Test public void testValidProvider() throws Exception {
    final Session mailSession = Session.getInstance(new Properties());

    mailSession.setProvider(new Provider(Type.TRANSPORT,
                                         "smtp",
                                         ServiceClass.class.getName(),
                                         "phil",
                                         "0.1"));
    emailMDB.setMailSession(mailSession);

    final TextMessage message = mock(TextMessage.class);
    when(message.getStringProperty(EMAIL_SUBJECT)).thenReturn("a subject");
    when(message.getStringProperty(EMAIL_ADDRESSEE)).thenReturn("a@b.c");
    when(message.getText()).thenReturn("Hello");

    emailMDB.onMessage(message);

    verifyNoMoreInteractions(logger);

    final javax.mail.Message lastMessage = ServiceClass.getLastMessage();

    assertEquals("a subject", lastMessage.getSubject());
    assertEquals(new InternetAddress("a@b.c"),
                 lastMessage.getAllRecipients()[0]);
    assertEquals("Hello", lastMessage.getContent());
  }

  public static class ServiceClass extends Transport {
    private static javax.mail.Message lastMessage;

    public ServiceClass(Session session, URLName urlName) {
      super(session, urlName);
    }

    @Override
    protected boolean protocolConnect(String host,
                                      int port,
                                      String user,
                                      String password)
      throws MessagingException {

      return true;
    }

    @Override
    public void sendMessage(javax.mail.Message message, Address[] addresses)
      throws MessagingException {
      lastMessage = message;
    }

    public static javax.mail.Message getLastMessage() {
      return lastMessage;
    }
  }
}