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
public class ProtobufServerSerializeDeserializer implements Deserializer<ProtobufRequest>, Serializer<ProtobufResponse>
{
  private static final Logger LOGGER = LoggerFactory.getLogger(ProtobufServerSerializeDeserializer.class);

  /**
   * @see org.springframework.core.serializer.Deserializer#deserialize(java.io.InputStream)
   */
  @Override
  public ProtobufRequest deserialize(InputStream inputStream) throws IOException
  {
    // Protobuf requires this InputStream to be closed
    inputStream.close();
    final ProtobufRequest protobufRequest = ProtobufRequest.parseFrom(inputStream);
    return protobufRequest;
  }

  /**
   * @see org.springframework.core.serializer.Serializer#serialize(java.lang.Object, java.io.OutputStream)
   */
  @Override
  public void serialize(ProtobufResponse protobufResponse, OutputStream outputStream) throws IOException
  {
    outputStream.write(protobufResponse.toByteArray());
  }
}