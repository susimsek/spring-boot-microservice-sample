package io.github.susimsek.card.consumer;

import io.github.susimsek.card.debezium.DebeziumEventDetails;
import io.github.susimsek.card.dto.CardDTO;
import io.github.susimsek.card.entity.Card;
import io.github.susimsek.card.mapper.CardMapper;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CardPublisher {

    @Getter
    private final Flowable<CardDTO> publisher;
    private ObservableEmitter<CardDTO> emitter;
    private final CardMapper cardMapper;

    public CardPublisher(CardMapper cardMapper) {
        this.cardMapper = cardMapper;
        Observable<CardDTO> ratingObservable = Observable.create(emitter -> this.emitter = emitter);
        ConnectableObservable<CardDTO> connectableObservable = ratingObservable.share().publish();
        connectableObservable.connect();
        this.publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    @Bean
    public Consumer<DebeziumEventDetails<Card>> receive() {
        return debeziumEvent -> {
            var payload = debeziumEvent.payload();
            Card entity = switch (payload.operation()) {
                case CREATE, UPDATE -> payload.after();
                default -> payload.before();
            };
            if (emitter != null) {
                emitter.onNext(cardMapper.toDto(entity));
            }
        };
    }
}