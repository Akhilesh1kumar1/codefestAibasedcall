package com.sr.capital.listner;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatusUpdateHanlder  {//implements MessageHandler

   /* @Override
    public void handle(MsgMessage msgMessage) throws Exception {

    }

    @Override
    public String getHandlerName() {
        return null;
    }*/
}
