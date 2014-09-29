// Copyright (c) 2014 Erick Bourgeois, All Rights Reserved

package ca.jeb.protobuf;

import ca.jeb.generated.proto.Messaging.ProtobufRequest;
import ca.jeb.generated.proto.Messaging.ProtobufResponse;
import ca.jeb.generated.proto.Messaging.RequestType;

import com.google.protobuf.ByteString;

/**
 * @author <a href="mailto:erick@jeb.ca">Erick Bourgeois</a>
 */
public class ProtobufRequestHandler
{
  public ProtobufResponse handleRequest(ProtobufRequest protobufRequest)
  {
    final RequestType requestType = protobufRequest.getRequestType();
    final ByteString byteString = protobufRequest.getPayload();

    return ProtobufResponse.newBuilder().build();
  }
}
