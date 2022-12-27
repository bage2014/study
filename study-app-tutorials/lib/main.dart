import 'package:flutter/material.dart';
import 'package:flutter_alarm_clock/flutter_alarm_clock.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Flutter alarm clock example'),
        ),
        body: Center(
            child: Column(children: <Widget>[
              Container(
                margin: const EdgeInsets.all(25),
                child: TextButton(
                  child: const Text(
                    'Create alarm at 23:59',
                    style: TextStyle(fontSize: 20.0),
                  ),
                  onPressed: () {
                    FlutterAlarmClock.createAlarm(23, 59);
                  },
                ),
              ),
              Container(
                margin: const EdgeInsets.all(25),
                child: TextButton(
                  child: const Text(
                    'Show alarms',
                    style: TextStyle(fontSize: 20.0),
                  ),
                  onPressed: () {
                    FlutterAlarmClock.showAlarms();
                  },
                ),
              ),
              Container(
                margin: const EdgeInsets.all(25),
                child: TextButton(
                  child: const Text(
                    'Create timer for 42 seconds',
                    style: TextStyle(fontSize: 20.0),
                  ),
                  onPressed: () {
                    FlutterAlarmClock.createTimer(42);
                  },
                ),
              ),
              Container(
                margin: const EdgeInsets.all(25),
                child: TextButton(
                  child: const Text(
                    'Show Timers',
                    style: TextStyle(fontSize: 20.0),
                  ),
                  onPressed: () {
                    FlutterAlarmClock.showTimers();
                  },
                ),
              ),
            ])),
      ),
    );
  }
}