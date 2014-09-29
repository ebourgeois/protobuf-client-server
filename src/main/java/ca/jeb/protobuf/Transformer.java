// Copyright (c) 2014 Erick Bourgeois, All Rights Reserved

package ca.jeb.protobuf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * @author <a href="mailto:erick@jeb.ca">Erick Bourgeois</a>
 */
public class Transformer implements MessageHandler
{
  private static final Logger LOGGER = LoggerFactory.getLogger(Transformer.class);

  /**
   * @see org.springframework.messaging.MessageHandler#handleMessage(org.springframework.messaging.Message)
   */
  @Override
  public void handleMessage(Message<?> message) throws MessagingException
  {
    LOGGER.debug("message: " + message);
  }
}