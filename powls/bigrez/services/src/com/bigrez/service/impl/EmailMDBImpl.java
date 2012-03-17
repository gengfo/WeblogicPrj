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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.interceptor.Interceptors;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import weblogic.logging.LoggingHelper;


/**
 * Message Driven Bean that sends out emails.
 *
 * @author Philip Aston
 * @see EmailServicesImpl
 */
@MessageDriven
(mappedName = "bigrez.jms.emailQueue",
 activationConfig = {
   @ActivationConfigProperty(propertyName = "destinationType",
                             propertyValue = "javax.jms.Queue")
  }
)
@Interceptors(LoggingInterceptor.class)
public class EmailMDBImpl implements MessageListener {

  public static final String EMAIL_SUBJECT = "EMAIL_SUBJECT";
  public static final String EMAIL_ADDRESSEE = "EMAIL_ADDRESSEE";

  private Logger logger = LoggingHelper.getServerLogger();

  @Resource private MessageDrivenContext context;
  @Resource(mappedName="bigrez.mail.session") private Session mailSession;
  @Resource private String sender = "reservations@bigrez.com";

  /**
   * Accessor for unit tests.
   */
  void setLogger(Logger logger) {
    this.logger = logger;
  }

  /**
   * Accessor for unit tests.
   */
  void setContext(MessageDrivenContext context) {
    this.context = context;
  }

  /**
   * Accessor for unit tests.
   */
  void setMailSession(Session mailSession) {
    this.mailSession = mailSession;
  }

  @Override
  public void onMessage(Message message) {

    final String subject;
    final String recipient;
    final String body;

    try {
      subject = message.getStringProperty(EMAIL_SUBJECT);
      recipient = message.getStringProperty(EMAIL_ADDRESSEE);
      body = ((TextMessage)message).getText();
    }
    catch (ClassCastException e) {
      logger.warning("Ignoring invalid message: " + message);
      return;
    }
    catch (JMSException e) {
      context.setRollbackOnly();
      logger.log(Level.SEVERE, "JMS internal error", e);
      return;
    }

    if (subject == null) {
      logger.warning("Ignoring invalid message - no subject ");
      return;
    }

    if (recipient == null) {
      logger.warning("Ignoring invalid message - no recipient ");
      return;
    }

    final MimeMessage mailMessage = new MimeMessage(mailSession);

    try {
      mailMessage.setFrom(new InternetAddress(sender));
      mailMessage.addRecipient(RecipientType.TO,
                               new InternetAddress(recipient));
      mailMessage.setSubject(subject);
      mailMessage.setText(body);

      Transport.send(mailMessage);
    }
    catch (AddressException e) {
      // Don't roll back, this is never going to work.
      logger.warning("Failed to parse email address: " + e.getMessage());
      return;
    }
    catch (MessagingException e) {
      // Roll back, possibly a transient error.
      context.setRollbackOnly();
      logger.warning("Failed to send message: " + e.getMessage());
      return;
    }
  }
}
