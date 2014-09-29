// Copyright (c) 2014 Erick Bourgeois, All Rights Reserved

package ca.jeb.protobuf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;

import ca.jeb.generated.proto.Message.Address;
import ca.jeb.generated.proto.Messaging.MessageRequest;
import ca.jeb.generated.proto.Messaging.MessageType;
import ca.jeb.generated.proto.Messaging.ProtobufRequest;
import ca.jeb.generated.proto.Messaging.ProtobufResponse;
import ca.jeb.generated.proto.Messaging.RequestType;

/**
 * @author <a href="mailto:erick@jeb.ca">Erick Bourgeois</a>
 */
public class ProtobufClient implements SmartLifecycle
{
  @Autowired
  ProtobufClientGateway       protobufClientGateway;

  private static final Logger LOGGER = LoggerFactory.getLogger(ProtobufClient.class);

  /**
   * @see org.springframework.context.Lifecycle#start()
   */
  @Override
  public void start()
  {
    final ProtobufRequest.Builder protobufRequestBuilder = ProtobufRequest.newBuilder();

    final Address.Builder address = Address.newBuilder().setStreet("1 Main Street").setCity("Foo Ville").setCountry("Canada")
            .setPostalCode("J0J 1H1");

    MessageRequest.Builder messageRequestBuilder = MessageRequest.newBuilder().setId(1);

    // PUT
    messageRequestBuilder.setId(1).setMessageType(MessageType.ADDRESS).setMessage(address.build().toByteString());
    protobufRequestBuilder.setRequestType(RequestType.PUT).setPayload(messageRequestBuilder.build().toByteString());

    ProtobufResponse protobufResponse = protobufClientGateway.sendAndReceive(protobufRequestBuilder.build());
    LOGGER.debug("PUT protobufResponse: " + protobufResponse);

    // POST
    address.setStreet("1 Server Ville");
    messageRequestBuilder.clear();
    messageRequestBuilder.setId(1).setMessageType(MessageType.ADDRESS).setMessage(address.build().toByteString());
    protobufRequestBuilder.setRequestType(RequestType.POST).setPayload(messageRequestBuilder.build().toByteString());

    protobufResponse = protobufClientGateway.sendAndReceive(protobufRequestBuilder.build());
    LOGGER.debug("POST protobufResponse: " + protobufResponse);

    // GET
    messageRequestBuilder.clear();
    messageRequestBuilder.setId(1).setMessageType(MessageType.ADDRESS);
    protobufRequestBuilder.setRequestType(RequestType.GET).setPayload(messageRequestBuilder.build().toByteString());

    protobufResponse = protobufClientGateway.sendAndReceive(protobufRequestBuilder.build());
    LOGGER.debug("GET protobufResponse: " + protobufResponse);

    // DELETE
    messageRequestBuilder.clear();
    messageRequestBuilder.setId(1).setMessageType(MessageType.ADDRESS);
    protobufRequestBuilder.setRequestType(RequestType.DELETE).setPayload(messageRequestBuilder.build().toByteString());

    protobufResponse = protobufClientGateway.sendAndReceive(protobufRequestBuilder.build());
    LOGGER.debug("DELETE protobufResponse: " + protobufResponse);

    // Now try another GET, this should throw an exception and return the message
    messageRequestBuilder.clear();
    messageRequestBuilder.setId(1).setMessageType(MessageType.ADDRESS);
    protobufRequestBuilder.setRequestType(RequestType.GET).setPayload(messageRequestBuilder.build().toByteString());

    protobufResponse = protobufClientGateway.sendAndReceive(protobufRequestBuilder.build());
    LOGGER.debug("GET protobufResponse: " + protobufResponse);

    System.exit(0);
  }

  /**
   * @see org.springframework.context.Lifecycle#isRunning()
   */
  @Override
  public boolean isRunning()
  {
    return false;
  }

  /**
   * @see org.springframework.context.Lifecycle#stop()
   */
  @Override
  public void stop()
  {
    // Nothing to do
  }

  /**
   * @see org.springframework.context.Phased#getPhase()
   */
  @Override
  public int getPhase()
  {
    return Integer.MAX_VALUE;
  }

  /**
   * @see org.springframework.context.SmartLifecycle#isAutoStartup()
   */
  @Override
  public boolean isAutoStartup()
  {
    return true;
  }

  /**
   * @see org.springframework.context.SmartLifecycle#stop(java.lang.Runnable)
   */
  @Override
  public void stop(Runnable callback)
  {
    // Nothing to do
  }
}