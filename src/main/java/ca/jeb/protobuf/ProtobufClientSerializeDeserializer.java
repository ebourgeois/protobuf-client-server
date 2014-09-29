// Copyright (c) 2014 Morgan Stanley & Co. Incorporated, All Rights Reserved

package ca.jeb.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;

import ca.jeb.generated.proto.Messaging.ProtobufRequest;
import ca.jeb.generated.proto.Messaging.ProtobufResponse;

/**
 * @author <a href="mailto:boureric@ms.com">boureric</a>
 */
public class ProtobufClientSerializeDeserializer implements Deserializer<ProtobufResponse>, Serializer<ProtobufRequest>
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ProtobufClientSerializeDeserializer.class);

  /**
   * @see org.springframework.core.serializer.Deserializer#deserialize(java.io.InputStream)
   */
  @Override
  public ProtobufResponse deserialize(InputStream inputStream) throws IOException
  {
    // Protobuf requires this InputStream to be closed
    inputStream.close();
    final ProtobufResponse protobufResponse = ProtobufResponse.parseFrom(inputStream);
    return protobufResponse;
  }

  /**
   * @see org.springframework.core.serializer.Serializer#serialize(java.lang.Object, java.io.OutputStream)
   */
  @Override
  public void serialize(ProtobufRequest protobufRequest, OutputStream outputStream) throws IOException
  {
    outputStream.write(protobufRequest.toByteArray());
  }
}