// Copyright (c) 2014 Erick Bourgeois, All Rights Reserved

package ca.jeb.protobuf;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.jeb.generated.proto.Message.Address;
import ca.jeb.generated.proto.Message.Person;
import ca.jeb.generated.proto.Messaging.MessageRequest;
import ca.jeb.generated.proto.Messaging.MessageType;
import ca.jeb.generated.proto.Messaging.ProtobufRequest;
import ca.jeb.generated.proto.Messaging.ProtobufResponse;
import ca.jeb.generated.proto.Messaging.RequestType;
import ca.jeb.generated.proto.Messaging.ResponseStatus;
import ca.jeb.generated.proto.Messaging.ResponseStatusHeader;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

/**
 * @author <a href="mailto:erick@jeb.ca">Erick Bourgeois</a>
 */
public class ProtobufRequestHandler
{
  private static final Map<Long, Address> ADDRESS_CACHE = new HashMap<>();
  private static final Map<Long, Person>  PERSON_CACHE  = new HashMap<>();

  private static final Logger             LOGGER        = LoggerFactory.getLogger(ProtobufServerSerializeDeserializer.class);

  public ProtobufResponse handleRequest(ProtobufRequest protobufRequest) throws InvalidProtocolBufferException
  {
    LOGGER.debug("protobufRequest: " + protobufRequest);
    final RequestType requestType = protobufRequest.getRequestType();
    final ByteString requestBody = protobufRequest.getPayload();
    final MessageRequest messageRequest = MessageRequest.parseFrom(requestBody);

    final ResponseStatusHeader.Builder responseHeaderBuilder = ResponseStatusHeader.newBuilder().setStatus(ResponseStatus.SUCCESS);

    ByteString response = null;

    try
    {
      switch (requestType)
      {
        case GET :
          LOGGER.info("GETting message " + messageRequest);
          response = getCachcedMessage(requestType, messageRequest);
          break;
        case DELETE :
          LOGGER.info("DELETing message " + messageRequest);
          response = getCachcedMessage(requestType, messageRequest);
          break;
        case PUT :
          LOGGER.warn("PUTting new message " + messageRequest);
          cacheMessage(requestType, messageRequest);
          break;
        case POST :
          LOGGER.warn("POSTing message " + messageRequest);
          cacheMessage(requestType, messageRequest);
          break;
        default :
          final String error = "Big problem, can not recognize this RequestType " + requestType;
          responseHeaderBuilder.setStatus(ResponseStatus.FAILURE);
          responseHeaderBuilder.setMessage(error);
          LOGGER.error(error);
      }
    }
    catch (Exception e)
    {
      final String error = "Caught exception while dealing with request " + requestType + ": " + e;
      responseHeaderBuilder.setStatus(ResponseStatus.FAILURE);
      responseHeaderBuilder.setMessage(error);
      LOGGER.error(error, e);
    }

    final ProtobufResponse.Builder protobufResponseBuilder = ProtobufResponse.newBuilder();
    if (response != null)
    {
      protobufResponseBuilder.setPayload(response);
    }

    return protobufResponseBuilder.setResponseStatusHeader(responseHeaderBuilder.build()).build();
  }

  private static final ByteString getCachcedMessage(RequestType requestType, MessageRequest messageRequest) throws ProtobufException
  {
    if (!messageRequest.hasId())
    {
      throw new ProtobufException("Can not process this messageRequest, as there is no ID and the RequestType is " + requestType);
    }

    final Long id = messageRequest.getId();
    LOGGER.info("Received " + messageRequest.getMessageType() + " request for ID " + id);

    final boolean isDelete = (requestType == RequestType.DELETE) ? true : false;

    Object object = null;
    switch (messageRequest.getMessageType())
    {
      case ADDRESS :
        if (isDelete)
        {
          object = ADDRESS_CACHE.remove(id);
        }
        else
        {
          object = ADDRESS_CACHE.get(id);
        }
        break;
      case PERSON :
        if (isDelete)
        {
          object = PERSON_CACHE.remove(id);
        }
        else
        {
          object = PERSON_CACHE.get(id);
        }
        break;
      default :
        ;
    }

    if (object == null)
    {
      throw new ProtobufException("Could not find a " + messageRequest.getMessageType() + " for ID " + id);
    }

    return ((Message)object).toByteString();
  }

  private static final boolean cacheMessage(RequestType requestType, MessageRequest messageRequest) throws ProtobufException,
          InvalidProtocolBufferException
  {
    if (messageRequest == null)
    {
      throw new ProtobufException("MessageRequest can not be null for RequestType " + requestType);
    }

    if (!messageRequest.hasId())
    {
      throw new ProtobufException("Can not process this messageRequest, as there is no ID and the RequestType is " + requestType);
    }

    final Long id = messageRequest.getId();
    final MessageType messageType = messageRequest.getMessageType();
    LOGGER.debug("messageType: " + messageType);

    LOGGER.info("Received " + messageType + " request for ID " + id);

    Object object = null;
    ByteString messagePayload = messageRequest.getMessage();

    switch (messageType)
    {
      case ADDRESS :
        Address address = Address.parseFrom(messagePayload);
        object = ADDRESS_CACHE.put(id, address);
        object = address;
        LOGGER.debug("object: " + object);
        break;
      case PERSON :
        Person person = Person.parseFrom(messagePayload);
        object = PERSON_CACHE.put(id, person);
        LOGGER.debug("object: " + object);
        break;
      default :
        ;
    }

    if (object == null)
    {
      throw new ProtobufException("Could store a " + messageRequest.getMessageType() + " for ID " + id);
    }

    return true;
  }
}