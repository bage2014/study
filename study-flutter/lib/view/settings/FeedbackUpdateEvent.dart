import 'package:app_lu_lu/component/log/Logs.dart';

class FeedbackUpdateEvent {
  @override
  var data;

  @override
  void consume(data) {
    Logs.info("FeedbackUpdateEvent --- ");
    Logs.info(data.toString());
  }
}
