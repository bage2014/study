import 'package:app_lu_lu/component/event/BaseEvent.dart';
import 'package:event_bus/event_bus.dart' as marcojakob;

class EventBus {
  static marcojakob.EventBus _eventBus = marcojakob.EventBus();

  static void publish(event) {
    _eventBus.fire(event);
  }

  static void initConsumers() {
    _eventBus.on<BaseEvent>().listen((event) {
      // All events are of type UserLoggedInEvent (or subtypes of it).
      event.consume(event.data);
    });
  }
}
