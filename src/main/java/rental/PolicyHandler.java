package rental;

import rental.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrderCanceled_ApprovalCancel(@Payload OrderCanceled orderCanceled){

        if(orderCanceled.isMe()){
            System.out.println("##### listener ApprovalCancel : " + orderCanceled.toJson());
            Payment payment = paymentRepository.findByOrderId(orderCanceled.getId());
            payment.setOrderId(orderCanceled.getId());
            payment.setStatus(orderCanceled.getStatus());
            paymentRepository.save(payment);
        }
    }

}