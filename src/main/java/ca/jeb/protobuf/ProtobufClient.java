// Copyright (c) 2014 Erick Bourgeois, All Rights Reserved

package ca.jeb.protobuf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.jeb.generated.proto.Message.Address;
import ca.jeb.generated.proto.Messaging.ProtobufRequest;
import ca.jeb.generated.proto.Messaging.ProtobufResponse;
import ca.jeb.generated.proto.Messaging.RequestType;

/**
 * @author <a href="mailto:erick@jeb.ca">Erick Bourgeois</a>
 */
public class ProtobufClient
{
  @Autowired
  ProtobufClientGateway       protobufClientGateway;

  private static final Logger LOGGER = LoggerFactory.getLogger(ProtobufClient.class);

  /**
   * 
   */
  private void init()
  {
    LOGGER.debug("protobufClientGateway: " + protobufClientGateway);
    final Address address = Address.newBuilder().setStreet("1 Main Street").setCity("Foo Ville").setCountry("Canada")
            .setPostalCode("J0J 1H1").build();
    final ProtobufRequest protobufRequest = ProtobufRequest.newBuilder().setRequestType(RequestType.POST)
            .setPayload(address.toByteString()).build();

    final ProtobufResponse protobufResponse = protobufClientGateway.sendAndReceiveMessage(protobufRequest);
    LOGGER.debug("protobufResponse: " + protobufResponse);
  }

}
