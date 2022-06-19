import 'package:event_bus/event_bus.dart' as marcojakob;

typedef EventCallback = void Function(dynamic event);

class EventBus {
  static marcojakob.EventBus _eventBus = marcojakob.EventBus();

  static void publish(event) {
    _eventBus.fire(event);
  }

  static void consume<T>(EventCallback callback) {
    _eventBus.on<T>().listen((event) {
      // All events are of type UserLoggedInEvent (or subtypes of it).
      callback(event);
    });
  }
}
