// Copyright (c) 2014 Erick Bourgeois, All Rights Reserved

package ca.jeb.protobuf;

import ca.jeb.generated.proto.Messaging.ProtobufRequest;
import ca.jeb.generated.proto.Messaging.ProtobufResponse;

/**
 * @author <a href="mailto:erick@jeb.ca">Erick Bourgeois</a>
 */
public interface ProtobufClientGateway
{
  public ProtobufResponse sendAndReceive(ProtobufRequest protobufResponse);
}